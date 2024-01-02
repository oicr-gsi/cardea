package ca.on.oicr.gsi.cardea.data;

import static java.util.Objects.requireNonNull;

import java.time.LocalDate;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

@JsonDeserialize(builder = Deliverable.Builder.class)
public class Deliverable {

  private final DeliverableType deliverableType;
  private final LocalDate analysisReviewQcDate;
  private final Boolean analysisReviewQcPassed;
  private final String analysisReviewQcUser;
  private final LocalDate releaseApprovalQcDate;
  private final Boolean releaseApprovalQcPassed;
  private final String releaseApprovalQcUser;
  private final LocalDate releaseQcDate;
  private final Boolean releaseQcPassed;
  private final String releaseQcUser;

  private Deliverable(Builder builder) {
    this.deliverableType = requireNonNull(builder.deliverableType);
    this.analysisReviewQcDate = builder.analysisReviewQcDate;
    this.analysisReviewQcPassed = builder.analysisReviewQcPassed;
    this.analysisReviewQcUser = builder.analysisReviewQcUser;
    this.releaseApprovalQcDate = builder.releaseApprovalQcDate;
    this.releaseApprovalQcPassed = builder.releaseApprovalQcPassed;
    this.releaseApprovalQcUser = builder.releaseApprovalQcUser;
    this.releaseQcDate = builder.releaseQcDate;
    this.releaseQcPassed = builder.releaseQcPassed;
    this.releaseQcUser = builder.releaseQcUser;
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

  public LocalDate getReleaseApprovalQcDate() {
    return releaseApprovalQcDate;
  }

  public Boolean getReleaseApprovalQcPassed() {
    return releaseApprovalQcPassed;
  }

  public String getReleaseApprovalQcUser() {
    return releaseApprovalQcUser;
  }

  public LocalDate getReleaseQcDate() {
    return releaseQcDate;
  }

  public Boolean getReleaseQcPassed() {
    return releaseQcPassed;
  }

  public String getReleaseQcUser() {
    return releaseQcUser;
  }

  @JsonPOJOBuilder(withPrefix = "")
  public static class Builder {

    private DeliverableType deliverableType;
    private LocalDate analysisReviewQcDate;
    private Boolean analysisReviewQcPassed;
    private String analysisReviewQcUser;
    private LocalDate releaseApprovalQcDate;
    private Boolean releaseApprovalQcPassed;
    private String releaseApprovalQcUser;
    private LocalDate releaseQcDate;
    private Boolean releaseQcPassed;
    private String releaseQcUser;

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

    public Builder releaseQcDate(LocalDate releaseQcDate) {
      this.releaseQcDate = releaseQcDate;
      return this;
    }

    public Builder releaseQcPassed(Boolean releaseQcPassed) {
      this.releaseQcPassed = releaseQcPassed;
      return this;
    }

    public Builder releaseQcUser(String releaseQcUser) {
      this.releaseQcUser = releaseQcUser;
      return this;
    }

    public Deliverable build() {
      return new Deliverable(this);
    }

  }

}
