package ca.on.oicr.gsi.cardea.data;

import java.time.LocalDate;
import java.util.List;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import ca.on.oicr.gsi.cardea.data.CaseQc.AnalysisReviewQcStatus;
import ca.on.oicr.gsi.cardea.data.CaseQc.ReleaseApprovalQcStatus;

@JsonDeserialize(as = CaseDeliverableImpl.class)
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
