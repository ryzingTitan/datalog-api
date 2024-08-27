Feature: Retrieve all tracks

  Scenario: Retrieve all tracks
    Given the following tracks exist:
      | name         | longitude | latitude |
      | Test Track 1 | -86.1374  | 42.4086  |
      | Test Track 2 | -90.1374  | 45.4086  |
    And the user has a valid authorization token
    When all tracks are retrieved
    Then the request response status is 'OK'
    And the following tracks are returned:
      | id | name         | longitude | latitude |
      | 1  | Test Track 1 | -86.1374  | 42.4086  |
      | 2  | Test Track 2 | -90.1374  | 45.4086  |
    And the application will log the following messages:
      | level | message               |
      | INFO  | Retrieving all tracks |

  Scenario: Tracks cannot be retrieved with an invalid authorization token
    Given the following tracks exist:
      | name         | longitude | latitude |
      | Test Track 1 | -86.1374  | 42.4086  |
      | Test Track 2 | -90.1374  | 45.4086  |
    And the user has an invalid authorization token
    When all tracks are retrieved
    Then the request response status is 'UNAUTHORIZED'
    And the following tracks are returned:
      | id | name | longitude | latitude |
    And the application will log the following messages:
      | level | message |