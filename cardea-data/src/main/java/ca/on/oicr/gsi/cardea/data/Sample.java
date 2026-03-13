package ca.on.oicr.gsi.cardea.data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(as = SampleImpl.class)
public interface Sample {

  Set<Long> getAssayIds();

  Integer getClustersPerSample();

  Integer getPreliminaryClustersPerSample();

  BigDecimal getConcentration();

  String getConcentrationUnits();

  LocalDate getCreatedDate();

  LocalDate getDataReviewDate();

  Boolean getDataReviewPassed();

  String getDataReviewUser();

  Donor getDonor();

  BigDecimal getDuplicationRate();

  String getGroupId();

  String getId();

  Integer getLambdaClusters();

  BigDecimal getLambdaMethylation();

  LocalDate getLatestActivityDate();

  String getLibraryDesignCode();

  Integer getLibrarySize();

  BigDecimal getMappedToCoding();

  BigDecimal getMeanCoverageDeduplicated();

  BigDecimal getPreliminaryMeanCoverageDeduplicated();

  BigDecimal getMeanInsertSize();

  BigDecimal getMedianInsertSize();

  String getName();

  String getNucleicAcidType();

  BigDecimal getOnTargetReads();

  BigDecimal getCollapsedCoverage();

  String getProject();

  Integer getPuc19Clusters();

  BigDecimal getPuc19Methylation();

  LocalDate getQcDate();

  Boolean getQcPassed();

  String getQcReason();

  String getQcNote();

  String getQcUser();

  BigDecimal getRawCoverage();

  Long getRequisitionId();

  String getRequisitionName();

  Run getRun();

  String getSecondaryId();

  String getSequencingLane();

  String getTargetedSequencing();

  String getTimepoint();

  String getTissueMaterial();

  String getTissueOrigin();

  String getTissueType();

  BigDecimal getVolume();

  BigDecimal getrRnaContamination();

  BigDecimal getRelativeCpgInRegions();

  BigDecimal getMethylationBeta();

  Integer getPeReads();

  LocalDate getTransferDate();

  BigDecimal getDv200();

  List<SampleMetric> getMetrics();

  Boolean getAnalysisSkipped();

}
