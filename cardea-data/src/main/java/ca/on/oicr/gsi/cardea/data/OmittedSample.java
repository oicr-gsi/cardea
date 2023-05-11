package ca.on.oicr.gsi.cardea.data;

import static java.util.Objects.requireNonNull;

import java.time.LocalDate;

/**
 * Immutable OmittedSample
 */
public class OmittedSample {

  private final Long assayId;
  private final LocalDate createdDate;
  private final Donor donor;
  private final String id;
  private final String name;
  private final String project;
  private final Long requisitionId;
  private final String requisitionName;

  private OmittedSample(Builder builder) {
    this.id = requireNonNull(builder.id);
    this.name = requireNonNull(builder.name);
    this.requisitionId = builder.requisition == null ? null : builder.requisition.getId();
    this.requisitionName = builder.requisition == null ? null : builder.requisition.getName();
    this.assayId = builder.requisition == null ? null : builder.requisition.getAssayId();
    this.project = requireNonNull(builder.project);
    this.donor = requireNonNull(builder.donor);
    this.createdDate = requireNonNull(builder.createdDate);
  }

  public Long getAssayId() {
    return assayId;
  }

  public LocalDate getCreatedDate() {
    return createdDate;
  }

  public Donor getDonor() {
    return donor;
  }

  public String getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public String getProject() {
    return project;
  }

  public Long getRequisitionId() {
    return requisitionId;
  }

  public String getRequisitionName() {
    return requisitionName;
  }

  public static class Builder {

    private LocalDate createdDate;
    private Donor donor;
    private String id;
    private String name;
    private String project;
    private Requisition requisition;

    public OmittedSample build() {
      return new OmittedSample(this);
    }

    public Builder createdDate(LocalDate createdDate) {
      this.createdDate = createdDate;
      return this;
    }

    public Builder donor(Donor donor) {
      this.donor = donor;
      return this;
    }

    public Builder id(String id) {
      this.id = id;
      return this;
    }

    public Builder name(String name) {
      this.name = name;
      return this;
    }

    public Builder project(String project) {
      this.project = project;
      return this;
    }

    public Builder requisition(Requisition requisition) {
      this.requisition = requisition;
      return this;
    }

  }

}
