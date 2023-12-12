package ca.on.oicr.gsi.cardea.data;

public enum DeliverableType {

  CLINICAL_REPORT("Clinical Report"), DATA_RELEASE("Data Release");

  private final String label;

  private DeliverableType(String label) {
    this.label = label;
  }

  public String getLabel() {
    return label;
  }
}
