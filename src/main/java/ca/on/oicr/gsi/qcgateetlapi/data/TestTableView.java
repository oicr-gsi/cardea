package ca.on.oicr.gsi.qcgateetlapi.data;

import static java.util.Objects.requireNonNull;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import javax.annotation.concurrent.Immutable;

@Immutable
public class TestTableView {
  private final Assay assay;
  private final String caseId;
  private final Donor donor;
  private final LocalDate latestActivityDate;
  private final Set<Project> projects;
  private final List<Sample> receipts;
  private final Requisition requisition;
  private final Test test;
  private final String timepoint;
  private final String tissueOrigin;
  private final String tissueType;

  public TestTableView(Case kase, Test test) {
    this.test = requireNonNull(test);
    this.caseId = requireNonNull(kase.getId());
    this.requisition = kase.getRequisition();
    this.donor = requireNonNull(kase.getDonor());
    this.projects = kase.getProjects();
    this.assay = requireNonNull(kase.getAssay());
    this.tissueOrigin = requireNonNull(kase.getTissueOrigin());
    this.tissueType = requireNonNull(kase.getTissueType());
    this.timepoint = kase.getTimepoint();
    this.receipts = kase.getReceipts();
    this.latestActivityDate = test.getLatestActivityDate() == null
        ? kase.getReceipts().stream().map(Sample::getLatestActivityDate).filter(Objects::nonNull)
            .max(LocalDate::compareTo).orElse(null)
        : test.getLatestActivityDate();
  }

  public Assay getAssay() {
    return assay;
  }

  public String getCaseId() {
    return caseId;
  }

  public Donor getDonor() {
    return donor;
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

  public Test getTest() {
    return test;
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

}
