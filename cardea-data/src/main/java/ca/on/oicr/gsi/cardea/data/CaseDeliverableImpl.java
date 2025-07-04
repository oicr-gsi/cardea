package ca.on.oicr.gsi.cardea.data;

import static java.util.Objects.requireNonNull;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import ca.on.oicr.gsi.cardea.data.CaseQc.AnalysisReviewQcStatus;
import ca.on.oicr.gsi.cardea.data.CaseQc.ReleaseApprovalQcStatus;

@JsonDeserialize(builder = CaseDeliverableImpl.Builder.class)
public class CaseDeliverableImpl implements CaseDeliverable {

  private final String deliverableType;
  private final boolean analysisReviewSkipped;
  private final LocalDate analysisReviewQcDate;
  private final AnalysisReviewQcStatus analysisReviewQcStatus;
  private final String analysisReviewQcUser;
  private final String analysisReviewQcNote;
  private final LocalDate releaseApprovalQcDate;
  private final ReleaseApprovalQcStatus releaseApprovalQcStatus;
  private final String releaseApprovalQcUser;
  private final String releaseApprovalQcNote;
  private final List<CaseRelease> releases;
  private final LocalDate latestActivityDate;
  private final int analysisReviewDaysSpent;
  private final int releaseApprovalDaysSpent;
  private final int releaseDaysSpent;
  private final int deliverableDaysSpent;

  private CaseDeliverableImpl(Builder builder) {
    this.deliverableType = requireNonNull(builder.deliverableCategory);
    this.analysisReviewSkipped = builder.analysisReviewSkipped;
    this.analysisReviewQcDate = builder.analysisReviewQcDate;
    this.analysisReviewQcStatus = builder.analysisReviewQcStatus;
    this.analysisReviewQcUser = builder.analysisReviewQcUser;
    this.analysisReviewQcNote = builder.analysisReviewQcNote;
    this.releaseApprovalQcDate = builder.releaseApprovalQcDate;
    this.releaseApprovalQcStatus = builder.releaseApprovalQcStatus;
    this.releaseApprovalQcUser = builder.releaseApprovalQcUser;
    this.releaseApprovalQcNote = builder.releaseApprovalQcNote;
    this.releases = builder.releases == null ? Collections.emptyList()
        : Collections.unmodifiableList(builder.releases);
    this.analysisReviewDaysSpent = builder.analysisReviewDaysSpent;
    this.releaseApprovalDaysSpent = builder.releaseApprovalDaysSpent;
    this.releaseDaysSpent = builder.releaseDaysSpent;
    this.deliverableDaysSpent = builder.deliverableDaysSpent;

    if (builder.latestActivityDate != null) {
      this.latestActivityDate = builder.latestActivityDate;
    } else {
      this.latestActivityDate = Stream
          .concat(
              releases == null ? Stream.empty() : releases.stream().map(CaseRelease::getQcDate),
              Stream.of(analysisReviewQcDate, releaseApprovalQcDate))
          .filter(Objects::nonNull)
          .max(LocalDate::compareTo)
          .orElse(null);
    }
  }

  @Override
  public String getDeliverableCategory() {
    return deliverableType;
  }

  @Override
  public boolean isAnalysisReviewSkipped() {
    return analysisReviewSkipped;
  }

  @Override
  public LocalDate getAnalysisReviewQcDate() {
    return analysisReviewQcDate;
  }

  @Override
  public AnalysisReviewQcStatus getAnalysisReviewQcStatus() {
    return analysisReviewQcStatus;
  }

  @Override
  public String getAnalysisReviewQcUser() {
    return analysisReviewQcUser;
  }

  @Override
  public String getAnalysisReviewQcNote() {
    return analysisReviewQcNote;
  }

  @Override
  public LocalDate getReleaseApprovalQcDate() {
    return releaseApprovalQcDate;
  }

  @Override
  public ReleaseApprovalQcStatus getReleaseApprovalQcStatus() {
    return releaseApprovalQcStatus;
  }

  @Override
  public String getReleaseApprovalQcUser() {
    return releaseApprovalQcUser;
  }

  @Override
  public String getReleaseApprovalQcNote() {
    return releaseApprovalQcNote;
  }

  @Override
  public List<CaseRelease> getReleases() {
    return releases;
  }

  @Override
  public int getAnalysisReviewDaysSpent() {
    return analysisReviewDaysSpent;
  }

  @Override
  public int getReleaseApprovalDaysSpent() {
    return releaseApprovalDaysSpent;
  }

  @Override
  public int getReleaseDaysSpent() {
    return releaseDaysSpent;
  }

  @Override
  public int getDeliverableDaysSpent() {
    return deliverableDaysSpent;
  }

  @Override
  public LocalDate getLatestActivityDate() {
    return latestActivityDate;
  }

  @JsonPOJOBuilder(withPrefix = "")
  public static class Builder {

    private String deliverableCategory;
    private boolean analysisReviewSkipped;
    private LocalDate analysisReviewQcDate;
    private AnalysisReviewQcStatus analysisReviewQcStatus;
    private String analysisReviewQcUser;
    private String analysisReviewQcNote;
    private LocalDate releaseApprovalQcDate;
    private ReleaseApprovalQcStatus releaseApprovalQcStatus;
    private String releaseApprovalQcUser;
    private String releaseApprovalQcNote;
    private List<CaseRelease> releases;
    private int analysisReviewDaysSpent;
    private int releaseApprovalDaysSpent;
    private int releaseDaysSpent;
    private int deliverableDaysSpent;
    private LocalDate latestActivityDate;

    public Builder deliverableCategory(String deliverableCategory) {
      this.deliverableCategory = deliverableCategory;
      return this;
    }

    public Builder analysisReviewSkipped(boolean analysisReviewSkipped) {
      this.analysisReviewSkipped = analysisReviewSkipped;
      return this;
    }

    public Builder analysisReviewQcDate(LocalDate analysisReviewQcDate) {
      this.analysisReviewQcDate = analysisReviewQcDate;
      return this;
    }

    public Builder analysisReviewQcStatus(AnalysisReviewQcStatus analysisReviewQcStatus) {
      this.analysisReviewQcStatus = analysisReviewQcStatus;
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

    public Builder releaseApprovalQcStatus(ReleaseApprovalQcStatus releaseApprovalQcPassed) {
      this.releaseApprovalQcStatus = releaseApprovalQcPassed;
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

    public Builder analysisReviewDaysSpent(int analysisReviewDaysSpent) {
      this.analysisReviewDaysSpent = analysisReviewDaysSpent;
      return this;
    }

    public Builder releaseApprovalDaysSpent(int releaseApprovalDaysSpent) {
      this.releaseApprovalDaysSpent = releaseApprovalDaysSpent;
      return this;
    }

    public Builder releaseDaysSpent(int releaseDaysSpent) {
      this.releaseDaysSpent = releaseDaysSpent;
      return this;
    }

    public Builder deliverableDaysSpent(int deliverableDaysSpent) {
      this.deliverableDaysSpent = deliverableDaysSpent;
      return this;
    }

    public Builder latestActivityDate(LocalDate latestActivityDate) {
      this.latestActivityDate = latestActivityDate;
      return this;
    }

    public CaseDeliverableImpl build() {
      return new CaseDeliverableImpl(this);
    }

  }

}
