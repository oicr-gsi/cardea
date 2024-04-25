package ca.on.oicr.gsi.cardea.data;

import static java.util.Objects.requireNonNull;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Set;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

/**
 * Immutable OmittedSample
 */
@JsonDeserialize(builder = OmittedSample.Builder.class)
public class OmittedSample {

  private final Set<Long> assayIds;
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
    this.requisitionId = builder.requisitionId;
    this.requisitionName = builder.requisitionName;
    this.assayIds = builder.assayIds == null ? Collections.emptySet()
        : Collections.unmodifiableSet(builder.assayIds);
    this.project = requireNonNull(builder.project);
    this.donor = requireNonNull(builder.donor);
    this.createdDate = requireNonNull(builder.createdDate);
  }

  public Set<Long> getAssayIds() {
    return assayIds;
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

  @JsonPOJOBuilder(withPrefix = "")
  public static class Builder {

    private LocalDate createdDate;
    private Donor donor;
    private String id;
    private String name;
    private String project;
    private Long requisitionId;
    private String requisitionName;
    private Set<Long> assayIds;

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
      if (requisition == null) {
        return this;
      }
      return this.requisitionId(requisition.getId())
          .requisitionName(requisition.getName())
          .assayIds(requisition.getAssayIds());
    }

    public Builder requisitionId(Long requisitionId) {
      this.requisitionId = requisitionId;
      return this;
    }

    public Builder requisitionName(String requisitionName) {
      this.requisitionName = requisitionName;
      return this;
    }

    public Builder assayIds(Set<Long> assayIds) {
      this.assayIds = assayIds;
      return this;
    }

  }

}
