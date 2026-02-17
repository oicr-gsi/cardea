package ca.on.oicr.gsi.cardea.data;

public enum ArchivingStatus {
  PENDING("Pending"), //
  STARTED("Started"), //
  PAUSED("Paused"), //
  COMPLETE("Complete");

  private final String label;

  private ArchivingStatus(String label) {
    this.label = label;
  }

  public String getLabel() {
    return label;
  }
}
