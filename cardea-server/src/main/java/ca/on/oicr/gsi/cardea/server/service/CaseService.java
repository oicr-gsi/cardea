package ca.on.oicr.gsi.cardea.server.service;

import ca.on.oicr.gsi.cardea.data.Case;
import ca.on.oicr.gsi.cardea.data.CaseData;
import ca.on.oicr.gsi.cardea.data.CaseStatus;
import ca.on.oicr.gsi.cardea.data.CaseStatusCountsForRun;
import ca.on.oicr.gsi.cardea.data.RequisitionQc;
import ca.on.oicr.gsi.cardea.data.Run;
import ca.on.oicr.gsi.cardea.server.CaseLoader;
import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ca.on.oicr.gsi.cardea.data.Requisition;
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

  public CaseStatusCountsForRun getCaseStatusCountsForRun(String runName) {
    Set<Case> casesMatchingRunName = caseData.getCases().stream()
        .filter(kase -> getRunNamesFor(kase).stream().anyMatch(rName -> runName.equals(rName)))
        .collect(Collectors.toSet());

    Map<CaseStatus, Long> statusCountsForRun = casesMatchingRunName.stream()
        .map(kase -> getReqStatus(kase.getRequisition()))
        .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

    return new CaseStatusCountsForRun(statusCountsForRun.get(CaseStatus.ACTIVE), statusCountsForRun.get(
        CaseStatus.COMPLETED), statusCountsForRun.get(CaseStatus.STOPPED));
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
          var lqs = test.getLibraryQualifications().stream().map(lq -> lq.getRun());
          var fdls = test.getFullDepthSequencings().stream().map(fdl -> fdl.getRun());
          return Stream.concat(lqs, fdls).filter(Objects::nonNull).map(Run::getName).collect(Collectors.toSet());
        })
        .flatMap(Set::stream)
        .collect(Collectors.toSet());
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
