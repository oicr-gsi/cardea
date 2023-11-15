package ca.on.oicr.gsi.cardea.data;

import static java.util.Collections.emptyList;
import static java.util.Collections.unmodifiableList;
import static java.util.Objects.requireNonNull;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Stream;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

/**
 * Immutable Requisition
 */
@JsonDeserialize(builder = Requisition.Builder.class)
public class Requisition {

  private final Long assayId;
  private final List<RequisitionQc> releaseApprovals;
  private final List<RequisitionQc> releases;
  private final long id;
  private final List<RequisitionQc> analysisReviews;
  private final LocalDate latestActivityDate;
  private final String name;
  private final List<RequisitionQcGroup> qcGroups;
  private final String stopReason;
  private final boolean stopped;
  private final boolean paused;
  private final String pauseReason;

  private Requisition(Builder builder) {
    this.id = requireNonNull(builder.id);
    this.name = requireNonNull(builder.name);
    this.assayId = builder.assayId;
    this.stopped = builder.stopped;
    this.stopReason = builder.stopReason;
    this.paused = builder.paused;
    this.pauseReason = builder.pauseReason;
    this.qcGroups = builder.qcGroups == null ? emptyList() : unmodifiableList(builder.qcGroups);
    this.analysisReviews = builder.analysisReviews == null ? emptyList()
        : unmodifiableList(builder.analysisReviews);
    this.releaseApprovals =
        builder.releaseApprovals == null ? emptyList() : unmodifiableList(builder.releaseApprovals);
    this.releases =
        builder.releases == null ? emptyList() : unmodifiableList(builder.releases);
    this.latestActivityDate =
        Stream.of(analysisReviews.stream(), releaseApprovals.stream(), releases.stream())
            .flatMap(Function.identity()).map(RequisitionQc::getQcDate).max(LocalDate::compareTo)
            .orElse(null);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    Requisition other = (Requisition) obj;
    return Objects.equals(id, other.id);
  }

  public Long getAssayId() {
    return assayId;
  }

  public List<RequisitionQc> getReleaseApprovals() {
    return releaseApprovals;
  }

  public List<RequisitionQc> getReleases() {
    return releases;
  }

  public long getId() {
    return id;
  }

  public List<RequisitionQc> getAnalysisReviews() {
    return analysisReviews;
  }

  public LocalDate getLatestActivityDate() {
    return latestActivityDate;
  }

  public String getName() {
    return name;
  }

  public List<RequisitionQcGroup> getQcGroups() {
    return qcGroups;
  }

  public boolean isStopped() {
    return stopped;
  }

  public String getStopReason() {
    return stopReason;
  }

  public boolean isPaused() {
    return paused;
  }

  public String getPauseReason() {
    return pauseReason;
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }

  @JsonPOJOBuilder(withPrefix = "")
  public static class Builder {

    private Long assayId;
    private List<RequisitionQc> releaseApprovals;
    private List<RequisitionQc> releases;
    private long id;
    private List<RequisitionQc> analysisReviews;
    private String name;
    private List<RequisitionQcGroup> qcGroups;
    private String stopReason;
    private boolean stopped;
    private boolean paused;
    private String pauseReason;

    public Builder assayId(Long assayId) {
      this.assayId = assayId;
      return this;
    }

    public Requisition build() {
      return new Requisition(this);
    }

    public Builder releaseApprovals(List<RequisitionQc> releaseApprovals) {
      this.releaseApprovals = releaseApprovals;
      return this;
    }

    public Builder releases(List<RequisitionQc> releases) {
      this.releases = releases;
      return this;
    }

    public Builder id(long id) {
      this.id = id;
      return this;
    }

    public Builder analysisReviews(List<RequisitionQc> analysisReviews) {
      this.analysisReviews = analysisReviews;
      return this;
    }

    public Builder name(String name) {
      this.name = name;
      return this;
    }

    public Builder qcGroups(List<RequisitionQcGroup> qcGroups) {
      this.qcGroups = qcGroups;
      return this;
    }

    public Builder stopReason(String stopReason) {
      this.stopReason = stopReason;
      return this;
    }

    public Builder stopped(boolean stopped) {
      this.stopped = stopped;
      return this;
    }

    public Builder paused(boolean paused) {
      this.paused = paused;
      return this;
    }

    public Builder pauseReason(String pauseReason) {
      this.pauseReason = pauseReason;
      return this;
    }
  }
}
