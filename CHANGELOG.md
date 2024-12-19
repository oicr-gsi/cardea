# Changelog

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

This file is updated automatically as described in [Unreleased Changes](changes/README.md).

---------------------------------------------------------------------------------------------------

## [1.19.0] - 2024-12-19

### Added

* Sample collapsed coverage field


## [1.18.0] - 2024-12-17

### Changed

* Analysis review, release approval, and release now have QC statuses that indicate both QC passed and
  the release status


## [1.17.1] - 2024-11-22

### Fixed

* Field on cardea detailed cases must be able to handle null values


## [1.17.0] - 2024-11-21

### Added

* Field qcFailed on sequencing items in shesmu detailed cases


## [1.16.0] - 2024-11-07

### Added

* Omitted run samples in Dimsum case data


## [1.15.0] - 2024-10-24

### Added

* Shesmu Detailed Cases endpoint


## [1.14.0] - 2024-08-06

### Added

* Sample/library DV200 field


## [1.13.0] - 2024-06-13

### Added

* Sample transferDate field for extractions
* Test fields for extraction TAT breakdown of days spent on preparation, QC, and transfer


## [1.12.0] - 2024-06-06

### Added

* Test fields for library qualification and full-depth sequencing TAT breakdown of days spent on
  loading, sequencing, and QC
* Run start date field


## [1.11.0] - 2024-05-08

### Changed

* The `requisition-cases` endpoint now returns a set of cases, which may have different assays
* A requisition may now have multiple assays; the single assayId fields on requisitions and samples
  have been changed to sets

### Fixed

* Missing requisition and assay information for omitted samples


## [1.10.0] - 2024-04-10

### Added

* Deliverable-type-level turn-around time fields
* Project analysisReviewSkipped field


## [1.9.1] - 2024-03-13

### Fixed

* Missing (null) `completedDate`s in `shesmu-cases` endpoint


## [1.9.0] - 2024-03-11

### Added

* New metrics for cfMeDIPs assays


## [1.8.0] - 2024-02-28

### Added

* QC notes to deliverables

### Changed

* Case deliverables now contain all QC information for analysis review, release approval, and release

### Removed

* Requisition QCs


## [1.7.0] - 2024-01-03

### Added

* Case deliverables
* Assay test field to specify if library qualification is skipped

### Fixed

* Loading samples that have no requisition


## [1.6.0] - 2023-11-16

### Added

* Days spent per QC step at case and test levels
* Requisition pause status and reason
* Assay turn-around time targets

### Changed

* Case start date provided by QC-Gate-ETL (based on latest sample receipt) will be used


## [1.5.0] - 2023-09-19

### Changed

* Renamed steps:
  * Informatics Review -> Analysis Review
  * Draft Report -> Release Approval
  * Final Report -> Release


## [1.4.0] - 2023-08-18

### Changed

* Data types to work with Dimsum data parsing and loading


## [1.3.0] - 2023-08-08

### Added

* Sample QC Note field
* capability for Jackson parsing for data models using Builder notation


## [1.2.0] - 2023-07-31

### Added

* Preliminary coverage for full-depth WG libraries, and preliminary clusters for full-depth WT libraries
* isStopped() function in Case and Test library design code field
* Sample median insert size field

### Fixed

* data for the wrong donor could be included in Informatics Review metrics on the Case QC Report
* Only run-library IUSes are included in `shesmu-cases` limsIds field


## [1.1.0] - 2023-05-19

### Added

* Add `/requisition-cases/{requisitionName}` endpoint

### Fixed

* Return list of case IDs instead of count of cases in summary by run


## [1.0.1] - 2023-05-10

### Fixed

* Convert `/shesmu-cases` `completedDate` to Instant for better Shesmu compatibility


## [1.0.0] - 2023-05-08

### Added

* Add /shesmu-cases endpoint

### Fixed

* OpenAPI URLs now displays correct URL scheme
* Forward headers for correct URL display in OpenAPI docs


# Unreleased

* Copied the Dimsum project and modified it to serve as the basis for the Cardea project
