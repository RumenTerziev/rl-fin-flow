Business Need: Currency convertor

  Scenario: Successfully convert some currency
    Given 20 BGN to convert
    And the result currency is EUR
    When I make a request to convert them
    Then the result is correct and status code is 200