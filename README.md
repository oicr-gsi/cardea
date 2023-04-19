# QC Gate ETL API

## Build Requirements

* Java 17 JDK
* Maven 3.8+

## Configuration

1. Create a `config` subdirectory within the directory you will run the app from
2. Copy [example-application.properties](example-application.properties) into the `config`
   directory and rename it to `application.properties`
3. Add the following line to `application.properties` to disable authentication:
     `spring.profiles.active=noauth`

## Build/Run

Build runnable `.jar` file: 

`mvn clean package`

Or run server via Maven:

`mvn clean spring-boot:run`

The server runs on port 8080 by default. To run on a different port, add a `server.port` setting
to your `application.properties`

## Monitoring

Prometheus metrics are available at `/metrics` on the deployed webapp.
