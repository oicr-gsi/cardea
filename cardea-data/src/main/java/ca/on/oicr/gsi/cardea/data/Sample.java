package ca.on.oicr.gsi.cardea.data;

import static java.util.Objects.requireNonNull;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Stream;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

/**
 * Immutable Sample
 */
@JsonDeserialize(builder = Sample.Builder.class)
public class Sample {

  private final Set<Long> assayIds;
  private final Integer clustersPerSample; // AKA "Pass Filter Clusters" for full-depth (call ready)
  private final Integer preliminaryClustersPerSample;
  private final BigDecimal concentration;
  private final String concentrationUnits;
  private final LocalDate createdDate;
  private final LocalDate dataReviewDate;
  private final Boolean dataReviewPassed;
  private final String dataReviewUser;
  private final Donor donor;
  private final BigDecimal duplicationRate;
  private final String groupId;
  private final String id;
  private final Integer lambdaClusters;
  private final BigDecimal lambdaMethylation;
  private final LocalDate latestActivityDate;
  private final String libraryDesignCode;
  private final Integer librarySize;
  private final BigDecimal mappedToCoding;
  private final BigDecimal meanCoverageDeduplicated;
  private final BigDecimal preliminaryMeanCoverageDeduplicated;
  private final BigDecimal meanInsertSize;
  private final BigDecimal medianInsertSize;
  private final String name;
  private final String nucleicAcidType;
  private final BigDecimal onTargetReads;
  private final BigDecimal collapsedCoverage;
  private final String project;
  private final Integer puc19Clusters;
  private final BigDecimal puc19Methylation;
  private final LocalDate qcDate;
  private final Boolean qcPassed;
  private final String qcReason;
  private final String qcNote;
  private final String qcUser;
  private final BigDecimal rRnaContamination;
  private final BigDecimal rawCoverage;
  private final Long requisitionId;
  private final String requisitionName;
  private final Run run;
  private final String secondaryId;
  private final String sequencingLane;
  private final String targetedSequencing;
  private final String timepoint;
  private final String tissueMaterial;
  private final String tissueOrigin;
  private final String tissueType;
  private final BigDecimal volume;
  private final BigDecimal relativeCpgInRegions;
  private final BigDecimal methylationBeta;
  private final Integer peReads;
  private final LocalDate transferDate;
  private final BigDecimal dv200;

  private Sample(Builder builder) {
    this.id = requireNonNull(builder.id);
    this.name = requireNonNull(builder.name);
    this.requisitionId = builder.requisitionId;
    this.requisitionName = builder.requisitionName;
    this.assayIds = builder.assayIds == null ? Collections.emptySet()
        : Collections.unmodifiableSet(builder.assayIds);
    this.tissueOrigin = requireNonNull(builder.tissueOrigin);
    this.tissueType = requireNonNull(builder.tissueType);
    this.tissueMaterial = builder.tissueMaterial;
    this.timepoint = builder.timepoint;
    this.secondaryId = builder.secondaryId;
    this.groupId = builder.groupId;
    this.project = requireNonNull(builder.project);
    this.nucleicAcidType = builder.nucleicAcidType;
    this.librarySize = builder.librarySize;
    this.libraryDesignCode = builder.libraryDesignCode;
    this.targetedSequencing = builder.targetedSequencing;
    this.createdDate = requireNonNull(builder.createdDate);
    this.volume = builder.volume;
    this.concentration = builder.concentration;
    this.concentrationUnits = builder.concentrationUnits;
    this.run = builder.run;
    this.donor = requireNonNull(builder.donor);
    this.meanInsertSize = builder.meanInsertSize;
    this.medianInsertSize = builder.medianInsertSize;
    this.clustersPerSample = builder.clustersPerSample;
    this.preliminaryClustersPerSample = builder.preliminaryClustersPerSample;
    this.duplicationRate = builder.duplicationRate;
    this.meanCoverageDeduplicated = builder.meanCoverageDeduplicated;
    this.preliminaryMeanCoverageDeduplicated = builder.preliminaryMeanCoverageDeduplicated;
    this.rRnaContamination = builder.rRnaContamination;
    this.mappedToCoding = builder.mappedToCoding;
    this.rawCoverage = builder.rawCoverage;
    this.onTargetReads = builder.onTargetReads;
    this.collapsedCoverage = builder.collapsedCoverage;
    this.lambdaMethylation = builder.lambdaMethylation;
    this.lambdaClusters = builder.lambdaClusters;
    this.puc19Methylation = builder.puc19Methylation;
    this.puc19Clusters = builder.puc19Clusters;
    this.relativeCpgInRegions = builder.relativeCpgInRegions;
    this.methylationBeta = builder.methylationBeta;
    this.peReads = builder.peReads;
    this.qcPassed = builder.qcPassed;
    this.qcReason = builder.qcReason;
    this.qcNote = builder.qcNote;
    this.qcUser = builder.qcUser;
    this.qcDate = builder.qcDate;
    this.dataReviewPassed = builder.dataReviewPassed;
    this.dataReviewUser = builder.dataReviewUser;
    this.dataReviewDate = builder.dataReviewDate;
    this.sequencingLane = builder.sequencingLane;
    if (builder.latestActivityDate != null) {
      this.latestActivityDate = builder.latestActivityDate;
    } else {
      this.latestActivityDate = Stream.of(createdDate, qcDate, dataReviewDate)
          .filter(Objects::nonNull).max(LocalDate::compareTo).orElseThrow();
    }
    this.transferDate = builder.transferDate;
    this.dv200 = builder.dv200;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    Sample other = (Sample) obj;
    return Objects.equals(id, other.id)
        && Objects.equals(run, other.run)
        && Objects.equals(sequencingLane, other.sequencingLane);
  }

  public Set<Long> getAssayIds() {
    return assayIds;
  }

  public Integer getClustersPerSample() {
    return clustersPerSample;
  }

  public Integer getPreliminaryClustersPerSample() {
    return preliminaryClustersPerSample;
  }

  public BigDecimal getConcentration() {
    return concentration;
  }

  public String getConcentrationUnits() {
    return concentrationUnits;
  }

  public LocalDate getCreatedDate() {
    return createdDate;
  }

  public LocalDate getDataReviewDate() {
    return dataReviewDate;
  }

  public Boolean getDataReviewPassed() {
    return dataReviewPassed;
  }

  public String getDataReviewUser() {
    return dataReviewUser;
  }

  public Donor getDonor() {
    return donor;
  }

  public BigDecimal getDuplicationRate() {
    return duplicationRate;
  }

  public String getGroupId() {
    return groupId;
  }

  public String getId() {
    return id;
  }

  public Integer getLambdaClusters() {
    return lambdaClusters;
  }

  public BigDecimal getLambdaMethylation() {
    return lambdaMethylation;
  }

  public LocalDate getLatestActivityDate() {
    return latestActivityDate;
  }

  public String getLibraryDesignCode() {
    return libraryDesignCode;
  }

  public Integer getLibrarySize() {
    return librarySize;
  }

  public BigDecimal getMappedToCoding() {
    return mappedToCoding;
  }

  public BigDecimal getMeanCoverageDeduplicated() {
    return meanCoverageDeduplicated;
  }

  public BigDecimal getPreliminaryMeanCoverageDeduplicated() {
    return preliminaryMeanCoverageDeduplicated;
  }

  public BigDecimal getMeanInsertSize() {
    return meanInsertSize;
  }

  public BigDecimal getMedianInsertSize() {
    return medianInsertSize;
  }

  public String getName() {
    return name;
  }

  public String getNucleicAcidType() {
    return nucleicAcidType;
  }

  public BigDecimal getOnTargetReads() {
    return onTargetReads;
  }

  public BigDecimal getCollapsedCoverage() {
    return collapsedCoverage;
  }

  public String getProject() {
    return project;
  }

  public Integer getPuc19Clusters() {
    return puc19Clusters;
  }

  public BigDecimal getPuc19Methylation() {
    return puc19Methylation;
  }

  public LocalDate getQcDate() {
    return qcDate;
  }

  public Boolean getQcPassed() {
    return qcPassed;
  }

  public String getQcReason() {
    return qcReason;
  }

  public String getQcNote() {
    return qcNote;
  }

  public String getQcUser() {
    return qcUser;
  }

  public BigDecimal getRawCoverage() {
    return rawCoverage;
  }

  public Long getRequisitionId() {
    return requisitionId;
  }

  public String getRequisitionName() {
    return requisitionName;
  }

  public Run getRun() {
    return run;
  }

  public String getSecondaryId() {
    return secondaryId;
  }

  public String getSequencingLane() {
    return sequencingLane;
  }

  public String getTargetedSequencing() {
    return targetedSequencing;
  }

  public String getTimepoint() {
    return timepoint;
  }

  public String getTissueMaterial() {
    return tissueMaterial;
  }

  public String getTissueOrigin() {
    return tissueOrigin;
  }

  public String getTissueType() {
    return tissueType;
  }

  public BigDecimal getVolume() {
    return volume;
  }

  public BigDecimal getrRnaContamination() {
    return rRnaContamination;
  }

  public BigDecimal getRelativeCpgInRegions() {
    return relativeCpgInRegions;
  }

  public BigDecimal getMethylationBeta() {
    return methylationBeta;
  }

  public Integer getPeReads() {
    return peReads;
  }

  public LocalDate getTransferDate() {
    return transferDate;
  }

  public BigDecimal getDv200() {
    return dv200;
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, run, sequencingLane);
  }

  @JsonPOJOBuilder(withPrefix = "")
  public static class Builder {

    private Integer clustersPerSample;
    private Integer preliminaryClustersPerSample;
    private BigDecimal concentration;
    private String concentrationUnits;
    private LocalDate createdDate;
    private LocalDate dataReviewDate;
    private Boolean dataReviewPassed;
    private String dataReviewUser;
    private Donor donor;
    private BigDecimal duplicationRate;
    private String groupId;
    private String id;
    private Integer lambdaClusters;
    private BigDecimal lambdaMethylation;
    private String libraryDesignCode;
    private Integer librarySize;
    private BigDecimal mappedToCoding;
    private BigDecimal meanCoverageDeduplicated;
    private BigDecimal preliminaryMeanCoverageDeduplicated;
    private BigDecimal meanInsertSize;
    private BigDecimal medianInsertSize;
    private String name;
    private String nucleicAcidType;
    private BigDecimal onTargetReads;
    private BigDecimal collapsedCoverage;
    private String project;
    private Integer puc19Clusters;
    private BigDecimal puc19Methylation;
    private LocalDate qcDate;
    private Boolean qcPassed;
    private String qcReason;
    private String qcNote;
    private String qcUser;
    private BigDecimal rRnaContamination;
    private BigDecimal rawCoverage;
    private Long requisitionId;
    private String requisitionName;
    private Set<Long> assayIds;
    private Run run;
    private String secondaryId;
    private String sequencingLane;
    private String targetedSequencing;
    private String timepoint;
    private String tissueMaterial;
    private String tissueOrigin;
    private String tissueType;
    private BigDecimal volume;
    private BigDecimal relativeCpgInRegions;
    private BigDecimal methylationBeta;
    private Integer peReads;
    private LocalDate latestActivityDate;
    private LocalDate transferDate;
    private BigDecimal dv200;

    public Sample build() {
      return new Sample(this);
    }

    public Builder clustersPerSample(Integer clustersPerSample) {
      this.clustersPerSample = clustersPerSample;
      return this;
    }

    public Builder preliminaryClustersPerSample(Integer preliminaryClustersPerSample) {
      this.preliminaryClustersPerSample = preliminaryClustersPerSample;
      return this;
    }

    public Builder concentration(BigDecimal concentration) {
      this.concentration = concentration;
      return this;
    }

    public Builder concentrationUnits(String concentrationUnits) {
      this.concentrationUnits = concentrationUnits;
      return this;
    }

    public Builder createdDate(LocalDate createdDate) {
      this.createdDate = createdDate;
      return this;
    }

    public Builder dataReviewDate(LocalDate dataReviewDate) {
      this.dataReviewDate = dataReviewDate;
      return this;
    }

    public Builder dataReviewPassed(Boolean dataReviewPassed) {
      this.dataReviewPassed = dataReviewPassed;
      return this;
    }

    public Builder dataReviewUser(String dataReviewUser) {
      this.dataReviewUser = dataReviewUser;
      return this;
    }

    public Builder donor(Donor donor) {
      this.donor = donor;
      return this;
    }

    public Builder duplicationRate(BigDecimal duplicationRate) {
      this.duplicationRate = duplicationRate;
      return this;
    }

    public Builder groupId(String groupId) {
      this.groupId = groupId;
      return this;
    }

    public Builder id(String id) {
      this.id = id;
      return this;
    }

    public Builder lambdaClusters(Integer lambdaClusters) {
      this.lambdaClusters = lambdaClusters;
      return this;
    }

    public Builder lambdaMethylation(BigDecimal lambdaMethylation) {
      this.lambdaMethylation = lambdaMethylation;
      return this;
    }

    public Builder libraryDesignCode(String libraryDesignCode) {
      this.libraryDesignCode = libraryDesignCode;
      return this;
    }

    public Builder librarySize(Integer librarySize) {
      this.librarySize = librarySize;
      return this;
    }

    public Builder mappedToCoding(BigDecimal mappedToCoding) {
      this.mappedToCoding = mappedToCoding;
      return this;
    }

    public Builder meanCoverageDeduplicated(BigDecimal meanCoverageDeduplicated) {
      this.meanCoverageDeduplicated = meanCoverageDeduplicated;
      return this;
    }

    public Builder preliminaryMeanCoverageDeduplicated(
        BigDecimal preliminaryMeanCoverageDeduplicated) {
      this.preliminaryMeanCoverageDeduplicated = preliminaryMeanCoverageDeduplicated;
      return this;
    }

    public Builder meanInsertSize(BigDecimal meanInsertSize) {
      this.meanInsertSize = meanInsertSize;
      return this;
    }

    public Builder medianInsertSize(BigDecimal medianInsertSize) {
      this.medianInsertSize = medianInsertSize;
      return this;
    }

    public Builder name(String name) {
      this.name = name;
      return this;
    }

    public Builder nucleicAcidType(String nucleicAcidType) {
      this.nucleicAcidType = nucleicAcidType;
      return this;
    }

    public Builder onTargetReads(BigDecimal onTargetReads) {
      this.onTargetReads = onTargetReads;
      return this;
    }

    public Builder collapsedCoverage(BigDecimal collapsedCoverage) {
      this.collapsedCoverage = collapsedCoverage;
      return this;
    }

    public Builder project(String project) {
      this.project = project;
      return this;
    }

    public Builder puc19Clusters(Integer puc19Clusters) {
      this.puc19Clusters = puc19Clusters;
      return this;
    }

    public Builder puc19Methylation(BigDecimal puc19Methylation) {
      this.puc19Methylation = puc19Methylation;
      return this;
    }

    public Builder qcDate(LocalDate qcDate) {
      this.qcDate = qcDate;
      return this;
    }

    public Builder qcPassed(Boolean qcPassed) {
      this.qcPassed = qcPassed;
      return this;
    }

    public Builder qcReason(String qcReason) {
      this.qcReason = qcReason;
      return this;
    }

    public Builder qcNote(String qcNote) {
      this.qcNote = qcNote;
      return this;
    }

    public Builder qcUser(String qcUser) {
      this.qcUser = qcUser;
      return this;
    }

    public Builder rRnaContamination(BigDecimal rRnaContamination) {
      this.rRnaContamination = rRnaContamination;
      return this;
    }

    public Builder rawCoverage(BigDecimal rawCoverage) {
      this.rawCoverage = rawCoverage;
      return this;
    }

    public Builder requisition(Requisition requisition) {
      if (requisition == null) {
        return this;
      }
      return this.requisitionId(requisition.getId())
          .requisitionName(requisition.getName());
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

    public Builder run(Run run) {
      this.run = run;
      return this;
    }

    public Builder secondaryId(String secondaryId) {
      this.secondaryId = secondaryId;
      return this;
    }

    public Builder sequencingLane(String sequencingLane) {
      this.sequencingLane = sequencingLane;
      return this;
    }

    public Builder targetedSequencing(String targetedSequencing) {
      this.targetedSequencing = targetedSequencing;
      return this;
    }

    public Builder timepoint(String timepoint) {
      this.timepoint = timepoint;
      return this;
    }

    public Builder tissueMaterial(String tissueMaterial) {
      this.tissueMaterial = tissueMaterial;
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

    public Builder volume(BigDecimal volume) {
      this.volume = volume;
      return this;
    }

    public Builder relativeCpgInRegions(BigDecimal relativeCpgInRegions) {
      this.relativeCpgInRegions = relativeCpgInRegions;
      return this;
    }

    public Builder methylationBeta(BigDecimal methylationBeta) {
      this.methylationBeta = methylationBeta;
      return this;
    }

    public Builder peReads(Integer peReads) {
      this.peReads = peReads;
      return this;
    }

    public Builder latestActivityDate(LocalDate latestActivityDate) {
      this.latestActivityDate = latestActivityDate;
      return this;
    }

    public Builder transferDate(LocalDate transferDate) {
      this.transferDate = transferDate;
      return this;
    }

    public Builder dv200(BigDecimal dv200) {
      this.dv200 = dv200;
      return this;
    }

  }
}
