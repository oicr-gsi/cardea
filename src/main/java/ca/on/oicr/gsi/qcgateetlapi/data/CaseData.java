package ca.on.oicr.gsi.qcgateetlapi.data;

import static java.util.Collections.unmodifiableList;
import static java.util.Objects.requireNonNull;
import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.List;
import javax.annotation.concurrent.Immutable;

@Immutable
public class CaseData {

  private final List<Case> cases;
  private final List<OmittedSample> omittedSamples;
  private final ZonedDateTime timestamp;

  public CaseData(List<Case> cases, List<OmittedSample> omittedSamples, ZonedDateTime timestamp) {
    this.cases = unmodifiableList(cases);
    this.omittedSamples = Collections.unmodifiableList(omittedSamples);
    this.timestamp = requireNonNull(timestamp);

  }

  public List<Case> getCases() {
    return cases;
  }

  public List<OmittedSample> getOmittedSamples() {
    return omittedSamples;
  }

  public ZonedDateTime getTimestamp() {
    return timestamp;
  }
}
