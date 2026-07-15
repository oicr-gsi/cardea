package ca.on.oicr.gsi.cardea.data;

import java.math.BigDecimal;

/** Immutable SampleMetricLane */
public record SampleMetricLane(
    int laneNumber, BigDecimal laneValue, BigDecimal read1Value, BigDecimal read2Value) {

  public int getLaneNumber() {
    return laneNumber;
  }

  public BigDecimal getLaneValue() {
    return laneValue;
  }

  public BigDecimal getRead1Value() {
    return read1Value;
  }

  public BigDecimal getRead2Value() {
    return read2Value;
  }
}
