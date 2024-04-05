package ca.on.oicr.gsi.cardea.data;

import java.time.LocalDate;
import java.util.List;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(as = CaseDeliverableImpl.class)
public interface CaseDeliverable {

  DeliverableType getDeliverableType();

  LocalDate getAnalysisReviewQcDate();

  Boolean getAnalysisReviewQcPassed();

  String getAnalysisReviewQcUser();

  String getAnalysisReviewQcNote();

  LocalDate getReleaseApprovalQcDate();

  Boolean getReleaseApprovalQcPassed();

  String getReleaseApprovalQcUser();

  String getReleaseApprovalQcNote();

  List<CaseRelease> getReleases();

  LocalDate getLatestActivityDate();

  int getAnalysisReviewDaysSpent();

  int getReleaseApprovalDaysSpent();

  int getReleaseDaysSpent();

  int getDeliverableDaysSpent();

}
