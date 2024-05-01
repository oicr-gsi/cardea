package ca.on.oicr.gsi.cardea.data;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum CaseStatus {
  ACTIVE, COMPLETED, STOPPED;

  @JsonValue
  public String getLabel() {
    return name().toLowerCase();
  }

  @JsonCreator
  public static CaseStatus fromString(String string) {
    return string == null ? null : CaseStatus.valueOf(string.toUpperCase());
  }
}
