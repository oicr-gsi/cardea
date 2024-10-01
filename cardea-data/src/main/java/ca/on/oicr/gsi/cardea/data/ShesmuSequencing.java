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

  private final String name;
  private final MetricCategory test;
  private final Set<ShesmuSample> limsIds;
  private final boolean complete;


  private ShesmuSequencing(Builder builder) {
    this.name = requireNonNull(builder.name);
    this.test = requireNonNull(builder.test);
    this.limsIds = unmodifiableSet(requireNonNull(builder.limsIds));
    this.complete = requireNonNull(builder.complete);
  }


  public String getName() {
    return name;
  }

  public MetricCategory getTest() {
    return test;
  }

  public Set<ShesmuSample> getLimsIds() {
    return limsIds;
  }

  public Boolean isComplete() {
    return complete;
  }



  @JsonPOJOBuilder(withPrefix = "")
  public static class Builder {

    private String name;
    private Set<ShesmuSample> limsIds;
    private MetricCategory test;
    private boolean complete;




    public ShesmuSequencing build() {
      return new ShesmuSequencing(this);
    }


    public Builder name(String name) {
      this.name = name;
      return this;
    }

    public Builder test(MetricCategory test) {
      this.test = test;
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
