package ca.on.oicr.gsi.cardea.server.service;

import ca.on.oicr.gsi.cardea.data.*;
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
        .map(k -> new Pair<String, String>(getCaseStatus(k).getLabel(), k.getId()))
        .collect(Collectors.toList());
    Map<String, Set<String>> statusesForRun = new HashMap<String, Set<String>>();
    statusForCaseList.forEach((p) -> {
      if (statusesForRun.get(p.first()) == null) {
        statusesForRun.put(p.first(), new HashSet<>());
      }
      statusesForRun.get(p.first()).add(p.second());
    });

    return new CaseStatusesForRun(statusesForRun.get(CaseStatus.ACTIVE.getLabel()),
        statusesForRun.get(
            CaseStatus.COMPLETED.getLabel()),
        statusesForRun.get(CaseStatus.STOPPED.getLabel()));
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

  private CaseStatus getCaseStatus(Case kase) {
    if (kase.isStopped()) {
      return CaseStatus.STOPPED;
    } else if (!kase.getDeliverables().isEmpty() && kase.getDeliverables().stream()
        .allMatch(deliverable -> deliverable.getReleases().stream()
            .allMatch(release -> Boolean.TRUE.equals(release.getQcPassed())))) {
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

  public Set<Case> getCasesForRequisition(String requisitionName) {
    Set<Case> cases = caseData.getCases().stream()
        .filter(kase -> requisitionName.equals(kase.getRequisition().getName()))
        .collect(Collectors.toSet());

    return cases.isEmpty() ? null : cases;
  }

  public Set<ShesmuCase> getShesmuCases() {
    return caseData.getCases().stream()
        .map(kase -> convertCaseToShesmuCase(kase))
        .collect(Collectors.toSet());
  }

  public Set<ShesmuDetailedCase> getShesmuDetailedCases() {
    return caseData.getCases().stream()
            .filter(kase -> !getSequencingForShesmuCase(kase).isEmpty())
            .map(kase -> convertCaseToShesmuDetailedCase(kase))
            .collect(Collectors.toSet());
  }

  private Set<ShesmuSequencing> getSequencingForShesmuCase(Case kase){

    Set<ShesmuSequencing> sequencings = new HashSet<>();
    final long reqId = kase.getRequisition().getId();

    for (Test test : kase.getTests()) {

      ShesmuSequencing fullDepthSeq = makeShesmuSequencing(test.getFullDepthSequencings(), MetricCategory.FULL_DEPTH_SEQUENCING, reqId, test.getName());
      if (fullDepthSeq != null) {
        sequencings.add(fullDepthSeq);
      }

      ShesmuSequencing libraryQual = makeShesmuSequencing(test.getLibraryQualifications(), MetricCategory.LIBRARY_QUALIFICATION, reqId, test.getName());
      if (libraryQual != null) {
        sequencings.add(libraryQual);
      }
    };

    return sequencings;
  }

  private ShesmuSequencing makeShesmuSequencing(List<Sample> samples, MetricCategory type, long reqId, String name){

    Set<ShesmuSample> shesmuSamples = new HashSet<>();

    boolean hasPassed = false;
    boolean hasWaiting = false;
    for (Sample sample : samples){

      if (sample.getDataReviewPassed() == null || sample.getQcPassed() == null){
        hasWaiting = true;
      } else if (sample.getDataReviewPassed() && sample.getQcPassed()) {
        hasPassed = true;
      }

      if (!(sample.getRun() == null && type.equals(MetricCategory.LIBRARY_QUALIFICATION))) {
        shesmuSamples.add(new ShesmuSample.Builder()
                .id(sample.getId())
                .supplemental(!Objects.equals(sample.getRequisitionId(), reqId))
                .build());
      }
    }

    if (shesmuSamples.isEmpty()) {
      return null;
    }
    return new ShesmuSequencing.Builder()
            .test(name)
            .limsIds(shesmuSamples)
            .complete((hasPassed && !hasWaiting))
            .type(type)
            .build();
  }

  private Set<String> getLimsIusIdsForShesmu(Case kase) {
    return kase.getTests().stream()
        .map(test -> {
          var lqs = test.getLibraryQualifications()
              .stream()
              .filter(s -> s.getRun() != null) // only keep library qualifications that are
                                               // run-libraries/IUSes
              .map(Sample::getId);
          var fdls = test.getFullDepthSequencings().stream().map(Sample::getId);
          return Stream.concat(lqs, fdls)
              .filter(Objects::nonNull)
              .collect(Collectors.toSet());
        })
        .flatMap(Set::stream)
        .collect(Collectors.toSet());
  }

  private LocalDate getCompletedDate(Case kase) {
    if (kase.getDeliverables().isEmpty()) {
      return null;
    }
    LocalDate completedDate = null;
    for (CaseDeliverable deliverable : kase.getDeliverables()) {
      if (deliverable.getReleases().isEmpty()) {
        return null;
      }
      for (CaseRelease release : deliverable.getReleases()) {
        if (!Boolean.TRUE.equals(release.getQcPassed())) {
          return null;
        }
        if (completedDate == null || completedDate.isBefore(release.getQcDate())) {
          completedDate = release.getQcDate();
        }
      }
    }
    return completedDate;
  }

  private ShesmuCase convertCaseToShesmuCase(Case kase) {
    return new ShesmuCase.Builder()
        .assayName(caseData.getAssaysById().get(kase.getAssayId()).getName())
        .assayVersion(caseData.getAssaysById().get(kase.getAssayId()).getVersion())
        .caseIdentifier(kase.getId())
        .caseStatus(getCaseStatus(kase))
        .completedDateLocal(getCompletedDate(kase))
        .limsIds(getLimsIusIdsForShesmu(kase))
        .requisitionId(kase.getRequisition().getId())
        .requisitionName(kase.getRequisition().getName())
        .build();
  }

  private ShesmuDetailedCase convertCaseToShesmuDetailedCase(Case kase) {
    return new ShesmuDetailedCase.Builder()
            .assayName(caseData.getAssaysById().get(kase.getAssayId()).getName())
            .assayVersion(caseData.getAssaysById().get(kase.getAssayId()).getVersion())
            .caseIdentifier(kase.getId())
            .caseStatus(getCaseStatus(kase))
            .paused(kase.getRequisition().isPaused())
            .stopped(kase.getRequisition().isStopped())
            .completedDateLocal(getCompletedDate(kase))
            .requisitionId(kase.getRequisition().getId())
            .requisitionName(kase.getRequisition().getName())
            .sequencing(getSequencingForShesmuCase(kase))
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
