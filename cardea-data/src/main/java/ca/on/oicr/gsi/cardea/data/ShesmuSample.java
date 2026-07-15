package ca.on.oicr.gsi.cardea.data;

import static java.util.Objects.requireNonNull;

/** Immutable Test */
@com.fasterxml.jackson.databind.annotation.JsonDeserialize(builder = ShesmuSample.Builder.class)
@tools.jackson.databind.annotation.JsonDeserialize(builder = ShesmuSample.Builder.class)
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

  public boolean isSupplemental() {
    return supplemental;
  }

  public boolean isQcFailed() {
    return qcFailed;
  }

  @com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder(withPrefix = "")
  @tools.jackson.databind.annotation.JsonPOJOBuilder(withPrefix = "")
  public static class Builder {

    private String id;
    private Boolean supplemental;
    private Boolean qcFailed;

    public ShesmuSample build() {
      return new ShesmuSample(this);
    }

    public Builder id(String id) {
      this.id = id;
      return this;
    }

    public Builder supplemental(Boolean supplemental) {
      this.supplemental = supplemental;
      return this;
    }

    public Builder qcFailed(Boolean qcFailed) {
      this.qcFailed = qcFailed;
      return this;
    }
  }
}
