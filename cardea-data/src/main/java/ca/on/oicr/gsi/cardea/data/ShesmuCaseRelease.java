package ca.on.oicr.gsi.cardea.data;

import static java.util.Objects.requireNonNull;

import ca.on.oicr.gsi.cardea.data.CaseQc.ReleaseQcStatus;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Optional;

@JsonDeserialize(builder = ShesmuCaseRelease.Builder.class)
public class ShesmuCaseRelease {

  private final String deliverable;
  private final Optional<Instant> qcDate;
  private final Optional<ReleaseQcStatus> qcStatus;
  private final Optional<String> qcUser;

  private ShesmuCaseRelease(Builder builder) {
    this.deliverable = builder.deliverable;
    this.qcDate = builder.qcDate;
    this.qcStatus = builder.qcStatus;
    this.qcUser = builder.qcUser;
  }

  public String getDeliverable() {
    return deliverable;
  }

  public Optional<Instant> getQcDate() {
    return qcDate;
  }

  public Optional<ReleaseQcStatus> getQcStatus() {
    return qcStatus;
  }

  public Optional<String> getQcUser() {
    return qcUser;
  }

  @JsonPOJOBuilder(withPrefix = "")
  public static class Builder {

    private String deliverable;
    private Optional<Instant> qcDate;
    private Optional<ReleaseQcStatus> qcStatus;
    private Optional<String> qcUser;

    public ShesmuCaseRelease build() { return new ShesmuCaseRelease(this); }

    public Builder deliverable(String deliverable) {
      this.deliverable = deliverable;
      return this;
    }

    public Builder qcDate(Instant qcDate) {
      this.qcDate = qcDate == null ? Optional.empty() : Optional.of(qcDate);
      return this;
    }

    public Builder qcDateLocal(LocalDate qcDate) {
      this.qcDate = convertLocalDate(qcDate);
      return this;
    }

    public Builder qcStatus(ReleaseQcStatus qcStatus) {
      this.qcStatus = qcStatus == null ? Optional.empty(): Optional.of(qcStatus);
      return this;
    }

    public Builder qcUser(String qcUser) {
      this.qcUser = qcUser == null ? Optional.empty() : Optional.of(qcUser);
      return this;
    }

    private Optional<Instant> convertLocalDate(LocalDate localDate) {
      if (localDate == null) {
        return Optional.empty();
      }
      return Optional.of(
          ZonedDateTime.of(
              localDate,
              LocalTime.MIDNIGHT,
              ZoneId.of("UTC")).toInstant());
    }
  }
}
