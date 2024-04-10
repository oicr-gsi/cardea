package ca.on.oicr.gsi.cardea.data;

import static java.util.Objects.requireNonNull;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

/**
 * Immutable Project
 */
@JsonDeserialize(builder = Project.Builder.class)
public class Project {

  private final String name;
  private final String pipeline;
  private final boolean analysisReviewSkipped;
  private final Map<DeliverableType, List<String>> deliverables;

  private Project(Builder builder) {
    this.name = requireNonNull(builder.name);
    this.pipeline = requireNonNull(builder.pipeline);
    this.analysisReviewSkipped = builder.analysisReviewSkipped;
    Map<DeliverableType, List<String>> tempDeliverables = builder.deliverables.entrySet().stream()
        .collect(Collectors.toMap(entry -> entry.getKey(),
            entry -> Collections.unmodifiableList(entry.getValue())));
    this.deliverables = Collections.unmodifiableMap(tempDeliverables);
  }

  public String getName() {
    return name;
  }

  public String getPipeline() {
    return pipeline;
  }

  public boolean isAnalysisReviewSkipped() {
    return analysisReviewSkipped;
  }

  public Map<DeliverableType, List<String>> getDeliverables() {
    return deliverables;
  }

  @JsonPOJOBuilder(withPrefix = "")
  public static class Builder {

    private String name;
    private String pipeline;
    private boolean analysisReviewSkipped;
    private Map<DeliverableType, List<String>> deliverables;

    public Project build() {
      return new Project(this);
    }

    public Builder name(String name) {
      this.name = name;
      return this;
    }

    public Builder pipeline(String pipeline) {
      this.pipeline = pipeline;
      return this;
    }

    public Builder analysisReviewSkipped(boolean analysisReviewSkipped) {
      this.analysisReviewSkipped = analysisReviewSkipped;
      return this;
    }

    public Builder deliverables(Map<DeliverableType, List<String>> deliverables) {
      this.deliverables = deliverables;
      return this;
    }

  }
}
