@users
Feature: Find users by first name

  Background:
    Given the following users exist
      | firstName | lastName | username      |
      | trusting  | tesla    | trustingTesla |
      | insane    | boyd     | insaneBoyd    |
      | trusting  | knuth    | trustingKnuth |

  Scenario: first name is empty
    When a request is made to find all users with first name ''
    Then the request response status is 'OK'
    And the following users are returned
      | firstName | lastName | username |

  Scenario: first name is not provided
    When a request is made with no first name parameter
    Then the request response status is 'BAD_REQUEST'

  Scenario: first name is valid
    When a request is made to find all users with first name 'trusting'
    Then the request response status is 'OK'
    And the following users are returned
      | id | firstName | lastName | fullName       |
      | 1  | trusting  | tesla    | trusting tesla |
      | 3  | trusting  | knuth    | trusting knuth |