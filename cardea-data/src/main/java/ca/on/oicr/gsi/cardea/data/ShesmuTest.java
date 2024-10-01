package ca.on.oicr.gsi.cardea.data;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

import java.util.Set;

import static java.util.Collections.*;
import static java.util.Objects.requireNonNull;

/**
 * Immutable Test
 */
@JsonDeserialize(builder = ShesmuTest.Builder.class)
public class ShesmuTest {

  private final String name;
  private final TestCategory test;
  private final Set<ShesmuSample> limsIds;
  private final Boolean isComplete;


  private ShesmuTest(Builder builder) {
    this.name = requireNonNull(builder.name);
    this.test = requireNonNull(builder.test);
    this.limsIds = unmodifiableSet(requireNonNull(builder.limsIds));
    this.isComplete = requireNonNull(builder.isComplete);
  }


  public String getName() {
    return name;
  }

  public TestCategory getTest() {
    return test;
  }

  public Set<ShesmuSample> getLimsIds() {
    return limsIds;
  }

  public Boolean getIsComplete() {
    return isComplete;
  }



  @JsonPOJOBuilder(withPrefix = "")
  public static class Builder {

    private String name;
    private Set<ShesmuSample> limsIds;
    private TestCategory test;
    private Boolean isComplete;




    public ShesmuTest build() {
      return new ShesmuTest(this);
    }


    public Builder name(String name) {
      this.name = name;
      return this;
    }

    public Builder test(TestCategory test) {
      this.test = test;
      return this;
    }

    public Builder limsIds(Set<ShesmuSample> limsIds) {
      this.limsIds = limsIds;
      return this;
    }

    public Builder isComplete(Boolean isComplete) {
      this.isComplete = isComplete;
      return this;
    }

  }
}
