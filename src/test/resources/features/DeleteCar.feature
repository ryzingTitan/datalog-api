Feature: Delete an existing car

  Scenario: Delete an existing car
    Given the following cars exist:
      | yearManufactured | make       | model    |
      | 2001             | Volkswagen | Jetta    |
      | 1999             | Chevrolet  | Corvette |
    And the user has a valid authorization token
    When the car with id 2 is deleted
    Then the request response status is 'OK'
    And the following cars will exist:
      | id | yearManufactured | make       | model |
      | 1  | 2001             | Volkswagen | Jetta |
    And the application will log the following messages:
      | level | message               |
      | INFO  | Deleted car with id 2 |

  Scenario: Deleting a car that does not exist will not throw an error
    Given the following cars exist:
      | yearManufactured | make       | model |
      | 2001             | Volkswagen | Jetta |
    And the user has a valid authorization token
    When the car with id 2 is deleted
    Then the request response status is 'OK'
    And the following cars will exist:
      | id | yearManufactured | make       | model |
      | 1  | 2001             | Volkswagen | Jetta |
    And the application will log the following messages:
      | level | message               |
      | INFO  | Deleted car with id 2 |

  Scenario: A car cannot be deleted with an invalid token
    Given the following cars exist:
      | yearManufactured | make       | model    |
      | 2001             | Volkswagen | Jetta    |
      | 1999             | Chevrolet  | Corvette |
    And the user has an invalid authorization token
    When the car with id 2 is deleted
    Then the request response status is 'UNAUTHORIZED'
    And the following cars will exist:
      | id | yearManufactured | make       | model    |
      | 1  | 2001             | Volkswagen | Jetta    |
      | 2  | 1999             | Chevrolet  | Corvette |
    And the application will log the following messages:
      | level | message |