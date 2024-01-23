package ca.on.oicr.gsi.cardea.data;

import static java.util.Collections.unmodifiableSet;
import static java.util.Objects.requireNonNull;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import java.util.Optional;
import java.util.Set;

/**
 * Immutable ShesmuCase
 */
public class ShesmuCase {

  private final String assayName;
  private final String assayVersion;
  private final String caseIdentifier;
  private final String caseStatus;
  private final Optional<Instant> completedDate;
  private final Set<String> limsIds;
  private final long requisitionId;
  private final String requisitionName;

  private ShesmuCase(Builder builder) {
    this.assayName = requireNonNull(builder.assayName);
    this.assayVersion = requireNonNull(builder.assayVersion);
    this.caseIdentifier = requireNonNull(builder.caseIdentifier);
    this.caseStatus = requireNonNull(builder.caseStatus.getLabel());
    this.completedDate = builder.completedDate;
    this.limsIds = unmodifiableSet(requireNonNull(builder.limsIds));
    this.requisitionId = requireNonNull(builder.requisitionId);
    this.requisitionName = requireNonNull(builder.requisitionName);
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

  public String getCaseStatus() {
    return caseStatus;
  }

  public Optional<Instant> getCompletedDate() {
    return completedDate;
  }

  public Set<String> getLimsIds() {
    return limsIds;
  }

  public long getRequisitionId() {
    return requisitionId;
  }

  public String getRequisitionName() {
    return requisitionName;
  }

  public static class Builder {

    private String assayName;
    private String assayVersion;
    private String caseIdentifier;
    private CaseStatus caseStatus;
    private Optional<Instant> completedDate;
    private Set<String> limsIds;
    private long requisitionId;
    private String requisitionName;

    public ShesmuCase build() {
      return new ShesmuCase(this);
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

    public Builder caseIdentifier(String caseIdentifier) {
      this.caseIdentifier = caseIdentifier;
      return this;
    }

    public Builder completedDate(LocalDate completedDate) {
      this.completedDate = convertCompletedDate(completedDate);
      return this;
    }

    public Builder limsIds(Set<String> limsIds) {
      this.limsIds = limsIds;
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
