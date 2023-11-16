# Changelog

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

This file is updated automatically as described in [Unreleased Changes](changes/README.md).

---------------------------------------------------------------------------------------------------

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
