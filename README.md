# Cardea Case API

## Build Requirements

* Java 17 JDK
* Maven 3.8+

## Configuration

1. Create a `config` subdirectory within the directory you will run the app from
2. Copy [example-application.properties](example-application.properties) into the `config`
   directory and rename it to `application.properties`

## Build/Run

From the project root, build runnable `.jar` file (located in `cardea-server/target`): 

`mvn clean package`

Or run server via Maven (ensure config is in cardea-server):

mvn clean install && mvn spring-boot:run -pl cardea-server

The server runs on port 8080 by default. To run on a different port, add a `server.port` setting
to your `application.properties`

## Monitoring

Prometheus metrics are available at `/metrics` on the deployed webapp.
