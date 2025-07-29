package ca.on.oicr.gsi.cardea.data;

import static java.util.Objects.requireNonNull;

import ca.on.oicr.gsi.cardea.data.CaseQc.AnalysisReviewQcStatus;
import ca.on.oicr.gsi.cardea.data.CaseQc.ReleaseApprovalQcStatus;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Optional;
import java.util.Set;

@JsonDeserialize(builder = ShesmuCaseDeliverable.Builder.class)
public class ShesmuCaseDeliverable {

  private final String deliverableCategory;
  private final boolean analysisReviewSkipped;
  private final Optional<Instant> analysisReviewQcDate;
  private final Optional<AnalysisReviewQcStatus> analysisReviewQcStatus;
  private final Optional<String> analysisReviewQcUser;
  private final Optional<Instant> releaseApprovalQcDate;
  private final Optional<ReleaseApprovalQcStatus> releaseApprovalQcStatus;
  private final Optional<String> releaseApprovalQcUser;
  private final Set<ShesmuCaseRelease> releases;

  private ShesmuCaseDeliverable(Builder builder) {
    this.deliverableCategory = requireNonNull(builder.deliverableCategory);
    this.analysisReviewSkipped = builder.analysisReviewSkipped;
    this.analysisReviewQcDate = builder.analysisReviewQcDate;
    this.analysisReviewQcStatus = builder.analysisReviewQcStatus;
    this.analysisReviewQcUser = builder.analysisReviewQcUser;
    this.releaseApprovalQcDate = builder.releaseApprovalQcDate;
    this.releaseApprovalQcStatus = builder.releaseApprovalQcStatus;
    this.releaseApprovalQcUser = builder.releaseApprovalQcUser;
    this.releases = builder.releases;
  }

  public String getDeliverableCategory() {
    return deliverableCategory;
  }

  public boolean isAnalysisReviewSkipped() {
    return analysisReviewSkipped;
  }

  public Optional<Instant> getAnalysisReviewQcDate() {
    return analysisReviewQcDate;
  }

  public Optional<AnalysisReviewQcStatus> getAnalysisReviewQcStatus() {
    return analysisReviewQcStatus;
  }

  public Optional<String> getAnalysisReviewQcUser() {
    return analysisReviewQcUser;
  }

  public Optional<Instant> getReleaseApprovalQcDate() {
    return releaseApprovalQcDate;
  }

  public Optional<ReleaseApprovalQcStatus> getReleaseApprovalQcStatus() {
    return releaseApprovalQcStatus;
  }

  public Optional<String> getReleaseApprovalQcUser() {
    return releaseApprovalQcUser;
  }

  public Set<ShesmuCaseRelease> getReleases() {
    return releases;
  }

  @JsonPOJOBuilder(withPrefix = "")
  public static class Builder {

    private String deliverableCategory;
    private boolean analysisReviewSkipped;
    private Optional<Instant> analysisReviewQcDate;
    private Optional<AnalysisReviewQcStatus> analysisReviewQcStatus;
    private Optional<String> analysisReviewQcUser;
    private Optional<Instant> releaseApprovalQcDate;
    private Optional<ReleaseApprovalQcStatus> releaseApprovalQcStatus;
    private Optional<String> releaseApprovalQcUser;
    private Set<ShesmuCaseRelease> releases;

    public ShesmuCaseDeliverable build() { return new ShesmuCaseDeliverable(this); }

    public Builder deliverableCategory(String deliverableCategory) {
      this.deliverableCategory = deliverableCategory;
      return this;
    }

    public Builder analysisReviewSkipped(boolean isAnalysisReviewSkipped) {
      this.analysisReviewSkipped = isAnalysisReviewSkipped;
      return this;
    }

    public Builder analysisReviewQcDate(Instant analysisReviewQcDate) {
      this.analysisReviewQcDate = analysisReviewQcDate == null ? Optional.empty() : Optional.of(analysisReviewQcDate);
      return this;
    }

    public Builder analysisReviewQcDateLocal(LocalDate analysisReviewQcDate) {
      this.analysisReviewQcDate = convertLocalDate(analysisReviewQcDate);
      return this;
    }

    public Builder analysisReviewQcStatus(AnalysisReviewQcStatus analysisReviewQcStatus) {
      this.analysisReviewQcStatus = analysisReviewQcStatus == null ? Optional.empty() : Optional.of(analysisReviewQcStatus);
      return this;
    }

    public Builder analysisReviewQcUser(String analysisReviewQcUser) {
      this.analysisReviewQcUser = analysisReviewQcUser == null ? Optional.empty() : Optional.of(analysisReviewQcUser);
      return this;
    }

    public Builder releaseApprovalQcDate(Instant releaseApprovalQcDate) {
      this.releaseApprovalQcDate = releaseApprovalQcDate == null ? Optional.empty() : Optional.of(releaseApprovalQcDate);
      return this;
    }

    public Builder releaseApprovalQcDateLocal(LocalDate releaseApprovalQcDate) {
      this.releaseApprovalQcDate = convertLocalDate(releaseApprovalQcDate);
      return this;
    }

    public Builder releaseApprovalQcStatus(ReleaseApprovalQcStatus releaseApprovalQcStatus) {
      this.releaseApprovalQcStatus = releaseApprovalQcStatus == null ? Optional.empty() : Optional.of(releaseApprovalQcStatus);
      return this;
    }

    public Builder releaseApprovalQcUser(String releaseApprovalQcUser) {
      this.releaseApprovalQcUser = releaseApprovalQcUser == null ? Optional.empty() : Optional.of(releaseApprovalQcUser);
      return this;
    }

    public Builder releases(Set<ShesmuCaseRelease> releases) {
      this.releases = releases;
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
