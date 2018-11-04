# Integration Test

## Configuration

Database configs are specified in :
```
 <root_repo>/integrationtest/src/main/resources/application.properties
```

Log4j configs are specified in :
```
<root_repo>/integrationtest/src/main/resources/log4j.properties
```

## Steps to Run the test :

Go to below path:
```
cd <root_repo>/integrationtest/
```
And run below command:
```
mvn clean install
mvn clean test
```

## Viewing the results
Once run is completed, test results are available here:

```
HTML report : <root_repo>/integrationtest/target/surefire-reports/index.html
Logs        : <root_repo>/integrationtest/target/logs/taskmanager.log
```
