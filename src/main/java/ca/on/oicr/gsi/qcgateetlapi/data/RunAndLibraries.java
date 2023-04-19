package ca.on.oicr.gsi.qcgateetlapi.data;

import static java.util.Objects.requireNonNull;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import javax.annotation.concurrent.Immutable;

@Immutable
public class RunAndLibraries {

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

}
