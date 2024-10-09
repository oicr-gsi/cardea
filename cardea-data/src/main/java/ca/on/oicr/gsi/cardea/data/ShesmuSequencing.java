package ca.on.oicr.gsi.cardea.data;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

import java.util.Set;

import static java.util.Collections.*;
import static java.util.Objects.requireNonNull;

/**
 * Immutable Test
 */
@JsonDeserialize(builder = ShesmuSequencing.Builder.class)
public class ShesmuSequencing {

  private final String test;
  private final MetricCategory type;
  private final Set<ShesmuSample> limsIds;
  private final boolean complete;


  private ShesmuSequencing(Builder builder) {
    this.test = requireNonNull(builder.test);
    this.type = requireNonNull(builder.type);
    this.limsIds = unmodifiableSet(requireNonNull(builder.limsIds));
    this.complete = requireNonNull(builder.complete);
  }


  public String getTest() {
    return test;
  }

  public MetricCategory getType() {
    return type;
  }

  public Set<ShesmuSample> getLimsIds() {
    return limsIds;
  }

  public Boolean isComplete() {
    return complete;
  }



  @JsonPOJOBuilder(withPrefix = "")
  public static class Builder {

    private String test;
    private Set<ShesmuSample> limsIds;
    private MetricCategory type;
    private boolean complete;

    public ShesmuSequencing build() {
      return new ShesmuSequencing(this);
    }

    public Builder test(String test) {
      this.test = test;
      return this;
    }

    public Builder type(MetricCategory type) {
      this.type = type;
      return this;
    }

    public Builder limsIds(Set<ShesmuSample> limsIds) {
      this.limsIds = limsIds;
      return this;
    }

    public Builder complete(boolean complete) {
      this.complete = complete;
      return this;
    }

  }
}
