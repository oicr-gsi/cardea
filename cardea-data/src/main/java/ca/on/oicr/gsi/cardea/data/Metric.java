package ca.on.oicr.gsi.cardea.data;

import static java.util.Objects.requireNonNull;

import java.math.BigDecimal;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

/**
 * Immutable Metric
 */
@JsonDeserialize(builder = Metric.Builder.class)
public class Metric {

  private final String containerModel;
  private final BigDecimal maximum;
  private final BigDecimal minimum;
  private final String name;
  private final boolean negateTissueType;
  private final String nucleicAcidType;
  private final Integer readLength;
  private final Integer readLength2;
  private final Integer sortPriority;
  private final ThresholdType thresholdType;
  private final String tissueMaterial;
  private final String tissueOrigin;
  private final String tissueType;
  private final String units;

  private Metric(Builder builder) {
    this.name = requireNonNull(builder.name);
    this.sortPriority = builder.sortPriority;
    this.minimum = builder.minimum;
    this.maximum = builder.maximum;
    this.units = builder.units;
    this.tissueMaterial = builder.tissueMaterial;
    this.tissueOrigin = builder.tissueOrigin;
    this.tissueType = builder.tissueType;
    this.negateTissueType = builder.negateTissueType;
    this.nucleicAcidType = builder.nucleicAcidType;
    this.containerModel = builder.containerModel;
    this.readLength = builder.readLength;
    this.readLength2 = builder.readLength2;
    this.thresholdType = requireNonNull(builder.thresholdType);
  }

  public String getContainerModel() {
    return containerModel;
  }

  public BigDecimal getMaximum() {
    return maximum;
  }

  public BigDecimal getMinimum() {
    return minimum;
  }

  public String getName() {
    return name;
  }

  public String getNucleicAcidType() {
    return nucleicAcidType;
  }

  public Integer getReadLength() {
    return readLength;
  }

  public Integer getReadLength2() {
    return readLength2;
  }

  public Integer getSortPriority() {
    return sortPriority;
  }

  public ThresholdType getThresholdType() {
    return thresholdType;
  }

  public String getTissueMaterial() {
    return tissueMaterial;
  }

  public String getTissueOrigin() {
    return tissueOrigin;
  }

  public String getTissueType() {
    return tissueType;
  }

  public String getUnits() {
    return units;
  }

  public boolean isNegateTissueType() {
    return negateTissueType;
  }

  @JsonPOJOBuilder(withPrefix = "")
  public static class Builder {

    private String containerModel;
    private BigDecimal maximum;
    private BigDecimal minimum;
    private String name;
    private boolean negateTissueType;
    private String nucleicAcidType;
    private Integer readLength;
    private Integer readLength2;
    private Integer sortPriority;
    private ThresholdType thresholdType;
    private String tissueMaterial;
    private String tissueOrigin;
    private String tissueType;
    private String units;

    public Metric build() {
      return new Metric(this);
    }

    public Builder containerModel(String containerModel) {
      this.containerModel = containerModel;
      return this;
    }

    public Builder maximum(BigDecimal maximum) {
      this.maximum = maximum;
      return this;
    }

    public Builder minimum(BigDecimal minimum) {
      this.minimum = minimum;
      return this;
    }

    public Builder name(String name) {
      this.name = name;
      return this;
    }

    public Builder negateTissueType(boolean negateTissueType) {
      this.negateTissueType = negateTissueType;
      return this;
    }

    public Builder nucleicAcidType(String nucleicAcidType) {
      this.nucleicAcidType = nucleicAcidType;
      return this;
    }

    public Builder readLength(Integer readLength) {
      this.readLength = readLength;
      return this;
    }

    public Builder readLength2(Integer readLength2) {
      this.readLength2 = readLength2;
      return this;
    }

    public Builder sortPriority(Integer sortPriority) {
      this.sortPriority = sortPriority;
      return this;
    }

    public Builder thresholdType(ThresholdType thresholdType) {
      this.thresholdType = thresholdType;
      return this;
    }

    public Builder tissueMaterial(String tissueMaterial) {
      this.tissueMaterial = tissueMaterial;
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

    public Builder units(String units) {
      this.units = units;
      return this;
    }

  }

}
