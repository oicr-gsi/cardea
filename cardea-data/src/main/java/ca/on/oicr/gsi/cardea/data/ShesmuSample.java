package ca.on.oicr.gsi.cardea.data;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

import java.util.Set;

import static java.util.Collections.unmodifiableSet;
import static java.util.Objects.requireNonNull;

/**
 * Immutable Test
 */
@JsonDeserialize(builder = ShesmuSample.Builder.class)
public class ShesmuSample {

  private final String id;
  private final Boolean isSupplemental;


  private ShesmuSample(Builder builder) {
    this.id = requireNonNull(builder.id);
    this.isSupplemental = requireNonNull(builder.isSupplemental);
  }


  public String getId() {
    return id;
  }

  public Boolean getIsSupplemental() {
    return isSupplemental;
  }



  @JsonPOJOBuilder(withPrefix = "")
  public static class Builder {

    private String id;
    private Boolean isSupplemental;

    public ShesmuSample build() {
      return new ShesmuSample(this);
    }


    public Builder id(String id) {
      this.id = id;
      return this;
    }

    public Builder isSupplemental(Boolean isSupplemental) {
      this.isSupplemental = isSupplemental;
      return this;
    }

  }
}
