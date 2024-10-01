package ca.on.oicr.gsi.cardea.data;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

import java.time.*;
import java.util.Optional;
import java.util.Set;

import static java.util.Collections.unmodifiableSet;
import static java.util.Objects.requireNonNull;

/**
 * Immutable ShesmuCase
 */
@JsonDeserialize(builder = ShesmuDetailedCase.Builder.class)
public class ShesmuDetailedCase {

  private final String assayName;
  private final String assayVersion;
  private final String caseIdentifier;
  private final CaseStatus caseStatus;
  private final boolean isStopped;
  private final boolean isPaused;
  private final Optional<Instant> completedDate;
  private final long requisitionId;
  private final String requisitionName;
  private final Set<ShesmuTest> sequencing;

  private ShesmuDetailedCase(Builder builder) {
    this.assayName = requireNonNull(builder.assayName);
    this.assayVersion = requireNonNull(builder.assayVersion);
    this.caseIdentifier = requireNonNull(builder.caseIdentifier);
    this.caseStatus = requireNonNull(builder.caseStatus);
    this.completedDate = builder.completedDate;
    this.isStopped = builder.isStopped;
    this.isPaused = builder.isPaused;
    this.requisitionId = requireNonNull(builder.requisitionId);
    this.requisitionName = requireNonNull(builder.requisitionName);
    this.sequencing = unmodifiableSet(requireNonNull(builder.sequencing));
  }

  public String getAssayName() {
    return assayName;
  }

  public String getAssayVersion() {
    return assayVersion;
  }

  public String getCaseIdentifier() {
    return caseIdentifier;
  }

  public CaseStatus getCaseStatus() {
    return caseStatus;
  }

  public boolean getIsStopped() {
    return isStopped;
  }

  public boolean getIsPaused() {
    return isPaused;
  }

  public Optional<Instant> getCompletedDate() {
    return completedDate;
  }

  public long getRequisitionId() {
    return requisitionId;
  }

  public String getRequisitionName() {
    return requisitionName;
  }

  public Set<ShesmuTest> getSequencing() {
    return sequencing;
  }

  @JsonPOJOBuilder(withPrefix = "")
  public static class Builder {

    private String assayName;
    private String assayVersion;
    private String caseIdentifier;
    private CaseStatus caseStatus;
    private boolean isPaused;
    private boolean isStopped;
    private Optional<Instant> completedDate;
    private long requisitionId;
    private String requisitionName;
    private Set<ShesmuTest> sequencing;

    public ShesmuDetailedCase build() {
      return new ShesmuDetailedCase(this);
    }

    public Builder assayName(String assayName) {
      this.assayName = assayName;
      return this;
    }

    public Builder assayVersion(String assayVersion) {
      this.assayVersion = assayVersion;
      return this;
    }

    public Builder caseStatus(CaseStatus caseStatus) {
      this.caseStatus = caseStatus;
      return this;
    }

    public Builder isPaused(boolean isPaused) {
      this.isPaused = isPaused;
      return this;
    }

    public Builder isStopped(boolean isStopped) {
      this.isStopped = isStopped;
      return this;
    }

    public Builder caseIdentifier(String caseIdentifier) {
      this.caseIdentifier = caseIdentifier;
      return this;
    }

    public Builder completedDate(Instant completedDate) {
      this.completedDate = completedDate == null ? Optional.empty() : Optional.of(completedDate);
      return this;
    }

    public Builder completedDateLocal(LocalDate completedDate) {
      this.completedDate = convertCompletedDate(completedDate);
      return this;
    }

    public Builder requisitionId(long requisitionId) {
      this.requisitionId = requisitionId;
      return this;
    }

    public Builder requisitionName(String requisitionName) {
      this.requisitionName = requisitionName;
      return this;
    }

    public Builder sequencing(Set<ShesmuTest> sequencing){
      this.sequencing = sequencing;
      return this;
    }

    private Optional<Instant> convertCompletedDate(LocalDate completedDate) {
      if (completedDate == null) {
        return Optional.empty();
      }
      return Optional.of(
          ZonedDateTime.of(
              completedDate,
              LocalTime.MIDNIGHT,
              ZoneId.of("UTC")).toInstant());
    }
  }
}
