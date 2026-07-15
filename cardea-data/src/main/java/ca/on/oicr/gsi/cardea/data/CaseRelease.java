package ca.on.oicr.gsi.cardea.data;

import ca.on.oicr.gsi.cardea.data.CaseQc.ReleaseQcStatus;
import java.time.LocalDate;

@com.fasterxml.jackson.databind.annotation.JsonDeserialize(as = CaseReleaseImpl.class)
@tools.jackson.databind.annotation.JsonDeserialize(as = CaseReleaseImpl.class)
public interface CaseRelease {

  String getDeliverable();

  LocalDate getQcDate();

  ReleaseQcStatus getQcStatus();

  String getQcUser();

  String getQcNote();
}
