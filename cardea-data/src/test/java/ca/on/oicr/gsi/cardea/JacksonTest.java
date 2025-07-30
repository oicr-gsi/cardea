package ca.on.oicr.gsi.cardea;

import static org.junit.jupiter.api.Assertions.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;

import ca.on.oicr.gsi.cardea.data.*;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import com.fasterxml.jackson.databind.ObjectMapper;
import ca.on.oicr.gsi.cardea.data.CaseQc.AnalysisReviewQcStatus;
import ca.on.oicr.gsi.cardea.data.CaseQc.ReleaseApprovalQcStatus;
import ca.on.oicr.gsi.cardea.data.CaseQc.ReleaseQcStatus;
import ca.on.oicr.gsi.cardea.data.SampleMetric.MetricLevel;

public class JacksonTest {

  private final ObjectMapper mapper = new Jackson2ObjectMapperBuilder().build();

  @org.junit.jupiter.api.Test
  public void testDonorSerializeDeserialize() throws Exception {
    Donor original = makeDonor();
    String serialized = mapper.writeValueAsString(original);
    Donor deserialized = mapper.readerFor(Donor.class).readValue(serialized);
    assertDonorsEqual(original, deserialized);
  }

  @org.junit.jupiter.api.Test
  public void testOmittedSampleSerializeDeserialize() throws Exception {
    Set<Long> assayIds = new HashSet<>();
    assayIds.add(2L);
    assayIds.add(3L);

    OmittedSample original = new OmittedSample.Builder()
        .id("SAM9")
        .name("Sample")
        .donor(makeDonor())
        .createdDate(LocalDate.of(2024, 1, 1))
        .project("PROJ")
        .requisitionId(1L)
        .requisitionName("Test Req")
        .assayIds(assayIds)
        .build();

    String serialized = mapper.writeValueAsString(original);
    OmittedSample deserialized = mapper.readerFor(OmittedSample.class).readValue(serialized);

    assertOmittedSamplesEqual(original, deserialized);
  }

  @org.junit.jupiter.api.Test
  public void testMetricSerializeDeserialize() throws Exception {
    Metric original = makeMetric();
    String serialized = mapper.writeValueAsString(original);
    Metric deserialized = mapper.readerFor(Metric.class).readValue(serialized);
    assertMetricsEqual(original, deserialized);
  }

  @org.junit.jupiter.api.Test
  public void testMetricSubcategorySerializeDeserialize() throws Exception {
    MetricSubcategory original = makeMetricSubcategory();
    String serialized = mapper.writeValueAsString(original);
    MetricSubcategory deserialized =
        mapper.readerFor(MetricSubcategory.class).readValue(serialized);
    assertMetricSubcategoriesEqual(original, deserialized);
  }

  @org.junit.jupiter.api.Test
  public void testAssayTargetsSerializeDeserialize() throws Exception {
    AssayTargets original = makeAssayTargets();
    String serialized = mapper.writeValueAsString(original);
    AssayTargets deserialized = mapper.readerFor(AssayTargets.class).readValue(serialized);
    assertAssayTargetsEqual(original, deserialized);
  }

  @org.junit.jupiter.api.Test
  public void testAssaySerializeDeserialize() throws Exception {
    Assay original = makeAssay();
    String serialized = mapper.writeValueAsString(original);
    Assay deserialized = mapper.readerFor(Assay.class).readValue(serialized);
    assertAssaysEqual(original, deserialized);
  }

  @org.junit.jupiter.api.Test
  public void testAnalysisQcGroupSerializeDeserialize() throws Exception {
    AnalysisQcGroup original = makeAnalysisQcGroup();
    String serialized = mapper.writeValueAsString(original);
    AnalysisQcGroup deserialized = mapper.readerFor(AnalysisQcGroup.class).readValue(serialized);
    assertAnalysisQcGroupEqual(original, deserialized);
  }

  @org.junit.jupiter.api.Test
  public void testCaseReleaseSerializeDeserialize() throws Exception {
    CaseRelease original = makeCaseRelease();
    String serialized = mapper.writeValueAsString(original);
    CaseRelease deserialized = mapper.readerFor(CaseRelease.class).readValue(serialized);
    assertCaseReleaseEqual(original, deserialized);
  }

  @org.junit.jupiter.api.Test
  public void testCaseDeliverableSerializeDeserialize() throws Exception {
    CaseDeliverable original = makeCaseDeliverable();
    String serialized = mapper.writeValueAsString(original);
    CaseDeliverable deserialized = mapper.readerFor(CaseDeliverable.class).readValue(serialized);
    assertCaseDeliverableEqual(original, deserialized);
  }

  @org.junit.jupiter.api.Test
  public void testProjectSerializeDeserialize() throws Exception {
    Project original = makeProject();
    String serialized = mapper.writeValueAsString(original);
    Project deserialized = mapper.readerFor(Project.class).readValue(serialized);
    assertProjectEqual(original, deserialized);
  }

  @org.junit.jupiter.api.Test
  public void testRequisitionSerializeDeserialize() throws Exception {
    Requisition original = makeRequisition();
    String serialized = mapper.writeValueAsString(original);
    Requisition deserialized = mapper.readerFor(Requisition.class).readValue(serialized);
    assertRequisitionEqual(original, deserialized);
  }

  @org.junit.jupiter.api.Test
  public void testLaneSerializeDeserialize() throws Exception {
    Lane original = makeLane();
    String serialized = mapper.writeValueAsString(original);
    Lane deserialized = mapper.readerFor(Lane.class).readValue(serialized);
    assertLaneEqual(original, deserialized);
  }

  @org.junit.jupiter.api.Test
  public void testRunSerializeDeserialize() throws Exception {
    Run original = makeRun();
    String serialized = mapper.writeValueAsString(original);
    Run deserialized = mapper.readerFor(Run.class).readValue(serialized);
    assertRunEqual(original, deserialized);
  }

  @org.junit.jupiter.api.Test
  public void testSampleSerializeDeserialize() throws Exception {
    Sample original = makeSample("Sammy");
    String serialized = mapper.writeValueAsString(original);
    Sample deserialized = mapper.readerFor(Sample.class).readValue(serialized);
    assertSampleEqual(original, deserialized);
  }

  @org.junit.jupiter.api.Test
  public void testTestSerializeDeserialize() throws Exception {
    Test original = makeTest();
    String serialized = mapper.writeValueAsString(original);
    Test deserialized = mapper.readerFor(Test.class).readValue(serialized);
    assertTestEqual(original, deserialized);
  }

  @org.junit.jupiter.api.Test
  public void testCaseSerializeDeserialize() throws Exception {
    Case original = makeCase();
    String serialized = mapper.writeValueAsString(original);
    Case deserialized = mapper.readerFor(Case.class).readValue(serialized);
    assertCaseEqual(original, deserialized);
  }

  @org.junit.jupiter.api.Test
  public void testOmittedRunSampleSerializeDeserialize() throws Exception {
    OmittedRunSample original = makeOmittedRunSample();
    String serialized = mapper.writeValueAsString(original);
    OmittedRunSample deserialized = mapper.readerFor(OmittedRunSample.class).readValue(serialized);
    assertOmittedRunSampleEqual(original, deserialized);
  }

  @org.junit.jupiter.api.Test
  public void testShesmuCaseSerializeDeserialize() throws Exception {
    ShesmuCase original = makeShesmuCase();
    String serialized = mapper.writeValueAsString(original);
    ShesmuCase deserialized = mapper.readerFor(ShesmuCase.class).readValue(serialized);
    assertShesmuCaseEqual(original, deserialized);
  }

  @org.junit.jupiter.api.Test
  public void testShesmuDetailedCaseSerializeDeserialize() throws Exception {
    ShesmuDetailedCase original = makeShesmuDetailedCase();
    String serialized = mapper.writeValueAsString(original);
    ShesmuDetailedCase deserialized =
        mapper.readerFor(ShesmuDetailedCase.class).readValue(serialized);
    assertShesmuDetailedCaseEqual(original, deserialized);
  }

  private static void assertOmittedSamplesEqual(OmittedSample one, OmittedSample two) {
    assertEquals(one.getId(), two.getId());
    assertEquals(one.getName(), two.getName());
    assertEquals(one.getAssayIds(), two.getAssayIds());
    assertEquals(one.getCreatedDate(), two.getCreatedDate());
    assertDonorsEqual(one.getDonor(), two.getDonor());
    assertEquals(one.getProject(), two.getProject());
    assertEquals(one.getRequisitionId(), two.getRequisitionId());
    assertEquals(one.getRequisitionName(), two.getRequisitionName());
  }

  private static void assertDonorsEqual(Donor one, Donor two) {
    assertEquals(one.getId(), two.getId());
    assertEquals(one.getName(), two.getName());
    assertEquals(one.getExternalName(), two.getExternalName());
  }

  private static void assertMetricsEqual(Metric one, Metric two) {
    assertEquals(one.getContainerModel(), two.getContainerModel());
    assertEquals(one.getMinimum(), two.getMinimum());
    assertEquals(one.getMaximum(), two.getMaximum());
    assertEquals(one.getName(), two.getName());
    assertEquals(one.isNegateTissueType(), two.isNegateTissueType());
    assertEquals(one.getNucleicAcidType(), two.getNucleicAcidType());
    assertEquals(one.getReadLength(), two.getReadLength());
    assertEquals(one.getReadLength2(), two.getReadLength2());
    assertEquals(one.getSortPriority(), two.getSortPriority());
    assertEquals(one.getThresholdType(), two.getThresholdType());
    assertEquals(one.getTissueMaterial(), two.getTissueMaterial());
    assertEquals(one.getTissueOrigin(), two.getTissueOrigin());
    assertEquals(one.getTissueType(), two.getTissueType());
    assertEquals(one.getUnits(), two.getUnits());
  }

  private static void assertMetricSubcategoriesEqual(MetricSubcategory one, MetricSubcategory two) {
    assertEquals(one.getName(), two.getName());
    assertEquals(one.getLibraryDesignCode(), two.getLibraryDesignCode());
    assertEquals(one.getSortPriority(), two.getSortPriority());
    assertEquals(one.getMetrics().size(), two.getMetrics().size());
    for (int i = 0; i < one.getMetrics().size(); i++) {
      assertMetricsEqual(one.getMetrics().get(i), two.getMetrics().get(i));
    }
  }

  private static void assertAssayTargetsEqual(AssayTargets one, AssayTargets two) {
    assertEquals(one.getCaseDays(), two.getCaseDays());
    assertEquals(one.getReceiptDays(), two.getReceiptDays());
    assertEquals(one.getExtractionDays(), two.getExtractionDays());
    assertEquals(one.getLibraryPreparationDays(), two.getLibraryPreparationDays());
    assertEquals(one.getLibraryQualificationDays(), two.getLibraryQualificationDays());
    assertEquals(one.getFullDepthSequencingDays(), two.getFullDepthSequencingDays());
    assertEquals(one.getAnalysisReviewDays(), two.getAnalysisReviewDays());
    assertEquals(one.getReleaseApprovalDays(), two.getReleaseApprovalDays());
    assertEquals(one.getReleaseDays(), two.getReleaseDays());
  }

  private static void assertAssaysEqual(Assay one, Assay two) {
    assertEquals(one.getId(), two.getId());
    assertEquals(one.getName(), two.getName());
    assertEquals(one.getDescription(), two.getDescription());
    assertEquals(one.getVersion(), two.getVersion());
    assertEquals(one.getMetricCategories().size(), two.getMetricCategories().size());
    for (MetricCategory category : one.getMetricCategories().keySet()) {
      List<MetricSubcategory> oneSubcategories = one.getMetricCategories().get(category);
      List<MetricSubcategory> twoSubcategories = two.getMetricCategories().get(category);
      assertEquals(oneSubcategories.size(), twoSubcategories.size());
      for (int i = 0; i < oneSubcategories.size(); i++) {
        MetricSubcategory oneSubcategory = oneSubcategories.get(i);
        MetricSubcategory twoSubcategory = twoSubcategories.get(i);
        assertMetricSubcategoriesEqual(oneSubcategory, twoSubcategory);
      }
    }
    assertAssayTargetsEqual(one.getTargets(), two.getTargets());
  }

  private static void assertAnalysisQcGroupEqual(AnalysisQcGroup one, AnalysisQcGroup two) {
    assertEquals(one.getCallability(), two.getCallability());
    assertEquals(one.getCollapsedCoverage(), two.getCollapsedCoverage());
    assertEquals(one.getGroupId(), two.getGroupId());
    assertEquals(one.getLibraryDesignCode(), two.getLibraryDesignCode());
    assertEquals(one.getPurity(), two.getPurity());
    assertEquals(one.getTissueOrigin(), two.getTissueOrigin());
    assertEquals(one.getTissueType(), two.getTissueType());
  }

  private static void assertCaseReleaseEqual(CaseRelease one, CaseRelease two) {
    assertEquals(one.getDeliverable(), two.getDeliverable());
    assertEquals(one.getQcDate(), two.getQcDate());
    assertEquals(one.getQcStatus(), two.getQcStatus());
    assertEquals(one.getQcNote(), two.getQcNote());
    assertEquals(one.getQcUser(), two.getQcUser());
  }

  private static void assertCaseDeliverableEqual(CaseDeliverable one, CaseDeliverable two) {
    assertEquals(one.getDeliverableCategory(), two.getDeliverableCategory());
    assertEquals(one.isAnalysisReviewSkipped(), two.isAnalysisReviewSkipped());
    assertEquals(one.getAnalysisReviewQcDate(), two.getAnalysisReviewQcDate());
    assertEquals(one.getAnalysisReviewQcStatus(), two.getAnalysisReviewQcStatus());
    assertEquals(one.getAnalysisReviewQcNote(), two.getAnalysisReviewQcNote());
    assertEquals(one.getAnalysisReviewQcUser(), two.getAnalysisReviewQcUser());
    assertEquals(one.getReleaseApprovalQcDate(), two.getReleaseApprovalQcDate());
    assertEquals(one.getReleaseApprovalQcStatus(), two.getReleaseApprovalQcStatus());
    assertEquals(one.getReleaseApprovalQcNote(), two.getReleaseApprovalQcNote());
    assertEquals(one.getReleaseApprovalQcUser(), two.getReleaseApprovalQcUser());
    assertEquals(one.getAnalysisReviewDaysSpent(), two.getAnalysisReviewDaysSpent());
    assertEquals(one.getReleaseApprovalDaysSpent(), two.getReleaseApprovalDaysSpent());
    assertEquals(one.getDeliverableDaysSpent(), two.getDeliverableDaysSpent());

    List<CaseRelease> oneReleases = one.getReleases();
    List<CaseRelease> twoReleases = two.getReleases();
    assertEquals(oneReleases.size(), twoReleases.size());
    for (int i = 0; i < oneReleases.size(); i++) {
      assertCaseReleaseEqual(oneReleases.get(i), twoReleases.get(i));
    }

    assertNotNull(one.getLatestActivityDate());
    assertEquals(one.getLatestActivityDate(), two.getLatestActivityDate());
  }

  private static void assertProjectEqual(Project one, Project two) {
    assertEquals(one.getName(), two.getName());
    assertEquals(one.getPipeline(), two.getPipeline());
    assertEquals(one.getDeliverables(), two.getDeliverables());
  }

  private static void assertRequisitionEqual(Requisition one, Requisition two) {
    assertEquals(one.getId(), two.getId());
    assertEquals(one.getName(), two.getName());
    assertEquals(one.isStopped(), two.isStopped());
    assertEquals(one.getStopReason(), two.getStopReason());
    assertEquals(one.isPaused(), two.isPaused());
    assertEquals(one.getPauseReason(), two.getPauseReason());
    assertEquals(one.getAssayIds(), two.getAssayIds());
  }

  private static void assertLaneEqual(Lane one, Lane two) {
    assertEquals(one.getClustersPf(), two.getClustersPf());
    assertEquals(one.getLaneNumber(), two.getLaneNumber());
    assertEquals(one.getPercentOverQ30Read1(), two.getPercentOverQ30Read1());
    assertEquals(one.getPercentOverQ30Read2(), two.getPercentOverQ30Read2());
    assertEquals(one.getPercentPfixRead1(), two.getPercentPfixRead1());
    assertEquals(one.getPercentPfixRead2(), two.getPercentPfixRead2());
  }

  private static void assertRunEqual(Run one, Run two) {
    assertEquals(one.getClustersPf(), two.getClustersPf());
    assertEquals(one.getStartDate(), two.getStartDate());
    assertEquals(one.getCompletionDate(), two.getCompletionDate());
    assertEquals(one.getContainerModel(), two.getContainerModel());
    assertEquals(one.getDataReviewDate(), two.getDataReviewDate());
    assertEquals(one.getDataReviewPassed(), two.getDataReviewPassed());
    assertEquals(one.getDataReviewUser(), two.getDataReviewUser());
    assertEquals(one.getId(), two.getId());
    assertEquals(one.hasJoinedLanes(), two.hasJoinedLanes());
    assertEquals(one.getLanes().size(), two.getLanes().size());
    for (int i = 0; i < one.getLanes().size(); i++) {
      assertLaneEqual(one.getLanes().get(i), two.getLanes().get(i));
    }
    assertEquals(one.getName(), two.getName());
    assertEquals(one.getPercentOverQ30(), two.getPercentOverQ30());
    assertEquals(one.getQcDate(), two.getQcDate());
    assertEquals(one.getQcPassed(), two.getQcPassed());
    assertEquals(one.getQcUser(), two.getQcUser());
    assertEquals(one.getReadLength(), two.getReadLength());
    assertEquals(one.getReadLength2(), two.getReadLength2());
    assertEquals(one.getSequencingParameters(), two.getSequencingParameters());
  }

  private static void assertSampleEqual(Sample one, Sample two) {
    assertEquals(one.getAssayIds(), two.getAssayIds());
    assertEquals(one.getClustersPerSample(), two.getClustersPerSample());
    assertEquals(one.getPreliminaryClustersPerSample(), two.getPreliminaryClustersPerSample());
    assertEquals(one.getConcentration(), two.getConcentration());
    assertEquals(one.getConcentrationUnits(), two.getConcentrationUnits());
    assertEquals(one.getCreatedDate(), two.getCreatedDate());
    assertEquals(one.getDataReviewDate(), two.getDataReviewDate());
    assertEquals(one.getDataReviewPassed(), two.getDataReviewPassed());
    assertEquals(one.getDataReviewUser(), two.getDataReviewUser());
    assertDonorsEqual(one.getDonor(), two.getDonor());
    assertEquals(one.getDuplicationRate(), two.getDuplicationRate());
    assertEquals(one.getGroupId(), two.getGroupId());
    assertEquals(one.getId(), two.getId());
    assertEquals(one.getLambdaClusters(), two.getLambdaClusters());
    assertEquals(one.getLambdaMethylation(), two.getLambdaMethylation());
    assertEquals(one.getLatestActivityDate(), two.getLatestActivityDate());
    assertEquals(one.getLibraryDesignCode(), two.getLibraryDesignCode());
    assertEquals(one.getLibrarySize(), two.getLibrarySize());
    assertEquals(one.getMappedToCoding(), two.getMappedToCoding());
    assertEquals(one.getMeanCoverageDeduplicated(), two.getMeanCoverageDeduplicated());
    assertEquals(one.getPreliminaryMeanCoverageDeduplicated(),
        two.getPreliminaryMeanCoverageDeduplicated());
    assertEquals(one.getMeanInsertSize(), two.getMeanInsertSize());
    assertEquals(one.getMedianInsertSize(), two.getMedianInsertSize());
    assertEquals(one.getName(), two.getName());
    assertEquals(one.getNucleicAcidType(), two.getNucleicAcidType());
    assertEquals(one.getOnTargetReads(), two.getOnTargetReads());
    assertEquals(one.getProject(), two.getProject());
    assertEquals(one.getPuc19Clusters(), two.getPuc19Clusters());
    assertEquals(one.getPuc19Methylation(), two.getPuc19Methylation());
    assertEquals(one.getQcDate(), two.getQcDate());
    assertEquals(one.getQcPassed(), two.getQcPassed());
    assertEquals(one.getQcReason(), two.getQcReason());
    assertEquals(one.getQcNote(), two.getQcNote());
    assertEquals(one.getQcUser(), two.getQcUser());
    assertEquals(one.getrRnaContamination(), two.getrRnaContamination());
    assertEquals(one.getRawCoverage(), two.getRawCoverage());
    assertEquals(one.getRequisitionId(), two.getRequisitionId());
    assertEquals(one.getRequisitionName(), two.getRequisitionName());
    assertRunEqual(one.getRun(), two.getRun());
    assertEquals(one.getSecondaryId(), two.getSecondaryId());
    assertEquals(one.getSequencingLane(), two.getSequencingLane());
    assertEquals(one.getTargetedSequencing(), two.getTargetedSequencing());
    assertEquals(one.getTimepoint(), two.getTimepoint());
    assertEquals(one.getTissueMaterial(), two.getTissueMaterial());
    assertEquals(one.getTissueOrigin(), two.getTissueOrigin());
    assertEquals(one.getTissueType(), two.getTissueType());
    assertEquals(one.getVolume(), two.getVolume());
    assertEquals(one.getRelativeCpgInRegions(), two.getRelativeCpgInRegions());
    assertEquals(one.getMethylationBeta(), two.getMethylationBeta());
    assertEquals(one.getPeReads(), two.getPeReads());
    assertEquals(one.getTransferDate(), two.getTransferDate());
    assertEquals(one.getDv200(), two.getDv200());
    assertMetricsEqual(one.getMetrics(), two.getMetrics());
  }

  private static void assertMetricsEqual(List<SampleMetric> one, List<SampleMetric> two) {
    assertEquals(one.size(), two.size());
    for (int i = 0; i < one.size(); i++) {
      SampleMetric metricOne = one.get(i);
      SampleMetric metricTwo = two.get(i);
      assertEquals(metricOne.getName(), metricTwo.getName());
      assertEquals(metricOne.getThresholdType(), metricTwo.getThresholdType());
      assertEquals(metricOne.getMinimum(), metricTwo.getMinimum());
      assertEquals(metricOne.getMaximum(), metricTwo.getMaximum());
      assertEquals(metricOne.getMetricLevel(), metricTwo.getMetricLevel());
      assertEquals(metricOne.getPreliminary(), metricTwo.getPreliminary());
      assertEquals(metricOne.getValue(), metricTwo.getValue());
      assertEquals(metricOne.getQcPassed(), metricTwo.getQcPassed());
      assertEquals(metricOne.getUnits(), metricTwo.getUnits());
      if (metricOne.getLaneValues() == null) {
        assertNull(metricTwo.getLaneValues());
      } else {
        assertEquals(metricOne.getLaneValues().size(), metricTwo.getLaneValues().size());
        for (SampleMetricLane laneOne : metricOne.getLaneValues()) {
          SampleMetricLane laneTwo = metricTwo.getLaneValues().stream()
              .filter(x -> x.getLaneNumber() == laneOne.getLaneNumber())
              .findAny().orElse(null);
          assertNotNull(laneTwo);
          assertEquals(laneOne.getLaneValue(), laneTwo.getLaneValue());
          assertEquals(laneOne.getRead1Value(), laneTwo.getRead1Value());
          assertEquals(laneOne.getRead2Value(), laneTwo.getRead2Value());
        }
      }
    }
  }

  private static void assertTestEqual(Test one, Test two) {
    assertEquals(one.isExtractionSkipped(), two.isExtractionSkipped());
    assertListsEqual(one.getExtractions(), two.getExtractions(), JacksonTest::assertSampleEqual);
    assertListsEqual(one.getFullDepthSequencings(), two.getFullDepthSequencings(),
        JacksonTest::assertSampleEqual);
    assertEquals(one.getGroupId(), two.getGroupId());
    assertEquals(one.getLatestActivityDate(), two.getLatestActivityDate());
    assertEquals(one.isLibraryPreparationSkipped(), two.isLibraryPreparationSkipped());
    assertListsEqual(one.getLibraryPreparations(), two.getLibraryPreparations(),
        JacksonTest::assertSampleEqual);
    assertEquals(one.isLibraryQualificationSkipped(), two.isLibraryQualificationSkipped());
    assertListsEqual(one.getLibraryQualifications(), two.getLibraryQualifications(),
        JacksonTest::assertSampleEqual);
    assertEquals(one.getName(), two.getName());
    assertEquals(one.getTargetedSequencing(), two.getTargetedSequencing());
    assertEquals(one.getTimepoint(), two.getTimepoint());
    assertEquals(one.getTissueOrigin(), two.getTissueOrigin());
    assertEquals(one.getTissueType(), two.getTissueType());
    assertEquals(one.getLibraryDesignCode(), two.getLibraryDesignCode());
    assertEquals(one.getExtractionDaysSpent(), two.getExtractionDaysSpent());
    assertEquals(one.getExtractionPreparationDaysSpent(), two.getExtractionPreparationDaysSpent());
    assertEquals(one.getExtractionQcDaysSpent(), two.getExtractionQcDaysSpent());
    assertEquals(one.getExtractionTransferDaysSpent(), two.getExtractionTransferDaysSpent());
    assertEquals(one.getLibraryPreparationDaysSpent(), two.getLibraryPreparationDaysSpent());
    assertEquals(one.getLibraryQualificationDaysSpent(), two.getLibraryQualificationDaysSpent());
    assertEquals(one.getLibraryQualificationLoadingDaysSpent(),
        two.getLibraryQualificationLoadingDaysSpent());
    assertEquals(one.getLibraryQualificationSequencingDaysSpent(),
        two.getLibraryQualificationSequencingDaysSpent());
    assertEquals(one.getLibraryQualificationQcDaysSpent(),
        two.getLibraryQualificationQcDaysSpent());
    assertEquals(one.getFullDepthSequencingDaysSpent(), two.getFullDepthSequencingDaysSpent());
    assertEquals(one.getFullDepthSequencingLoadingDaysSpent(),
        two.getFullDepthSequencingLoadingDaysSpent());
    assertEquals(one.getFullDepthSequencingSequencingDaysSpent(),
        two.getFullDepthSequencingSequencingDaysSpent());
    assertEquals(one.getFullDepthSequencingQcDaysSpent(), two.getFullDepthSequencingQcDaysSpent());
  }

  private static <T> void assertListsEqual(List<T> one, List<T> two, BiConsumer<T, T> assertEqual) {
    assertEquals(one.size(), two.size());
    for (int i = 0; i < one.size(); i++) {
      assertEqual.accept(one.get(i), two.get(i));
    }
  }

  private static void assertCaseEqual(Case one, Case two) {
    assertEquals(one.getAssayId(), two.getAssayId());
    assertEquals(one.getAssayName(), two.getAssayName());
    assertEquals(one.getAssayDescription(), two.getAssayDescription());
    assertDonorsEqual(one.getDonor(), two.getDonor());
    assertEquals(one.getId(), two.getId());
    assertEquals(one.getLatestActivityDate(), two.getLatestActivityDate());
    assertEquals(1, one.getProjects().size());
    assertEquals(1, two.getProjects().size());
    assertProjectEqual(one.getProjects().iterator().next(), two.getProjects().iterator().next());
    assertListsEqual(one.getReceipts(), two.getReceipts(), JacksonTest::assertSampleEqual);
    assertRequisitionEqual(one.getRequisition(), two.getRequisition());
    assertEquals(one.getStartDate(), two.getStartDate());
    assertListsEqual(one.getTests(), two.getTests(), JacksonTest::assertTestEqual);
    assertListsEqual(one.getQcGroups(), two.getQcGroups(), JacksonTest::assertAnalysisQcGroupEqual);
    assertListsEqual(one.getDeliverables(), two.getDeliverables(),
        JacksonTest::assertCaseDeliverableEqual);
    assertEquals(one.getTimepoint(), two.getTimepoint());
    assertEquals(one.getTissueOrigin(), two.getTissueOrigin());
    assertEquals(one.getTissueType(), two.getTissueType());
    assertEquals(one.getReceiptDaysSpent(), two.getReceiptDaysSpent());
    assertEquals(one.getAnalysisReviewDaysSpent(), two.getAnalysisReviewDaysSpent());
    assertEquals(one.getReleaseApprovalDaysSpent(), two.getReleaseApprovalDaysSpent());
    assertEquals(one.getReceiptDaysSpent(), two.getReceiptDaysSpent());
    assertEquals(one.getCaseDaysSpent(), two.getCaseDaysSpent());
    assertEquals(one.getPauseDays(), two.getPauseDays());
  }

  private static void assertOmittedRunSampleEqual(OmittedRunSample one, OmittedRunSample two) {
    assertEquals(one.getId(), two.getId());
    assertEquals(one.getName(), two.getName());
    assertEquals(one.getProject(), two.getProject());
    assertEquals(one.getRunId(), two.getRunId());
    assertEquals(one.getRunName(), two.getRunName());
    assertEquals(one.getSequencingLane(), two.getSequencingLane());
    assertEquals(one.getSequencingType(), two.getSequencingType());
    assertEquals(one.getQcPassed(), two.getQcPassed());
    assertEquals(one.getQcReason(), two.getQcReason());
    assertEquals(one.getQcNote(), two.getQcNote());
    assertEquals(one.getQcUser(), two.getQcUser());
    assertEquals(one.getQcDate(), two.getQcDate());
    assertEquals(one.getDataReviewPassed(), two.getDataReviewPassed());
    assertEquals(one.getDataReviewUser(), two.getDataReviewUser());
    assertEquals(one.getDataReviewDate(), two.getDataReviewDate());
  }

  private static void assertShesmuCaseEqual(ShesmuCase one, ShesmuCase two) {
    assertEquals(one.getAssayName(), two.getAssayName());
    assertEquals(one.getAssayVersion(), two.getAssayVersion());
    assertEquals(one.getCaseIdentifier(), two.getCaseIdentifier());
    assertEquals(one.getCaseStatus(), two.getCaseStatus());
    assertEquals(one.getCompletedDate(), two.getCompletedDate());
    assertEquals(one.getLimsIds(), two.getLimsIds());
    assertEquals(one.getRequisitionId(), two.getRequisitionId());
    assertEquals(one.getRequisitionName(), two.getRequisitionName());
  }

  private static void assertShesmuDetailedCaseEqual(ShesmuDetailedCase one,
      ShesmuDetailedCase two) {
    assertEquals(one.getAssayName(), two.getAssayName());
    assertEquals(one.getAssayVersion(), two.getAssayVersion());
    assertEquals(one.getCaseIdentifier(), two.getCaseIdentifier());
    assertEquals(one.isPaused(), two.isPaused());
    assertEquals(one.isStopped(), two.isStopped());
    assertEquals(one.getCaseStatus(), two.getCaseStatus());
    assertEquals(one.getCompletedDate(), two.getCompletedDate());
    assertEquals(one.getClinicalCompletedDate(), two.getClinicalCompletedDate());
    assertEquals(one.getSequencing().size(), two.getSequencing().size());
    assertShesmuTestEqual(one.getSequencing().iterator().next(),
        two.getSequencing().iterator().next());
    assertEquals(one.getRequisitionId(), two.getRequisitionId());
    assertEquals(one.getRequisitionName(), two.getRequisitionName());
  }

  private static void assertShesmuSampleEqual(ShesmuSample one, ShesmuSample two) {
    assertEquals(one.getId(), two.getId());
    assertEquals(one.isSupplemental(), two.isSupplemental());
    assertEquals(one.isQcFailed(), two.isQcFailed());
  }

  private static void assertShesmuTestEqual(ShesmuSequencing one, ShesmuSequencing two) {
    assertEquals(one.getTest(), two.getTest());
    assertEquals(one.getType(), two.getType());
    assertEquals(one.isComplete(), two.isComplete());
    assertEquals(one.getLimsIds().size(), two.getLimsIds().size());
    assertShesmuSampleEqual(one.getLimsIds().iterator().next(), two.getLimsIds().iterator().next());

  }

  private static Donor makeDonor() {
    return new Donor.Builder()
        .id("SAM1")
        .name("Donor")
        .externalName("ext")
        .build();
  }

  private static Metric makeMetric() {
    return new Metric.Builder()
        .containerModel("Flow cell")
        .minimum(new BigDecimal(12.34d))
        .maximum(new BigDecimal(56.78d))
        .name("Metric")
        .negateTissueType(true)
        .nucleicAcidType("DNA")
        .readLength(123)
        .readLength2(456)
        .sortPriority(50)
        .thresholdType(ThresholdType.BETWEEN)
        .tissueMaterial("FFPE")
        .tissueOrigin("Lv")
        .tissueType("R")
        .units("%")
        .build();
  }

  private static MetricSubcategory makeMetricSubcategory() {
    return new MetricSubcategory.Builder()
        .name("Subcategory")
        .libraryDesignCode("WG")
        .sortPriority(20)
        .metrics(Collections.singletonList(makeMetric()))
        .build();
  }

  private static AssayTargets makeAssayTargets() {
    return new AssayTargets.Builder()
        .caseDays(37)
        .receiptDays(1)
        .extractionDays(2)
        .libraryPreparationDays(3)
        .libraryQualificationDays(4)
        .fullDepthSequencingDays(5)
        .analysisReviewDays(6)
        .releaseApprovalDays(7)
        .releaseDays(8)
        .build();
  }

  private static Assay makeAssay() {
    Map<MetricCategory, List<MetricSubcategory>> categories = new HashMap<>();
    categories.put(MetricCategory.RECEIPT, Collections.singletonList(makeMetricSubcategory()));

    return new Assay.Builder()
        .id(77L)
        .name("Assay")
        .description("Long assay description")
        .version("2.0")
        .targets(makeAssayTargets())
        .metricCategories(categories)
        .build();
  }

  private static AnalysisQcGroup makeAnalysisQcGroup() {
    return new AnalysisQcGroup.Builder()
        .callability(new BigDecimal("12.34"))
        .collapsedCoverage(new BigDecimal("56.78"))
        .groupId("group")
        .libraryDesignCode("PG")
        .purity(new BigDecimal("90.12"))
        .tissueOrigin("Pl")
        .tissueType("R")
        .build();
  }

  private static CaseRelease makeCaseRelease() {
    return new CaseReleaseImpl.Builder()
        .deliverable("FastQ")
        .qcDate(LocalDate.of(2024, 1, 2))
        .qcStatus(ReleaseQcStatus.FAILED_STOP)
        .qcNote("No good")
        .qcUser("Me")
        .build();
  }

  private static CaseDeliverable makeCaseDeliverable() {
    List<CaseRelease> releases = Collections.singletonList(makeCaseRelease());

    return new CaseDeliverableImpl.Builder()
        .deliverableCategory("Data Release")
        .analysisReviewSkipped(false)
        .analysisReviewQcDate(LocalDate.of(2024, 1, 3))
        .analysisReviewQcStatus(AnalysisReviewQcStatus.PASSED)
        .analysisReviewQcNote("A-OK")
        .analysisReviewQcUser("Someone")
        .releaseApprovalQcDate(LocalDate.of(2024, 1, 4))
        .releaseApprovalQcStatus(ReleaseApprovalQcStatus.FAILED_STOP)
        .releaseApprovalQcNote("Something went wrong")
        .releaseApprovalQcUser("Someone else")
        .releases(releases)
        .analysisReviewDaysSpent(2)
        .releaseApprovalDaysSpent(3)
        .deliverableDaysSpent(6)
        .build();
  }

  private static Project makeProject() {
    Map<String, List<String>> deliverables = new HashMap<>();
    deliverables.put("Data Release", Collections.singletonList("FastQ"));

    return new Project.Builder()
        .name("PROJ")
        .pipeline("RUO")
        .deliverables(deliverables)
        .build();
  }

  private static Requisition makeRequisition() {
    return new Requisition.Builder()
        .id(42)
        .name("My Requisition")
        .stopped(false)
        .stopReason("Not stopped")
        .paused(true)
        .pauseReason("Waiting...")
        .assayIds(Collections.singleton(77L))
        .build();
  }

  private static Lane makeLane() {
    return new Lane.Builder()
        .clustersPf(876L)
        .laneNumber(3)
        .percentOverQ30Read1(67)
        .percentOverQ30Read2(76)
        .percentPfixRead1(new BigDecimal("2.3"))
        .percentPfixRead2(new BigDecimal("3.2"))
        .build();
  }

  private static Run makeRun() {
    return new Run.Builder()
        .clustersPf(12L)
        .startDate(LocalDate.of(2024, 1, 5))
        .completionDate(LocalDate.of(2024, 1, 6))
        .containerModel("Flow cell")
        .dataReviewDate(LocalDate.of(2024, 1, 10))
        .dataReviewPassed(true)
        .dataReviewUser("Run Reviewer")
        .id(23L)
        .joinedLanes(false)
        .lanes(Collections.singletonList(makeLane()))
        .name("Runrunrun")
        .percentOverQ30(new BigDecimal("98.76"))
        .qcDate(LocalDate.of(2024, 1, 9))
        .qcPassed(true)
        .qcUser("Run Runner")
        .readLength(123)
        .readLength2(456)
        .sequencingParameters("params")
        .build();
  }

  private static Sample makeSample(String name) {
    return new Sample.Builder()
        .assayIds(Collections.singleton(77L))
        .clustersPerSample(5)
        .preliminaryClustersPerSample(6)
        .concentration(new BigDecimal("12.34"))
        .concentrationUnits("ng/uL")
        .createdDate(LocalDate.of(2024, 1, 5))
        .dataReviewDate(LocalDate.of(2024, 1, 8))
        .dataReviewPassed(true)
        .dataReviewUser("QA Person")
        .donor(makeDonor())
        .duplicationRate(new BigDecimal("23.45"))
        .groupId("Group")
        .id("SAM2")
        .lambdaClusters(8)
        .lambdaMethylation(new BigDecimal("34.56"))
        .libraryDesignCode("WG")
        .librarySize(111)
        .mappedToCoding(new BigDecimal("45.67"))
        .meanCoverageDeduplicated(new BigDecimal("56.78"))
        .preliminaryMeanCoverageDeduplicated(new BigDecimal("67.89"))
        .meanInsertSize(new BigDecimal("78.90"))
        .medianInsertSize(new BigDecimal("89.01"))
        .name(name)
        .nucleicAcidType("DNA")
        .onTargetReads(new BigDecimal("90.12"))
        .project("PROJ")
        .puc19Clusters(22)
        .puc19Methylation(new BigDecimal("2.22"))
        .qcDate(LocalDate.of(2024, 1, 7))
        .qcPassed(true)
        .qcReason("Ready")
        .qcNote("All good")
        .qcUser("Lab Tech")
        .rRnaContamination(new BigDecimal("3.33"))
        .rawCoverage(new BigDecimal("4.44"))
        .requisition(makeRequisition())
        .run(makeRun())
        .secondaryId("asdf1234")
        .sequencingLane("3")
        .targetedSequencing("Panel")
        .timepoint("First")
        .tissueMaterial("FFPE")
        .tissueOrigin("Lv")
        .tissueType("P")
        .volume(new BigDecimal("5.55"))
        .relativeCpgInRegions(new BigDecimal("6.66"))
        .methylationBeta(new BigDecimal("7.77"))
        .peReads(35)
        .transferDate(LocalDate.of(2024, 06, 11))
        .dv200(new BigDecimal("999.99"))
        .metrics(makeSampleMetrics())
        .build();
  }

  private static List<SampleMetric> makeSampleMetrics() {
    return Collections.singletonList(
        new SampleMetric.Builder()
            .name("Test Metric")
            .thresholdType(ThresholdType.BETWEEN)
            .minimum(new BigDecimal("0.0"))
            .maximum(new BigDecimal("99.99"))
            .metricLevel(MetricLevel.LANE)
            .preliminary(false)
            .value(new BigDecimal("12.34"))
            .laneValues(Collections.singleton(new SampleMetricLane(1, new BigDecimal("23.34"),
                new BigDecimal("34.56"), new BigDecimal("45.67"))))
            .qcPassed(true)
            .units("%")
            .build());
  }

  private static Test makeTest() {
    return new Test.Builder()
        .extractionSkipped(true)
        .extractions(Collections.singletonList(makeSample("extraction")))
        .fullDepthSequencings(Collections.singletonList(makeSample("sequenced")))
        .groupId("Group")
        .libraryPreparationSkipped(false)
        .libraryPreparations(Collections.singletonList(makeSample("library")))
        .libraryQualificationSkipped(true)
        .libraryQualifications(Collections.singletonList(makeSample("lib.qual")))
        .name("Single Test")
        .targetedSequencing("Panel")
        .timepoint("First")
        .tissueOrigin("Lv")
        .tissueType("P")
        .libraryDesignCode("WG")
        .extractionDaysSpent(3)
        .extractionPreparationDaysSpent(1)
        .extractionQcDaysSpent(0)
        .extractionTransferDaysSpent(2)
        .libraryPreparationDaysSpent(2)
        .libraryQualificationDaysSpent(3)
        .libraryQualificationLoadingDaysSpent(1)
        .libraryQualificationSequencingDaysSpent(0)
        .libraryQualificationQcDaysSpent(2)
        .fullDepthSequencingDaysSpent(4)
        .fullDepthSequencingLoadingDaysSpent(0)
        .fullDepthSequencingSequencingDaysSpent(3)
        .fullDepthSequencingQcDaysSpent(1)
        .build();
  }

  private static Case makeCase() {
    return new CaseImpl.Builder()
        .assayId(1L)
        .assayName("Assay")
        .assayDescription("Description")
        .donor(makeDonor())
        .id("CASE123")
        .latestActivityDate(LocalDate.of(2024, 1, 11))
        .projects(Collections.singleton(makeProject()))
        .receipts(Collections.singletonList(makeSample("receipt")))
        .requisition(makeRequisition())
        .startDate(LocalDate.of(2023, 12, 31))
        .tests(Collections.singletonList(makeTest()))
        .qcGroups(Collections.singletonList(makeAnalysisQcGroup()))
        .deliverables(Collections.singletonList(makeCaseDeliverable()))
        .timepoint("First")
        .tissueOrigin("Lv")
        .tissueType("P")
        .receiptDaysSpent(1)
        .analysisReviewDaysSpent(2)
        .releaseApprovalDaysSpent(3)
        .releaseDaysSpent(4)
        .caseDaysSpent(35)
        .pauseDays(1)
        .build();
  }

  private static OmittedRunSample makeOmittedRunSample() {
    return new OmittedRunSample.Builder()
        .id("123_4_LDI567")
        .name("Joe")
        .project("TEST")
        .runId(123L)
        .runName("Test Run")
        .sequencingLane(4)
        .sequencingType(MetricCategory.FULL_DEPTH_SEQUENCING)
        .qcPassed(true)
        .qcReason("Good stuff")
        .qcNote("This is my note")
        .qcUser("Me")
        .qcDate(LocalDate.now().minusDays(1L))
        .dataReviewPassed(true)
        .dataReviewUser("Someone else")
        .dataReviewDate(LocalDate.now())
        .build();
  }

  private static ShesmuCase makeShesmuCase() {
    Set<String> limsIds = new HashSet<>();
    limsIds.add("ID1");
    limsIds.add("ID2");

    return new ShesmuCase.Builder()
        .assayName("Assay")
        .assayVersion("2.0")
        .caseIdentifier("CASE10")
        .caseStatus(CaseStatus.COMPLETED)
        .completedDateLocal(LocalDate.of(2024, 1, 13))
        .limsIds(limsIds)
        .requisitionId(1L)
        .requisitionName("Some Req")
        .build();
  }

  private static ShesmuDetailedCase makeShesmuDetailedCase() {
    Set<ShesmuSequencing> sequencing = new HashSet<>();
    Set<ShesmuSample> limsIds = new HashSet<>();
    limsIds.add(new ShesmuSample.Builder()
        .id("ID1")
        .supplemental(false)
        .qcFailed(false)
        .build());
    sequencing.add(new ShesmuSequencing.Builder()
        .test("Some Test")
        .limsIds(limsIds)
        .complete(true)
        .type(MetricCategory.LIBRARY_QUALIFICATION)
        .build());

    return new ShesmuDetailedCase.Builder()
        .assayName("Assay")
        .assayVersion("2.0")
        .caseIdentifier("CASE10")
        .stopped(false)
        .paused(false)
        .caseStatus(CaseStatus.COMPLETED)
        .completedDateLocal(LocalDate.of(2024, 1, 13))
        .clinicalCompletedDateLocal(LocalDate.of(2024, 1, 13))
        .sequencing(sequencing)
        .requisitionId(1L)
        .requisitionName("Some Req")
        .build();
  }

}
