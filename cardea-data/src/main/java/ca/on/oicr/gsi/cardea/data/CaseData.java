package ca.on.oicr.gsi.cardea.data;

import static java.util.Collections.unmodifiableList;
import static java.util.Objects.requireNonNull;
import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Immutable CaseData
 */
public class CaseData {

  private final Map<Long, Assay> assaysById;
  private final List<Case> cases;
  private final List<OmittedSample> omittedSamples;
  private final ZonedDateTime timestamp;

  public CaseData(List<Case> cases, List<OmittedSample> omittedSamples, Map<Long, Assay> assaysById,
      ZonedDateTime timestamp) {
    this.assaysById = Collections.unmodifiableMap(assaysById);
    this.cases = unmodifiableList(cases);
    this.omittedSamples = Collections.unmodifiableList(omittedSamples);
    this.timestamp = requireNonNull(timestamp);

  }

  public Map<Long, Assay> getAssaysById() {
    return assaysById;
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
