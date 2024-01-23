package ca.on.oicr.gsi.cardea.data;

import static java.util.Objects.requireNonNull;

import java.math.BigDecimal;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

/**
 * Immutable RequisitionQcGroup
 */
@JsonDeserialize(builder = AnalysisQcGroup.Builder.class)
public class AnalysisQcGroup {

  private final BigDecimal callability;
  private final BigDecimal collapsedCoverage;
  private final String groupId;
  private final String libraryDesignCode;
  private final BigDecimal purity;
  private final String tissueOrigin;
  private final String tissueType;

  private AnalysisQcGroup(Builder builder) {
    this.tissueOrigin = requireNonNull(builder.tissueOrigin);
    this.tissueType = requireNonNull(builder.tissueType);
    this.libraryDesignCode = requireNonNull(builder.libraryDesignCode);
    this.groupId = builder.groupId;
    this.purity = builder.purity;
    this.collapsedCoverage = builder.collapsedCoverage;
    this.callability = builder.callability;
  }

  public BigDecimal getCallability() {
    return callability;
  }

  public BigDecimal getCollapsedCoverage() {
    return collapsedCoverage;
  }

  public String getGroupId() {
    return groupId;
  }

  public String getLibraryDesignCode() {
    return libraryDesignCode;
  }

  public BigDecimal getPurity() {
    return purity;
  }

  public String getTissueOrigin() {
    return tissueOrigin;
  }

  public String getTissueType() {
    return tissueType;
  }

  @JsonPOJOBuilder(withPrefix = "")
  public static class Builder {

    private BigDecimal callability;
    private BigDecimal collapsedCoverage;
    private String groupId;
    private String libraryDesignCode;
    private BigDecimal purity;
    private String tissueOrigin;
    private String tissueType;

    public AnalysisQcGroup build() {
      return new AnalysisQcGroup(this);
    }

    public Builder callability(BigDecimal callability) {
      this.callability = callability;
      return this;
    }

    public Builder collapsedCoverage(BigDecimal collapsedCoverage) {
      this.collapsedCoverage = collapsedCoverage;
      return this;
    }

    public Builder groupId(String groupId) {
      this.groupId = groupId;
      return this;
    }

    public Builder libraryDesignCode(String libraryDesignCode) {
      this.libraryDesignCode = libraryDesignCode;
      return this;
    }

    public Builder purity(BigDecimal purity) {
      this.purity = purity;
      return this;
    }

    public Builder tissueOrigin(String tissueOrigin) {
      this.tissueOrigin = tissueOrigin;
      return this;
    }

    public Builder tissueType(String tissueType) {
      this.tissueType = tissueType;
      return this;
    }

  }

}
