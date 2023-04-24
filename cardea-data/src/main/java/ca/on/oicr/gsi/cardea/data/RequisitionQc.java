package ca.on.oicr.gsi.qcgateetlapi.data;

import static java.util.Objects.requireNonNull;

import java.time.LocalDate;

import org.immutables.value.Value;

public class RequisitionQc {

  @Value.Immutable
  public static class Builder {

    private LocalDate qcDate;
    private boolean qcPassed;
    private String qcUser;

    public RequisitionQc build() {
      return new RequisitionQc(this);
    }

    public Builder qcDate(LocalDate qcDate) {
      this.qcDate = qcDate;
      return this;
    }

    public Builder qcPassed(boolean qcPassed) {
      this.qcPassed = qcPassed;
      return this;
    }

    public Builder qcUser(String qcUser) {
      this.qcUser = qcUser;
      return this;
    }
  }
  private final LocalDate qcDate;
  private final boolean qcPassed;
  private final String qcUser;

  private RequisitionQc(Builder builder) {
    this.qcPassed = requireNonNull(builder.qcPassed);
    this.qcUser = requireNonNull(builder.qcUser);
    this.qcDate = requireNonNull(builder.qcDate);
  }

  public LocalDate getQcDate() {
    return qcDate;
  }

  public String getQcUser() {
    return qcUser;
  }

  public boolean isQcPassed() {
    return qcPassed;
  }
}
