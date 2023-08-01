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

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

/**
 * Immutable case
 */
@JsonDeserialize(builder = Case.Builder.class)
public class Case {

  private final Assay assay;
  private final long assayId;
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

  private Case(Builder builder) {
    this.id = requireNonNull(builder.id);
    this.donor = requireNonNull(builder.donor);
    this.projects = unmodifiableSet(builder.projects);
    this.assay = requireNonNull(builder.assay);
    this.assayId = this.assay.getId();
    this.tissueOrigin = requireNonNull(builder.tissueOrigin);
    this.tissueType = requireNonNull(builder.tissueType);
    this.timepoint = builder.timepoint;
    this.receipts = unmodifiableList(builder.receipts);
    this.tests = unmodifiableList(builder.tests);
    this.requisition = builder.requisition;
    this.startDate = builder.receipts.stream()
        .filter(sample -> sample.getRequisitionId().longValue() == builder.requisition.getId())
        .map(Sample::getCreatedDate)
        .min(LocalDate::compareTo).orElse(null);
    this.latestActivityDate = Stream
        .of(receipts.stream().map(Sample::getLatestActivityDate),
            tests.stream().map(Test::getLatestActivityDate),
            Stream.of(requisition.getLatestActivityDate()))
        .flatMap(Function.identity()).filter(Objects::nonNull).max(LocalDate::compareTo)
        .orElse(null);
  }

  @JsonIgnore
  public Assay getAssay() {
    return assay;
  }

  public long getAssayId() {
    return assayId;
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

  // method used by Dimsum
  public boolean isStopped() {
    return requisition.isStopped();
  }

  @JsonPOJOBuilder(withPrefix = "")
  public static class Builder {

    private Assay assay;
    private Donor donor;
    private String id;
    private Set<Project> projects;
    private List<Sample> receipts;
    private Requisition requisition;
    private List<Test> tests;
    private String timepoint;
    private String tissueOrigin;
    private String tissueType;

    public Builder assay(Assay assay) {
      this.assay = assay;
      return this;
    }

    public Case build() {
      return new Case(this);
    }

    public Builder donor(Donor donor) {
      this.donor = donor;
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
  }
}
