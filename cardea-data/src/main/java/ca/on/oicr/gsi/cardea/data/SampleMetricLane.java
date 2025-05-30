package ca.on.oicr.gsi.cardea.data;

import java.math.BigDecimal;

/**
 * Immutable SampleMetricLane
 */
public class SampleMetricLane {

  private final int laneNumber;
  private final BigDecimal laneValue;
  private final BigDecimal read1Value;
  private final BigDecimal read2Value;

  public SampleMetricLane(int laneNumber, BigDecimal laneValue, BigDecimal read1Value,
      BigDecimal read2Value) {
    this.laneNumber = laneNumber;
    this.laneValue = laneValue;
    this.read1Value = read1Value;
    this.read2Value = read2Value;
  }

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
