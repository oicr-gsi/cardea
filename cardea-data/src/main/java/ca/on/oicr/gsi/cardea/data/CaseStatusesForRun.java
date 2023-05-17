package ca.on.oicr.gsi.cardea.data;

import static java.util.Collections.unmodifiableSet;

import java.util.Collections;
import java.util.Set;

/**
 * Immutable CaseStatusesForRun
 */
public class CaseStatusesForRun {
  private final Set<String> activeCases;
  private final Set<String> completedCases;
  private final Set<String> stoppedCases;

  public CaseStatusesForRun(Set<String> activeCases, Set<String> completedCases,
      Set<String> stoppedCases) {
    this.activeCases = activeCases == null ? Collections.emptySet() : unmodifiableSet(activeCases);
    this.completedCases = completedCases == null ? Collections.emptySet() : unmodifiableSet(completedCases);
    this.stoppedCases = stoppedCases == null ? Collections.emptySet() : unmodifiableSet(stoppedCases);
  }

  public Set<String> getActiveCases() {
    return activeCases;
  }

  public Set<String> getCompletedCases() {
    return completedCases;
  }

  public Set<String> getStoppedCases() {
    return stoppedCases;
  }
}
