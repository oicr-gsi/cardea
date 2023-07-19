package ca.on.oicr.gsi.cardea.server.service;

import ca.on.oicr.gsi.cardea.data.Assay;
import ca.on.oicr.gsi.cardea.data.Case;
import ca.on.oicr.gsi.cardea.data.CaseData;
import ca.on.oicr.gsi.cardea.data.CaseStatus;
import ca.on.oicr.gsi.cardea.data.CaseStatusesForRun;
import ca.on.oicr.gsi.cardea.data.CasesForRequisition;
import ca.on.oicr.gsi.cardea.data.Requisition;
import ca.on.oicr.gsi.cardea.data.RequisitionQc;
import ca.on.oicr.gsi.cardea.data.Run;
import ca.on.oicr.gsi.cardea.data.Sample;
import ca.on.oicr.gsi.cardea.data.ShesmuCase;
import ca.on.oicr.gsi.cardea.server.CaseLoader;

import ca.on.oicr.gsi.Pair;

import java.time.Duration;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;

@Service
public class CaseService {

  private static final Logger log = LoggerFactory.getLogger(CaseService.class);
  private CaseData caseData;
  @Autowired
  private CaseLoader dataLoader;
  private int refreshFailures = 0;

  public CaseService(@Autowired MeterRegistry meterRegistry) {
    if (meterRegistry != null) {
      Gauge.builder("case_data_refresh_failures", this::getRefreshFailures)
          .description("Number of consecutive failures to refresh the case data")
          .register(meterRegistry);
      Gauge.builder("case_data_age_seconds", () -> this.getDataAge().toSeconds())
          .description("Time since case data was refreshed").register(meterRegistry);
    }
  }

  public CaseData getCaseData() {
    return caseData;
  }

  protected void setCaseData(CaseData caseData) {
    this.caseData = caseData;
  }

  public CaseStatusesForRun getCaseStatusesForRun(String runName) {
    Set<Case> casesMatchingRunName = caseData.getCases().stream()
        .filter(kase -> getRunNamesFor(kase).stream().anyMatch(rName -> runName.equals(rName)))
        .collect(Collectors.toSet());

    List<Pair<String, String>> statusForCaseList = casesMatchingRunName.stream()
        .map(k -> new Pair<String, String>(getReqStatus(k.getRequisition()).getLabel(), k.getId()))
        .collect(Collectors.toList());
    Map<String, Set<String>> statusesForRun = new HashMap<String, Set<String>>();
    statusForCaseList.forEach((p) -> {
      if (statusesForRun.get(p.first()) == null) {
        statusesForRun.put(p.first(), new HashSet<>());
      }
      statusesForRun.get(p.first()).add(p.second());
    });

    return new CaseStatusesForRun(statusesForRun.get(CaseStatus.ACTIVE.getLabel()), statusesForRun.get(
        CaseStatus.COMPLETED.getLabel()), statusesForRun.get(CaseStatus.STOPPED.getLabel()));
  }

  public Duration getDataAge() {
    CaseData currentData = caseData;
    if (currentData == null) {
      return Duration.ZERO;
    }
    return Duration.between(currentData.getTimestamp(), ZonedDateTime.now());
  }

  private int getRefreshFailures() {
    return refreshFailures;
  }

  private CaseStatus getReqStatus(Requisition req) {
    if (req.isStopped()) {
      return CaseStatus.STOPPED;
    }
    var reqQcs = req.getFinalReports().stream().map(RequisitionQc::isQcPassed).collect(Collectors.toSet());
    if (reqQcs.isEmpty()) {
      return CaseStatus.ACTIVE;
    } else if (reqQcs.stream().anyMatch(status -> status.equals(true))) {
      return CaseStatus.COMPLETED;
    } else {
      return CaseStatus.ACTIVE;
    }
  }

  private Set<String> getRunNamesFor(Case kase) {
    return kase.getTests().stream()
        .map(test -> {
          var lqs = test.getLibraryQualifications().stream().map(Sample::getRun);
          var fdls = test.getFullDepthSequencings().stream().map(Sample::getRun);
          return Stream.concat(lqs, fdls)
              .filter(Objects::nonNull)
              .map(Run::getName)
              .collect(Collectors.toSet());
        })
        .flatMap(Set::stream)
        .collect(Collectors.toSet());
  }

  public CasesForRequisition getCasesForRequisition(String requisitionName) {
    Set<Case> cases = caseData.getCases().stream()
        .filter(kase -> requisitionName.equals(kase.getRequisition().getName()))
        .collect(Collectors.toSet());
    if (cases.isEmpty()) {
      return null;
    }
    Long assayId = cases.stream()
        .map(kase -> kase.getRequisition().getAssayId())
        .findFirst().get();

    Assay assay = caseData.getAssaysById().get(assayId);

    return new CasesForRequisition.Builder()
        .assayName(assay.getName())
        .assayVersion(assay.getVersion())
        .cases(cases)
        .build();
  }

  public Set<ShesmuCase> getShesmuCases() {
    return caseData.getCases().stream()
        .map(kase -> convertCaseToShesmuCase(kase))
        .collect(Collectors.toSet());
  }

  private Set<String> getLimsIusIdsForShesmu(Case kase) {
    return kase.getTests().stream()
        .map(test -> {
          var lqs = test.getLibraryQualifications()
              .stream()
              .filter(s -> s.getRun() != null) // only keep library qualifications that are run-libraries/IUSes
              .map(Sample::getId);
          var fdls = test.getFullDepthSequencings().stream().map(Sample::getId);
          return Stream.concat(lqs, fdls)
              .filter(Objects::nonNull)
              .collect(Collectors.toSet());
        })
        .flatMap(Set::stream)
        .collect(Collectors.toSet());
  }

  private ShesmuCase convertCaseToShesmuCase(Case kase) {
    Optional<LocalDate> completedDate = kase.getRequisition().getFinalReports().stream()
        .map(qc -> qc.getQcDate())
        .max(LocalDate::compareTo);
    return new ShesmuCase.Builder()
        .assayName(kase.getAssay().getName())
        .assayVersion(kase.getAssay().getVersion())
        .caseIdentifier(kase.getId())
        .caseStatus(getReqStatus(kase.getRequisition()))
        .completedDate(completedDate.orElse(null))
        .limsIds(getLimsIusIdsForShesmu(kase))
        .requisitionId(kase.getRequisition().getId())
        .requisitionName(kase.getRequisition().getName())
        .build();
  }

  @Scheduled(fixedDelay = 1L, timeUnit = TimeUnit.MINUTES)
  private void refreshData() {
    try {
      ZonedDateTime previousTimestamp = caseData == null ? null : caseData.getTimestamp();
      CaseData newData = dataLoader.load(previousTimestamp);
      refreshFailures = 0;
      if (newData != null) {
        caseData = newData;
      }
    } catch (Exception e) {
      refreshFailures++;
      log.error("Failed to refresh case data", e);
    }
  }

}
