Feature: Update an existing track

  Scenario: Update an existing track
    Given the following tracks exist:
      | trackId                              | name         | longitude | latitude |
      | cdafd38c-675a-4807-bf68-88af88507214 | Test Track 1 | -86.1374  | 42.4086  |
    And the user has a valid authorization token
    When the following track is updated:
      | id                                   | name         | longitude | latitude |
      | cdafd38c-675a-4807-bf68-88af88507214 | Test Track 1 | -89.1374  | 45.4086  |
    Then the request response status is 'OK'
    And the following tracks will exist:
      | trackId                              | name         | longitude | latitude |
      | cdafd38c-675a-4807-bf68-88af88507214 | Test Track 1 | -89.1374  | 45.4086  |
    And the application will log the following messages:
      | level | message                          |
      | INFO  | Updated track named Test Track 1 |

  Scenario: A track that does not exist cannot be updated
    Given the following tracks exist:
      | trackId                              | name         | longitude | latitude |
      | d864a09b-a349-4ca7-bd69-f71ae1ba2371 | Test Track 2 | -86.1374  | 42.4086  |
    And the user has a valid authorization token
    When the following track is updated:
      | id                                   | name         | longitude | latitude |
      | 29d447e6-7e01-402b-974a-3aa951ca37c6 | Test Track 1 | -89.1374  | 45.4086  |
    Then the request response status is 'GONE'
    And the following tracks will exist:
      | trackId                              | name         | longitude | latitude |
      | d864a09b-a349-4ca7-bd69-f71ae1ba2371 | Test Track 2 | -86.1374  | 42.4086  |
    And the application will log the following messages:
      | level | message                                   |
      | ERROR | A track named Test Track 1 does not exist |

  Scenario: A session cannot be updated with an invalid token
    Given the following tracks exist:
      | trackId                              | name         | longitude | latitude |
      | 29d447e6-7e01-402b-974a-3aa951ca37c6 | Test Track 1 | -86.1374  | 42.4086  |
    And the user has an invalid authorization token
    When the following track is updated:
      | id                                   | name         | longitude | latitude |
      | 29d447e6-7e01-402b-974a-3aa951ca37c6 | Test Track 1 | -89.1374  | 45.4086  |
    Then the request response status is 'UNAUTHORIZED'
    And the following tracks will exist:
      | trackId                              | name         | longitude | latitude |
      | 29d447e6-7e01-402b-974a-3aa951ca37c6 | Test Track 1 | -86.1374  | 42.4086  |
    And the application will log the following messages:
      | level | message |
