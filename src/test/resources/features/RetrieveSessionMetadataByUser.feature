Feature: Retrieve metadata for all sessions by user

  Scenario: Retrieve metadata for a user session with a single record
    Given the following datalogs exist:
      | sessionId                            | epochMilliseconds | longitude          | latitude           | altitude | trackName  | trackLatitude | trackLongitude | firstName | lastName | email         |
      | c61cc339-f93d-45a4-aa2b-923f0482b97f | 1663510547968     | -86.14170333333335 | 42.406800000000004 | 188.4    | Test Track | 42.4086       | -86.1374       | test      | tester   | test@test.com |
    And the user has a valid authorization token
    When the metadata for the sessions is retrieved for user 'test@test.com'
    Then the request response status is 'OK'
    And the following session metadata is returned:
      | sessionId                            | startTime                | endTime                  |
      | c61cc339-f93d-45a4-aa2b-923f0482b97f | 2022-09-18T14:15:47.968Z | 2022-09-18T14:15:47.968Z |
    And the application will log the following messages:
      | level | message                                                      |
      | INFO  | Retrieving metadata for all sessions for user: test@test.com |

  Scenario: Retrieve metadata for a user session with multiple records
    Given the following datalogs exist:
      | sessionId                            | epochMilliseconds | longitude          | latitude           | altitude | trackName  | trackLatitude | trackLongitude | firstName | lastName | email         |
      | c61cc339-f93d-45a4-aa2b-923f0482b97f | 1663510547968     | -86.14170333333335 | 42.406800000000004 | 188.4    | Test Track | 42.4086       | -86.1374       | test      | tester   | test@test.com |
      | c61cc339-f93d-45a4-aa2b-923f0482b97f | 1663510668962     | -86.14170333333335 | 42.406800000000004 | 188.4    | Test Track | 42.4086       | -86.1374       | test      | tester   | test@test.com |
      | c61cc339-f93d-45a4-aa2b-923f0482b97f | 1663510549968     | -86.14170333333335 | 42.406800000000004 | 188.4    | Test Track | 42.4086       | -86.1374       | test      | tester   | test@test.com |
    And the user has a valid authorization token
    When the metadata for the sessions is retrieved for user 'test@test.com'
    Then the request response status is 'OK'
    And the following session metadata is returned:
      | sessionId                            | startTime                | endTime                  |
      | c61cc339-f93d-45a4-aa2b-923f0482b97f | 2022-09-18T14:15:47.968Z | 2022-09-18T14:17:48.962Z |
    And the application will log the following messages:
      | level | message                                                      |
      | INFO  | Retrieving metadata for all sessions for user: test@test.com |

  Scenario: Retrieve metadata for multiple user sessions with multiple records
    Given the following datalogs exist:
      | sessionId                            | epochMilliseconds | longitude          | latitude           | altitude | trackName  | trackLatitude | trackLongitude | firstName | lastName | email         |
      | c61cc339-f93d-45a4-aa2b-923f0482b97f | 1663510547968     | -86.14170333333335 | 42.406800000000004 | 188.4    | Test Track | 42.4086       | -86.1374       | test      | tester   | test@test.com |
      | 28278c48-3d4f-496d-8c38-62a2e2745455 | 1663510547965     | -86.14170333333335 | 42.406800000000004 | 188.4    | Test Track | 42.4086       | -86.1374       | test      | tester   | test@test.com |
      | c61cc339-f93d-45a4-aa2b-923f0482b97f | 1663510668962     | -86.14170333333335 | 42.406800000000004 | 188.4    | Test Track | 42.4086       | -86.1374       | test      | tester   | test@test.com |
      | c61cc339-f93d-45a4-aa2b-923f0482b97f | 1663510549968     | -86.14170333333335 | 42.406800000000004 | 188.4    | Test Track | 42.4086       | -86.1374       | test      | tester   | test@test.com |
      | 28278c48-3d4f-496d-8c38-62a2e2745455 | 1663510549968     | -86.14170333333335 | 42.406800000000004 | 188.4    | Test Track | 42.4086       | -86.1374       | test      | tester   | test@test.com |
    And the user has a valid authorization token
    When the metadata for the sessions is retrieved for user 'test@test.com'
    Then the request response status is 'OK'
    And the following session metadata is returned:
      | sessionId                            | startTime                | endTime                  |
      | c61cc339-f93d-45a4-aa2b-923f0482b97f | 2022-09-18T14:15:47.968Z | 2022-09-18T14:17:48.962Z |
      | 28278c48-3d4f-496d-8c38-62a2e2745455 | 2022-09-18T14:15:47.965Z | 2022-09-18T14:15:49.968Z |
    And the application will log the following messages:
      | level | message                                                      |
      | INFO  | Retrieving metadata for all sessions for user: test@test.com |

  Scenario: Retrieve session metadata for active user
    Given the following datalogs exist:
      | sessionId                            | epochMilliseconds | longitude          | latitude           | altitude | trackName  | trackLatitude | trackLongitude | firstName | lastName | email          |
      | c61cc339-f93d-45a4-aa2b-923f0482b97f | 1663510547968     | -86.14170333333335 | 42.406800000000004 | 188.4    | Test Track | 42.4086       | -86.1374       | test      | tester   | test@test.com  |
      | 28278c48-3d4f-496d-8c38-62a2e2745455 | 1663510547965     | -86.14170333333335 | 42.406800000000004 | 188.4    | Test Track | 42.4086       | -86.1374       | test      | tester   | test@test.com  |
      | c61cc339-f93d-45a4-aa2b-923f0482b97f | 1663510668962     | -86.14170333333335 | 42.406800000000004 | 188.4    | Test Track | 42.4086       | -86.1374       | test      | tester   | test@test.com  |
      | c61cc339-f93d-45a4-aa2b-923f0482b97f | 1663510549968     | -86.14170333333335 | 42.406800000000004 | 188.4    | Test Track | 42.4086       | -86.1374       | test      | tester   | test@test.com  |
      | 28278c48-3d4f-496d-8c38-62a2e2745455 | 1663510549968     | -86.14170333333335 | 42.406800000000004 | 188.4    | Test Track | 42.4086       | -86.1374       | test      | tester   | test@test.com  |
      | 3af1a751-76f0-4fcc-ac04-a94e92dcecff | 1663510549978     | -86.14170333333335 | 42.406800000000004 | 188.4    | Test Track | 42.4086       | -86.1374       | another   | tester   | test2@test.com |
    And the user has a valid authorization token
    When the metadata for the sessions is retrieved for user 'test@test.com'
    Then the request response status is 'OK'
    And the following session metadata is returned:
      | sessionId                            | startTime                | endTime                  |
      | c61cc339-f93d-45a4-aa2b-923f0482b97f | 2022-09-18T14:15:47.968Z | 2022-09-18T14:17:48.962Z |
      | 28278c48-3d4f-496d-8c38-62a2e2745455 | 2022-09-18T14:15:47.965Z | 2022-09-18T14:15:49.968Z |
    And the application will log the following messages:
      | level | message                                                      |
      | INFO  | Retrieving metadata for all sessions for user: test@test.com |

  Scenario: Session metadata cannot be read with an invalid authorization token
    Given the following datalogs exist:
      | sessionId                            | epochMilliseconds | longitude          | latitude           | altitude | trackName  | trackLatitude | trackLongitude | firstName | lastName | email         |
      | c61cc339-f93d-45a4-aa2b-923f0482b97f | 1663510547968     | -86.14170333333335 | 42.406800000000004 | 188.4    | Test Track | 42.4086       | -86.1374       | test      | tester   | test@test.com |
    And the user has an invalid authorization token
    When the metadata for the sessions is retrieved for user 'test@test.com'
    Then the request response status is 'UNAUTHORIZED'
    And the following session metadata is returned:
      | sessionId | startTime | endTime |
    And the application will log the following messages:
      | level | message |