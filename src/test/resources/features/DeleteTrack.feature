Feature: Delete an existing track

  Scenario: Delete an existing track
    Given the following tracks exist:
      | trackId                              | name         | longitude | latitude |
      | cdafd38c-675a-4807-bf68-88af88507214 | Test Track 1 | -86.1374  | 42.4086  |
      | 73b9f345-1cd7-4518-82c6-1899998005db | Test Track 2 | -90.1374  | 45.4086  |
    And the user has a valid authorization token
    When the track with id '73b9f345-1cd7-4518-82c6-1899998005db' is deleted
    Then the request response status is 'OK'
    And the following tracks will exist:
      | trackId                              | name         | longitude | latitude |
      | cdafd38c-675a-4807-bf68-88af88507214 | Test Track 1 | -86.1374  | 42.4086  |
    And the application will log the following messages:
      | level | message                                                    |
      | INFO  | Deleted track with id 73b9f345-1cd7-4518-82c6-1899998005db |

  Scenario: Deleting a track that does not exist will not throw an error
    Given the following tracks exist:
      | trackId                              | name         | longitude | latitude |
      | d864a09b-a349-4ca7-bd69-f71ae1ba2371 | Test Track 2 | -86.1374  | 42.4086  |
    And the user has a valid authorization token
    When the track with id '73b9f345-1cd7-4518-82c6-1899998005db' is deleted
    Then the request response status is 'OK'
    And the following tracks will exist:
      | trackId                              | name         | longitude | latitude |
      | d864a09b-a349-4ca7-bd69-f71ae1ba2371 | Test Track 2 | -86.1374  | 42.4086  |
    And the application will log the following messages:
      | level | message                                                    |
      | INFO  | Deleted track with id 73b9f345-1cd7-4518-82c6-1899998005db |

  Scenario: A track cannot be deleted with an invalid token
    Given the following tracks exist:
      | trackId                              | name         | longitude | latitude |
      | 29d447e6-7e01-402b-974a-3aa951ca37c6 | Test Track 1 | -86.1374  | 42.4086  |
      | 73b9f345-1cd7-4518-82c6-1899998005db | Test Track 2 | -90.1374  | 45.4086  |
    And the user has an invalid authorization token
    When the track with id '73b9f345-1cd7-4518-82c6-1899998005db' is deleted
    Then the request response status is 'UNAUTHORIZED'
    And the following tracks will exist:
      | trackId                              | name         | longitude | latitude |
      | 29d447e6-7e01-402b-974a-3aa951ca37c6 | Test Track 1 | -86.1374  | 42.4086  |
      | 73b9f345-1cd7-4518-82c6-1899998005db | Test Track 2 | -90.1374  | 45.4086  |
    And the application will log the following messages:
      | level | message |