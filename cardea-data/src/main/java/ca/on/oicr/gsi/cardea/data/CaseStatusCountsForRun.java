package ca.on.oicr.gsi.cardea.data;

/**
 * Immutable CaseStatusCountsForRun
 */
public class CaseStatusCountsForRun {
  private final Long activeCaseCount;
  private final Long completedCaseCount;
  private final Long stoppedCaseCount;

  public CaseStatusCountsForRun(Long activeCaseCount, Long completedCaseCount, Long stoppedCaseCount) {
    this.activeCaseCount = activeCaseCount == null ? 0L : activeCaseCount;
    this.completedCaseCount = completedCaseCount == null ? 0L : completedCaseCount;
    this.stoppedCaseCount = stoppedCaseCount == null ? 0L : stoppedCaseCount;
  }

  public Long getActiveCaseCount() {
    return activeCaseCount;
  }

  public Long getCompletedCaseCount() {
    return completedCaseCount;
  }

  public Long getStoppedCaseCount() {
    return stoppedCaseCount;
  }
}
