package ca.on.oicr.gsi.cardea.server;

import ca.on.oicr.gsi.cardea.data.Assay;
import ca.on.oicr.gsi.cardea.data.AssayTargets;
import ca.on.oicr.gsi.cardea.data.Case;
import ca.on.oicr.gsi.cardea.data.CaseData;
import ca.on.oicr.gsi.cardea.data.Deliverable;
import ca.on.oicr.gsi.cardea.data.DeliverableType;
import ca.on.oicr.gsi.cardea.data.Donor;
import ca.on.oicr.gsi.cardea.data.Lane;
import ca.on.oicr.gsi.cardea.data.Metric;
import ca.on.oicr.gsi.cardea.data.MetricCategory;
import ca.on.oicr.gsi.cardea.data.MetricSubcategory;
import ca.on.oicr.gsi.cardea.data.OmittedSample;
import ca.on.oicr.gsi.cardea.data.Project;
import ca.on.oicr.gsi.cardea.data.Requisition;
import ca.on.oicr.gsi.cardea.data.RequisitionQc;
import ca.on.oicr.gsi.cardea.data.RequisitionQcGroup;
import ca.on.oicr.gsi.cardea.data.Run;
import ca.on.oicr.gsi.cardea.data.Sample;
import ca.on.oicr.gsi.cardea.data.Test;
import ca.on.oicr.gsi.cardea.data.ThresholdType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class CaseLoader {

  private static final Logger log = LoggerFactory.getLogger(CaseLoader.class);
  private File assayFile;
  private File caseFile;
  private File donorFile;
  private File noCaseFile;
  private File projectFile;
  private File requisitionFile;
  private File runFile;
  private File sampleFile;
  private File timestampFile;

  private Timer refreshTimer = null;

  private ObjectMapper mapper = new ObjectMapper();

  public CaseLoader(
      @Value("${datadirectory}") File dataDirectory,
      @Autowired MeterRegistry meterRegistry) {
    if (!dataDirectory.isDirectory() || !dataDirectory.canRead()) {
      throw new IllegalStateException(
          String.format("Data directory unreadable: %s", dataDirectory.getAbsolutePath()));
    }
    if (meterRegistry != null) {
      refreshTimer = Timer.builder("case_data_refresh_time")
          .description("Time taken to refresh the case data").register(meterRegistry);
    }
    projectFile = new File(dataDirectory, "projects.json");
    caseFile = new File(dataDirectory, "cases.json");
    donorFile = new File(dataDirectory, "donors.json");
    requisitionFile = new File(dataDirectory, "requisitions.json");
    runFile = new File(dataDirectory, "runs.json");
    sampleFile = new File(dataDirectory, "samples.json");
    assayFile = new File(dataDirectory, "assays.json");
    timestampFile = new File(dataDirectory, "timestamp");
    noCaseFile = new File(dataDirectory, "receipts_nocase.json");
  }

  /**
   * @return The time that the data finished writing, or null if the data is currently being written
   * @throws IOException if there is an error reading the timestamp file from disk
   */
  private ZonedDateTime getDataTimestamp() throws IOException {
    String timeString = Files.readString(timestampFile.toPath()).trim();
    if ("WORKING".equals(timeString)) {
      return null;
    }
    return ZonedDateTime.parse(timeString, DateTimeFormatter.ISO_DATE_TIME);
  }

  /**
   * Loads new case data if available
   *
   * @param previousTimestamp timestamp of previous successful load
   * @return case data if it is available and newer than the previousTimestamp; null otherwise
   * @throws DataParseException if there is an error parsing the data from file
   * @throws IOException if there is an error reading from disk
   */
  public CaseData load(ZonedDateTime previousTimestamp) throws DataParseException, IOException {
    log.debug("Loading case data...");
    ZonedDateTime beforeTimestamp = getDataTimestamp();
    if (beforeTimestamp == null) {
      log.debug("New case data is currently being written; aborting load.");
      return null;
    } else if (previousTimestamp != null && !beforeTimestamp.isAfter(previousTimestamp)) {
      log.debug("Current case data is up to date with data files; aborting load");
      return null;
    }
    long startTimeMillis = System.currentTimeMillis();
    try (FileReader projectReader = getProjectReader();
        FileReader sampleReader = getSampleReader();
        FileReader donorReader = getDonorReader();
        FileReader requisitionReader = getRequisitionReader();
        FileReader runReader = getRunReader();
        FileReader assayReader = getAssayReader();
        FileReader caseReader = getCaseReader();
        FileReader nocaseReader = getNoCaseReader();) {
      ZonedDateTime afterTimestamp = getDataTimestamp();
      if (afterTimestamp == null) {
        log.debug("New case data is currently being written; aborting load.");
        return null;
      } else if (!afterTimestamp.equals(beforeTimestamp)) {
        // Data was written in the middle of loading. Restart
        log.debug("New case data was written while we were reading; reloading...");
        return load(previousTimestamp);
      }
      Map<String, Project> projectsByName = loadProjects(projectReader);
      Map<String, Donor> donorsById = loadDonors(donorReader);
      Map<Long, Run> runsById = loadRuns(runReader);
      Map<Long, Requisition> requisitionsById = loadRequisitions(requisitionReader, donorsById);
      Map<String, Sample> samplesById =
          loadSamples(sampleReader, donorsById, runsById, requisitionsById);
      List<OmittedSample> omittedSamples =
          loadOmittedSamples(nocaseReader, donorsById, requisitionsById);
      Map<Long, Assay> assaysById = loadAssays(assayReader);
      List<Case> cases = loadCases(caseReader, projectsByName, samplesById, donorsById,
          requisitionsById, assaysById);
      if (refreshTimer != null) {
        refreshTimer.record(System.currentTimeMillis() - startTimeMillis, TimeUnit.MILLISECONDS);
      }

      CaseData caseData = new CaseData.Builder()
          .assaysById(assaysById)
          .cases(cases)
          .omittedSamples(omittedSamples)
          .timestamp(afterTimestamp)
          .build();

      log.debug(String.format("Completed loading %d cases.", cases.size()));

      return caseData;
    }
  }

  protected FileReader getAssayReader() throws FileNotFoundException {
    return new FileReader(assayFile);
  }

  protected FileReader getCaseReader() throws FileNotFoundException {
    return new FileReader(caseFile);
  }

  protected FileReader getDonorReader() throws FileNotFoundException {
    return new FileReader(donorFile);
  }

  protected FileReader getNoCaseReader() throws FileNotFoundException {
    return new FileReader(noCaseFile);
  }

  protected FileReader getProjectReader() throws FileNotFoundException {
    return new FileReader(projectFile);
  }

  protected FileReader getRequisitionReader() throws FileNotFoundException {
    return new FileReader(requisitionFile);
  }

  protected FileReader getRunReader() throws FileNotFoundException {
    return new FileReader(runFile);
  }

  protected FileReader getSampleReader() throws FileNotFoundException {
    return new FileReader(sampleFile);
  }

  protected Map<Long, Assay> loadAssays(FileReader fileReader)
      throws DataParseException, IOException {
    List<Assay> assays = loadFromJsonArrayFile(fileReader, json -> new Assay.Builder()
        .id(parseLong(json, "id", true))
        .name(parseString(json, "name", true))
        .description(parseString(json, "description", false))
        .version(parseString(json, "version", true))
        .metricCategories(parseMetricCategories(json.get("metric_categories")))
        .targets(parseAssayTargets(json.get("targets")))
        .build());
    return assays.stream().collect(Collectors.toMap(Assay::getId, Function.identity()));
  }

  private List<Case> loadCases(FileReader fileReader, Map<String, Project> projectsByName,
      Map<String, Sample> samplesById, Map<String, Donor> donorsById,
      Map<Long, Requisition> requisitionsById, Map<Long, Assay> assaysById)
      throws DataParseException, IOException {
    return loadFromJsonArrayFile(fileReader, json -> {
      String donorId = parseString(json, "donor_id", true);
      Long requisitionId = parseLong(json, "requisition_id", true);
      Long assayId = parseLong(json, "assay_id", true);
      Assay assay = assaysById.get(assayId);
      return new Case.Builder()
          .id(parseString(json, "id", true))
          .donor(donorsById.get(donorId))
          .projects(parseProjects(json, "project_names", projectsByName))
          .assayId(assayId)
          .assayName(assay.getName())
          .assayDescription(assay.getDescription())
          .tissueOrigin(parseString(json, "tissue_origin", true))
          .tissueType(parseString(json, "tissue_type", true))
          .timepoint(parseString(json, "timepoint"))
          .receipts(parseIdsAndGet(json, "receipt_ids", JsonNode::asText, samplesById))
          .tests(parseTests(json, "assay_tests", samplesById))
          .deliverables(parseDeliverables(json.get("deliverables")))
          .requisition(requisitionsById.get(requisitionId))
          .startDate(parseDate(json, "start_date"))
          .receiptDaysSpent(parseInteger(json, "receipt_days_spent", true))
          .analysisReviewDaysSpent(parseInteger(json, "analysis_review_days_spent", true))
          .releaseApprovalDaysSpent(parseInteger(json, "release_approval_days_spent", true))
          .releaseDaysSpent(parseInteger(json, "release_days_spent", true))
          .caseDaysSpent(parseInteger(json, "case_days_spent", true))
          .pauseDays(parseInteger(json, "pause_days", true))
          .build();
    });
  }

  protected Map<String, Donor> loadDonors(FileReader fileReader)
      throws DataParseException, IOException {
    List<Donor> donors = loadFromJsonArrayFile(fileReader,
        json -> new Donor.Builder().id(parseString(json, "id", true))
            .name(parseString(json, "name", true))
            .externalName(parseString(json, "external_name", true)).build());

    return donors.stream().collect(Collectors.toMap(Donor::getId, Function.identity()));
  }

  private <T> List<T> loadFromJsonArrayFile(FileReader fileReader, ParseFunction<JsonNode, T> map)
      throws DataParseException, IOException {
    JsonNode json = mapper.readTree(fileReader);
    if (!json.isArray()) {
      throw new DataParseException("Top level JSON node is not an array");
    }
    List<T> loaded = new ArrayList<>();
    for (JsonNode node : json) {
      loaded.add(map.apply(node));
    }
    return loaded;
  }

  protected List<OmittedSample> loadOmittedSamples(FileReader fileReader,
      Map<String, Donor> donorsById, Map<Long, Requisition> requisitionsById)
      throws DataParseException, IOException {
    return loadFromJsonArrayFile(fileReader, json -> {
      Long requisitionId = parseLong(json, "requisition_id", false);
      if (requisitionId != null && !requisitionsById.containsKey(requisitionId)) {
        throw new DataParseException(String.format("Requisition ID %d not found", requisitionId));
      }
      return new OmittedSample.Builder()
          .id(parseString(json, "sample_id", true))
          .name(parseString(json, "oicr_internal_name", true))
          .requisition(requisitionId == null ? null : requisitionsById.get(requisitionId))
          .project(parseString(json, "project_name", true))
          .donor(donorsById.get(parseString(json, "donor_id")))
          .createdDate(parseSampleCreatedDate(json))
          .build();
    });
  }

  protected Map<String, Project> loadProjects(FileReader fileReader)
      throws DataParseException, IOException {
    List<Project> projects = loadFromJsonArrayFile(fileReader,
        json -> new Project.Builder().name(parseString(json, "name", true))
            .pipeline(parseString(json, "pipeline", true)).build());

    return projects.stream().collect(Collectors.toMap(Project::getName, Function.identity()));
  }

  protected Map<Long, Requisition> loadRequisitions(FileReader fileReader,
      Map<String, Donor> donorsById) throws DataParseException, IOException {
    List<Requisition> requisitions = loadFromJsonArrayFile(fileReader, json -> {
      Requisition requisition = new Requisition.Builder()
          .id(parseLong(json, "id", true))
          .name(parseString(json, "name", true))
          .assayId(parseLong(json, "assay_id", false))
          .stopped(parseBoolean(json, "stopped"))
          .stopReason(parseString(json, "stop_reason", false))
          .paused(parseBoolean(json, "paused"))
          .pauseReason(parseString(json, "pause_reason", false))
          .qcGroups(parseRequisitionQcGroups(json.get("qc_groups"), donorsById))
          .analysisReviews(parseRequisitionQcs(json, "analysis_reviews"))
          .releaseApprovals(parseRequisitionQcs(json, "release_approvals"))
          .releases(parseRequisitionQcs(json, "releases")).build();
      return requisition;
    });

    return requisitions.stream().collect(Collectors.toMap(Requisition::getId, Function.identity()));
  }

  protected Map<Long, Run> loadRuns(FileReader fileReader) throws DataParseException, IOException {
    List<Run> runs = loadFromJsonArrayFile(fileReader, json -> {
      return new Run.Builder()
          .id(parseLong(json, "id", true))
          .name(parseString(json, "name", true))
          .containerModel(parseString(json, "container_model"))
          .joinedLanes(parseBoolean(json, "joined_lanes"))
          .sequencingParameters(parseString(json, "sequencing_parameters"))
          .readLength(parseInteger(json, "read_length", false))
          .readLength2(parseInteger(json, "read_length_2", false))
          .completionDate(parseDate(json, "completion_date"))
          .percentOverQ30(parseDecimal(json, "percent_over_q30", false))
          .clustersPf(parseLong(json, "clusters_pf", false))
          .lanes(parseLanes(json.get("lanes")))
          .qcPassed(parseQcPassed(json, "qc_state", true))
          .qcUser(parseString(json, "qc_user"))
          .qcDate(parseDate(json, "qc_date"))
          .dataReviewPassed(parseDataReviewPassed(json, "data_review_state"))
          .dataReviewUser(parseString(json, "data_review_user"))
          .dataReviewDate(parseDate(json, "data_review_date"))
          .build();
    });

    return runs.stream().collect(Collectors.toMap(Run::getId, Function.identity()));
  }

  protected Map<String, Sample> loadSamples(FileReader fileReader, Map<String, Donor> donorsById,
      Map<Long, Run> runsById, Map<Long, Requisition> requisitionsById) throws DataParseException,
      IOException {
    List<Sample> samples = loadFromJsonArrayFile(fileReader, json -> {
      Long runId = parseLong(json, "sequencing_run_id", false);
      if (runId != null && !runsById.containsKey(runId)) {
        throw new DataParseException(String.format("Run ID %d not found", runId));
      }
      Long requisitionId = parseLong(json, "requisition_id", false);
      Requisition requisition = null;
      if (requisitionId != null) {
        requisition = requisitionsById.get(requisitionId);
        if (requisition == null) {
          throw new DataParseException(String.format("Requisition ID %d not found", requisitionId));
        }
      }
      return new Sample.Builder().id(parseString(json, "sample_id", true))
          .name(parseString(json, "oicr_internal_name", true))
          .assayId(requisition == null ? null : requisition.getAssayId())
          .requisitionId(requisitionId)
          .requisitionName(requisition == null ? null : requisition.getName())
          .tissueOrigin(parseString(json, "tissue_origin", true))
          .tissueType(parseString(json, "tissue_type", true))
          .tissueMaterial(parseString(json, "tissue_material"))
          .timepoint(parseString(json, "timepoint"))
          .secondaryId(parseString(json, "secondary_id"))
          .groupId(parseString(json, "group_id"))
          .project(parseString(json, "project_name", true))
          .nucleicAcidType(parseString(json, "nucleic_acid_type"))
          .librarySize(parseInteger(json, "library_size", false))
          .libraryDesignCode(parseString(json, "library_design"))
          .targetedSequencing(parseString(json, "targeted_sequencing"))
          .createdDate(parseSampleCreatedDate(json))
          .volume(parseDecimal(json, "volume", false))
          .concentration(parseDecimal(json, "concentration", false))
          .concentrationUnits(parseString(json, "concentration_units", false))
          .run(runsById.get(runId))
          .donor(donorsById.get(parseString(json, "donor_id")))
          .meanInsertSize(parseDecimal(json, "mean_insert", false))
          .medianInsertSize(parseDecimal(json, "median_insert", false))
          .clustersPerSample(parseInteger(json, "clusters_per_sample", false))
          .preliminaryClustersPerSample(
              parseInteger(json, "preliminary_clusters_per_sample", false))
          .duplicationRate(parseDecimal(json, "duplication_rate", false))
          .meanCoverageDeduplicated(parseDecimal(json, "mean_coverage_deduplicated", false))
          .preliminaryMeanCoverageDeduplicated(
              parseDecimal(json, "preliminary_mean_coverage_deduplicated", false))
          .rRnaContamination(parseDecimal(json, "rrna_contamination", false))
          .mappedToCoding(parseDecimal(json, "mapped_to_coding", false))
          .rawCoverage(parseDecimal(json, "raw_coverage", false))
          .onTargetReads(parseDecimal(json, "on_target_reads", false))
          .lambdaMethylation(parseDecimal(json, "lambda_methylation", false))
          .lambdaClusters(parseInteger(json, "lambda_clusters", false))
          .puc19Methylation(parseDecimal(json, "puc19_methylation", false))
          .puc19Clusters(parseInteger(json, "puc19_clusters", false))
          .qcPassed(parseQcPassed(json, "qc_state", true))
          .qcReason(parseString(json, "qc_reason"))
          .qcNote(parseString(json, "qc_note"))
          .qcUser(parseString(json, "qc_user"))
          .qcDate(parseDate(json, "qc_date"))
          .dataReviewPassed(parseDataReviewPassed(json, "data_review_state"))
          .dataReviewUser(parseString(json, "data_review_user"))
          .dataReviewDate(parseDate(json, "data_review_date"))
          .sequencingLane(parseString(json, "sequencing_lane"))
          .build();
    });

    return samples.stream().collect(Collectors.toMap(Sample::getId, Function.identity()));
  }

  private static JsonNode getNode(JsonNode json, String fieldName, boolean required)
      throws DataParseException {
    JsonNode node = json.get(fieldName);
    if (node == null || node.isNull()) {
      if (required) {
        throw new DataParseException(makeMissingFieldMessage(fieldName));
      } else {
        return null;
      }
    } else {
      return node;
    }
  }

  private static String makeMissingFieldMessage(String fieldName) {
    return String.format("Required value '%s' missing", fieldName);
  }

  private static boolean parseBoolean(JsonNode json, String fieldName) throws DataParseException {
    JsonNode node = json.get(fieldName);
    if (node == null || node.isNull()) {
      throw new DataParseException(makeMissingFieldMessage(fieldName));
    } else {
      return node.asBoolean();
    }
  }

  private static Boolean parseDataReviewPassed(JsonNode json, String fieldName)
      throws DataParseException {
    String qcState = parseString(json, fieldName);
    if (qcState == null) {
      return null;
    }
    switch (qcState) {
      case "Passed":
        return Boolean.TRUE;
      case "Failed":
        return Boolean.FALSE;
      case "Pending":
        return null;
      default:
        throw new DataParseException(String.format("Unhandled data review state: %s", qcState));
    }
  }

  private static LocalDate parseDate(JsonNode json, String fieldName) throws DataParseException {
    String dateString = parseString(json, fieldName);
    if (dateString == null) {
      return null;
    }
    // Remove time portion if included. Result is original recorded date (unmodified
    // by timezone)
    if (dateString.contains("T")) {
      dateString = dateString.split("T")[0];
    }
    try {
      return LocalDate.parse(dateString);
    } catch (DateTimeParseException e) {
      throw new DataParseException(String.format("Bad date format: %s", dateString));
    }
  }

  private static BigDecimal parseDecimal(JsonNode json, String fieldName, boolean required)
      throws DataParseException {
    String stringValue = parseString(json, fieldName, required);
    return stringValue == null ? null : new BigDecimal(stringValue);
  }

  private static Integer parseInteger(JsonNode json, String fieldName, boolean required)
      throws DataParseException {
    JsonNode node = getNode(json, fieldName, required);
    return node == null ? null : node.asInt();
  }

  private static Long parseLong(JsonNode json, String fieldName, boolean required)
      throws DataParseException {
    JsonNode node = getNode(json, fieldName, required);
    return node == null ? null : node.asLong();
  }

  private static Boolean parseQcPassed(JsonNode json, String fieldName, boolean required)
      throws DataParseException {
    String qcState = parseString(json, fieldName, required);
    if (qcState == null) {
      return null;
    }
    switch (qcState) {
      case "Ready":
        return Boolean.TRUE;
      case "Failed":
        return Boolean.FALSE;
      case "Not Ready":
        return null;
      default:
        throw new DataParseException(String.format("Unhandled QC state: %s", qcState));
    }
  }

  private static List<RequisitionQcGroup> parseRequisitionQcGroups(JsonNode json,
      Map<String, Donor> donorsById) throws DataParseException {
    if (json == null || !json.isArray()) {
      throw new DataParseException("Invalid requisition qc_groups");
    }
    List<RequisitionQcGroup> qcGroups = new ArrayList<>();
    for (JsonNode node : json) {
      qcGroups.add(new RequisitionQcGroup.Builder()
          .donor(donorsById.get(parseString(node, "donor_id", true)))
          .tissueOrigin(parseString(node, "tissue_origin", true))
          .tissueType(parseString(node, "tissue_type", true))
          .libraryDesignCode(parseString(node, "library_design", true))
          .groupId(parseString(node, "group_id", false))
          .purity(parseDecimal(node, "purity", false))
          .collapsedCoverage(parseDecimal(node, "collapsed_coverage", false))
          .callability(parseDecimal(node, "callability", false))
          .build());
    }
    return qcGroups;
  }

  private static List<RequisitionQc> parseRequisitionQcs(JsonNode json, String fieldName)
      throws DataParseException {
    JsonNode arr = json.get(fieldName);
    if (arr == null || !arr.isArray()) {
      throw new DataParseException(String.format("%s is not an array", fieldName));
    }
    List<RequisitionQc> qcs = new ArrayList<>();
    for (JsonNode node : arr) {
      qcs.add(new RequisitionQc.Builder()
          .qcPassed(parseQcPassed(node, "qc_state", true))
          .qcUser(parseString(node, "qc_user"))
          .qcDate(parseDate(node, "qc_date"))
          .build());
    }
    return qcs;
  }

  private static LocalDate parseSampleCreatedDate(JsonNode json) throws DataParseException {
    LocalDate receivedDate = parseDate(json, "received");
    if (receivedDate != null) {
      return receivedDate;
    }
    LocalDate createdDate = parseDate(json, "created");
    if (createdDate != null) {
      return createdDate;
    }
    return parseDate(json, "entered");
  }

  private static String parseString(JsonNode json, String fieldName) throws DataParseException {
    return parseString(json, fieldName, false);
  }

  private static String parseString(JsonNode json, String fieldName, boolean required)
      throws DataParseException {
    JsonNode node = getNode(json, fieldName, required);
    return node == null ? null : node.asText();
  }

  private <I, T> List<T> parseIdsAndGet(JsonNode json, String idsFieldName,
      Function<JsonNode, I> convertField, Map<I, T> itemsById) throws DataParseException {
    JsonNode idsNode = json.get(idsFieldName);
    if (idsNode == null || !idsNode.isArray()) {
      throw new DataParseException(String.format("Field %s is not an array", idsFieldName));
    }
    List<T> items = new ArrayList<>();
    for (JsonNode idNode : idsNode) {
      I itemId = convertField.apply(idNode);
      T item = itemsById.get(itemId);
      if (item == null) {
        throw new DataParseException(String.format("Item %s not found", itemId));
      }
      items.add(item);
    }
    return items;
  }

  private Lane parseLane(JsonNode json) throws DataParseException {
    return new Lane.Builder()
        .laneNumber(parseInteger(json, "lane_number", true))
        .percentOverQ30Read1(parseInteger(json, "percent_over_q30_read1", false))
        .percentOverQ30Read2(parseInteger(json, "percent_over_q30_read2", false))
        .clustersPf(parseLong(json, "clusters_pf", false))
        .percentPfixRead1(parseDecimal(json, "percent_phix_read1", false))
        .percentPfixRead2(parseDecimal(json, "percent_phix_read2", false))
        .build();
  }

  private List<Lane> parseLanes(JsonNode json) throws DataParseException {
    if (json == null || !json.isObject()) {
      throw new DataParseException("Invalid run lanes");
    }
    List<Lane> lanes = new ArrayList<>();
    Iterator<Entry<String, JsonNode>> iterator = json.fields();
    while (iterator.hasNext()) {
      Entry<String, JsonNode> entry = iterator.next();
      Lane lane = parseLane(entry.getValue());
      lanes.add(lane);
    }
    return lanes;
  }

  private Map<MetricCategory, List<MetricSubcategory>> parseMetricCategories(JsonNode json)
      throws DataParseException {
    if (json == null || !json.isObject()) {
      throw new DataParseException("Invalid assay metric categories");
    }
    Map<MetricCategory, List<MetricSubcategory>> map = new HashMap<>();
    Iterator<Entry<String, JsonNode>> iterator = json.fields();
    while (iterator.hasNext()) {
      Entry<String, JsonNode> entry = iterator.next();
      MetricCategory category = MetricCategory.valueOf(entry.getKey());
      map.put(category, parseMetricSubcategories(entry.getValue()));
    }
    return map;
  }

  private List<MetricSubcategory> parseMetricSubcategories(JsonNode json)
      throws DataParseException {
    if (json == null || !json.isArray()) {
      throw new DataParseException("Invalid metric subcategories");
    }
    List<MetricSubcategory> subcategories = new ArrayList<>();
    for (JsonNode node : json) {
      subcategories.add(new MetricSubcategory.Builder()
          .name(parseString(node, "name", false))
          .sortPriority(parseInteger(node, "sort_priority", false))
          .libraryDesignCode(parseString(node, "library_design", false))
          .metrics(parseMetrics(node.get("metrics")))
          .build());
    }
    return subcategories;
  }

  private List<Metric> parseMetrics(JsonNode json) throws DataParseException {
    if (json == null || !json.isArray()) {
      throw new DataParseException("Invalid metrics");
    }
    List<Metric> metrics = new ArrayList<>();
    for (JsonNode node : json) {
      ThresholdType thresholdType = ThresholdType.valueOf(node.get("threshold_type").asText());
      metrics.add(new Metric.Builder()
          .name(parseString(node, "name", true))
          .sortPriority(parseInteger(node, "sort_priority", false))
          .minimum(parseDecimal(node, "minimum", false))
          .maximum(parseDecimal(node, "maximum", false))
          .units(parseString(node, "units", false))
          .tissueMaterial(parseString(node, "tissue_material", false))
          .tissueOrigin(parseString(node, "tissue_origin", false))
          .tissueType(parseString(node, "tissue_type", false))
          .negateTissueType(parseBoolean(node, "negate_tissue_type"))
          .nucleicAcidType(parseString(node, "nucleic_acid_type", false))
          .containerModel(parseString(node, "container_model", false))
          .readLength(parseInteger(node, "read_length", false))
          .readLength2(parseInteger(node, "read_length_2", false))
          .thresholdType(thresholdType)
          .build());
    }
    return metrics;
  }

  private AssayTargets parseAssayTargets(JsonNode json) throws DataParseException {
    AssayTargets.Builder builder = new AssayTargets.Builder();
    if (json != null && json.isObject()) {
      builder.caseDays(parseInteger(json, "case_days", false))
          .receiptDays(parseInteger(json, "receipt_days", false))
          .extractionDays(parseInteger(json, "extraction_days", false))
          .libraryPreparationDays(parseInteger(json, "library_preparation_days", false))
          .libraryQualificationDays(parseInteger(json, "library_qualification_days", false))
          .fullDepthSequencingDays(parseInteger(json, "full_depth_sequencing_days", false))
          .analysisReviewDays(parseInteger(json, "analysis_review_days", false))
          .releaseApprovalDays(parseInteger(json, "release_approval_days", false))
          .releaseDays(parseInteger(json, "release_days", false));
    }
    return builder.build();
  }

  private Set<Project> parseProjects(JsonNode json, String fieldName,
      Map<String, Project> projectsByName) throws DataParseException {
    JsonNode projectsNode = json.get(fieldName);
    if (projectsNode == null || !projectsNode.isArray() || projectsNode.isEmpty()) {
      throw new DataParseException("Case has no projects");
    }
    Set<Project> projects = new HashSet<>();
    for (JsonNode projectNode : projectsNode) {
      projects.add(projectsByName.get(projectNode.asText()));
    }
    return projects;
  }

  private List<Test> parseTests(JsonNode json, String fieldName, Map<String, Sample> samplesById)
      throws DataParseException {
    JsonNode testsNode = json.get(fieldName);
    if (testsNode == null || !testsNode.isArray()) {
      throw new DataParseException(String.format("Field %s is not an array", fieldName));
    }
    List<Test> tests = new ArrayList<>();
    for (JsonNode testNode : testsNode) {
      tests.add(new Test.Builder()
          .name(parseString(testNode, "name", true))
          .tissueOrigin(parseString(testNode, "tissue_origin"))
          .tissueType(parseString(testNode, "tissue_type"))
          .timepoint(parseString(testNode, "timepoint"))
          .groupId(parseString(testNode, "group_id"))
          .libraryDesignCode(parseString(testNode, "library_design", false))
          .targetedSequencing(parseString(testNode, "targeted_sequencing"))
          .extractionSkipped(parseBoolean(testNode, "extraction_skipped"))
          .libraryPreparationSkipped(parseBoolean(testNode, "library_preparation_skipped"))
          .libraryQualificationSkipped(parseBoolean(testNode, "library_qualification_skipped"))
          .extractions(parseIdsAndGet(testNode, "extraction_ids", JsonNode::asText, samplesById))
          .libraryPreparations(
              parseIdsAndGet(testNode, "library_preparation_ids", JsonNode::asText, samplesById))
          .libraryQualifications(
              parseIdsAndGet(testNode, "library_qualification_ids", JsonNode::asText, samplesById))
          .fullDepthSequencings(
              parseIdsAndGet(testNode, "full_depth_sequencing_ids", JsonNode::asText, samplesById))
          .extractionDaysSpent(parseInteger(testNode, "extraction_days_spent", true))
          .libraryPreparationDaysSpent(
              parseInteger(testNode, "library_preparation_days_spent", true))
          .libraryQualificationDaysSpent(
              parseInteger(testNode, "library_qualification_days_spent", true))
          .fullDepthSequencingDaysSpent(
              parseInteger(testNode, "full_depth_sequencing_days_spent", true))
          .build());
    }
    return tests;
  }

  private List<Deliverable> parseDeliverables(JsonNode deliverablesNode) throws DataParseException {
    if (deliverablesNode == null) {
      return null;
    }
    List<Deliverable> deliverables = new ArrayList<>();
    for (JsonNode node : deliverablesNode) {
      deliverables.add(new Deliverable.Builder()
          .deliverableType(DeliverableType.valueOf(parseString(node, "deliverable_type")))
          .analysisReviewQcDate(parseDate(node, "analysis_review_qc_date"))
          .analysisReviewQcPassed(parseQcPassed(node, "analysis_review_qc_state", false))
          .analysisReviewQcUser(parseString(node, "analysis_review_qc_user"))
          .releaseApprovalQcDate(parseDate(node, "release_approval_qc_date"))
          .releaseApprovalQcPassed(parseQcPassed(node, "release_approval_qc_state", false))
          .releaseApprovalQcUser(parseString(node, "release_approval_qc_user"))
          .releaseQcDate(parseDate(node, "release_qc_date"))
          .releaseQcPassed(parseQcPassed(node, "release_qc_state", false))
          .releaseQcUser(parseString(node, "release_qc_user"))
          .build());
    }
    return deliverables;
  }

  @FunctionalInterface
  private static interface ParseFunction<T, R> {
    R apply(T input) throws DataParseException;
  }

}
