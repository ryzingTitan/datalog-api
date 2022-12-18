Feature: Retrieve datalog records by session

  Scenario: Retrieve datalog records for a session with a single record
    Given the following datalog records exist:
      | sessionId                            | timestamp                | intakeAirTemperature |
      | c61cc339-f93d-45a4-aa2b-923f0482b97f | 2022-09-18T18:15:47.968Z | 123.8                |
    When the datalog records for session with id 'c61cc339-f93d-45a4-aa2b-923f0482b97f' are retrieved
    Then the following data log records are returned:
      | sessionId                            | timestamp                | intakeAirTemperature |
      | c61cc339-f93d-45a4-aa2b-923f0482b97f | 2022-09-18T18:15:47.968Z | 123.8                |
    And the application will log the following messages:
      | level | message                                                                         |
      | INFO  | Retrieving datalog records for session id: c61cc339-f93d-45a4-aa2b-923f0482b97f |

#  Scenario: Parse multiple records with valid intake air temperature data
#    Given a file with the following rows:
#      | Device Time              | Intake Air Temperature(°F) |
#      | 18-Sep-2022 14:15:47.968 | 123.8                      |
#      | 18-Sep-2022 14:15:48.962 | 130                        |
#    When the file is parsed
#    Then the following data log records will be created:
#      | sessionId                            | timestamp                | intakeAirTemperature |
#      | c61cc339-f93d-45a4-aa2b-923f0482b97f | 2022-09-18T18:15:47.968Z | 123.8                |
#      | c61cc339-f93d-45a4-aa2b-923f0482b97f | 2022-09-18T18:15:48.962Z | 130.0                |
#    And the application will log the following messages:
#      | level | message                 |
#      | INFO  | Beginning to parse file |
#      | INFO  | File parsing completed  |
#
#  Scenario: Parse records with invalid intake air temperature data
#    Given a file with the following rows:
#      | Device Time              | Intake Air Temperature(°F) |
#      | 18-Sep-2022 14:15:47.968 | 123.8                      |
#      | 18-Sep-2022 14:15:48.962 | -                          |
#      | 18-Sep-2022 14:15:49.965 | 130                        |
#    When the file is parsed
#    Then the following data log records will be created:
#      | sessionId                            | timestamp                | intakeAirTemperature |
#      | c61cc339-f93d-45a4-aa2b-923f0482b97f | 2022-09-18T18:15:47.968Z | 123.8                |
#      | c61cc339-f93d-45a4-aa2b-923f0482b97f | 2022-09-18T18:15:48.962Z |                      |
#      | c61cc339-f93d-45a4-aa2b-923f0482b97f | 2022-09-18T18:15:49.965Z | 130.0                |
#    And the application will log the following messages:
#      | level | message                 |
#      | INFO  | Beginning to parse file |
#      | INFO  | File parsing completed  |