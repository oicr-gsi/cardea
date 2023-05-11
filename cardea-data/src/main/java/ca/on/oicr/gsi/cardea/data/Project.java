package ca.on.oicr.gsi.cardea.data;

import static java.util.Objects.requireNonNull;

/**
 * Immutable Project
 */
public class Project {

  private final String name;
  private final String pipeline;

  private Project(Builder builder) {
    this.name = requireNonNull(builder.name);
    this.pipeline = requireNonNull(builder.pipeline);
  }

  public String getName() {
    return name;
  }

  public String getPipeline() {
    return pipeline;
  }

  public static class Builder {

    private String name;
    private String pipeline;

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

  }
}
