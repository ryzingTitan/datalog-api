Feature: Retrieve metadata for all sessions

  Scenario: Retrieve metadata for a session with a single record
    Given the following datalog records exist:
      | sessionId                            | timestamp                | longitude          | latitude           | altitude |
      | c61cc339-f93d-45a4-aa2b-923f0482b97f | 2022-09-18T14:15:47.968Z | -86.14170333333335 | 42.406800000000004 | 188.4    |
    When the metadata for the sessions is retrieved
    Then the request response status is 'OK'
    And the following session metadata is returned:
      | sessionId                            | startTime                | endTime                  | longitude          | latitude           | altitude |
      | c61cc339-f93d-45a4-aa2b-923f0482b97f | 2022-09-18T14:15:47.968Z | 2022-09-18T14:15:47.968Z | -86.14170333333335 | 42.406800000000004 | 188.4    |

  Scenario: Retrieve metadata for a session with multiple records
    Given the following datalog records exist:
      | sessionId                            | timestamp                | longitude          | latitude           | altitude |
      | c61cc339-f93d-45a4-aa2b-923f0482b97f | 2022-09-18T14:15:47.968Z | -86.14170333333335 | 42.406800000000004 | 188.4    |
      | c61cc339-f93d-45a4-aa2b-923f0482b97f | 2022-09-18T14:17:48.962Z | -86.14170333333335 | 42.406800000000004 | 188.4    |
      | c61cc339-f93d-45a4-aa2b-923f0482b97f | 2022-09-18T14:15:49.968Z | -86.14170333333335 | 42.406800000000004 | 188.4    |
    When the metadata for the sessions is retrieved
    Then the request response status is 'OK'
    And the following session metadata is returned:
      | sessionId                            | startTime                | endTime                  |
      | c61cc339-f93d-45a4-aa2b-923f0482b97f | 2022-09-18T14:15:47.968Z | 2022-09-18T14:17:48.962Z |

  Scenario: Retrieve metadata for multiple session with multiple records
    Given the following datalog records exist:
      | sessionId                            | timestamp                | longitude          | latitude           | altitude |
      | c61cc339-f93d-45a4-aa2b-923f0482b97f | 2022-09-18T14:15:47.968Z | -86.14170333333335 | 42.406800000000004 | 188.4    |
      | 28278c48-3d4f-496d-8c38-62a2e2745455 | 2022-09-18T14:15:47.965Z | -86.14170333333335 | 42.406800000000004 | 188.4    |
      | c61cc339-f93d-45a4-aa2b-923f0482b97f | 2022-09-18T14:17:48.962Z | -86.14170333333335 | 42.406800000000004 | 188.4    |
      | c61cc339-f93d-45a4-aa2b-923f0482b97f | 2022-09-18T14:15:49.968Z | -86.14170333333335 | 42.406800000000004 | 188.4    |
      | 28278c48-3d4f-496d-8c38-62a2e2745455 | 2022-09-18T14:15:49.968Z | -86.14170333333335 | 42.406800000000004 | 188.4    |
    When the metadata for the sessions is retrieved
    Then the request response status is 'OK'
    And the following session metadata is returned:
      | sessionId                            | startTime                | endTime                  |
      | c61cc339-f93d-45a4-aa2b-923f0482b97f | 2022-09-18T14:15:47.968Z | 2022-09-18T14:17:48.962Z |
      | 28278c48-3d4f-496d-8c38-62a2e2745455 | 2022-09-18T14:15:47.965Z | 2022-09-18T14:15:49.968Z |

