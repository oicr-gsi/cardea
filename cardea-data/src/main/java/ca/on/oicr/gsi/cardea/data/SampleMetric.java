package ca.on.oicr.gsi.cardea.data;

import static java.util.Objects.requireNonNull;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.Set;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

/**
 * Immutable SampleMetric
 */
@JsonDeserialize(builder = SampleMetric.Builder.class)
public class SampleMetric {

  public static enum MetricLevel {
    SAMPLE, RUN, LANE
  }

  private final String name;
  private final ThresholdType thresholdType;
  private final BigDecimal minimum;
  private final BigDecimal maximum;
  private final MetricLevel metricLevel;
  private final Boolean preliminary;
  private final BigDecimal value;
  private final Set<SampleMetricLane> laneValues;
  private final Boolean qcPassed;
  private final String units;

  private SampleMetric(Builder builder) {
    this.name = requireNonNull(builder.name);
    this.thresholdType = requireNonNull(builder.thresholdType);
    this.minimum = builder.minimum;
    this.maximum = builder.maximum;
    this.metricLevel = requireNonNull(builder.metricLevel);
    this.preliminary = builder.preliminary;
    this.value = builder.value;
    this.laneValues =
        builder.laneValues == null ? null : Collections.unmodifiableSet(builder.laneValues);
    this.qcPassed = builder.qcPassed;
    this.units = builder.units;
  }

  public String getName() {
    return name;
  }

  public ThresholdType getThresholdType() {
    return thresholdType;
  }

  public BigDecimal getMinimum() {
    return minimum;
  }

  public BigDecimal getMaximum() {
    return maximum;
  }

  public MetricLevel getMetricLevel() {
    return metricLevel;
  }

  public Boolean getPreliminary() {
    return preliminary;
  }

  public BigDecimal getValue() {
    return value;
  }

  public Set<SampleMetricLane> getLaneValues() {
    return laneValues;
  }

  public Boolean getQcPassed() {
    return qcPassed;
  }

  public String getUnits() {
    return units;
  }

  @JsonPOJOBuilder(withPrefix = "")
  public static class Builder {

    private String name;
    private ThresholdType thresholdType;
    private BigDecimal minimum;
    private BigDecimal maximum;
    private MetricLevel metricLevel;
    private Boolean preliminary;
    private BigDecimal value;
    private Set<SampleMetricLane> laneValues;
    private Boolean qcPassed;
    private String units;

    public Builder name(String name) {
      this.name = name;
      return this;
    }

    public Builder thresholdType(ThresholdType thresholdType) {
      this.thresholdType = thresholdType;
      return this;
    }

    public Builder minimum(BigDecimal minimum) {
      this.minimum = minimum;
      return this;
    }

    public Builder maximum(BigDecimal maximum) {
      this.maximum = maximum;
      return this;
    }

    public Builder metricLevel(MetricLevel metricLevel) {
      this.metricLevel = metricLevel;
      return this;
    }

    public Builder preliminary(Boolean preliminary) {
      this.preliminary = preliminary;
      return this;
    }

    public Builder value(BigDecimal value) {
      this.value = value;
      return this;
    }

    public Builder laneValues(Set<SampleMetricLane> laneValues) {
      this.laneValues = laneValues;
      return this;
    }

    public Builder qcPassed(Boolean qcPassed) {
      this.qcPassed = qcPassed;
      return this;
    }

    public Builder units(String units) {
      this.units = units;
      return this;
    }

    public SampleMetric build() {
      return new SampleMetric(this);
    }

  }
}
