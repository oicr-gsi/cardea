package ca.on.oicr.gsi.cardea.data;

import java.time.LocalDate;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(as = CaseReleaseImpl.class)
public interface CaseRelease {

  String getDeliverable();

  LocalDate getQcDate();

  Boolean getQcPassed();

  String getQcUser();

  String getQcNote();

}
