package ca.on.oicr.gsi.cardea.data;

public enum CaseStatus {
  ACTIVE, COMPLETED, STOPPED;

  public String getLabel() {
    return name().toLowerCase();
  }
}