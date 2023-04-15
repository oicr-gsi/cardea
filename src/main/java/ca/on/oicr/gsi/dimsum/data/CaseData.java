package ca.on.oicr.gsi.dimsum.data;

import static java.util.Collections.unmodifiableList;
import static java.util.Objects.requireNonNull;
import java.time.ZonedDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import javax.annotation.concurrent.Immutable;

@Immutable
public class CaseData {

  private final Map<Long, Assay> assaysById;
  private final List<Case> cases;
  private final List<OmittedSample> omittedSamples;
  private final Map<String, ProjectSummary> projectSummariesByName;
  private final Map<String, RunAndLibraries> runsByName;
  private final ZonedDateTime timestamp;

  public CaseData(List<Case> cases, Map<String, RunAndLibraries> runsByName,
      Map<Long, Assay> assaysById, List<OmittedSample> omittedSamples, ZonedDateTime timestamp,
      Map<String, ProjectSummary> projectSummariesByName) {
    this.cases = unmodifiableList(cases);
    this.runsByName = Collections.unmodifiableMap(runsByName);
    this.assaysById = Collections.unmodifiableMap(assaysById);
    this.omittedSamples = Collections.unmodifiableList(omittedSamples);
    this.timestamp = requireNonNull(timestamp);
    this.projectSummariesByName = Collections.unmodifiableMap(projectSummariesByName);

  }

  public Map<Long, Assay> getAssaysById() {
    return assaysById;
  }

  public List<Case> getCases() {
    return cases;
  }

  public List<OmittedSample> getOmittedSamples() {
    return omittedSamples;
  }

  public Map<String, ProjectSummary> getProjectSummariesByName() {
    return projectSummariesByName;
  }

  public RunAndLibraries getRunAndLibraries(String runName) {
    return runsByName.get(runName);
  }

  public Collection<RunAndLibraries> getRunsAndLibraries() {
    return runsByName.values();
  }

  public Map<String, RunAndLibraries> getRunsAndLibrariesByName() {
    return runsByName;
  }

  public ZonedDateTime getTimestamp() {
    return timestamp;
  }
}
