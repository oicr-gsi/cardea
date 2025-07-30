package ca.on.oicr.gsi.cardea.server;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import ca.on.oicr.gsi.cardea.data.Assay;
import ca.on.oicr.gsi.cardea.data.AssayTargets;
import ca.on.oicr.gsi.cardea.data.Case;
import ca.on.oicr.gsi.cardea.data.CaseData;
import ca.on.oicr.gsi.cardea.data.CaseDeliverable;
import ca.on.oicr.gsi.cardea.data.CaseRelease;
import ca.on.oicr.gsi.cardea.data.Donor;
import ca.on.oicr.gsi.cardea.data.Lane;
import ca.on.oicr.gsi.cardea.data.Metric;
import ca.on.oicr.gsi.cardea.data.MetricCategory;
import ca.on.oicr.gsi.cardea.data.MetricSubcategory;
import ca.on.oicr.gsi.cardea.data.OmittedRunSample;
import ca.on.oicr.gsi.cardea.data.OmittedSample;
import ca.on.oicr.gsi.cardea.data.Project;
import ca.on.oicr.gsi.cardea.data.Requisition;
import ca.on.oicr.gsi.cardea.data.AnalysisQcGroup;
import ca.on.oicr.gsi.cardea.data.Run;
import ca.on.oicr.gsi.cardea.data.Sample;
import ca.on.oicr.gsi.cardea.data.SampleMetric;
import ca.on.oicr.gsi.cardea.data.SampleMetricLane;
import ca.on.oicr.gsi.cardea.data.ThresholdType;
import ca.on.oicr.gsi.cardea.data.CaseQc.AnalysisReviewQcStatus;
import ca.on.oicr.gsi.cardea.data.SampleMetric.MetricLevel;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileReader;
import java.math.BigDecimal;
import java.net.URL;
import java.time.*;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class CaseLoaderTest {

  private static final String testDonorId = "SAM413576";
  private static final String testProjectName = "PROJ";
  private static final String testSampleId = "SAM413577";
  private static final long testRunId = 5476L;
  private static File dataDirectory;
  private CaseLoader sut;

  @BeforeAll
  public static void setupClass() {
    ClassLoader classLoader = CaseLoaderTest.class.getClassLoader();
    URL caseFileUrl = classLoader.getResource("testdata/cases.json");
    File caseFile = new File(caseFileUrl.getFile());
    dataDirectory = caseFile.getParentFile();
  }

  private static void assertDonor(Donor donor) {
    assertNotNull(donor);
    assertEquals(testDonorId, donor.getId());
    assertEquals("PROJ_1289", donor.getName());
  }

  private static void assertOmittedSample(OmittedSample sample) {
    assertNotNull(sample);
    assertEquals("SAM123457", sample.getId());
    assertEquals("NOCASE_0001_01", sample.getName());
    assertEquals("NOCASE", sample.getProject());
    assertNotNull(sample.getDonor());
    assertEquals("SAM123456", sample.getDonor().getId());
    assertEquals(999L, sample.getRequisitionId());
    assertEquals("REQ-X", sample.getRequisitionName());
    assertEquals(LocalDate.of(2022, 12, 13), sample.getCreatedDate());
  }

  private static void assertProject(Project project) {
    assertNotNull(project);
    assertEquals(testProjectName, project.getName());
    assertEquals("Research", project.getPipeline());
    assertNotNull(project.getDeliverables());
    assertEquals(1, project.getDeliverables().size());
    List<String> deliverables = project.getDeliverables().get("Data Release");
    assertNotNull(deliverables);
    assertEquals(2, deliverables.size());
    assertTrue(deliverables.contains("Full Pipeline"));
    assertTrue(deliverables.contains("cBioPortal Submission"));
  }

  private static void assertSample(Sample sample) {
    assertNotNull(sample);
    assertEquals(testSampleId, sample.getId());
    assertEquals("PROJ_1289_Ly_R_nn_1-1", sample.getName());
    assertEquals(Boolean.TRUE, sample.getQcPassed());
    assertEquals(LocalDate.of(2021, 7, 19), sample.getQcDate());
    assertNull(sample.getTransferDate());
  }

  private static void assertRun(Run run) {
    assertNotNull(run);
    assertEquals(testRunId, run.getId());
    assertEquals("210810_A00469_0195_BH2YF2DMXY", run.getName());
    assertEquals("S2", run.getContainerModel());
    assertFalse(run.hasJoinedLanes());
    assertEquals("2×151", run.getSequencingParameters());
    assertEquals(151, run.getReadLength());
    assertEquals(151, run.getReadLength2());
    assertEquals(LocalDate.of(2021, 8, 11), run.getStartDate());
    assertEquals(LocalDate.of(2021, 8, 12), run.getCompletionDate());
    assertTrue(run.getQcPassed());
    assertEquals("Technician One", run.getQcUser());
    assertEquals(LocalDate.of(2021, 8, 12), run.getQcDate());
    assertEquals(new BigDecimal("89.1"), run.getPercentOverQ30());
    assertEquals(2, run.getLanes().size());
    Lane lane1 = run.getLanes().get(0);
    assertEquals(1, lane1.getLaneNumber());
    assertEquals(90, lane1.getPercentOverQ30Read1());
    assertEquals(88, lane1.getPercentOverQ30Read2());
    assertEquals(2163613696L, lane1.getClustersPf());
    assertEquals(new BigDecimal("0.66"), lane1.getPercentPfixRead1());
    assertEquals(new BigDecimal("0.65"), lane1.getPercentPfixRead2());
  }

  private static void assertRunSample(Sample sample) {
    assertEquals("5476_1_LDI73620", sample.getId());
    assertEquals("PROJ_1289_Ly_R_PE_567_WG", sample.getName());
    assertEquals(Boolean.TRUE, sample.getQcPassed());
    assertNotNull(sample.getMetrics());
    assertEquals(1, sample.getMetrics().size());
    SampleMetric metric = sample.getMetrics().get(0);
    assertEquals("Bases Over Q30", metric.getName());
    assertEquals(ThresholdType.GT, metric.getThresholdType());
    assertEquals(MetricLevel.RUN, metric.getMetricLevel());
    assertEquals(new BigDecimal("75.0"), metric.getMinimum());
    assertNull(metric.getMaximum());
    assertEquals(new BigDecimal("89.1"), metric.getValue());
    assertNotNull(metric.getLaneValues());
    assertEquals(2, metric.getLaneValues().size());
    SampleMetricLane lane1 = metric.getLaneValues().stream()
        .filter(x -> x.getLaneNumber() == 1)
        .findAny().orElse(null);
    assertNotNull(lane1);
    assertNull(lane1.getLaneValue());
    assertEquals(new BigDecimal("90.0"), lane1.getRead1Value());
    assertEquals(new BigDecimal("88.0"), lane1.getRead2Value());
    SampleMetricLane lane2 = metric.getLaneValues().stream()
        .filter(x -> x.getLaneNumber() == 2)
        .findAny().orElse(null);
    assertNotNull(lane2);
    assertNull(lane2.getLaneValue());
    assertEquals(new BigDecimal("90.0"), lane2.getRead1Value());
    assertEquals(new BigDecimal("88.0"), lane2.getRead2Value());
  }

  private static void assertOmittedRunSample(OmittedRunSample sample) {
    assertEquals("5459_1_LDI36185", sample.getId());
    assertEquals("MISS_011408_Co_P_PE_490_WG", sample.getName());
    assertEquals(5459L, sample.getRunId());
    assertEquals(1, sample.getSequencingLane());
    assertNull(sample.getQcPassed());
    assertEquals("Not Ready", sample.getQcReason());
    assertNull(sample.getQcUser());
    assertNull(sample.getQcDate());
    assertNull(sample.getQcNote());
    assertNull(sample.getDataReviewPassed());
    assertNull(sample.getDataReviewUser());
    assertNull(sample.getDataReviewDate());
  }

  @BeforeEach
  public void setup() {
    sut = new CaseLoader(dataDirectory, null);
  }

  @Test
  public void testLoad() throws Exception {
    CaseData data = sut.load(null);
    assertNotNull(data);

    ZonedDateTime timestamp = data.getTimestamp();
    assertNotNull(timestamp);
    assertEquals(ZonedDateTime.of(2022, 5, 26, 14, 33, 28, 0, ZoneOffset.UTC), timestamp);

    List<Case> cases = data.getCases();
    assertNotNull(cases);
    assertEquals(1, cases.size());
    Case kase = cases.get(0);
    assertDonor(kase.getDonor());

    assertNotNull(kase.getProjects());
    assertEquals(1, kase.getProjects().size());
    Project project = kase.getProjects().iterator().next();
    assertProject(project);

    assertEquals("WGTS - 40XT/30XN", data.getAssaysById().get(kase.getAssayId()).getName());

    assertNotNull(kase.getReceipts());
    assertEquals(5, kase.getReceipts().size());
    Sample sample = kase.getReceipts().stream().filter(x -> x.getId().equals(testSampleId))
        .findAny().orElse(null);
    assertSample(sample);

    assertNotNull(kase.getTests());
    assertEquals(3, kase.getTests().size());
    ca.on.oicr.gsi.cardea.data.Test test = kase.getTests().stream()
        .filter(x -> x.getName().equals("Normal WG")).findAny().orElse(null);
    assertNotNull(test);
    assertEquals("Normal WG", test.getName());
    assertNotNull(test.getExtractions());
    assertEquals(1, test.getExtractions().size());
    assertNotNull(test.getLibraryPreparations());
    assertEquals(1, test.getLibraryPreparations().size());
    assertNotNull(test.getLibraryQualifications());
    assertEquals(2, test.getLibraryQualifications().size());
    assertNotNull(test.getFullDepthSequencings());
    assertEquals(1, test.getFullDepthSequencings().size());
    Sample fullDepth = test.getFullDepthSequencings().get(0);
    assertRunSample(fullDepth);
    assertEquals(1, test.getExtractionDaysSpent());
    assertEquals(0, test.getExtractionPreparationDaysSpent());
    assertEquals(0, test.getExtractionQcDaysSpent());
    assertEquals(1, test.getExtractionTransferDaysSpent());
    assertEquals(2, test.getLibraryPreparationDaysSpent());
    assertEquals(6, test.getLibraryQualificationDaysSpent());
    assertEquals(1, test.getLibraryQualificationLoadingDaysSpent());
    assertEquals(2, test.getLibraryQualificationSequencingDaysSpent());
    assertEquals(3, test.getLibraryQualificationQcDaysSpent());
    assertEquals(9, test.getFullDepthSequencingDaysSpent());
    assertEquals(2, test.getFullDepthSequencingLoadingDaysSpent());
    assertEquals(3, test.getFullDepthSequencingSequencingDaysSpent());
    assertEquals(4, test.getFullDepthSequencingQcDaysSpent());
    assertRun(fullDepth.getRun());

    assertNotNull(kase.getDeliverables());
    assertEquals(1, kase.getDeliverables().size());
    CaseDeliverable deliverable = kase.getDeliverables().get(0);
    assertEquals("Clinical Report", deliverable.getDeliverableCategory());
    assertEquals("Person", deliverable.getAnalysisReviewQcUser());
    assertEquals(LocalDate.of(2021, 8, 10), deliverable.getAnalysisReviewQcDate());
    assertEquals(AnalysisReviewQcStatus.PASSED, deliverable.getAnalysisReviewQcStatus());
    List<CaseRelease> releases = deliverable.getReleases();
    assertNotNull(releases);
    assertEquals(1, releases.size());
    assertEquals("Clinical Report", releases.get(0).getDeliverable());

    assertNotNull(kase.getQcGroups());
    assertEquals(3, kase.getQcGroups().size());
    AnalysisQcGroup qcGroup = kase.getQcGroups().stream()
        .filter(x -> "M".equals(x.getTissueType()) && "WG".equals(x.getLibraryDesignCode()))
        .findAny().orElse(null);
    assertNotNull(qcGroup);
    assertEquals(new BigDecimal("87.6189"), qcGroup.getCallability());

    List<OmittedRunSample> omittedRunSamples = data.getOmittedRunSamples();
    assertNotNull(omittedRunSamples);
    assertEquals(2, omittedRunSamples.size());
    OmittedRunSample omittedRunSample = omittedRunSamples.stream()
        .filter(x -> "MISS_011408_Co_P_PE_490_WG".equals(x.getName()))
        .findFirst().orElse(null);
    assertNotNull(omittedRunSample);
    assertOmittedRunSample(omittedRunSample);
  }

  @Test
  public void testLoadAssays() throws Exception {
    try (FileReader reader = sut.getAssayReader()) {
      Map<Long, Assay> assaysById = sut.loadAssays(reader);
      assertEquals(1, assaysById.size());
      Long assayId = 2L;
      Assay assay = assaysById.get(assayId);
      assertNotNull(assay);
      assertEquals(assayId, assay.getId());
      assertEquals("WGTS - 40XT/30XN", assay.getName());
      assertEquals(6, assay.getMetricCategories().size());
      assertNotNull(assay.getMetricCategories());
      List<MetricSubcategory> subcategories =
          assay.getMetricCategories().get(MetricCategory.LIBRARY_PREP);
      assertNotNull(subcategories);
      assertEquals(2, subcategories.size());
      MetricSubcategory subcategory = subcategories.stream()
          .filter(x -> "WT Library QC (Qubit, TS, FA)".equals(x.getName()))
          .findAny()
          .orElse(null);
      assertNotNull(subcategory);
      assertEquals("WT", subcategory.getLibraryDesignCode());
      Metric metric = subcategory.getMetrics().stream()
          .filter(x -> "Concentration (Qubit)".equals(x.getName()))
          .findAny()
          .orElse(null);
      assertNotNull(metric);
      assertEquals(new BigDecimal("0.7"), metric.getMinimum());
      AssayTargets targets = assay.getTargets();
      assertNotNull(targets);
      assertEquals(Integer.valueOf(45), targets.getCaseDays());
      assertEquals(Integer.valueOf(2), targets.getReceiptDays());
      assertEquals(Integer.valueOf(3), targets.getExtractionDays());
      assertEquals(Integer.valueOf(3), targets.getLibraryPreparationDays());
      assertEquals(Integer.valueOf(4), targets.getLibraryQualificationDays());
      assertEquals(Integer.valueOf(7), targets.getFullDepthSequencingDays());
      assertEquals(Integer.valueOf(2), targets.getAnalysisReviewDays());
      assertEquals(Integer.valueOf(3), targets.getReleaseApprovalDays());
      assertEquals(Integer.valueOf(3), targets.getReleaseDays());
    }
  }

  @Test
  public void testLoadDonors() throws Exception {
    try (FileReader reader = sut.getDonorReader()) {
      Map<String, Donor> donorsById = sut.loadDonors(reader);
      assertEquals(2, donorsById.size());
      Donor donor = donorsById.get(testDonorId);
      assertDonor(donor);
    }
  }

  @Test
  public void testLoadOmittedSamples() throws Exception {
    Donor donor = mock(Donor.class);
    when(donor.getId()).thenReturn("SAM123456");
    Map<String, Donor> donorsById = Map.of(donor.getId(), donor);

    Requisition requisition = mock(Requisition.class);
    when(requisition.getId()).thenReturn(999L);
    when(requisition.getName()).thenReturn("REQ-X");
    Map<Long, Requisition> requisitionsById = Collections.singletonMap(999L, requisition);

    try (FileReader reader = sut.getNoCaseReader()) {
      List<OmittedSample> samples = sut.loadOmittedSamples(reader, donorsById, requisitionsById);
      assertEquals(2, samples.size());
      OmittedSample sample = samples.stream()
          .filter(x -> Objects.equals("SAM123457", x.getId()))
          .findFirst().orElse(null);
      assertOmittedSample(sample);
    }
  }

  @Test
  public void testLoadProjects() throws Exception {
    try (FileReader reader = sut.getProjectReader()) {
      Map<String, Project> projectsByName = sut.loadProjects(reader);
      assertEquals(1, projectsByName.size());
      assertProject(projectsByName.get(testProjectName));
    }
  }

  @Test
  public void testLoadRequisitions() throws Exception {
    Donor donor = mock(Donor.class);
    when(donor.getId()).thenReturn("SAM413576");
    Map<String, Donor> donorsById = Map.of(donor.getId(), donor);

    try (FileReader reader = sut.getRequisitionReader()) {
      Map<Long, Requisition> requisitionsById = sut.loadRequisitions(reader, donorsById);
      assertEquals(2, requisitionsById.size());
      Long requisitionId = 512L;
      Requisition requisition = requisitionsById.get(requisitionId);
      assertNotNull(requisition);
      assertEquals(requisitionId, requisition.getId());
      assertEquals("REQ-1", requisition.getName());
    }
  }

  @Test
  public void testLoadSamples() throws Exception {
    try (FileReader reader = sut.getSampleReader()) {
      Donor donor = mock(Donor.class);
      when(donor.getId()).thenReturn("SAM413576");
      Map<String, Donor> donorsById = Map.of(donor.getId(), donor);

      Map<Long, Run> runsById = new HashMap<>();
      runsById.put(5459L, mock(Run.class));
      runsById.put(5460L, mock(Run.class));
      runsById.put(5467L, mock(Run.class));
      runsById.put(5476L, mock(Run.class));
      runsById.put(5481L, mock(Run.class));
      runsById.put(5540L, mock(Run.class));

      Requisition requisition = mock(Requisition.class);
      when(requisition.getName()).thenReturn("REQ-1");
      Map<Long, Requisition> requisitionsById = Collections.singletonMap(512L, requisition);

      Map<String, Sample> samplesById =
          sut.loadSamples(reader, donorsById, runsById, requisitionsById);
      assertEquals(20, samplesById.size());
      assertSample(samplesById.get(testSampleId));
    }
  }

  @Test
  public void testLoadRuns() throws Exception {
    try (FileReader reader = sut.getRunReader()) {
      Map<Long, Run> runsById = sut.loadRuns(reader);
      assertEquals(6, runsById.size());
      assertRun(runsById.get(testRunId));
    }
  }

  @Test
  public void testLoadOmittedRunSamples() throws Exception {
    try (FileReader reader = sut.getNoCaseRunlibReader()) {
      Run run = mock(Run.class);
      when(run.getId()).thenReturn(5459L);
      when(run.getName()).thenReturn("210805_M00753_0369_000000000-G8WYC");
      Map<Long, Run> runsById = new HashMap<>();
      runsById.put(5459L, run);

      List<OmittedRunSample> samples = sut.loadOmittedRunSamples(reader, runsById);
      assertEquals(2, samples.size());
      OmittedRunSample sample = samples.stream()
          .filter(x -> "MISS_011408_Co_P_PE_490_WG".equals(x.getName()))
          .findFirst().orElse(null);
      assertOmittedRunSample(sample);
    }
  }

}
