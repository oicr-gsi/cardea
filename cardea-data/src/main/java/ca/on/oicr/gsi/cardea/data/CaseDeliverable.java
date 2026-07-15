package ca.on.oicr.gsi.cardea.data;

import java.time.LocalDate;
import java.util.List;
import ca.on.oicr.gsi.cardea.data.CaseQc.AnalysisReviewQcStatus;
import ca.on.oicr.gsi.cardea.data.CaseQc.ReleaseApprovalQcStatus;

@com.fasterxml.jackson.databind.annotation.JsonDeserialize(as = CaseDeliverableImpl.class)
@tools.jackson.databind.annotation.JsonDeserialize(as = CaseDeliverableImpl.class)
public interface CaseDeliverable {

  String getDeliverableCategory();

  boolean isAnalysisReviewSkipped();

  LocalDate getAnalysisReviewQcDate();

  AnalysisReviewQcStatus getAnalysisReviewQcStatus();

  String getAnalysisReviewQcUser();

  String getAnalysisReviewQcNote();

  LocalDate getReleaseApprovalQcDate();

  ReleaseApprovalQcStatus getReleaseApprovalQcStatus();

  String getReleaseApprovalQcUser();

  String getReleaseApprovalQcNote();

  List<CaseRelease> getReleases();

  LocalDate getLatestActivityDate();

  int getAnalysisReviewDaysSpent();

  int getReleaseApprovalDaysSpent();

  int getReleaseDaysSpent();

  int getDeliverableDaysSpent();

}
