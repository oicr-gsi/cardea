package ca.on.oicr.gsi.cardea.data;

import java.time.LocalDate;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import ca.on.oicr.gsi.cardea.data.CaseQc.ReleaseQcStatus;

@JsonDeserialize(as = CaseReleaseImpl.class)
public interface CaseRelease {

  String getDeliverable();

  LocalDate getQcDate();

  ReleaseQcStatus getQcStatus();

  String getQcUser();

  String getQcNote();

}
