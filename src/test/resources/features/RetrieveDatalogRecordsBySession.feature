Feature: Retrieve datalog records by session id

  Scenario: Retrieve datalog records for a session with a single record
    Given the following datalog records exist:
      | sessionId                            | timestamp                | intakeAirTemperature | boostPressure | coolantTemperature | engineRpm |
      | c61cc339-f93d-45a4-aa2b-923f0482b97f | 2022-09-18T18:15:47.968Z | 123                  | 15.6          | 150                | 5000      |
    When the datalog records for session with id 'c61cc339-f93d-45a4-aa2b-923f0482b97f' are retrieved
    Then the request response status is 'OK'
    And the following data log records are returned:
      | sessionId                            | timestamp                | intakeAirTemperature | boostPressure | coolantTemperature | engineRpm |
      | c61cc339-f93d-45a4-aa2b-923f0482b97f | 2022-09-18T18:15:47.968Z | 123                  | 15.6          | 150                | 5000      |
    And the application will log the following messages:
      | level | message                                                                         |
      | INFO  | Retrieving datalog records for session id: c61cc339-f93d-45a4-aa2b-923f0482b97f |

  Scenario: Retrieve datalog records for a session with multiple records
    Given the following datalog records exist:
      | sessionId                            | timestamp                | intakeAirTemperature | boostPressure | coolantTemperature | engineRpm |
      | c61cc339-f93d-45a4-aa2b-923f0482b97f | 2022-09-18T14:15:47.968Z | 123                  | 15.6          | 150                | 5000      |
      | c61cc339-f93d-45a4-aa2b-923f0482b97f | 2022-09-18T14:15:46.962Z | 130                  | 15.0          | 165                | 5500      |
    When the datalog records for session with id 'c61cc339-f93d-45a4-aa2b-923f0482b97f' are retrieved
    Then the request response status is 'OK'
    And the following data log records are returned:
      | sessionId                            | timestamp                | intakeAirTemperature | boostPressure | coolantTemperature | engineRpm |
      | c61cc339-f93d-45a4-aa2b-923f0482b97f | 2022-09-18T14:15:46.962Z | 130                  | 15.0          | 165                | 5500      |
      | c61cc339-f93d-45a4-aa2b-923f0482b97f | 2022-09-18T14:15:47.968Z | 123                  | 15.6          | 150                | 5000      |
    And the application will log the following messages:
      | level | message                                                                         |
      | INFO  | Retrieving datalog records for session id: c61cc339-f93d-45a4-aa2b-923f0482b97f |

  Scenario: Retrieve datalog records with null intake air temperature data
    Given the following datalog records exist:
      | sessionId                            | timestamp                | intakeAirTemperature | boostPressure | coolantTemperature | engineRpm |
      | c61cc339-f93d-45a4-aa2b-923f0482b97f | 2022-09-18T14:15:47.968Z | 123                  |               | 150                |           |
      | c61cc339-f93d-45a4-aa2b-923f0482b97f | 2022-09-18T14:15:48.962Z |                      | 15.6          | 165                | 5000      |
      | c61cc339-f93d-45a4-aa2b-923f0482b97f | 2022-09-18T14:15:49.965Z | 130                  | 15.0          |                    | 5500      |
    When the datalog records for session with id 'c61cc339-f93d-45a4-aa2b-923f0482b97f' are retrieved
    Then the request response status is 'OK'
    And the following data log records are returned:
      | sessionId                            | timestamp                | intakeAirTemperature | boostPressure | coolantTemperature | engineRpm |
      | c61cc339-f93d-45a4-aa2b-923f0482b97f | 2022-09-18T14:15:47.968Z | 123                  |               | 150                |           |
      | c61cc339-f93d-45a4-aa2b-923f0482b97f | 2022-09-18T14:15:48.962Z |                      | 15.6          | 165                | 5000      |
      | c61cc339-f93d-45a4-aa2b-923f0482b97f | 2022-09-18T14:15:49.965Z | 130                  | 15.0          |                    | 5500      |
    And the application will log the following messages:
      | level | message                                                                         |
      | INFO  | Retrieving datalog records for session id: c61cc339-f93d-45a4-aa2b-923f0482b97f |

  Scenario: Retrieve datalog records for a session that does not exist
    Given the following datalog records exist:
      | sessionId                            | timestamp                | intakeAirTemperature | boostPressure | coolantTemperature | engineRpm |
      | c61cc339-f93d-45a4-aa2b-923f0482b97f | 2022-09-18T14:15:47.968Z | 123                  | 15.6          | 150                | 5000      |
      | c61cc339-f93d-45a4-aa2b-923f0482b97f | 2022-09-18T14:15:48.962Z | 130                  | 15            | 165                | 5500      |
    When the datalog records for session with id 'd854a6f2-6642-4a7c-8b06-1b6d559f0171' are retrieved
    Then the request response status is 'OK'
    And the following data log records are returned:
      | sessionId | timestamp | intakeAirTemperature | boostPressure | coolantTemperature | engineRpm |
    And the application will log the following messages:
      | level | message                                                                         |
      | INFO  | Retrieving datalog records for session id: d854a6f2-6642-4a7c-8b06-1b6d559f0171 |