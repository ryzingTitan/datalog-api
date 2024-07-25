Feature: Delete an existing track

  Scenario: Delete an existing track
    Given the following tracks exist:
      | name         | longitude | latitude |
      | Test Track 1 | -86.1374  | 42.4086  |
      | Test Track 2 | -90.1374  | 45.4086  |
    And the user has a valid authorization token
    When the track with id 2 is deleted
    Then the request response status is 'OK'
    And the following tracks will exist:
      | id | name         | longitude | latitude |
      | 1  | Test Track 1 | -86.1374  | 42.4086  |
    And the application will log the following messages:
      | level | message                 |
      | INFO  | Deleted track with id 2 |

  Scenario: Deleting a track that does not exist will not throw an error
    Given the following tracks exist:
      | name         | longitude | latitude |
      | Test Track 2 | -86.1374  | 42.4086  |
    And the user has a valid authorization token
    When the track with id 2 is deleted
    Then the request response status is 'OK'
    And the following tracks will exist:
      | id | name         | longitude | latitude |
      | 1  | Test Track 2 | -86.1374  | 42.4086  |
    And the application will log the following messages:
      | level | message                 |
      | INFO  | Deleted track with id 2 |

  Scenario: A track cannot be deleted with an invalid token
    Given the following tracks exist:
      | name         | longitude | latitude |
      | Test Track 1 | -86.1374  | 42.4086  |
      | Test Track 2 | -90.1374  | 45.4086  |
    And the user has an invalid authorization token
    When the track with id 2 is deleted
    Then the request response status is 'UNAUTHORIZED'
    And the following tracks will exist:
      | id | name         | longitude | latitude |
      | 1  | Test Track 1 | -86.1374  | 42.4086  |
      | 2  | Test Track 2 | -90.1374  | 45.4086  |
    And the application will log the following messages:
      | level | message |