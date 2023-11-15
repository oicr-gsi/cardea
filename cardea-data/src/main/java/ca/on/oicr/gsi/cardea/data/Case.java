package ca.on.oicr.gsi.cardea.data;

import static java.util.Collections.unmodifiableList;
import static java.util.Collections.unmodifiableSet;
import static java.util.Objects.requireNonNull;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Stream;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

/**
 * Immutable case
 */
@JsonDeserialize(builder = Case.Builder.class)
public class Case {

  private final long assayId;
  private final String assayName;
  private final String assayDescription;
  private final Donor donor;
  private final String id;
  private final LocalDate latestActivityDate;
  private final Set<Project> projects;
  private final List<Sample> receipts;
  private final Requisition requisition;
  private final LocalDate startDate;
  private final List<Test> tests;
  private final String timepoint;
  private final String tissueOrigin;
  private final String tissueType;
  private final int receiptDaysSpent;
  private final int analysisReviewDaysSpent;
  private final int releaseApprovalDaysSpent;
  private final int releaseDaysSpent;
  private final int caseDaysSpent;
  private final int pauseDays;

  private Case(Builder builder) {
    this.id = requireNonNull(builder.id);
    this.donor = requireNonNull(builder.donor);
    this.projects = unmodifiableSet(builder.projects);
    this.assayId = builder.assayId;
    // fields needed for Dimsum sorting/filtering
    this.assayName = builder.assayName;
    this.assayDescription = builder.assayDescription;

    this.tissueOrigin = requireNonNull(builder.tissueOrigin);
    this.tissueType = requireNonNull(builder.tissueType);
    this.timepoint = builder.timepoint;
    this.receipts = unmodifiableList(builder.receipts);
    this.tests = unmodifiableList(builder.tests);
    this.requisition = requireNonNull(builder.requisition);
    this.startDate = requireNonNull(builder.startDate);
    this.latestActivityDate = Stream
        .of(receipts.stream().map(Sample::getLatestActivityDate),
            tests.stream().map(Test::getLatestActivityDate),
            Stream.of(requisition.getLatestActivityDate()))
        .flatMap(Function.identity()).filter(Objects::nonNull).max(LocalDate::compareTo)
        .orElse(null);
    this.receiptDaysSpent = builder.receiptDaysSpent;
    this.analysisReviewDaysSpent = builder.analysisReviewDaysSpent;
    this.releaseApprovalDaysSpent = builder.releaseApprovalDaysSpent;
    this.releaseDaysSpent = builder.releaseDaysSpent;
    this.caseDaysSpent = builder.caseDaysSpent;
    this.pauseDays = builder.pauseDays;
  }

  public long getAssayId() {
    return assayId;
  }

  public String getAssayName() {
    return assayName;
  }

  public String getAssayDescription() {
    return assayDescription;
  }

  public Donor getDonor() {
    return donor;
  }

  public String getId() {
    return id;
  }

  public LocalDate getLatestActivityDate() {
    return latestActivityDate;
  }

  public Set<Project> getProjects() {
    return projects;
  }

  public List<Sample> getReceipts() {
    return receipts;
  }

  public Requisition getRequisition() {
    return requisition;
  }

  public LocalDate getStartDate() {
    return startDate;
  }

  public List<Test> getTests() {
    return tests;
  }

  public String getTimepoint() {
    return timepoint;
  }

  public String getTissueOrigin() {
    return tissueOrigin;
  }

  public String getTissueType() {
    return tissueType;
  }

  public int getReceiptDaysSpent() {
    return receiptDaysSpent;
  }

  public int getAnalysisReviewDaysSpent() {
    return analysisReviewDaysSpent;
  }

  public int getReleaseApprovalDaysSpent() {
    return releaseApprovalDaysSpent;
  }

  public int getReleaseDaysSpent() {
    return releaseDaysSpent;
  }

  public int getCaseDaysSpent() {
    return caseDaysSpent;
  }

  public int getPauseDays() {
    return pauseDays;
  }

  public boolean isStopped() {
    return requisition.isStopped();
  }

  @JsonPOJOBuilder(withPrefix = "")
  public static class Builder {

    private long assayId;
    private String assayName;
    private String assayDescription;
    private Donor donor;
    private String id;
    private Set<Project> projects;
    private List<Sample> receipts;
    private Requisition requisition;
    private List<Test> tests;
    private String timepoint;
    private String tissueOrigin;
    private String tissueType;
    private LocalDate startDate;
    private int receiptDaysSpent;
    private int analysisReviewDaysSpent;
    private int releaseApprovalDaysSpent;
    private int releaseDaysSpent;
    private int caseDaysSpent;
    private int pauseDays;

    public Case build() {
      return new Case(this);
    }

    public Builder assayId(long assayId) {
      this.assayId = assayId;
      return this;
    }

    public Builder assayName(String assayName) {
      this.assayName = assayName;
      return this;
    }

    public Builder assayDescription(String assayDescription) {
      this.assayDescription = assayDescription;
      return this;
    }

    public Builder donor(Donor donor) {
      this.donor = donor;
      return this;
    }

    public Builder startDate(LocalDate starDate) {
      this.startDate = starDate;
      return this;
    }

    public Builder id(String id) {
      this.id = id;
      return this;
    }

    public Builder projects(Set<Project> projects) {
      this.projects = projects;
      return this;
    }

    public Builder receipts(List<Sample> receipts) {
      this.receipts = receipts;
      return this;
    }

    public Builder requisition(Requisition requisition) {
      this.requisition = requisition;
      return this;
    }

    public Builder tests(List<Test> tests) {
      this.tests = tests;
      return this;
    }

    public Builder timepoint(String timepoint) {
      this.timepoint = timepoint;
      return this;
    }

    public Builder tissueOrigin(String tissueOrigin) {
      this.tissueOrigin = tissueOrigin;
      return this;
    }

    public Builder tissueType(String tissueType) {
      this.tissueType = tissueType;
      return this;
    }

    public Builder receiptDaysSpent(int days) {
      this.receiptDaysSpent = days;
      return this;
    }

    public Builder analysisReviewDaysSpent(int days) {
      this.analysisReviewDaysSpent = days;
      return this;
    }

    public Builder releaseApprovalDaysSpent(int days) {
      this.releaseApprovalDaysSpent = days;
      return this;
    }

    public Builder releaseDaysSpent(int days) {
      this.releaseDaysSpent = days;
      return this;
    }

    public Builder caseDaysSpent(int days) {
      this.caseDaysSpent = days;
      return this;
    }

    public Builder pauseDays(int days) {
      this.pauseDays = days;
      return this;
    }
  }
}
