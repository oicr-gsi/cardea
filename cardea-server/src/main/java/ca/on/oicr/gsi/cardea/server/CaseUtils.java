package ca.on.oicr.gsi.cardea.server;

import java.util.Objects;
import ca.on.oicr.gsi.cardea.data.Assay;
import ca.on.oicr.gsi.cardea.data.Case;

public class CaseUtils {

  public static int getCasePriority(Case kase, Assay assay) {
    Integer target = assay.getTargets().getCaseDays();
    if (target == null) {
      return kase.getCaseDaysSpent();
    }
    int caseAge = kase.getCaseDaysSpent();

    int clinicalMod = hasDeliverable(kase, "Clinical Report") ? 10 : 1;
    int thing1 =
        (int) Math.ceil(100D * clinicalMod * (1.0 / Integer.max(target - caseAge + 1, 1)));
    int thing2 = Math.max(0, caseAge - target);

    int priority = 100 + thing1 + thing2;
    return priority;
  }

  public static boolean hasDeliverable(Case kase, String deliverable) {
    return kase.getDeliverables().stream()
        .flatMap(x -> x.getReleases().stream())
        .anyMatch(release -> Objects.equals(release.getDeliverable(), deliverable));
  }

}
