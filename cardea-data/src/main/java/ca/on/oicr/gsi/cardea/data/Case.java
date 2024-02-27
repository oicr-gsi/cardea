package ca.on.oicr.gsi.cardea.data;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(as = CaseImpl.class)
public interface Case {

  long getAssayId();

  String getAssayName();

  String getAssayDescription();

  Donor getDonor();

  String getId();

  LocalDate getLatestActivityDate();

  Set<Project> getProjects();

  List<Sample> getReceipts();

  Requisition getRequisition();

  LocalDate getStartDate();

  List<Test> getTests();

  List<AnalysisQcGroup> getQcGroups();

  List<CaseDeliverable> getDeliverables();

  String getTimepoint();

  String getTissueOrigin();

  String getTissueType();

  int getReceiptDaysSpent();

  int getAnalysisReviewDaysSpent();

  int getReleaseApprovalDaysSpent();

  int getReleaseDaysSpent();

  int getCaseDaysSpent();

  int getPauseDays();

  boolean isStopped();

}
