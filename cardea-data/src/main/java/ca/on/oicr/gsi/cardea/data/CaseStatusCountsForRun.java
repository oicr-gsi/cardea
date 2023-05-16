package ca.on.oicr.gsi.cardea.data;

import static java.util.Collections.unmodifiableSet;

import java.util.Collections;
import java.util.Set;

/**
 * Immutable CaseStatusCountsForRun
 */
public class CaseStatusCountsForRun {
  private final Set<String> activeCaseCount;
  private final Set<String> completedCaseCount;
  private final Set<String> stoppedCaseCount;

  public CaseStatusCountsForRun(Set<String> activeCaseCount, Set<String> completedCaseCount,
      Set<String> stoppedCaseCount) {
    this.activeCaseCount = activeCaseCount == null ? Collections.emptySet() : unmodifiableSet(activeCaseCount);
    this.completedCaseCount = completedCaseCount == null ? Collections.emptySet() : unmodifiableSet(completedCaseCount);
    this.stoppedCaseCount = stoppedCaseCount == null ? Collections.emptySet() : unmodifiableSet(stoppedCaseCount);
  }

  public Set<String> getActiveCaseCount() {
    return activeCaseCount;
  }

  public Set<String> getCompletedCaseCount() {
    return completedCaseCount;
  }

  public Set<String> getStoppedCaseCount() {
    return stoppedCaseCount;
  }
}
