package ca.on.oicr.gsi.cardea.data;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

import static java.util.Objects.requireNonNull;

/**
 * Immutable Test
 */
@JsonDeserialize(builder = ShesmuSample.Builder.class)
public class ShesmuSample {

  private final String id;
  private final boolean supplemental;
  private final boolean qcFailed;


  private ShesmuSample(Builder builder) {
    this.id = requireNonNull(builder.id);
    this.supplemental = requireNonNull(builder.supplemental);
    this.qcFailed = requireNonNull(builder.qcFailed);
  }


  public String getId() {
    return id;
  }

  public Boolean isSupplemental() {
    return supplemental;
  }

  public Boolean qcFailed() {
    return qcFailed;
  }



  @JsonPOJOBuilder(withPrefix = "")
  public static class Builder {

    private String id;
    private boolean supplemental;
    private boolean qcFailed;

    public ShesmuSample build() {
      return new ShesmuSample(this);
    }


    public Builder id(String id) {
      this.id = id;
      return this;
    }

    public Builder supplemental(boolean supplemental) {
      this.supplemental = supplemental;
      return this;
    }

    public Builder qcFailed(boolean qcFailed) {
      this.qcFailed = qcFailed;
      return this;
    }


  }
}
