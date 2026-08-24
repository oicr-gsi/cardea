package ca.on.oicr.gsi.cardea.data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@com.fasterxml.jackson.databind.annotation.JsonDeserialize(as = SampleImpl.class)
@tools.jackson.databind.annotation.JsonDeserialize(as = SampleImpl.class)
public interface Sample {

  Set<Long> getAssayIds();

  BigDecimal getConcentration();

  String getConcentrationUnits();

  LocalDate getCreatedDate();

  LocalDate getDataReviewDate();

  Boolean getDataReviewPassed();

  String getDataReviewUser();

  Donor getDonor();

  String getGroupId();

  String getId();

  LocalDate getLatestActivityDate();

  String getLibraryDesignCode();

  Integer getLibrarySize();

  String getName();

  String getNucleicAcidType();

  BigDecimal getCollapsedCoverage();

  String getProject();

  LocalDate getQcDate();

  Boolean getQcPassed();

  String getQcReason();

  String getQcNote();

  String getQcUser();

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

  LocalDate getTransferDate();

  BigDecimal getDv200();

  List<SampleMetric> getMetrics();

  Boolean getAnalysisSkipped();
}
