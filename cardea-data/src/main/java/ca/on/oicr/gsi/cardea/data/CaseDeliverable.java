package ca.on.oicr.gsi.cardea.data;

import static java.util.Objects.requireNonNull;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import ca.on.oicr.gsi.CaseRelease;

@JsonDeserialize(builder = CaseDeliverable.Builder.class)
public class CaseDeliverable {

  private final DeliverableType deliverableType;
  private final LocalDate analysisReviewQcDate;
  private final Boolean analysisReviewQcPassed;
  private final String analysisReviewQcUser;
  private final String analysisReviewQcNote;
  private final LocalDate releaseApprovalQcDate;
  private final Boolean releaseApprovalQcPassed;
  private final String releaseApprovalQcUser;
  private final String releaseApprovalQcNote;
  private final List<CaseRelease> releases;
  private final LocalDate latestActivityDate;

  private CaseDeliverable(Builder builder) {
    this.deliverableType = requireNonNull(builder.deliverableType);
    this.analysisReviewQcDate = builder.analysisReviewQcDate;
    this.analysisReviewQcPassed = builder.analysisReviewQcPassed;
    this.analysisReviewQcUser = builder.analysisReviewQcUser;
    this.analysisReviewQcNote = builder.analysisReviewQcNote;
    this.releaseApprovalQcDate = builder.releaseApprovalQcDate;
    this.releaseApprovalQcPassed = builder.releaseApprovalQcPassed;
    this.releaseApprovalQcUser = builder.releaseApprovalQcUser;
    this.releaseApprovalQcNote = builder.releaseApprovalQcNote;
    this.releases = builder.releases == null ? Collections.emptyList()
        : Collections.unmodifiableList(builder.releases);
    this.latestActivityDate = Stream
        .concat(releases == null ? Stream.empty() : releases.stream().map(CaseRelease::getQcDate),
            Stream.of(analysisReviewQcDate, releaseApprovalQcDate))
        .filter(Objects::nonNull)
        .max(LocalDate::compareTo)
        .orElse(null);
  }

  public DeliverableType getDeliverableType() {
    return deliverableType;
  }

  public LocalDate getAnalysisReviewQcDate() {
    return analysisReviewQcDate;
  }

  public Boolean getAnalysisReviewQcPassed() {
    return analysisReviewQcPassed;
  }

  public String getAnalysisReviewQcUser() {
    return analysisReviewQcUser;
  }

  public String getAnalysisReviewQcNote() {
    return analysisReviewQcNote;
  }

  public LocalDate getReleaseApprovalQcDate() {
    return releaseApprovalQcDate;
  }

  public Boolean getReleaseApprovalQcPassed() {
    return releaseApprovalQcPassed;
  }

  public String getReleaseApprovalQcUser() {
    return releaseApprovalQcUser;
  }

  public String getReleaseApprovalQcNote() {
    return releaseApprovalQcNote;
  }

  public List<CaseRelease> getReleases() {
    return releases;
  }

  public LocalDate getLatestActivityDate() {
    return latestActivityDate;
  }


  @JsonPOJOBuilder(withPrefix = "")
  public static class Builder {

    private DeliverableType deliverableType;
    private LocalDate analysisReviewQcDate;
    private Boolean analysisReviewQcPassed;
    private String analysisReviewQcUser;
    private String analysisReviewQcNote;
    private LocalDate releaseApprovalQcDate;
    private Boolean releaseApprovalQcPassed;
    private String releaseApprovalQcUser;
    private String releaseApprovalQcNote;
    private List<CaseRelease> releases;

    public Builder deliverableType(DeliverableType deliverableType) {
      this.deliverableType = deliverableType;
      return this;
    }

    public Builder analysisReviewQcDate(LocalDate analysisReviewQcDate) {
      this.analysisReviewQcDate = analysisReviewQcDate;
      return this;
    }

    public Builder analysisReviewQcPassed(Boolean analysisReviewQcPassed) {
      this.analysisReviewQcPassed = analysisReviewQcPassed;
      return this;
    }

    public Builder analysisReviewQcUser(String analysisReviewQcUser) {
      this.analysisReviewQcUser = analysisReviewQcUser;
      return this;
    }

    public Builder analysisReviewQcNote(String analysisReviewQcNote) {
      this.analysisReviewQcNote = analysisReviewQcNote;
      return this;
    }

    public Builder releaseApprovalQcDate(LocalDate releaseApprovalQcDate) {
      this.releaseApprovalQcDate = releaseApprovalQcDate;
      return this;
    }

    public Builder releaseApprovalQcPassed(Boolean releaseApprovalQcPassed) {
      this.releaseApprovalQcPassed = releaseApprovalQcPassed;
      return this;
    }

    public Builder releaseApprovalQcUser(String releaseApprovalQcUser) {
      this.releaseApprovalQcUser = releaseApprovalQcUser;
      return this;
    }

    public Builder releaseApprovalQcNote(String releaseApprovalQcNote) {
      this.releaseApprovalQcNote = releaseApprovalQcNote;
      return this;
    }

    public Builder releases(List<CaseRelease> releases) {
      this.releases = releases;
      return this;
    }

    public CaseDeliverable build() {
      return new CaseDeliverable(this);
    }

  }

}
