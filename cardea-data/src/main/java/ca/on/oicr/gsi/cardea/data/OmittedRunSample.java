package ca.on.oicr.gsi.cardea.data;

import static java.util.Objects.requireNonNull;
import java.time.LocalDate;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

/**
 * Immutable OmittedRunSample
 */
@JsonDeserialize(builder = OmittedRunSample.Builder.class)
public class OmittedRunSample {

  private final String id;
  private final String name;
  private final long runId;
  private final int sequencingLane;
  private final LocalDate qcDate;
  private final Boolean qcPassed;
  private final String qcReason;
  private final String qcNote;
  private final String qcUser;
  private final LocalDate dataReviewDate;
  private final Boolean dataReviewPassed;
  private final String dataReviewUser;

  private OmittedRunSample(Builder builder) {
    this.id = requireNonNull(builder.id);
    this.name = requireNonNull(builder.name);
    this.runId = requireNonNull(builder.runId);
    this.sequencingLane = requireNonNull(builder.sequencingLane);
    this.qcPassed = builder.qcPassed;
    this.qcReason = builder.qcReason;
    this.qcNote = builder.qcNote;
    this.qcUser = builder.qcUser;
    this.qcDate = builder.qcDate;
    this.dataReviewPassed = builder.dataReviewPassed;
    this.dataReviewUser = builder.dataReviewUser;
    this.dataReviewDate = builder.dataReviewDate;
  }

  public String getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public long getRunId() {
    return runId;
  }

  public int getSequencingLane() {
    return sequencingLane;
  }

  public LocalDate getQcDate() {
    return qcDate;
  }

  public Boolean getQcPassed() {
    return qcPassed;
  }

  public String getQcReason() {
    return qcReason;
  }

  public String getQcNote() {
    return qcNote;
  }

  public String getQcUser() {
    return qcUser;
  }

  public LocalDate getDataReviewDate() {
    return dataReviewDate;
  }

  public Boolean getDataReviewPassed() {
    return dataReviewPassed;
  }

  public String getDataReviewUser() {
    return dataReviewUser;
  }

  @JsonPOJOBuilder(withPrefix = "")
  public static class Builder {

    private String id;
    private String name;
    private Long runId;
    private Integer sequencingLane;
    private LocalDate qcDate;
    private Boolean qcPassed;
    private String qcReason;
    private String qcNote;
    private String qcUser;
    private LocalDate dataReviewDate;
    private Boolean dataReviewPassed;
    private String dataReviewUser;

    public OmittedRunSample build() {
      return new OmittedRunSample(this);
    }

    public Builder id(String id) {
      this.id = id;
      return this;
    }

    public Builder name(String name) {
      this.name = name;
      return this;
    }

    public Builder runId(Long runId) {
      this.runId = runId;
      return this;
    }

    public Builder sequencingLane(Integer sequencingLane) {
      this.sequencingLane = sequencingLane;
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

    public Builder qcReason(String qcReason) {
      this.qcReason = qcReason;
      return this;
    }

    public Builder qcNote(String qcNote) {
      this.qcNote = qcNote;
      return this;
    }

    public Builder qcUser(String qcUser) {
      this.qcUser = qcUser;
      return this;
    }

    public Builder dataReviewDate(LocalDate dataReviewDate) {
      this.dataReviewDate = dataReviewDate;
      return this;
    }

    public Builder dataReviewPassed(Boolean dataReviewPassed) {
      this.dataReviewPassed = dataReviewPassed;
      return this;
    }

    public Builder dataReviewUser(String dataReviewUser) {
      this.dataReviewUser = dataReviewUser;
      return this;
    }

  }

}
