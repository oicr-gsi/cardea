package ca.on.oicr.gsi.cardea.server.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import ca.on.oicr.gsi.cardea.data.CaseStatusesForRun;
import ca.on.oicr.gsi.cardea.data.Run;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import org.junit.jupiter.api.BeforeEach;
import ca.on.oicr.gsi.cardea.data.Case;
import ca.on.oicr.gsi.cardea.data.CaseData;
import ca.on.oicr.gsi.cardea.data.CaseDeliverable;
import ca.on.oicr.gsi.cardea.data.CaseRelease;
import ca.on.oicr.gsi.cardea.data.Donor;
import ca.on.oicr.gsi.cardea.data.Requisition;
import ca.on.oicr.gsi.cardea.data.Sample;
import ca.on.oicr.gsi.cardea.data.Test;
import ca.on.oicr.gsi.cardea.data.CaseQc.ReleaseQcStatus;

public class CaseServiceTest {

  private CaseData caseData;
  private CaseService sut;

  private void addCase(CaseData data, int caseNumber, int requisitionNumber) {
    Case kase = mock(Case.class);
    when(kase.getId()).thenReturn("R" + caseNumber);
    when(kase.getLatestActivityDate()).thenReturn(LocalDate.now().minusDays(caseNumber));
    when(kase.getReceipts()).thenReturn(new ArrayList<>());
    addSample(kase.getReceipts(), makeSampleName(caseNumber, "N", "A", 1), null);
    addSample(kase.getReceipts(), makeSampleName(caseNumber, "N", "A", 2), null);
    when(kase.getTests()).thenReturn(new ArrayList<>());
    addTest(kase, caseNumber, "A");
    addTest(kase, caseNumber, "B");
    when(kase.getRequisition()).thenReturn(makeRequisition(requisitionNumber));
    when(kase.isStopped()).thenReturn(requisitionNumber % 2 == 1 ? true : false);
    when(kase.getDeliverables()).thenReturn(new ArrayList<>());
    CaseDeliverable deliverable = mock(CaseDeliverable.class);
    when(deliverable.getReleases()).thenReturn(new ArrayList<>());
    CaseRelease release = mock(CaseRelease.class);
    when(release.getDeliverable()).thenReturn("Full Pipeline");
    if (requisitionNumber % 1 != 1) {
      when(release.getQcStatus()).thenReturn(ReleaseQcStatus.PASSED_RELEASE);
      when(release.getQcUser()).thenReturn("User");
      when(release.getQcDate()).thenReturn(LocalDate.now());
    }
    deliverable.getReleases().add(release);
    kase.getDeliverables().add(deliverable);
    data.getCases().add(kase);
  }

  private void addSample(Collection<Sample> collection, String name, String runName) {
    Sample newSample;
    // Not using mocks because we're kind-of testing hashcode for distinct filters
    // here too
    if (runName == null) {
      newSample = new Sample.Builder().id(name).name(name).donor(mock(Donor.class)).project("PROJ")
          .tissueOrigin("To")
          .tissueType("T")
          .metrics(new ArrayList<>())
          .createdDate(LocalDate.now())
          .build();
    } else {
      newSample = new Sample.Builder().id(name).name(name).donor(mock(Donor.class)).project("PROJ")
          .tissueOrigin("To")
          .tissueType("T")
          .run(new Run.Builder().name(runName).lanes(new ArrayList<>()).build())
          .metrics(new ArrayList<>())
          .createdDate(LocalDate.now())
          .build();
    }

    collection.add(newSample);
  }

  private void addTest(Case kase, int caseNumber, String testLetter) {
    Test test = mock(Test.class);
    when(test.getName()).thenReturn(testLetter);
    when(test.getExtractions()).thenReturn(new ArrayList<>());
    addSample(test.getExtractions(), makeSampleName(caseNumber, testLetter, "B", 1), null);
    addSample(test.getExtractions(), makeSampleName(caseNumber, testLetter, "B", 2), null);
    when(test.getLibraryPreparations()).thenReturn(new ArrayList<>());
    addSample(test.getLibraryPreparations(), makeSampleName(caseNumber, testLetter, "C", 1), null);
    addSample(test.getLibraryPreparations(), makeSampleName(caseNumber, testLetter, "C", 2), null);
    when(test.getLibraryQualifications()).thenReturn(new ArrayList<>());
    addSample(test.getLibraryQualifications(), makeSampleName(caseNumber, testLetter, "D", 1),
        String.format("Run%s", caseNumber));
    addSample(test.getLibraryQualifications(), makeSampleName(caseNumber, testLetter, "D", 2),
        String.format("Run%s", (caseNumber + 1)));
    when(test.getFullDepthSequencings()).thenReturn(new ArrayList<>());
    addSample(test.getFullDepthSequencings(), makeSampleName(caseNumber, testLetter, "E", 1),
        String.format("Run%s", caseNumber));
    addSample(test.getFullDepthSequencings(), makeSampleName(caseNumber, testLetter, "E", 2),
        String.format("Run%s", (caseNumber + 1)));
    kase.getTests().add(test);
  }

  private Requisition makeRequisition(int requisitionNumber) {
    // Not using mocks because we're kind-of testing hashcode for distinct filters
    // here too
    return new Requisition.Builder()
        .id(requisitionNumber)
        .name(String.format("REQ_%d", requisitionNumber))
        .assayIds(Collections.singleton(2L))
        .stopped(requisitionNumber % 2 == 1 ? true : false)
        .build();
  }

  // Sample names are case number + test letter (N if n/a) + gate letter
  // (receipt=A, extract=B,
  // etc.) + sample number
  private String makeSampleName(int caseNumber, String testLetter, String gateLetter,
      int sampleNumber) {
    return caseNumber + testLetter + "-" + gateLetter + sampleNumber;
  }

  @BeforeEach
  public void setup() {
    sut = new CaseService(null);
    caseData = mock(CaseData.class);
    when(caseData.getCases()).thenReturn(new ArrayList<>());
    addCase(caseData, 1, 1);
    addCase(caseData, 2, 2);
    sut.setCaseData(caseData);
  }

  @org.junit.jupiter.api.Test
  public void testGetCaseCountsForRun_noMatchingRuns() {
    CaseStatusesForRun matches = sut.getCaseStatusesForRun("Run100");
    assertEquals(0, matches.getCompletedCases().size());
    assertEquals(0, matches.getStoppedCases().size());
    assertEquals(0, matches.getActiveCases().size());
  }

  @org.junit.jupiter.api.Test
  public void testGetCaseCountsForRun_oneMatchingRun() {
    CaseStatusesForRun matches = sut.getCaseStatusesForRun("Run1");
    assertEquals(0, matches.getCompletedCases().size());
    assertEquals(1, matches.getStoppedCases().size());
    assertEquals(0, matches.getActiveCases().size());
  }

  @org.junit.jupiter.api.Test
  public void testGetCaseCountsForRun_twoMatchingRuns() {
    CaseStatusesForRun matches = sut.getCaseStatusesForRun("Run2");
    assertEquals(1, matches.getCompletedCases().size());
    assertEquals(1, matches.getStoppedCases().size());
    assertEquals(0, matches.getActiveCases().size());
  }

  @org.junit.jupiter.api.Test
  public void testGetCaseData() {
    CaseData data = sut.getCaseData();
    assertNotNull(data);
    assertEquals(2, data.getCases().size());
  }

}
