package ca.on.oicr.gsi.cardea.data;

import static java.util.Objects.requireNonNull;
import java.util.Collections;
import java.util.Objects;
import java.util.Set;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

/**
 * Immutable Requisition
 */
@JsonDeserialize(builder = Requisition.Builder.class)
public class Requisition {

  private final Set<Long> assayIds;
  private final long id;
  private final String name;
  private final String stopReason;
  private final boolean stopped;
  private final boolean paused;
  private final String pauseReason;

  private Requisition(Builder builder) {
    this.id = requireNonNull(builder.id);
    this.name = requireNonNull(builder.name);
    this.assayIds = builder.assayIds == null ? Collections.emptySet()
        : Collections.unmodifiableSet(builder.assayIds);
    this.stopped = builder.stopped;
    this.stopReason = builder.stopReason;
    this.paused = builder.paused;
    this.pauseReason = builder.pauseReason;
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

  public Set<Long> getAssayIds() {
    return assayIds;
  }

  public long getId() {
    return id;
  }

  public String getName() {
    return name;
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

    private Set<Long> assayIds;
    private long id;
    private String name;
    private String stopReason;
    private boolean stopped;
    private boolean paused;
    private String pauseReason;

    public Builder assayIds(Set<Long> assayIds) {
      this.assayIds = assayIds;
      return this;
    }

    public Requisition build() {
      return new Requisition(this);
    }

    public Builder id(long id) {
      this.id = id;
      return this;
    }

    public Builder name(String name) {
      this.name = name;
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
