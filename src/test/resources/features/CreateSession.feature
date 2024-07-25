Feature: Create a new session from an uploaded file

  Scenario: Create a new session with a single datalog with valid session data
    Given a file with the following rows:
      | Device Time              | Longitude          | Latitude           | Altitude | Engine Coolant Temperature(°F) | Engine RPM(rpm) | Intake Air Temperature(°F) | Speed (OBD)(mph) | Throttle Position(Manifold)(%) | Turbo Boost & Vacuum Gauge(psi) | Air Fuel Ratio(Measured)(:1) |
      | 18-Sep-2022 14:15:47.968 | -86.14170333333335 | 42.406800000000004 | 188.4    | 95.9                           | 3500.35         | 123.8                      | 74.56            | 5.6                            | 16.5                            | 17.5                         |
    And the user has a valid authorization token
    When the file is uploaded for a session with the following data:
      | trackName  | longitude | latitude | firstName | lastName | email         |
      | Test Track | -86.1374  | 42.4086  | test      | tester   | test@test.com |
    Then the request response status is 'CREATED'
    And the location header will end with '/api/sessions/c61cc339-f93d-45a4-aa2b-923f0482b97f'
    And the following datalogs will exist:
      | sessionId                            | epochMilliseconds | longitude          | latitude           | altitude | intakeAirTemperature | boostPressure | coolantTemperature | engineRpm | speed | throttlePosition | airFuelRatio | trackName  | trackLatitude | trackLongitude | firstName | lastName | email         |
      | c61cc339-f93d-45a4-aa2b-923f0482b97f | 1663524947968     | -86.14170333333335 | 42.406800000000004 | 188.4    | 123                  | 16.5          | 95                 | 3500      | 74    | 5.6              | 17.5         | Test Track | 42.4086       | -86.1374       | test      | tester   | test@test.com |
    And the application will log the following messages:
      | level | message                                              |
      | INFO  | Beginning to parse file: testFile.txt                |
      | INFO  | File parsing completed for file: testFile.txt        |
      | INFO  | Session c61cc339-f93d-45a4-aa2b-923f0482b97f created |

  Scenario: Create a new session with multiple datalogs with valid session data
    Given a file with the following rows:
      | Device Time              | Longitude          | Latitude            | Altitude | Engine Coolant Temperature(°F) | Engine RPM(rpm) | Intake Air Temperature(°F) | Speed (OBD)(mph) | Throttle Position(Manifold)(%) | Turbo Boost & Vacuum Gauge(psi) | Air Fuel Ratio(Measured)(:1) |
      | 18-Sep-2022 14:15:47.968 | -86.14170333333335 | 42.406800000000004  | 188.4    | 95.9                           | 3500.35         | 123.8                      | 74.56            | 5.6                            | 16.5                            | 17.5                         |
      | 18-Sep-2022 14:15:48.962 | 86.14162999999999  | -42.406816666666664 | 188.0    | 98                             | 2500            | 130                        | 79               | 7                              | 15                              | 14.7                         |
    And the user has a valid authorization token
    When the file is uploaded for a session with the following data:
      | trackName  | longitude | latitude | firstName | lastName | email         |
      | Test Track | -86.1374  | 42.4086  | test      | tester   | test@test.com |
    Then the request response status is 'CREATED'
    And the location header will end with '/api/sessions/c61cc339-f93d-45a4-aa2b-923f0482b97f'
    And the following datalogs will exist:
      | sessionId                            | epochMilliseconds | longitude          | latitude            | altitude | intakeAirTemperature | boostPressure | coolantTemperature | engineRpm | speed | throttlePosition | airFuelRatio | trackName  | trackLatitude | trackLongitude | firstName | lastName | email         |
      | c61cc339-f93d-45a4-aa2b-923f0482b97f | 1663524947968     | -86.14170333333335 | 42.406800000000004  | 188.4    | 123                  | 16.5          | 95                 | 3500      | 74    | 5.6              | 17.5         | Test Track | 42.4086       | -86.1374       | test      | tester   | test@test.com |
      | c61cc339-f93d-45a4-aa2b-923f0482b97f | 1663524948962     | 86.14162999999999  | -42.406816666666664 | 188.0    | 130                  | 15.0          | 98                 | 2500      | 79    | 7.0              | 14.7         | Test Track | 42.4086       | -86.1374       | test      | tester   | test@test.com |
    And the application will log the following messages:
      | level | message                                              |
      | INFO  | Beginning to parse file: testFile.txt                |
      | INFO  | File parsing completed for file: testFile.txt        |
      | INFO  | Session c61cc339-f93d-45a4-aa2b-923f0482b97f created |

  Scenario: Create a new session with missing air fuel ratio data column
    Given a file with the following rows:
      | Device Time              | Longitude          | Latitude            | Altitude | Engine Coolant Temperature(°F) | Engine RPM(rpm) | Intake Air Temperature(°F) | Speed (OBD)(mph) | Throttle Position(Manifold)(%) | Turbo Boost & Vacuum Gauge(psi) |
      | 18-Sep-2022 14:15:47.968 | -86.14170333333335 | 42.406800000000004  | 188.4    | 95.9                           | 3500.35         | 123.8                      | 74.56            | 5.6                            | 16.5                            |
      | 18-Sep-2022 14:15:48.962 | 86.14162999999999  | -42.406816666666664 | 188.0    | 98                             | 2500            | 130                        | 79               | 7                              | 15                              |
    And the user has a valid authorization token
    When the file is uploaded for a session with the following data:
      | trackName  | longitude | latitude | firstName | lastName | email         |
      | Test Track | -86.1374  | 42.4086  | test      | tester   | test@test.com |
    Then the request response status is 'CREATED'
    And the location header will end with '/api/sessions/c61cc339-f93d-45a4-aa2b-923f0482b97f'
    And the following datalogs will exist:
      | sessionId                            | epochMilliseconds | longitude          | latitude            | altitude | intakeAirTemperature | boostPressure | coolantTemperature | engineRpm | speed | throttlePosition | airFuelRatio | trackName  | trackLatitude | trackLongitude | firstName | lastName | email         |
      | c61cc339-f93d-45a4-aa2b-923f0482b97f | 1663524947968     | -86.14170333333335 | 42.406800000000004  | 188.4    | 123                  | 16.5          | 95                 | 3500      | 74    | 5.6              |              | Test Track | 42.4086       | -86.1374       | test      | tester   | test@test.com |
      | c61cc339-f93d-45a4-aa2b-923f0482b97f | 1663524948962     | 86.14162999999999  | -42.406816666666664 | 188.0    | 130                  | 15.0          | 98                 | 2500      | 79    | 7.0              |              | Test Track | 42.4086       | -86.1374       | test      | tester   | test@test.com |
    And the application will log the following messages:
      | level | message                                              |
      | INFO  | Beginning to parse file: testFile.txt                |
      | INFO  | File parsing completed for file: testFile.txt        |
      | INFO  | Session c61cc339-f93d-45a4-aa2b-923f0482b97f created |

  Scenario: Create a new session with datalogs with invalid session data
    Given a file with the following rows:
      | Device Time              | Longitude          | Latitude            | Altitude | Engine Coolant Temperature(°F) | Engine RPM(rpm) | Intake Air Temperature(°F) | Speed (OBD)(mph) | Throttle Position(Manifold)(%) | Turbo Boost & Vacuum Gauge(psi) | Air Fuel Ratio(Measured)(:1) |
      | 18-Sep-2022 14:15:47.968 | -86.14170333333335 | 42.406800000000004  | 188.4    | 166.2                          | -               | 123.8                      | 74.56            | 5.6                            | -                               | -                            |
      | 18-Sep-2022 14:15:48.962 | 86.14162999999999  | -42.406816666666664 | 188.0    | 95.9                           | 3500.35         | -                          | -                | 7                              | 16.5                            | 17.5                         |
      | 18-Sep-2022 14:15:49.965 | -86.14162          | 42.406800000000004  | 186.8    | -                              | 2500            | 130                        | 79               | -                              | 15.0                            | 14.8                         |
    And the user has a valid authorization token
    When the file is uploaded for a session with the following data:
      | trackName  | longitude | latitude | firstName | lastName | email         |
      | Test Track | -86.1374  | 42.4086  | test      | tester   | test@test.com |
    Then the request response status is 'CREATED'
    And the location header will end with '/api/sessions/c61cc339-f93d-45a4-aa2b-923f0482b97f'
    And the following datalogs will exist:
      | sessionId                            | epochMilliseconds | longitude          | latitude            | altitude | intakeAirTemperature | boostPressure | coolantTemperature | engineRpm | speed | throttlePosition | airFuelRatio | trackName  | trackLatitude | trackLongitude | firstName | lastName | email         |
      | c61cc339-f93d-45a4-aa2b-923f0482b97f | 1663524947968     | -86.14170333333335 | 42.406800000000004  | 188.4    | 123                  |               | 166                |           | 74    | 5.6              |              | Test Track | 42.4086       | -86.1374       | test      | tester   | test@test.com |
      | c61cc339-f93d-45a4-aa2b-923f0482b97f | 1663524948962     | 86.14162999999999  | -42.406816666666664 | 188.0    |                      | 16.5          | 95                 | 3500      |       | 7.0              | 17.5         | Test Track | 42.4086       | -86.1374       | test      | tester   | test@test.com |
      | c61cc339-f93d-45a4-aa2b-923f0482b97f | 1663524949965     | -86.14162          | 42.406800000000004  | 186.8    | 130                  | 15.0          |                    | 2500      | 79    |                  | 14.8         | Test Track | 42.4086       | -86.1374       | test      | tester   | test@test.com |
    And the application will log the following messages:
      | level | message                                              |
      | INFO  | Beginning to parse file: testFile.txt                |
      | INFO  | File parsing completed for file: testFile.txt        |
      | INFO  | Session c61cc339-f93d-45a4-aa2b-923f0482b97f created |

  Scenario: Create a new session with datalogs with unparseable data rows
    Given a file with the following rows:
      | Device Time              | Longitude          | Latitude            | Altitude | Engine Coolant Temperature(°F) | Engine RPM(rpm) | Intake Air Temperature(°F) | Speed (OBD)(mph) | Throttle Position(Manifold)(%) | Turbo Boost & Vacuum Gauge(psi) | Air Fuel Ratio(Measured)(:1) |
      | 18-Sep-2022 14:15:47.968 | -86.14170333333335 | 42.406800000000004  | 188.4    | 166.2                          | -               | 123.8                      | 74.56            | 5.6                            | -                               | -                            |
      | 18-Sep-2022 14:15:48.962 | 86.14162999999999  | -42.406816666666664 | 188.0    | 95.9                           | 3500.35         | -                          | -                | 7                              | 16.5                            | 17.5                         |
      | Device Time              | abc                | def                 | ghi      | jkl                            | mno             | qpr                        | st               | uv                             | wx                              | yz                           |
    And the user has a valid authorization token
    When the file is uploaded for a session with the following data:
      | trackName  | longitude | latitude | firstName | lastName | email         |
      | Test Track | -86.1374  | 42.4086  | test      | tester   | test@test.com |
    Then the request response status is 'CREATED'
    And the location header will end with '/api/sessions/c61cc339-f93d-45a4-aa2b-923f0482b97f'
    And the following datalogs will exist:
      | sessionId                            | epochMilliseconds | longitude          | latitude            | altitude | intakeAirTemperature | boostPressure | coolantTemperature | engineRpm | speed | throttlePosition | airFuelRatio | trackName  | trackLatitude | trackLongitude | firstName | lastName | email         |
      | c61cc339-f93d-45a4-aa2b-923f0482b97f | 1663524947968     | -86.14170333333335 | 42.406800000000004  | 188.4    | 123                  |               | 166                |           | 74    | 5.6              |              | Test Track | 42.4086       | -86.1374       | test      | tester   | test@test.com |
      | c61cc339-f93d-45a4-aa2b-923f0482b97f | 1663524948962     | 86.14162999999999  | -42.406816666666664 | 188.0    |                      | 16.5          | 95                 | 3500      |       | 7.0              | 17.5         | Test Track | 42.4086       | -86.1374       | test      | tester   | test@test.com |
    And the application will log the following messages:
      | level | message                                                              |
      | INFO  | Beginning to parse file: testFile.txt                                |
      | ERROR | Unable to parse row: Device Time,abc,def,ghi,jkl,mno,qpr,st,uv,wx,yz |
      | INFO  | File parsing completed for file: testFile.txt                        |
      | INFO  | Session c61cc339-f93d-45a4-aa2b-923f0482b97f created                 |

  Scenario: Do not overwrite data for other users when creating a new session
    Given a file with the following rows:
      | Device Time              | Longitude          | Latitude            | Altitude | Engine Coolant Temperature(°F) | Engine RPM(rpm) | Intake Air Temperature(°F) | Speed (OBD)(mph) | Throttle Position(Manifold)(%) | Turbo Boost & Vacuum Gauge(psi) | Air Fuel Ratio(Measured)(:1) |
      | 18-Sep-2022 14:15:47.968 | -86.14170333333335 | 42.406800000000004  | 188.4    | 95.9                           | 3500.35         | 123.8                      | 74.56            | 5.6                            | 16.5                            | 17.5                         |
      | 18-Sep-2022 14:15:48.962 | 86.14162999999999  | -42.406816666666664 | 188.0    | 98                             | 2500            | 130                        | 79               | 7                              | 15                              | 14.9                         |
    And the following datalogs exist:
      | sessionId                            | epochMilliseconds | longitude         | latitude            | altitude | intakeAirTemperature | boostPressure | coolantTemperature | engineRpm | speed | throttlePosition | airFuelRatio | trackName  | trackLatitude | trackLongitude | firstName | lastName | email          |
      | 9628a8bb-0a44-4c31-af7d-a54ff16f080f | 1663524948962     | 86.14162999999999 | -42.406816666666664 | 188.0    | 130                  | 9.0           | 98                 | 1500      | 79    | 7.0              | 15.8         | Test Track | 42.4086       | -86.1374       | test2     | tester   | test2@test.com |
    And the user has a valid authorization token
    When the file is uploaded for a session with the following data:
      | trackName  | longitude | latitude | firstName | lastName | email         |
      | Test Track | -86.1374  | 42.4086  | test      | tester   | test@test.com |
    Then the request response status is 'CREATED'
    And the location header will end with '/api/sessions/c61cc339-f93d-45a4-aa2b-923f0482b97f'
    And the following datalogs will exist:
      | sessionId                            | epochMilliseconds | longitude          | latitude            | altitude | intakeAirTemperature | boostPressure | coolantTemperature | engineRpm | speed | throttlePosition | airFuelRatio | trackName  | trackLatitude | trackLongitude | firstName | lastName | email          |
      | c61cc339-f93d-45a4-aa2b-923f0482b97f | 1663524947968     | -86.14170333333335 | 42.406800000000004  | 188.4    | 123                  | 16.5          | 95                 | 3500      | 74    | 5.6              | 17.5         | Test Track | 42.4086       | -86.1374       | test      | tester   | test@test.com  |
      | c61cc339-f93d-45a4-aa2b-923f0482b97f | 1663524948962     | 86.14162999999999  | -42.406816666666664 | 188.0    | 130                  | 15.0          | 98                 | 2500      | 79    | 7.0              | 14.9         | Test Track | 42.4086       | -86.1374       | test      | tester   | test@test.com  |
      | 9628a8bb-0a44-4c31-af7d-a54ff16f080f | 1663524948962     | 86.14162999999999  | -42.406816666666664 | 188.0    | 130                  | 9.0           | 98                 | 1500      | 79    | 7.0              | 15.8         | Test Track | 42.4086       | -86.1374       | test2     | tester   | test2@test.com |
    And the application will log the following messages:
      | level | message                                              |
      | INFO  | Beginning to parse file: testFile.txt                |
      | INFO  | File parsing completed for file: testFile.txt        |
      | INFO  | Session c61cc339-f93d-45a4-aa2b-923f0482b97f created |

  Scenario: Do not create duplicate sessions for a user
    Given a file with the following rows:
      | Device Time              | Longitude          | Latitude            | Altitude | Engine Coolant Temperature(°F) | Engine RPM(rpm) | Intake Air Temperature(°F) | Speed (OBD)(mph) | Throttle Position(Manifold)(%) | Turbo Boost & Vacuum Gauge(psi) | Air Fuel Ratio(Measured)(:1) |
      | 18-Sep-2022 14:15:47.968 | -86.14170333333335 | 42.406800000000004  | 188.4    | 95.9                           | 3500.35         | 123.8                      | 74.56            | 5.6                            | 16.5                            | 17.5                         |
      | 18-Sep-2022 14:15:48.962 | 86.14162999999999  | -42.406816666666664 | 188.0    | 98                             | 2500            | 130                        | 79               | 7                              | 15                              | 14.9                         |
    And the following datalogs exist:
      | sessionId                            | epochMilliseconds | longitude         | latitude            | altitude | intakeAirTemperature | boostPressure | coolantTemperature | engineRpm | speed | throttlePosition | airFuelRatio | trackName  | trackLatitude | trackLongitude | firstName | lastName | email         |
      | 9628a8bb-0a44-4c31-af7d-a54ff16f080f | 1663524947968     | 86.14162999999999 | -42.406816666666664 | 188.0    | 130                  | 9.0           | 98                 | 1500      | 79    | 7.0              | 15.8         | Test Track | 42.4086       | -86.1374       | test      | tester   | test@test.com |
    And the user has a valid authorization token
    When the file is uploaded for a session with the following data:
      | trackName  | longitude | latitude | firstName | lastName | email         |
      | Test Track | -86.1374  | 42.4086  | test      | tester   | test@test.com |
    Then the request response status is 'CONFLICT'
    And the following datalogs will exist:
      | sessionId                            | epochMilliseconds | longitude         | latitude            | altitude | intakeAirTemperature | boostPressure | coolantTemperature | engineRpm | speed | throttlePosition | airFuelRatio | trackName  | trackLatitude | trackLongitude | firstName | lastName | email         |
      | 9628a8bb-0a44-4c31-af7d-a54ff16f080f | 1663524947968     | 86.14162999999999 | -42.406816666666664 | 188.0    | 130                  | 9.0           | 98                 | 1500      | 79    | 7.0              | 15.8         | Test Track | 42.4086       | -86.1374       | test      | tester   | test@test.com |
    And the application will log the following messages:
      | level | message                                              |
      | INFO  | Beginning to parse file: testFile.txt                |
      | INFO  | File parsing completed for file: testFile.txt        |
      | ERROR | A session already exists for this user and timestamp |

  Scenario: A new session cannot be created with an invalid token
    Given a file with the following rows:
      | Device Time              | Longitude          | Latitude           | Altitude | Engine Coolant Temperature(°F) | Engine RPM(rpm) | Intake Air Temperature(°F) | Speed (OBD)(mph) | Throttle Position(Manifold)(%) | Turbo Boost & Vacuum Gauge(psi) | Air Fuel Ratio(Measured)(:1) |
      | 18-Sep-2022 14:15:47.968 | -86.14170333333335 | 42.406800000000004 | 188.4    | 95.9                           | 3500.35         | 123.8                      | 74.56            | 5.6                            | 16.5                            | 17.5                         |
    And the user has an invalid authorization token
    When the file is uploaded for a session with the following data:
      | trackName  | longitude | latitude | firstName | lastName | email         |
      | Test Track | -86.1374  | 42.4086  | test      | tester   | test@test.com |
    Then the request response status is 'UNAUTHORIZED'
    And the following datalogs will exist:
      | sessionId | epochMilliseconds | longitude | latitude | altitude | intakeAirTemperature | boostPressure | coolantTemperature | engineRpm | speed | throttlePosition | airFuelRatio | trackName | trackLatitude | trackLongitude | firstName | lastName | email |
    And the application will log the following messages:
      | level | message |