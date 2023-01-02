# DATALOG API
___

This application returns data parsed from log files created by the [Torque](https://torque-bhp.com/) application
that has been stored in a MongoDB database.

### Usage
___

#### Running locally

* Clone the repository: `git clone https://github.com/ryzingTitan/datalog-api.git`
* Navigate to the folder where the repository has been cloned: `cd datalog-api`
* Update the `production` profile in the [configuration file](./src/main/resources/application.yml)
  * Update the MongoDB connection information to match the instance of MongoDB where the parsed data is stored
* Build the Dockerfile: `docker build -t datalog-api:1.6.0 .`
* Run the Dockerfile locally: `docker run -d -p 8080:8080 --name datalog-api datalog-api:1.6.0`

### Integration Tests
___

The Cucumber integration tests for this application require a connection to an active MongoDB instance.
By default, the integration test profile (the cucumber profile) is configured to use a local instance of MongoDB
running on the default port (27017). The tests will create a database named `cucumberTest` to use during testing
to avoid data conflicts. If you prefer to use an instance of MongoDB that is not running locally or not using the
default port, you will need to update the integration test profile with the connection information of the MongoDB
instance you want to use.

### Acknowledgements
___
 
All the hard work done by the developers of the [Torque](https://torque-bhp.com/) application has enabled me to create this project.