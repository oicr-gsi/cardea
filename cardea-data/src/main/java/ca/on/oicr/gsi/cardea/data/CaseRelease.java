package ca.on.oicr.gsi.cardea.data;

import java.time.LocalDate;
import java.util.Objects;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

@JsonDeserialize(builder = CaseRelease.Builder.class)
public class CaseRelease {

  private final String deliverable;
  private final LocalDate qcDate;
  private final Boolean qcPassed;
  private final String qcUser;
  private final String qcNote;

  private CaseRelease(Builder builder) {
    this.deliverable = Objects.requireNonNull(builder.deliverable);
    this.qcDate = builder.qcDate;
    this.qcPassed = builder.qcPassed;
    this.qcUser = builder.qcUser;
    this.qcNote = builder.qcNote;
  }

  public String getDeliverable() {
    return deliverable;
  }

  public LocalDate getQcDate() {
    return qcDate;
  }

  public Boolean getQcPassed() {
    return qcPassed;
  }

  public String getQcUser() {
    return qcUser;
  }

  public String getQcNote() {
    return qcNote;
  }

  @JsonPOJOBuilder(withPrefix = "")
  public static class Builder {

    private String deliverable;
    private LocalDate qcDate;
    private Boolean qcPassed;
    private String qcUser;
    private String qcNote;

    public Builder deliverable(String deliverable) {
      this.deliverable = deliverable;
      return this;
    }

    public Builder qcDate(LocalDate qcDate) {
      this.qcDate = qcDate;
      return this;
    }

    public Builder qcPassed(Boolean qcPassed) {
      this.qcPassed = qcPassed;
      return this;
    }

    public Builder qcUser(String qcUser) {
      this.qcUser = qcUser;
      return this;
    }

    public Builder qcNote(String qcNote) {
      this.qcNote = qcNote;
      return this;
    }

    public CaseRelease build() {
      return new CaseRelease(this);
    }

  }
}
