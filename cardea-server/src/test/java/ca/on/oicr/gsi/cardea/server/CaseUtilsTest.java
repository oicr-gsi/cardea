package ca.on.oicr.gsi.cardea.server;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;
import java.util.Collections;
import org.junit.jupiter.api.Test;
import ca.on.oicr.gsi.cardea.data.Assay;
import ca.on.oicr.gsi.cardea.data.AssayTargets;
import ca.on.oicr.gsi.cardea.data.Case;
import ca.on.oicr.gsi.cardea.data.CaseDeliverable;
import ca.on.oicr.gsi.cardea.data.CaseRelease;

public class CaseUtilsTest {

  private static final String CLINICAL_REPORT = "Clinical Report";

  @Test
  public void testHasDeliverable() {
    Case clinical = mockCase(true, 0);
    Case ruo = mockCase(false, 0);
    assertTrue(CaseUtils.hasDeliverable(clinical, CLINICAL_REPORT));
    assertFalse(CaseUtils.hasDeliverable(ruo, CLINICAL_REPORT));
  }

  @Test
  public void testGetCasePriority() {
    // clinical
    testGetCasePriority(true, 10, 45, 128);
    testGetCasePriority(true, 10, 14, 300);
    testGetCasePriority(true, 41, 45, 300);
    testGetCasePriority(true, 45, 45, 1100);
    testGetCasePriority(true, 50, 45, 1105);

    // non-clinical with TAT target
    testGetCasePriority(false, 10, 45, 103);
    testGetCasePriority(false, 45, 45, 200);
    testGetCasePriority(false, 50, 45, 205);

    // non-clinical without TAT target
    testGetCasePriority(false, 40, null, 40);
    testGetCasePriority(false, 55, null, 55);
  }

  private static void testGetCasePriority(boolean clinical, int caseAge, Integer tatTarget,
      int expectedPriority) {
    Case kase = mockCase(clinical, caseAge);
    Assay assay = mockAssay(tatTarget);
    assertEquals(expectedPriority, CaseUtils.getCasePriority(kase, assay));
  }

  private static Case mockCase(boolean clinical, int age) {
    Case kase = mock(Case.class);
    if (clinical) {
      CaseDeliverable deliverable = mock(CaseDeliverable.class);
      CaseRelease release = mock(CaseRelease.class);
      when(release.getDeliverable()).thenReturn(CLINICAL_REPORT);
      when(deliverable.getReleases()).thenReturn(Collections.singletonList(release));
      when(kase.getDeliverables()).thenReturn(Collections.singletonList(deliverable));
    }
    when(kase.getCaseDaysSpent()).thenReturn(age);
    return kase;
  }

  private static Assay mockAssay(Integer tatTarget) {
    Assay assay = mock(Assay.class);
    AssayTargets targets = mock(AssayTargets.class);
    when(targets.getCaseDays()).thenReturn(tatTarget);
    when(assay.getTargets()).thenReturn(targets);
    return assay;
  }

}
