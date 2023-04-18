package ca.on.oicr.gsi.dimsum.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import ca.on.oicr.gsi.dimsum.data.CaseStatusCountsForRun;
import ca.on.oicr.gsi.dimsum.data.RequisitionQc;
import ca.on.oicr.gsi.dimsum.data.Run;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import org.junit.jupiter.api.BeforeEach;
import ca.on.oicr.gsi.dimsum.data.Case;
import ca.on.oicr.gsi.dimsum.data.CaseData;
import ca.on.oicr.gsi.dimsum.data.Donor;
import ca.on.oicr.gsi.dimsum.data.Requisition;
import ca.on.oicr.gsi.dimsum.data.Sample;
import ca.on.oicr.gsi.dimsum.data.Test;

public class CaseServiceTest {

  private CaseData caseData;
  private CaseService sut;

  private void addCase(CaseData data, int caseNumber, int requisitionNumber) {
    Case kase = mock(Case.class);
    when(kase.getLatestActivityDate()).thenReturn(LocalDate.now().minusDays(caseNumber));
    when(kase.getReceipts()).thenReturn(new ArrayList<>());
    addSample(kase.getReceipts(), makeSampleName(caseNumber, "N", "A", 1), null);
    addSample(kase.getReceipts(), makeSampleName(caseNumber, "N", "A", 2), null);
    when(kase.getTests()).thenReturn(new ArrayList<>());
    addTest(kase, caseNumber, "A");
    addTest(kase, caseNumber, "B");
    when(kase.getRequisition()).thenReturn(makeRequisition(requisitionNumber));
    data.getCases().add(kase);
  }

  private void addSample(Collection<Sample> collection, String name, String runName) {
    Sample newSample;
    // Not using mocks because we're kind-of testing hashcode for distinct filters here too
    if (runName == null) {
      newSample = new Sample.Builder().id(name).name(name).donor(mock(Donor.class)).project("PROJ")
          .tissueOrigin("To").tissueType("T")
          .createdDate(LocalDate.now()).build();
    } else {
      newSample = new Sample.Builder().id(name).name(name).donor(mock(Donor.class)).project("PROJ")
          .tissueOrigin("To").tissueType("T").run(new Run.Builder().name(runName).lanes(new ArrayList<>()).build())
          .createdDate(LocalDate.now()).build();
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
    // Not using mocks because we're kind-of testing hashcode for distinct filters here too
    return new Requisition.Builder()
        .id(requisitionNumber)
        .name(String.format("REQ_%d", requisitionNumber))
        .assayId(2L)
        .stopped(requisitionNumber % 2 == 1 ? true : false)
        .finalReports(requisitionNumber % 2 == 1 ? null :
            Arrays.asList(new RequisitionQc.Builder().qcPassed(true).qcUser("test").qcDate(
                LocalDate.now()).build()))
        .build();
  }

  // Sample names are case number + test letter (N if n/a) + gate letter (receipt=A, extract=B,
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
    CaseStatusCountsForRun matches = sut.getCaseStatusCountsForRun("Run100");
    assertEquals(0, matches.getCompletedCaseCount());
    assertEquals(0, matches.getStoppedCaseCount());
    assertEquals(0, matches.getActiveCaseCount());
  }

  @org.junit.jupiter.api.Test
  public void testGetCaseCountsForRun_oneMatchingRun() {
    CaseStatusCountsForRun matches = sut.getCaseStatusCountsForRun("Run1");
    assertEquals(0, matches.getCompletedCaseCount());
    assertEquals(1, matches.getStoppedCaseCount());
    assertEquals(0, matches.getActiveCaseCount());
  }

  @org.junit.jupiter.api.Test
  public void testGetCaseCountsForRun_twoMatchingRuns() {
    CaseStatusCountsForRun matches = sut.getCaseStatusCountsForRun("Run2");
    assertEquals(1, matches.getCompletedCaseCount());
    assertEquals(1, matches.getStoppedCaseCount());
    assertEquals(0, matches.getActiveCaseCount());
  }

  @org.junit.jupiter.api.Test
  public void testGetCaseData() {
    CaseData data = sut.getCaseData();
    assertNotNull(data);
    assertEquals(2, data.getCases().size());
  }

}
