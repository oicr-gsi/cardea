package ca.on.oicr.gsi.cardea.data;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

/**
 * Immutable Assay Targets
 */
@JsonDeserialize(builder = AssayTargets.Builder.class)
public class AssayTargets {

  private final Integer caseDays;
  private final Integer receiptDays;
  private final Integer extractionDays;
  private final Integer libraryPreparationDays;
  private final Integer libraryQualificationDays;
  private final Integer fullDepthSequencingDays;
  private final Integer analysisReviewDays;
  private final Integer releaseApprovalDays;
  private final Integer releaseDays;

  private AssayTargets(Builder builder) {
    this.caseDays = builder.caseDays;
    this.receiptDays = builder.receiptDays;
    this.extractionDays = builder.extractionDays;
    this.libraryPreparationDays = builder.libraryPreparationDays;
    this.libraryQualificationDays = builder.libraryQualificationDays;
    this.fullDepthSequencingDays = builder.fullDepthSequencingDays;
    this.analysisReviewDays = builder.analysisReviewDays;
    this.releaseApprovalDays = builder.releaseApprovalDays;
    this.releaseDays = builder.releaseDays;
  }

  public Integer getCaseDays() {
    return caseDays;
  }

  public Integer getReceiptDays() {
    return receiptDays;
  }

  public Integer getExtractionDays() {
    return extractionDays;
  }

  public Integer getLibraryPreparationDays() {
    return libraryPreparationDays;
  }

  public Integer getLibraryQualificationDays() {
    return libraryQualificationDays;
  }

  public Integer getFullDepthSequencingDays() {
    return fullDepthSequencingDays;
  }

  public Integer getAnalysisReviewDays() {
    return analysisReviewDays;
  }

  public Integer getReleaseApprovalDays() {
    return releaseApprovalDays;
  }

  public Integer getReleaseDays() {
    return releaseDays;
  }

  @JsonPOJOBuilder(withPrefix = "")
  public static class Builder {

    private Integer caseDays;
    private Integer receiptDays;
    private Integer extractionDays;
    private Integer libraryPreparationDays;
    private Integer libraryQualificationDays;
    private Integer fullDepthSequencingDays;
    private Integer analysisReviewDays;
    private Integer releaseApprovalDays;
    private Integer releaseDays;

    public Builder caseDays(Integer caseDays) {
      this.caseDays = caseDays;
      return this;
    }

    public Builder receiptDays(Integer receiptDays) {
      this.receiptDays = receiptDays;
      return this;
    }

    public Builder extractionDays(Integer extractionDays) {
      this.extractionDays = extractionDays;
      return this;
    }

    public Builder libraryPreparationDays(Integer libraryPreparationDays) {
      this.libraryPreparationDays = libraryPreparationDays;
      return this;
    }

    public Builder libraryQualificationDays(Integer libraryQualificationDays) {
      this.libraryQualificationDays = libraryQualificationDays;
      return this;
    }

    public Builder fullDepthSequencingDays(Integer fullDepthSequencingDays) {
      this.fullDepthSequencingDays = fullDepthSequencingDays;
      return this;
    }

    public Builder analysisReviewDays(Integer analysisReviewDays) {
      this.analysisReviewDays = analysisReviewDays;
      return this;
    }

    public Builder releaseApprovalDays(Integer releaseApprovalDays) {
      this.releaseApprovalDays = releaseApprovalDays;
      return this;
    }

    public Builder releaseDays(Integer releaseDays) {
      this.releaseDays = releaseDays;
      return this;
    }

    public AssayTargets build() {
      return new AssayTargets(this);
    }

  }

}
