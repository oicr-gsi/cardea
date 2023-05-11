package ca.on.oicr.gsi.cardea.data;

import static java.util.Collections.emptyList;
import static java.util.Collections.unmodifiableList;
import static java.util.Objects.requireNonNull;

import java.time.LocalDate;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

/**
 * Immutable Test
 */
public class Test {

  private final boolean extractionSkipped;
  private final List<Sample> extractions;
  private final List<Sample> fullDepthSequencings;
  private final String groupId;
  private final LocalDate latestActivityDate;
  private final boolean libraryPreparationSkipped;
  private final List<Sample> libraryPreparations;
  private final List<Sample> libraryQualifications;
  private final String name;
  private final String targetedSequencing;
  private final String timepoint;
  private final String tissueOrigin;
  private final String tissueType;

  private Test(Builder builder) {
    this.name = requireNonNull(builder.name);
    this.tissueOrigin = builder.tissueOrigin;
    this.tissueType = builder.tissueType;
    this.timepoint = builder.timepoint;
    this.groupId = builder.groupId;
    this.targetedSequencing = builder.targetedSequencing;
    this.extractionSkipped = builder.extractionSkipped;
    this.libraryPreparationSkipped = builder.libraryPreparationSkipped;
    this.extractions = builder.extractions == null ? emptyList() : unmodifiableList(builder.extractions);
    this.libraryPreparations = builder.libraryPreparations == null ? emptyList()
        : unmodifiableList(builder.libraryPreparations);
    this.libraryQualifications = builder.libraryQualifications == null ? emptyList()
        : unmodifiableList(builder.libraryQualifications);
    this.fullDepthSequencings = builder.fullDepthSequencings == null ? emptyList()
        : unmodifiableList(builder.fullDepthSequencings);
    this.latestActivityDate = Stream
        .of(extractions.stream(), libraryPreparations.stream(), libraryQualifications.stream(),
            fullDepthSequencings.stream())
        .flatMap(Function.identity()).map(Sample::getLatestActivityDate).max(LocalDate::compareTo)
        .orElse(null);
  }

  public List<Sample> getExtractions() {
    return extractions;
  }

  public List<Sample> getFullDepthSequencings() {
    return fullDepthSequencings;
  }

  public String getGroupId() {
    return groupId;
  }

  public LocalDate getLatestActivityDate() {
    return latestActivityDate;
  }

  public List<Sample> getLibraryPreparations() {
    return libraryPreparations;
  }

  public List<Sample> getLibraryQualifications() {
    return libraryQualifications;
  }

  public String getName() {
    return name;
  }

  public String getTargetedSequencing() {
    return targetedSequencing;
  }

  public String getTimepoint() {
    return timepoint;
  }

  public String getTissueOrigin() {
    return tissueOrigin;
  }

  public String getTissueType() {
    return tissueType;
  }

  public boolean isExtractionSkipped() {
    return extractionSkipped;
  }

  public boolean isLibraryPreparationSkipped() {
    return libraryPreparationSkipped;
  }

  public static class Builder {

    private boolean extractionSkipped;
    private List<Sample> extractions;
    private List<Sample> fullDepthSequencings;
    private String groupId;
    private boolean libraryPreparationSkipped;
    private List<Sample> libraryPreparations;
    private List<Sample> libraryQualifications;
    private String name;
    private String targetedSequencing;
    private String timepoint;
    private String tissueOrigin;
    private String tissueType;

    public Test build() {
      return new Test(this);
    }

    public Builder extractionSkipped(boolean extractionSkipped) {
      this.extractionSkipped = extractionSkipped;
      return this;
    }

    public Builder extractions(List<Sample> extractions) {
      this.extractions = extractions;
      return this;
    }

    public Builder fullDepthSequencings(List<Sample> fullDepthSequencings) {
      this.fullDepthSequencings = fullDepthSequencings;
      return this;
    }

    public Builder groupId(String groupId) {
      this.groupId = groupId;
      return this;
    }

    public Builder libraryPreparationSkipped(boolean libraryPreparationSkipped) {
      this.libraryPreparationSkipped = libraryPreparationSkipped;
      return this;
    }

    public Builder libraryPreparations(List<Sample> libraryPreparations) {
      this.libraryPreparations = libraryPreparations;
      return this;
    }

    public Builder libraryQualifications(List<Sample> libraryQualifications) {
      this.libraryQualifications = libraryQualifications;
      return this;
    }

    public Builder name(String name) {
      this.name = name;
      return this;
    }

    public Builder targetedSequencing(String targetedSequencing) {
      this.targetedSequencing = targetedSequencing;
      return this;
    }

    public Builder timepoint(String timepoint) {
      this.timepoint = timepoint;
      return this;
    }

    public Builder tissueOrigin(String tissueOrigin) {
      this.tissueOrigin = tissueOrigin;
      return this;
    }

    public Builder tissueType(String tissueType) {
      this.tissueType = tissueType;
      return this;
    }
  }
}
