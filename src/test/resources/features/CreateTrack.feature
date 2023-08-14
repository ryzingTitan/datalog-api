Feature: Create a new track

  Scenario: Create a new track
    Given the user has a valid authorization token
    When the following track is created:
      | id | name         | longitude | latitude |
      |    | Test Track 1 | -86.1374  | 42.4086  |
    Then the request response status is 'CREATED'
    And the location header will end with '/api/tracks/c61cc339-f93d-45a4-aa2b-923f0482b97f'
    And the following tracks will exist:
      | trackId                              | name         | longitude | latitude |
      | c61cc339-f93d-45a4-aa2b-923f0482b97f | Test Track 1 | -86.1374  | 42.4086  |
    And the application will log the following messages:
      | level | message                          |
      | INFO  | Created track named Test Track 1 |

  Scenario: Do not create duplicate tracks
    Given the following tracks exist:
      | trackId                              | name         | longitude | latitude |
      | 5507dfa1-98ce-4ad0-8a93-874aba2c1c46 | Test Track 2 | -90.1374  | 45.4086  |
    And the user has a valid authorization token
    When the following track is created:
      | id | name         | longitude | latitude |
      |    | Test Track 2 | -86.1374  | 42.4086  |
    Then the request response status is 'CONFLICT'
    And the following tracks will exist:
      | trackId                              | name         | longitude | latitude |
      | 5507dfa1-98ce-4ad0-8a93-874aba2c1c46 | Test Track 2 | -90.1374  | 45.4086  |
    And the application will log the following messages:
      | level | message                                   |
      | ERROR | A track already exists named Test Track 2 |

  Scenario: A new track cannot be created with an invalid token
    Given the user has an invalid authorization token
    When the following track is created:
      | id | name         | longitude | latitude |
      |    | Test Track 1 | -86.1374  | 42.4086  |
    Then the request response status is 'UNAUTHORIZED'
    And the following tracks will exist:
      | sessionId | timestamp | longitude | latitude | altitude | intakeAirTemperature | boostPressure | coolantTemperature | engineRpm | speed | throttlePosition | airFuelRatio | trackName | trackLatitude | trackLongitude | firstName | lastName | email |
    And the application will log the following messages:
      | level | message |