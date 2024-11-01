package ca.on.oicr.gsi.cardea.data;

import static java.util.Collections.*;
import static java.util.Objects.requireNonNull;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

/**
 * Immutable CaseData
 */
@JsonDeserialize(builder = CaseData.Builder.class)
public class CaseData {
  private final Map<Long, Assay> assaysById;
  private final List<Case> cases;
  private final List<OmittedSample> omittedSamples;
  private final List<OmittedRunSample> omittedRunSamples;
  private final ZonedDateTime timestamp;

  private CaseData(Builder builder) {
    this.assaysById = unmodifiableMap(builder.assaysById);
    this.cases = unmodifiableList(builder.cases);
    this.omittedSamples = unmodifiableList(builder.omittedSamples);
    this.omittedRunSamples = unmodifiableList(builder.omittedRunSamples);
    this.timestamp = requireNonNull(builder.timestamp);
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

  public List<OmittedRunSample> getOmittedRunSamples() {
    return omittedRunSamples;
  }

  public ZonedDateTime getTimestamp() {
    return timestamp;
  }

  @JsonPOJOBuilder(withPrefix = "")
  public static class Builder {
    private Map<Long, Assay> assaysById;
    private List<Case> cases;
    private List<OmittedSample> omittedSamples;
    private List<OmittedRunSample> omittedRunSamples;
    private ZonedDateTime timestamp;

    public CaseData build() {
      return new CaseData(this);
    }

    public Builder assaysById(Map<Long, Assay> assaysById) {
      this.assaysById = assaysById;
      return this;
    }

    public Builder cases(List<Case> cases) {
      this.cases = cases;
      return this;
    }

    public Builder omittedSamples(List<OmittedSample> omittedSamples) {
      this.omittedSamples = omittedSamples;
      return this;
    }

    public Builder omittedRunSamples(List<OmittedRunSample> omittedRunSamples) {
      this.omittedRunSamples = omittedRunSamples;
      return this;
    }

    public Builder timestamp(ZonedDateTime timestamp) {
      this.timestamp = timestamp;
      return this;
    }


  }

}
