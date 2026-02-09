package ca.on.oicr.gsi.cardea.data;

public enum ArchivalStatus {
  PENDING("Pending"), //
  STARTED("Started"), //
  PAUSED("Paused"), //
  COMPLETE("Complete");

  private final String label;

  private ArchivalStatus(String label) {
    this.label = label;
  }

  public String getLabel() {
    return label;
  }
}
