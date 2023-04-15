package ca.on.oicr.gsi.dimsum;

import ca.on.oicr.gsi.dimsum.data.Case;
import ca.on.oicr.gsi.dimsum.data.CaseData;
import ca.on.oicr.gsi.dimsum.data.CaseDataResponse;
import ca.on.oicr.gsi.dimsum.service.CaseService;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class QcGateEtlApiController {

  private CaseData caseData;
  private int refreshFailures = 0;

  public QcGateEtlApiController(@Autowired MeterRegistry meterRegistry) {
    if (meterRegistry != null) {
      Gauge.builder("case_data_refresh_failures", this::getRefreshFailures)
          .description("Number of consecutive failures to refresh the case data")
          .register(meterRegistry);
      Gauge.builder("case_data_age_seconds", () -> this.getDataAge().toSeconds())
          .description("Time since case data was refreshed").register(meterRegistry);
    }
  }

  public Duration getDataAge() {
    CaseData currentData = caseData;
    if (currentData == null) {
      return Duration.ZERO;
    }
    return Duration.between(currentData.getTimestamp(), ZonedDateTime.now());
  }

  @GetMapping("/dimsum")
  public CaseData getDimsumData() {
    return caseData;
  }

  private int getRefreshFailures() {
    return refreshFailures;
  }

  protected void setCaseData(CaseData caseData) {
    this.caseData = caseData;
  }
}
