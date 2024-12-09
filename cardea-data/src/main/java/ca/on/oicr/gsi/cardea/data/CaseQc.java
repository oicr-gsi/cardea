package ca.on.oicr.gsi.cardea.data;

public interface CaseQc {

  String getLabel();

  Boolean getQcPassed();

  Boolean getRelease();

  public enum AnalysisReviewQcStatus implements CaseQc {
    PENDING("Pending", null, null), //
    PASSED("Passed", true, null), //
    FAILED("Failed", false, null), //
    NOT_APPLICABLE("N/A", null, false);

    private final String label;
    private final Boolean qcPassed;
    private final Boolean release;

    private AnalysisReviewQcStatus(String label, Boolean qcPassed, Boolean release) {
      this.label = label;
      this.qcPassed = qcPassed;
      this.release = release;
    }

    @Override
    public String getLabel() {
      return label;
    }

    @Override
    public Boolean getQcPassed() {
      return qcPassed;
    }

    @Override
    public Boolean getRelease() {
      return release;
    }

    public static AnalysisReviewQcStatus of(Boolean qcPassed, Boolean release) {
      return CaseQc.of(AnalysisReviewQcStatus.values(), qcPassed, release);
    }
  }

  public enum ReleaseApprovalQcStatus implements CaseQc {
    PENDING("Pending", null, null), //
    PASSED_PROCEED("Passed/Proceed", true, true), //
    FAILED_PROCEED("Failed/Proceed", false, true), //
    FAILED_STOP("Failed/Stop", false, false), //
    NOT_APPLICABLE("N/A", null, false);

    private final String label;
    private final Boolean qcPassed;
    private final Boolean release;

    private ReleaseApprovalQcStatus(String label, Boolean qcPassed, Boolean release) {
      this.label = label;
      this.qcPassed = qcPassed;
      this.release = release;
    }

    @Override
    public String getLabel() {
      return label;
    }

    @Override
    public Boolean getQcPassed() {
      return qcPassed;
    }

    @Override
    public Boolean getRelease() {
      return release;
    }

    public static ReleaseApprovalQcStatus of(Boolean qcPassed, Boolean release) {
      return CaseQc.of(ReleaseApprovalQcStatus.values(), qcPassed, release);
    }
  }

  public enum ReleaseQcStatus implements CaseQc {
    PENDING("Pending", null, null), //
    PASSED_RELEASE("Passed/Released", true, true), //
    FAILED_RELEASE("Failure Released", false, true), //
    FAILED_STOP("Failure Not Released", false, false), //
    NOT_APPLICABLE("N/A", null, false);

    private final String label;
    private final Boolean qcPassed;
    private final Boolean release;

    private ReleaseQcStatus(String label, Boolean qcPassed, Boolean release) {
      this.label = label;
      this.qcPassed = qcPassed;
      this.release = release;
    }

    @Override
    public String getLabel() {
      return label;
    }

    @Override
    public Boolean getQcPassed() {
      return qcPassed;
    }

    @Override
    public Boolean getRelease() {
      return release;
    }

    public static ReleaseQcStatus of(Boolean qcPassed, Boolean release) {
      return CaseQc.of(ReleaseQcStatus.values(), qcPassed, release);
    }
  }

  private static <T extends CaseQc> T of(T[] values, Boolean qcPassed, Boolean release) {
    if (qcPassed == null && release == null) {
      return null;
    }
    for (T status : values) {
      if (status.getQcPassed() == qcPassed && status.getRelease() == release) {
        return status;
      }
    }
    throw new IllegalArgumentException(
        "No value for qcPassed=%s, release=%s".formatted(qcPassed, release));
  }

  public default boolean isPending() {
    return getQcPassed() == null && getRelease() == null;
  }

}
