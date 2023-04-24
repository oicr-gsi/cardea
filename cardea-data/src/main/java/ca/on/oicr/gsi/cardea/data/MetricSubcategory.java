package ca.on.oicr.gsi.cardea.data;

import org.immutables.value.Value;
import java.util.Collections;
import java.util.List;

public class MetricSubcategory {

  private final String libraryDesignCode;
  private final List<Metric> metrics;
  private final String name;
  private final Integer sortPriority;
  private MetricSubcategory(Builder builder) {
    this.name = builder.name;
    this.sortPriority = builder.sortPriority;
    this.libraryDesignCode = builder.libraryDesignCode;
    this.metrics = Collections.unmodifiableList(builder.metrics);
  }

  public String getLibraryDesignCode() {
    return libraryDesignCode;
  }

  public List<Metric> getMetrics() {
    return metrics;
  }

  public String getName() {
    return name;
  }

  public Integer getSortPriority() {
    return sortPriority;
  }

  @Value.Immutable
  public static class Builder {

    private String libraryDesignCode;
    private List<Metric> metrics;
    private String name;
    private Integer sortPriority;

    public MetricSubcategory build() {
      return new MetricSubcategory(this);
    }

    public Builder libraryDesignCode(String libraryDesignCode) {
      this.libraryDesignCode = libraryDesignCode;
      return this;
    }

    public Builder metrics(List<Metric> metrics) {
      this.metrics = metrics;
      return this;
    }

    public Builder name(String name) {
      this.name = name;
      return this;
    }

    public Builder sortPriority(Integer sortPriority) {
      this.sortPriority = sortPriority;
      return this;
    }

  }

}
