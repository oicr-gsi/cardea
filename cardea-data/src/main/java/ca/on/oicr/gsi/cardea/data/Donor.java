package ca.on.oicr.gsi.cardea.data;

import static java.util.Objects.requireNonNull;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

/**
 * Immutable Donor
 */
@JsonDeserialize(builder = Donor.Builder.class)
public class Donor {

  private final String externalName;
  private final String id;
  private final String name;

  private Donor(Builder builder) {
    this.id = requireNonNull(builder.id);
    this.name = requireNonNull(builder.name);
    this.externalName = requireNonNull(builder.externalName);
  }

  public String getExternalName() {
    return externalName;
  }

  public String getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  @JsonPOJOBuilder(withPrefix = "")
  public static class Builder {

    private String externalName;
    private String id;
    private String name;

    public Donor build() {
      return new Donor(this);
    }

    public Builder externalName(String externalName) {
      this.externalName = externalName;
      return this;
    }

    public Builder id(String id) {
      this.id = id;
      return this;
    }

    public Builder name(String name) {
      this.name = name;
      return this;
    }

  }
}
