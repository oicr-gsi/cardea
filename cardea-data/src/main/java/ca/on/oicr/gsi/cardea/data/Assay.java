package ca.on.oicr.gsi.cardea.data;

import static java.util.Objects.requireNonNull;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

/**
 * Immutable Assay
 */
@JsonDeserialize(builder = Assay.Builder.class)
public class Assay {

  private final String description;
  private final Long id;
  private final Map<MetricCategory, List<MetricSubcategory>> metricCategories;
  private final String name;
  private final String version;
  private final AssayTargets targets;

  private Assay(Builder builder) {
    this.id = requireNonNull(builder.id);
    this.name = requireNonNull(builder.name);
    this.description = builder.description;
    this.version = requireNonNull(builder.version);
    Map<MetricCategory, List<MetricSubcategory>> tempMap =
        builder.metricCategories.entrySet().stream()
            .collect(Collectors.toMap(entry -> entry.getKey(),
                entry -> Collections.unmodifiableList(entry.getValue())));
    this.metricCategories = Collections.unmodifiableMap(tempMap);
    this.targets = requireNonNull(builder.targets);
  }

  public String getDescription() {
    return description;
  }

  public Long getId() {
    return id;
  }

  public Map<MetricCategory, List<MetricSubcategory>> getMetricCategories() {
    return metricCategories;
  }

  public String getName() {
    return name;
  }

  public String getVersion() {
    return version;
  }

  public AssayTargets getTargets() {
    return targets;
  }

  @JsonPOJOBuilder(withPrefix = "")
  public static class Builder {

    private String description;
    private Long id;
    private Map<MetricCategory, List<MetricSubcategory>> metricCategories;
    private String name;
    private String version;
    private AssayTargets targets;

    public Assay build() {
      return new Assay(this);
    }

    public Builder description(String description) {
      this.description = description;
      return this;
    }

    public Builder id(Long id) {
      this.id = id;
      return this;
    }

    public Builder metricCategories(Map<MetricCategory, List<MetricSubcategory>> metricCategories) {
      this.metricCategories = metricCategories;
      return this;
    }

    public Builder name(String name) {
      this.name = name;
      return this;
    }

    public Builder version(String version) {
      this.version = version;
      return this;
    }

    public Builder targets(AssayTargets targets) {
      this.targets = targets;
      return this;
    }

  }

}
