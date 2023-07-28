Feature: Retrieve datalog records by session id

  Scenario: Retrieve datalog records for a session with a single record
    Given the following datalogs exist:
      | sessionId                            | epochMilliseconds | longitude          | latitude           | altitude | intakeAirTemperature | boostPressure | coolantTemperature | engineRpm | speed | throttlePosition | airFuelRatio | trackName  | trackLatitude | trackLongitude | firstName | lastName | email         |
      | c61cc339-f93d-45a4-aa2b-923f0482b97f | 1663524947968     | -86.14170333333335 | 42.406800000000004 | 188.4    | 123                  | 15.6          | 150                | 5000      | 85    | 75.6             | 14.7         | Test Track | 42.4086       | -86.1374       | test      | tester   | test@test.com |
    And the user has a valid authorization token
    When the datalogs for session with id 'c61cc339-f93d-45a4-aa2b-923f0482b97f' are retrieved
    Then the request response status is 'OK'
    And the following datalogs are returned:
      | sessionId                            | timestamp                | longitude          | latitude           | altitude | intakeAirTemperature | boostPressure | coolantTemperature | engineRpm | speed | throttlePosition | airFuelRatio | trackName  | trackLatitude | trackLongitude | firstName | lastName | email         |
      | c61cc339-f93d-45a4-aa2b-923f0482b97f | 2022-09-18T18:15:47.968Z | -86.14170333333335 | 42.406800000000004 | 188.4    | 123                  | 15.6          | 150                | 5000      | 85    | 75.6             | 14.7         | Test Track | 42.4086       | -86.1374       | test      | tester   | test@test.com |
    And the application will log the following messages:
      | level | message                                                                         |
      | INFO  | Retrieving datalog records for session id: c61cc339-f93d-45a4-aa2b-923f0482b97f |

  Scenario: Retrieve datalog records for a session with multiple records
    Given the following datalogs exist:
      | sessionId                            | epochMilliseconds | longitude          | latitude            | altitude | intakeAirTemperature | boostPressure | coolantTemperature | engineRpm | speed | throttlePosition | airFuelRatio | trackName  | trackLatitude | trackLongitude | firstName | lastName | email         |
      | c61cc339-f93d-45a4-aa2b-923f0482b97f | 1663510547968     | -86.14170333333335 | 42.406800000000004  | 188.4    | 123                  | 15.6          | 150                | 5000      | 85    | 75.6             | 14.7         | Test Track | 42.4086       | -86.1374       | test      | tester   | test@test.com |
      | c61cc339-f93d-45a4-aa2b-923f0482b97f | 1663510546962     | 86.14162999999999  | -42.406816666666664 | 188.0    | 130                  | 15.0          | 165                | 5500      | 80    | 75.0             | 15.8         | Test Track | 42.4086       | -86.1374       | test      | tester   | test@test.com |
    When the datalogs for session with id 'c61cc339-f93d-45a4-aa2b-923f0482b97f' are retrieved
    Then the request response status is 'OK'
    And the following datalogs are returned:
      | sessionId                            | timestamp                | longitude          | latitude            | altitude | intakeAirTemperature | boostPressure | coolantTemperature | engineRpm | speed | throttlePosition | airFuelRatio | trackName  | trackLatitude | trackLongitude | firstName | lastName | email         |
      | c61cc339-f93d-45a4-aa2b-923f0482b97f | 2022-09-18T14:15:46.962Z | 86.14162999999999  | -42.406816666666664 | 188.0    | 130                  | 15.0          | 165                | 5500      | 80    | 75.0             | 15.8         | Test Track | 42.4086       | -86.1374       | test      | tester   | test@test.com |
      | c61cc339-f93d-45a4-aa2b-923f0482b97f | 2022-09-18T14:15:47.968Z | -86.14170333333335 | 42.406800000000004  | 188.4    | 123                  | 15.6          | 150                | 5000      | 85    | 75.6             | 14.7         | Test Track | 42.4086       | -86.1374       | test      | tester   | test@test.com |
    And the application will log the following messages:
      | level | message                                                                         |
      | INFO  | Retrieving datalog records for session id: c61cc339-f93d-45a4-aa2b-923f0482b97f |

  Scenario: Retrieve datalog records with null data
    Given the following datalogs exist:
      | sessionId                            | epochMilliseconds | longitude          | latitude            | altitude | intakeAirTemperature | boostPressure | coolantTemperature | engineRpm | speed | throttlePosition | airFuelRatio | trackName  | trackLatitude | trackLongitude | firstName | lastName | email         |
      | c61cc339-f93d-45a4-aa2b-923f0482b97f | 1663510547968     | -86.14170333333335 | 42.406800000000004  | 188.4    | 123                  |               | 150                |           | 85    |                  |              | Test Track | 42.4086       | -86.1374       | test      | tester   | test@test.com |
      | c61cc339-f93d-45a4-aa2b-923f0482b97f | 1663510548962     | 86.14162999999999  | -42.406816666666664 | 188.0    |                      | 15.6          | 165                | 5000      |       | 75.6             | 15.8         | Test Track | 42.4086       | -86.1374       | test      | tester   | test@test.com |
      | c61cc339-f93d-45a4-aa2b-923f0482b97f | 1663510549965     | -86.14162          | 42.406800000000004  | 186.8    | 130                  | 15.0          |                    | 5500      | 80    | 75.0             | 14.7         | Test Track | 42.4086       | -86.1374       | test      | tester   | test@test.com |
    When the datalogs for session with id 'c61cc339-f93d-45a4-aa2b-923f0482b97f' are retrieved
    Then the request response status is 'OK'
    And the following datalogs are returned:
      | sessionId                            | timestamp                | longitude          | latitude            | altitude | intakeAirTemperature | boostPressure | coolantTemperature | engineRpm | speed | throttlePosition | airFuelRatio | trackName  | trackLatitude | trackLongitude | firstName | lastName | email         |
      | c61cc339-f93d-45a4-aa2b-923f0482b97f | 2022-09-18T14:15:47.968Z | -86.14170333333335 | 42.406800000000004  | 188.4    | 123                  |               | 150                |           | 85    |                  |              | Test Track | 42.4086       | -86.1374       | test      | tester   | test@test.com |
      | c61cc339-f93d-45a4-aa2b-923f0482b97f | 2022-09-18T14:15:48.962Z | 86.14162999999999  | -42.406816666666664 | 188.0    |                      | 15.6          | 165                | 5000      |       | 75.6             | 15.8         | Test Track | 42.4086       | -86.1374       | test      | tester   | test@test.com |
      | c61cc339-f93d-45a4-aa2b-923f0482b97f | 2022-09-18T14:15:49.965Z | -86.14162          | 42.406800000000004  | 186.8    | 130                  | 15.0          |                    | 5500      | 80    | 75.0             | 14.7         | Test Track | 42.4086       | -86.1374       | test      | tester   | test@test.com |
    And the application will log the following messages:
      | level | message                                                                         |
      | INFO  | Retrieving datalog records for session id: c61cc339-f93d-45a4-aa2b-923f0482b97f |

  Scenario: Retrieve datalog records for a session that does not exist
    Given the following datalogs exist:
      | sessionId                            | epochMilliseconds | longitude          | latitude            | altitude | intakeAirTemperature | boostPressure | coolantTemperature | engineRpm | speed | throttlePosition | airFuelRatio | trackName  | trackLatitude | trackLongitude | firstName | lastName | email         |
      | c61cc339-f93d-45a4-aa2b-923f0482b97f | 1663524947968     | -86.14170333333335 | 42.406800000000004  | 188.4    | 123                  | 15.6          | 150                | 5000      | 85    | 75.6             | 15.8         | Test Track | 42.4086       | -86.1374       | test      | tester   | test@test.com |
      | c61cc339-f93d-45a4-aa2b-923f0482b97f | 1663510548962     | 86.14162999999999  | -42.406816666666664 | 188.0    | 130                  | 15            | 165                | 5500      | 80    | 75.0             | 14.9         | Test Track | 42.4086       | -86.1374       | test      | tester   | test@test.com |
    When the datalogs for session with id 'd854a6f2-6642-4a7c-8b06-1b6d559f0171' are retrieved
    Then the request response status is 'OK'
    And the following datalogs are returned:
      | sessionId | timestamp | intakeAirTemperature | longitude | latitude | altitude | boostPressure | coolantTemperature | engineRpm | speed | throttlePosition | airFuelRatio | trackName | trackLatitude | trackLongitude | firstName | lastName | email |
    And the application will log the following messages:
      | level | message                                                                         |
      | INFO  | Retrieving datalog records for session id: d854a6f2-6642-4a7c-8b06-1b6d559f0171 |

  Scenario: Backwards compatibility for records without air fuel ratio data
    Given the following datalogs exist:
      | sessionId                            | epochMilliseconds | longitude          | latitude           | altitude | intakeAirTemperature | boostPressure | coolantTemperature | engineRpm | speed | throttlePosition | trackName  | trackLatitude | trackLongitude | firstName | lastName | email         |
      | c61cc339-f93d-45a4-aa2b-923f0482b97f | 1663524947968     | -86.14170333333335 | 42.406800000000004 | 188.4    | 123                  | 15.6          | 150                | 5000      | 85    | 75.6             | Test Track | 42.4086       | -86.1374       | test      | tester   | test@test.com |
    When the datalogs for session with id 'c61cc339-f93d-45a4-aa2b-923f0482b97f' are retrieved
    Then the request response status is 'OK'
    And the following datalogs are returned:
      | sessionId                            | timestamp                | longitude          | latitude           | altitude | intakeAirTemperature | boostPressure | coolantTemperature | engineRpm | speed | throttlePosition | airFuelRatio | trackName  | trackLatitude | trackLongitude | firstName | lastName | email         |
      | c61cc339-f93d-45a4-aa2b-923f0482b97f | 2022-09-18T18:15:47.968Z | -86.14170333333335 | 42.406800000000004 | 188.4    | 123                  | 15.6          | 150                | 5000      | 85    | 75.6             |              | Test Track | 42.4086       | -86.1374       | test      | tester   | test@test.com |
    And the application will log the following messages:
      | level | message                                                                         |
      | INFO  | Retrieving datalog records for session id: c61cc339-f93d-45a4-aa2b-923f0482b97f |

  Scenario: Datalogs cannot be read with an invalid authorization token
    Given the following datalogs exist:
      | sessionId                            | epochMilliseconds | longitude          | latitude           | altitude | intakeAirTemperature | boostPressure | coolantTemperature | engineRpm | speed | throttlePosition | airFuelRatio | trackName  | trackLatitude | trackLongitude | firstName | lastName | email         |
      | c61cc339-f93d-45a4-aa2b-923f0482b97f | 1663524947968     | -86.14170333333335 | 42.406800000000004 | 188.4    | 123                  | 15.6          | 150                | 5000      | 85    | 75.6             | 14.7         | Test Track | 42.4086       | -86.1374       | test      | tester   | test@test.com |
    And the user has an invalid authorization token
    When the datalogs for session with id 'c61cc339-f93d-45a4-aa2b-923f0482b97f' are retrieved
    Then the request response status is 'UNAUTHORIZED'
    And the following datalogs are returned:
      | sessionId | timestamp | longitude | latitude | altitude | intakeAirTemperature | boostPressure | coolantTemperature | engineRpm | speed | throttlePosition | airFuelRatio | trackName | trackLatitude | trackLongitude | firstName | lastName | email |
    And the application will log the following messages:
      | level | message |
