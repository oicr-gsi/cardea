package ca.on.oicr.gsi.cardea.data;

import java.time.LocalDate;
import java.util.Objects;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import ca.on.oicr.gsi.cardea.data.CaseQc.ReleaseQcStatus;

@JsonDeserialize(builder = CaseReleaseImpl.Builder.class)
public class CaseReleaseImpl implements CaseRelease {

  private final String deliverable;
  private final LocalDate qcDate;
  private final ReleaseQcStatus qcStatus;
  private final String qcUser;
  private final String qcNote;

  private CaseReleaseImpl(Builder builder) {
    this.deliverable = Objects.requireNonNull(builder.deliverable);
    this.qcDate = builder.qcDate;
    this.qcStatus = builder.qcStatus;
    this.qcUser = builder.qcUser;
    this.qcNote = builder.qcNote;
  }

  @Override
  public String getDeliverable() {
    return deliverable;
  }

  @Override
  public LocalDate getQcDate() {
    return qcDate;
  }

  @Override
  public ReleaseQcStatus getQcStatus() {
    return qcStatus;
  }

  @Override
  public String getQcUser() {
    return qcUser;
  }

  @Override
  public String getQcNote() {
    return qcNote;
  }

  @JsonPOJOBuilder(withPrefix = "")
  public static class Builder {

    private String deliverable;
    private LocalDate qcDate;
    private ReleaseQcStatus qcStatus;
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

    public Builder qcStatus(ReleaseQcStatus qcStatus) {
      this.qcStatus = qcStatus;
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

    public CaseReleaseImpl build() {
      return new CaseReleaseImpl(this);
    }

  }
}
