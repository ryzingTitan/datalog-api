Feature: Retrieve metadata for all sessions

  Scenario: Retrieve metadata for a session with a single record
    Given the following datalog records exist:
      | sessionId                            | timestamp                | intakeAirTemperature |
      | c61cc339-f93d-45a4-aa2b-923f0482b97f | 2022-09-18T14:15:47.968Z | 123.8                |
    When the metadata for the sessions is retrieved
    Then the request response status is 'OK'
    And the following session metadata is returned:
      | sessionId                            | startTime                | endTime                  |
      | c61cc339-f93d-45a4-aa2b-923f0482b97f | 2022-09-18T14:15:47.968Z | 2022-09-18T14:15:47.968Z |

  Scenario: Retrieve metadata for a session with multiple records
    Given the following datalog records exist:
      | sessionId                            | timestamp                | intakeAirTemperature |
      | c61cc339-f93d-45a4-aa2b-923f0482b97f | 2022-09-18T14:15:47.968Z | 123.8                |
      | c61cc339-f93d-45a4-aa2b-923f0482b97f | 2022-09-18T14:17:48.962Z | 130.0                |
      | c61cc339-f93d-45a4-aa2b-923f0482b97f | 2022-09-18T14:15:49.968Z | 135.0                |
    When the metadata for the sessions is retrieved
    Then the request response status is 'OK'
    And the following session metadata is returned:
      | sessionId                            | startTime                | endTime                  |
      | c61cc339-f93d-45a4-aa2b-923f0482b97f | 2022-09-18T14:15:47.968Z | 2022-09-18T14:17:48.962Z |

  Scenario: Retrieve metadata for multiple session with multiple records
    Given the following datalog records exist:
      | sessionId                            | timestamp                | intakeAirTemperature |
      | c61cc339-f93d-45a4-aa2b-923f0482b97f | 2022-09-18T14:15:47.968Z | 123.8                |
      | 28278c48-3d4f-496d-8c38-62a2e2745455 | 2022-09-18T14:15:47.968Z | 123.8                |
      | c61cc339-f93d-45a4-aa2b-923f0482b97f | 2022-09-18T14:17:48.962Z | 130.0                |
      | c61cc339-f93d-45a4-aa2b-923f0482b97f | 2022-09-18T14:15:49.968Z | 135.0                |
      | 28278c48-3d4f-496d-8c38-62a2e2745455 | 2022-09-18T14:15:49.968Z | 135.0                |
    When the metadata for the sessions is retrieved
    Then the request response status is 'OK'
    And the following session metadata is returned:
      | sessionId                            | startTime                | endTime                  |
      | c61cc339-f93d-45a4-aa2b-923f0482b97f | 2022-09-18T14:15:47.968Z | 2022-09-18T14:17:48.962Z |
      | 28278c48-3d4f-496d-8c38-62a2e2745455 | 2022-09-18T14:15:47.968Z | 2022-09-18T14:15:49.968Z |

