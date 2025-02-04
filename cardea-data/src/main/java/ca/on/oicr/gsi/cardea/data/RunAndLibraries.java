package ca.on.oicr.gsi.cardea.data;

import static java.util.Objects.requireNonNull;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

/**
 * Immutable RunAndLibraries
 * 
 * This is being moved to Dimsum because its only intended use is for data transformation within
 * Dimsum
 */
@JsonDeserialize(builder = RunAndLibraries.Builder.class)
@Deprecated(forRemoval = true)
public class RunAndLibraries {

  private Set<Sample> fullDepthSequencings;
  private Set<Sample> libraryQualifications;
  private Run run;

  private RunAndLibraries(Builder builder) {
    this.run = requireNonNull(builder.run);
    this.libraryQualifications = Collections.unmodifiableSet(builder.libraryQualifications);
    this.fullDepthSequencings = Collections.unmodifiableSet(builder.fullDepthSequencings);
  }

  public Set<Sample> getFullDepthSequencings() {
    return fullDepthSequencings;
  }

  public Set<Sample> getLibraryQualifications() {
    return libraryQualifications;
  }

  public Run getRun() {
    return run;
  }

  @JsonPOJOBuilder(withPrefix = "")
  public static class Builder {

    private Set<Sample> fullDepthSequencings = new HashSet<>();
    private Set<Sample> libraryQualifications = new HashSet<>();
    private Run run;

    public Builder addFullDepthSequencing(Sample sample) {
      fullDepthSequencings.add(sample);
      return this;
    }

    public Builder addLibraryQualification(Sample sample) {
      libraryQualifications.add(sample);
      return this;
    }

    public RunAndLibraries build() {
      return new RunAndLibraries(this);
    }

    public Builder run(Run run) {
      this.run = run;
      return this;
    }

  }

}
