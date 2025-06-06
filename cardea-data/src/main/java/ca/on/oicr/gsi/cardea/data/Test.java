package ca.on.oicr.gsi.cardea.data;

import static java.util.Collections.emptyList;
import static java.util.Collections.unmodifiableList;
import static java.util.Objects.requireNonNull;

import java.time.LocalDate;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

/**
 * Immutable Test
 */
@JsonDeserialize(builder = Test.Builder.class)
public class Test {

  private final boolean extractionSkipped;
  private final List<Sample> extractions;
  private final List<Sample> fullDepthSequencings;
  private final String groupId;
  private final LocalDate latestActivityDate;
  private final boolean libraryPreparationSkipped;
  private final List<Sample> libraryPreparations;
  private final boolean libraryQualificationSkipped;
  private final List<Sample> libraryQualifications;
  private final String name;
  private final String targetedSequencing;
  private final String timepoint;
  private final String tissueOrigin;
  private final String tissueType;
  private final String libraryDesignCode;
  private final String libraryQualificationDesignCode;
  private final int extractionDaysSpent;
  private final int extractionPreparationDaysSpent;
  private final int extractionQcDaysSpent;
  private final int extractionTransferDaysSpent;
  private final int libraryPreparationDaysSpent;
  private final int libraryQualificationDaysSpent;
  private final int libraryQualificationLoadingDaysSpent;
  private final int libraryQualificationSequencingDaysSpent;
  private final int libraryQualificationQcDaysSpent;
  private final int fullDepthSequencingDaysSpent;
  private final int fullDepthSequencingLoadingDaysSpent;
  private final int fullDepthSequencingSequencingDaysSpent;
  private final int fullDepthSequencingQcDaysSpent;

  private Test(Builder builder) {
    this.name = requireNonNull(builder.name);
    this.tissueOrigin = builder.tissueOrigin;
    this.tissueType = builder.tissueType;
    this.timepoint = builder.timepoint;
    this.groupId = builder.groupId;
    this.libraryDesignCode = builder.libraryDesignCode;
    this.libraryQualificationDesignCode = builder.libraryQualificationDesignCode;
    this.targetedSequencing = builder.targetedSequencing;
    this.extractionSkipped = builder.extractionSkipped;
    this.libraryPreparationSkipped = builder.libraryPreparationSkipped;
    this.libraryQualificationSkipped = builder.libraryQualificationSkipped;
    this.extractions =
        builder.extractions == null ? emptyList() : unmodifiableList(builder.extractions);
    this.libraryPreparations = builder.libraryPreparations == null ? emptyList()
        : unmodifiableList(builder.libraryPreparations);
    this.libraryQualifications = builder.libraryQualifications == null ? emptyList()
        : unmodifiableList(builder.libraryQualifications);
    this.fullDepthSequencings = builder.fullDepthSequencings == null ? emptyList()
        : unmodifiableList(builder.fullDepthSequencings);

    if (builder.latestActivityDate != null) {
      this.latestActivityDate = builder.latestActivityDate;
    } else {
      this.latestActivityDate = Stream
          .of(extractions.stream(), libraryPreparations.stream(), libraryQualifications.stream(),
              fullDepthSequencings.stream())
          .flatMap(Function.identity()).map(Sample::getLatestActivityDate).max(LocalDate::compareTo)
          .orElse(null);
    }

    this.extractionDaysSpent = builder.extractionDaysSpent;
    this.extractionPreparationDaysSpent = builder.extractionPreparationDaysSpent;
    this.extractionQcDaysSpent = builder.extractionQcDaysSpent;
    this.extractionTransferDaysSpent = builder.extractionTransferDaysSpent;
    this.libraryPreparationDaysSpent = builder.libraryPreparationDaysSpent;
    this.libraryQualificationDaysSpent = builder.libraryQualificationDaysSpent;
    this.libraryQualificationLoadingDaysSpent = builder.libraryQualificationLoadingDaysSpent;
    this.libraryQualificationSequencingDaysSpent = builder.libraryQualificationSequencingDaysSpent;
    this.libraryQualificationQcDaysSpent = builder.libraryQualificationQcDaysSpent;
    this.fullDepthSequencingDaysSpent = builder.fullDepthSequencingDaysSpent;
    this.fullDepthSequencingLoadingDaysSpent = builder.fullDepthSequencingLoadingDaysSpent;
    this.fullDepthSequencingSequencingDaysSpent = builder.fullDepthSequencingSequencingDaysSpent;
    this.fullDepthSequencingQcDaysSpent = builder.fullDepthSequencingQcDaysSpent;
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

  public String getLibraryDesignCode() {
    return libraryDesignCode;
  }

  public String getLibraryQualificationDesignCode() {
    return libraryQualificationDesignCode;
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

  public boolean isLibraryQualificationSkipped() {
    return libraryQualificationSkipped;
  }

  public int getExtractionDaysSpent() {
    return extractionDaysSpent;
  }

  public int getExtractionPreparationDaysSpent() {
    return extractionPreparationDaysSpent;
  }

  public int getExtractionQcDaysSpent() {
    return extractionQcDaysSpent;
  }

  public int getExtractionTransferDaysSpent() {
    return extractionTransferDaysSpent;
  }

  public int getLibraryPreparationDaysSpent() {
    return libraryPreparationDaysSpent;
  }

  public int getLibraryQualificationDaysSpent() {
    return libraryQualificationDaysSpent;
  }

  public int getLibraryQualificationLoadingDaysSpent() {
    return libraryQualificationLoadingDaysSpent;
  }

  public int getLibraryQualificationSequencingDaysSpent() {
    return libraryQualificationSequencingDaysSpent;
  }

  public int getLibraryQualificationQcDaysSpent() {
    return libraryQualificationQcDaysSpent;
  }

  public int getFullDepthSequencingDaysSpent() {
    return fullDepthSequencingDaysSpent;
  }

  public int getFullDepthSequencingLoadingDaysSpent() {
    return fullDepthSequencingLoadingDaysSpent;
  }

  public int getFullDepthSequencingSequencingDaysSpent() {
    return fullDepthSequencingSequencingDaysSpent;
  }

  public int getFullDepthSequencingQcDaysSpent() {
    return fullDepthSequencingQcDaysSpent;
  }

  @JsonPOJOBuilder(withPrefix = "")
  public static class Builder {

    private boolean extractionSkipped;
    private List<Sample> extractions;
    private List<Sample> fullDepthSequencings;
    private String groupId;
    private String libraryDesignCode;
    private String libraryQualificationDesignCode;
    private boolean libraryPreparationSkipped;
    private List<Sample> libraryPreparations;
    private boolean libraryQualificationSkipped;
    private List<Sample> libraryQualifications;
    private String name;
    private String targetedSequencing;
    private String timepoint;
    private String tissueOrigin;
    private String tissueType;
    private int extractionDaysSpent;
    private int extractionPreparationDaysSpent;
    private int extractionQcDaysSpent;
    private int extractionTransferDaysSpent;
    private int libraryPreparationDaysSpent;
    private int libraryQualificationDaysSpent;
    private int libraryQualificationLoadingDaysSpent;
    private int libraryQualificationSequencingDaysSpent;
    private int libraryQualificationQcDaysSpent;
    private int fullDepthSequencingDaysSpent;
    private int fullDepthSequencingLoadingDaysSpent;
    private int fullDepthSequencingSequencingDaysSpent;
    private int fullDepthSequencingQcDaysSpent;
    private LocalDate latestActivityDate;

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

    public Builder libraryDesignCode(String libraryDesignCode) {
      this.libraryDesignCode = libraryDesignCode;
      return this;
    }

    public Builder libraryQualificationDesignCode(String libraryQualificationDesignCode) {
      this.libraryQualificationDesignCode = libraryQualificationDesignCode;
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

    public Builder libraryQualificationSkipped(boolean libraryQualificationSkipped) {
      this.libraryQualificationSkipped = libraryQualificationSkipped;
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

    public Builder extractionDaysSpent(int days) {
      this.extractionDaysSpent = days;
      return this;
    }

    public Builder extractionPreparationDaysSpent(int days) {
      this.extractionPreparationDaysSpent = days;
      return this;
    }

    public Builder extractionQcDaysSpent(int days) {
      this.extractionQcDaysSpent = days;
      return this;
    }

    public Builder extractionTransferDaysSpent(int days) {
      this.extractionTransferDaysSpent = days;
      return this;
    }

    public Builder libraryPreparationDaysSpent(int days) {
      this.libraryPreparationDaysSpent = days;
      return this;
    }

    public Builder libraryQualificationDaysSpent(int days) {
      this.libraryQualificationDaysSpent = days;
      return this;
    }

    public Builder libraryQualificationLoadingDaysSpent(int days) {
      this.libraryQualificationLoadingDaysSpent = days;
      return this;
    }

    public Builder libraryQualificationSequencingDaysSpent(int days) {
      this.libraryQualificationSequencingDaysSpent = days;
      return this;
    }

    public Builder libraryQualificationQcDaysSpent(int days) {
      this.libraryQualificationQcDaysSpent = days;
      return this;
    }

    public Builder fullDepthSequencingDaysSpent(int days) {
      this.fullDepthSequencingDaysSpent = days;
      return this;
    }

    public Builder fullDepthSequencingLoadingDaysSpent(int days) {
      this.fullDepthSequencingLoadingDaysSpent = days;
      return this;
    }

    public Builder fullDepthSequencingSequencingDaysSpent(int days) {
      this.fullDepthSequencingSequencingDaysSpent = days;
      return this;
    }

    public Builder fullDepthSequencingQcDaysSpent(int days) {
      this.fullDepthSequencingQcDaysSpent = days;
      return this;
    }

    public Builder latestActivityDate(LocalDate latestActivityDate) {
      this.latestActivityDate = latestActivityDate;
      return this;
    }
  }
}
