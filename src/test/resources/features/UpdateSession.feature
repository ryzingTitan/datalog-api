Feature: Update an existing session from an uploaded file

  Scenario: Update datalogs for an existing session
    Given a file with the following rows:
      | Device Time              | Longitude          | Latitude            | Altitude | Engine Coolant Temperature(°F) | Engine RPM(rpm) | Intake Air Temperature(°F) | Speed (OBD)(mph) | Throttle Position(Manifold)(%) | Turbo Boost & Vacuum Gauge(psi) | Air Fuel Ratio(Measured)(:1) |
      | 18-Sep-2022 14:15:47.968 | -86.14170333333335 | 42.406800000000004  | 188.4    | 95.9                           | 3500.35         | 123.8                      | 74.56            | 5.6                            | 16.5                            | 17.5                         |
      | 18-Sep-2022 14:15:48.962 | 86.14162999999999  | -42.406816666666664 | 188.0    | 98                             | 2500            | 130                        | 79               | 7                              | 15                              | 14.9                         |
    And the following datalogs exist:
      | sessionId                            | epochMilliseconds | longitude          | latitude            | altitude | intakeAirTemperature | boostPressure | coolantTemperature | engineRpm | speed | throttlePosition | airFuelRatio | trackName  | trackLatitude | trackLongitude | firstName | lastName | email         |
      | 9628a8bb-0a44-4c31-af7d-a54ff16f080f | 1663524947968     | -86.14170333333335 | 42.406800000000004  | 188.4    | 100                  | 16.5          | 95                 | 4500      | 74    | 5.6              | 14.7         | Test Track | 42.4086       | -86.1374       | test      | tester   | test@test.com |
      | 9628a8bb-0a44-4c31-af7d-a54ff16f080f | 1663524948962     | 86.14162999999999  | -42.406816666666664 | 188.0    | 130                  | 9.0           | 98                 | 1500      | 79    | 7.0              | 15.8         | Test Track | 42.4086       | -86.1374       | test      | tester   | test@test.com |
    And the user has a valid authorization token
    When the file is uploaded for a session with the following data and session id '9628a8bb-0a44-4c31-af7d-a54ff16f080f':
      | trackName  | longitude | latitude | firstName | lastName | email         |
      | Test Track | -86.1374  | 42.4086  | test      | tester   | test@test.com |
    Then the request response status is 'OK'
    And the following datalogs will exist:
      | sessionId                            | epochMilliseconds | longitude          | latitude            | altitude | intakeAirTemperature | boostPressure | coolantTemperature | engineRpm | speed | throttlePosition | airFuelRatio | trackName  | trackLatitude | trackLongitude | firstName | lastName | email         |
      | 9628a8bb-0a44-4c31-af7d-a54ff16f080f | 1663524947968     | -86.14170333333335 | 42.406800000000004  | 188.4    | 123                  | 16.5          | 95                 | 3500      | 74    | 5.6              | 17.5         | Test Track | 42.4086       | -86.1374       | test      | tester   | test@test.com |
      | 9628a8bb-0a44-4c31-af7d-a54ff16f080f | 1663524948962     | 86.14162999999999  | -42.406816666666664 | 188.0    | 130                  | 15.0          | 98                 | 2500      | 79    | 7.0              | 14.9         | Test Track | 42.4086       | -86.1374       | test      | tester   | test@test.com |
    And the application will log the following messages:
      | level | message                                              |
      | INFO  | Beginning to parse file: testFile.txt                |
      | INFO  | File parsing completed for file: testFile.txt        |
      | INFO  | Session 9628a8bb-0a44-4c31-af7d-a54ff16f080f updated |

  Scenario: Update datalogs for an existing session when file contains unparseable session data
    Given a file with the following rows:
      | Device Time              | Longitude          | Latitude            | Altitude | Engine Coolant Temperature(°F) | Engine RPM(rpm) | Intake Air Temperature(°F) | Speed (OBD)(mph) | Throttle Position(Manifold)(%) | Turbo Boost & Vacuum Gauge(psi) | Air Fuel Ratio(Measured)(:1) |
      | 18-Sep-2022 14:15:47.968 | -86.14170333333335 | 42.406800000000004  | 188.4    | 95.9                           | 3500.35         | 123.8                      | 74.56            | 5.6                            | 16.5                            | 17.5                         |
      | 18-Sep-2022 14:15:48.962 | 86.14162999999999  | -42.406816666666664 | 188.0    | 98                             | 2500            | 130                        | 79               | 7                              | 15                              | 14.9                         |
      | Device Time              | abc                | def                 | ghi      | jkl                            | mno             | qpr                        | st               | uv                             | wx                              | yz                           |
    And the following datalogs exist:
      | sessionId                            | epochMilliseconds | longitude          | latitude            | altitude | intakeAirTemperature | boostPressure | coolantTemperature | engineRpm | speed | throttlePosition | airFuelRatio | trackName  | trackLatitude | trackLongitude | firstName | lastName | email         |
      | 9628a8bb-0a44-4c31-af7d-a54ff16f080f | 1663524947968     | -86.14170333333335 | 42.406800000000004  | 188.4    | 100                  | 16.5          | 95                 | 4500      | 74    | 5.6              | 14.7         | Test Track | 42.4086       | -86.1374       | test      | tester   | test@test.com |
      | 9628a8bb-0a44-4c31-af7d-a54ff16f080f | 1663524948962     | 86.14162999999999  | -42.406816666666664 | 188.0    | 130                  | 9.0           | 98                 | 1500      | 79    | 7.0              | 15.8         | Test Track | 42.4086       | -86.1374       | test      | tester   | test@test.com |
    And the user has a valid authorization token
    When the file is uploaded for a session with the following data and session id '9628a8bb-0a44-4c31-af7d-a54ff16f080f':
      | trackName  | longitude | latitude | firstName | lastName | email         |
      | Test Track | -86.1374  | 42.4086  | test      | tester   | test@test.com |
    Then the request response status is 'OK'
    And the following datalogs will exist:
      | sessionId                            | epochMilliseconds | longitude          | latitude            | altitude | intakeAirTemperature | boostPressure | coolantTemperature | engineRpm | speed | throttlePosition | airFuelRatio | trackName  | trackLatitude | trackLongitude | firstName | lastName | email         |
      | 9628a8bb-0a44-4c31-af7d-a54ff16f080f | 1663524947968     | -86.14170333333335 | 42.406800000000004  | 188.4    | 123                  | 16.5          | 95                 | 3500      | 74    | 5.6              | 17.5         | Test Track | 42.4086       | -86.1374       | test      | tester   | test@test.com |
      | 9628a8bb-0a44-4c31-af7d-a54ff16f080f | 1663524948962     | 86.14162999999999  | -42.406816666666664 | 188.0    | 130                  | 15.0          | 98                 | 2500      | 79    | 7.0              | 14.9         | Test Track | 42.4086       | -86.1374       | test      | tester   | test@test.com |
    And the application will log the following messages:
      | level | message                                                              |
      | INFO  | Beginning to parse file: testFile.txt                                |
      | ERROR | Unable to parse row: Device Time,abc,def,ghi,jkl,mno,qpr,st,uv,wx,yz |
      | INFO  | File parsing completed for file: testFile.txt                        |
      | INFO  | Session 9628a8bb-0a44-4c31-af7d-a54ff16f080f updated                 |

  Scenario: Only update datalogs that belong to the current user when updating a session
    Given a file with the following rows:
      | Device Time              | Longitude          | Latitude            | Altitude | Engine Coolant Temperature(°F) | Engine RPM(rpm) | Intake Air Temperature(°F) | Speed (OBD)(mph) | Throttle Position(Manifold)(%) | Turbo Boost & Vacuum Gauge(psi) | Air Fuel Ratio(Measured)(:1) |
      | 18-Sep-2022 14:15:47.968 | -86.14170333333335 | 42.406800000000004  | 188.4    | 95.9                           | 3500.35         | 123.8                      | 74.56            | 5.6                            | 16.5                            | 17.5                         |
      | 18-Sep-2022 14:15:48.962 | 86.14162999999999  | -42.406816666666664 | 188.0    | 98                             | 2500            | 130                        | 79               | 7                              | 15                              | 14.9                         |
    And the following datalogs exist:
      | sessionId                            | epochMilliseconds | longitude          | latitude            | altitude | intakeAirTemperature | boostPressure | coolantTemperature | engineRpm | speed | throttlePosition | airFuelRatio | trackName  | trackLatitude | trackLongitude | firstName | lastName | email          |
      | d864a09b-a349-4ca7-bd69-f71ae1ba2371 | 1663524947968     | -86.14170333333335 | 42.406800000000004  | 188.4    | 100                  | 16.5          | 95                 | 4500      | 74    | 5.6              | 14.7         | Test Track | 42.4086       | -86.1374       | test      | tester   | test@test.com  |
      | d864a09b-a349-4ca7-bd69-f71ae1ba2371 | 1663524948962     | 86.14162999999999  | -42.406816666666664 | 188.0    | 130                  | 9.0           | 98                 | 1500      | 79    | 7.0              | 15.8         | Test Track | 42.4086       | -86.1374       | test      | tester   | test@test.com  |
      | 9628a8bb-0a44-4c31-af7d-a54ff16f080f | 1663524948962     | 86.14162999999999  | -42.406816666666664 | 188.0    | 130                  | 9.0           | 98                 | 1500      | 79    | 7.0              | 15.8         | Test Track | 42.4086       | -86.1374       | test2     | tester   | test2@test.com |
    And the user has a valid authorization token
    When the file is uploaded for a session with the following data and session id 'd864a09b-a349-4ca7-bd69-f71ae1ba2371':
      | trackName  | longitude | latitude | firstName | lastName | email         |
      | Test Track | -86.1374  | 42.4086  | test      | tester   | test@test.com |
    Then the request response status is 'OK'
    And the following datalogs will exist:
      | sessionId                            | epochMilliseconds | longitude          | latitude            | altitude | intakeAirTemperature | boostPressure | coolantTemperature | engineRpm | speed | throttlePosition | airFuelRatio | trackName  | trackLatitude | trackLongitude | firstName | lastName | email          |
      | d864a09b-a349-4ca7-bd69-f71ae1ba2371 | 1663524947968     | -86.14170333333335 | 42.406800000000004  | 188.4    | 123                  | 16.5          | 95                 | 3500      | 74    | 5.6              | 17.5         | Test Track | 42.4086       | -86.1374       | test      | tester   | test@test.com  |
      | d864a09b-a349-4ca7-bd69-f71ae1ba2371 | 1663524948962     | 86.14162999999999  | -42.406816666666664 | 188.0    | 130                  | 15.0          | 98                 | 2500      | 79    | 7.0              | 14.9         | Test Track | 42.4086       | -86.1374       | test      | tester   | test@test.com  |
      | 9628a8bb-0a44-4c31-af7d-a54ff16f080f | 1663524948962     | 86.14162999999999  | -42.406816666666664 | 188.0    | 130                  | 9.0           | 98                 | 1500      | 79    | 7.0              | 15.8         | Test Track | 42.4086       | -86.1374       | test2     | tester   | test2@test.com |
    And the application will log the following messages:
      | level | message                                              |
      | INFO  | Beginning to parse file: testFile.txt                |
      | INFO  | File parsing completed for file: testFile.txt        |
      | INFO  | Session d864a09b-a349-4ca7-bd69-f71ae1ba2371 updated |

  Scenario: A session that does not exist cannot be updated
    Given a file with the following rows:
      | Device Time              | Longitude          | Latitude            | Altitude | Engine Coolant Temperature(°F) | Engine RPM(rpm) | Intake Air Temperature(°F) | Speed (OBD)(mph) | Throttle Position(Manifold)(%) | Turbo Boost & Vacuum Gauge(psi) | Air Fuel Ratio(Measured)(:1) |
      | 18-Sep-2022 14:15:47.968 | -86.14170333333335 | 42.406800000000004  | 188.4    | 95.9                           | 3500.35         | 123.8                      | 74.56            | 5.6                            | 16.5                            | 17.5                         |
      | 18-Sep-2022 14:15:48.962 | 86.14162999999999  | -42.406816666666664 | 188.0    | 98                             | 2500            | 130                        | 79               | 7                              | 15                              | 14.9                         |
    And the following datalogs exist:
      | sessionId                            | epochMilliseconds | longitude          | latitude            | altitude | intakeAirTemperature | boostPressure | coolantTemperature | engineRpm | speed | throttlePosition | airFuelRatio | trackName  | trackLatitude | trackLongitude | firstName | lastName | email         |
      | 9628a8bb-0a44-4c31-af7d-a54ff16f080f | 1663524947968     | -86.14170333333335 | 42.406800000000004  | 188.4    | 100                  | 16.5          | 95                 | 4500      | 74    | 5.6              | 14.7         | Test Track | 42.4086       | -86.1374       | test      | tester   | test@test.com |
      | 9628a8bb-0a44-4c31-af7d-a54ff16f080f | 1663524948962     | 86.14162999999999  | -42.406816666666664 | 188.0    | 130                  | 9.0           | 98                 | 1500      | 79    | 7.0              | 15.8         | Test Track | 42.4086       | -86.1374       | test      | tester   | test@test.com |
    And the user has a valid authorization token
    When the file is uploaded for a session with the following data and session id 'b3200382-11f1-4243-aff6-e84018f37564':
      | trackName  | longitude | latitude | firstName | lastName | email         |
      | Test Track | -86.1374  | 42.4086  | test      | tester   | test@test.com |
    Then the request response status is 'GONE'
    And the following datalogs will exist:
      | sessionId                            | epochMilliseconds | longitude          | latitude            | altitude | intakeAirTemperature | boostPressure | coolantTemperature | engineRpm | speed | throttlePosition | airFuelRatio | trackName  | trackLatitude | trackLongitude | firstName | lastName | email         |
      | 9628a8bb-0a44-4c31-af7d-a54ff16f080f | 1663524947968     | -86.14170333333335 | 42.406800000000004  | 188.4    | 100                  | 16.5          | 95                 | 4500      | 74    | 5.6              | 14.7         | Test Track | 42.4086       | -86.1374       | test      | tester   | test@test.com |
      | 9628a8bb-0a44-4c31-af7d-a54ff16f080f | 1663524948962     | 86.14162999999999  | -42.406816666666664 | 188.0    | 130                  | 9.0           | 98                 | 1500      | 79    | 7.0              | 15.8         | Test Track | 42.4086       | -86.1374       | test      | tester   | test@test.com |
    And the application will log the following messages:
      | level | message                                                        |
      | ERROR | Session id b3200382-11f1-4243-aff6-e84018f37564 does not exist |

  Scenario: A session cannot be updated with an invalid token
    Given a file with the following rows:
      | Device Time              | Longitude          | Latitude            | Altitude | Engine Coolant Temperature(°F) | Engine RPM(rpm) | Intake Air Temperature(°F) | Speed (OBD)(mph) | Throttle Position(Manifold)(%) | Turbo Boost & Vacuum Gauge(psi) | Air Fuel Ratio(Measured)(:1) |
      | 18-Sep-2022 14:15:47.968 | -86.14170333333335 | 42.406800000000004  | 188.4    | 95.9                           | 3500.35         | 123.8                      | 74.56            | 5.6                            | 16.5                            | 17.5                         |
      | 18-Sep-2022 14:15:48.962 | 86.14162999999999  | -42.406816666666664 | 188.0    | 98                             | 2500            | 130                        | 79               | 7                              | 15                              | 14.9                         |
    And the following datalogs exist:
      | sessionId                            | epochMilliseconds | longitude          | latitude            | altitude | intakeAirTemperature | boostPressure | coolantTemperature | engineRpm | speed | throttlePosition | airFuelRatio | trackName  | trackLatitude | trackLongitude | firstName | lastName | email         |
      | 9628a8bb-0a44-4c31-af7d-a54ff16f080f | 1663524947968     | -86.14170333333335 | 42.406800000000004  | 188.4    | 100                  | 16.5          | 95                 | 4500      | 74    | 5.6              | 14.7         | Test Track | 42.4086       | -86.1374       | test      | tester   | test@test.com |
      | 9628a8bb-0a44-4c31-af7d-a54ff16f080f | 1663524948962     | 86.14162999999999  | -42.406816666666664 | 188.0    | 130                  | 9.0           | 98                 | 1500      | 79    | 7.0              | 15.8         | Test Track | 42.4086       | -86.1374       | test      | tester   | test@test.com |
    And the user has an invalid authorization token
    When the file is uploaded for a session with the following data and session id '9628a8bb-0a44-4c31-af7d-a54ff16f080f':
      | trackName  | longitude | latitude | firstName | lastName | email         |
      | Test Track | -86.1374  | 42.4086  | test      | tester   | test@test.com |
    Then the request response status is 'UNAUTHORIZED'
    And the following datalogs will exist:
      | sessionId                            | epochMilliseconds | longitude          | latitude            | altitude | intakeAirTemperature | boostPressure | coolantTemperature | engineRpm | speed | throttlePosition | airFuelRatio | trackName  | trackLatitude | trackLongitude | firstName | lastName | email         |
      | 9628a8bb-0a44-4c31-af7d-a54ff16f080f | 1663524947968     | -86.14170333333335 | 42.406800000000004  | 188.4    | 100                  | 16.5          | 95                 | 4500      | 74    | 5.6              | 14.7         | Test Track | 42.4086       | -86.1374       | test      | tester   | test@test.com |
      | 9628a8bb-0a44-4c31-af7d-a54ff16f080f | 1663524948962     | 86.14162999999999  | -42.406816666666664 | 188.0    | 130                  | 9.0           | 98                 | 1500      | 79    | 7.0              | 15.8         | Test Track | 42.4086       | -86.1374       | test      | tester   | test@test.com |
    And the application will log the following messages:
      | level | message |
