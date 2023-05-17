package ca.on.oicr.gsi.cardea.data;

import java.util.Set;

import static java.util.Collections.unmodifiableSet;
import static java.util.Objects.requireNonNull;

/**
 * Immutable DjerbaCases
 * 
 * A set of Cases that all have the same requisition (and therefore the same
 * assay)
 */
public class DjerbaCases {

  private final String assayName;
  private final String assayVersion;
  private final Set<Case> cases;

  private DjerbaCases(Builder builder) {
    // older assays might be null
    this.assayName = builder.assayName;
    this.assayVersion = builder.assayVersion;
    this.cases = unmodifiableSet(requireNonNull(builder.cases));
  }

  public String getAssayName() {
    return assayName;
  }

  public String getAssayVersion() {
    return assayVersion;
  }

  public Set<Case> getCases() {
    return cases;
  }

  public static class Builder {

    private String assayName;
    private String assayVersion;
    private Set<Case> cases;

    public DjerbaCases build() {
      return new DjerbaCases(this);
    }

    public Builder assayName(String assayName) {
      this.assayName = assayName;
      return this;
    }

    public Builder assayVersion(String assayVersion) {
      this.assayVersion = assayVersion;
      return this;
    }

    public Builder cases(Set<Case> cases) {
      this.cases = cases;
      return this;
    }
  }
}