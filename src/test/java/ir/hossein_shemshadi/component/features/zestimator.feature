Feature: Enrich with zestimate

  Scenario Outline: Enrich and put in excel file
    Given we have a property in <city> city
    And it's located in <state> state
    And it's zip-code is <zip_code>
    And it's full address is <address>
    When we put this property in our zestimator application
    Then we can find the zestimate in the resulted excel file
    Examples:
      | city             | state | zip_code | address                        |
      | Garden City      | UT    | 84028    | 861 N Blackberry Dr            |
      | LAHAINA          | HI    | 96761    | 2560 Kekaa Dr UNIT A201        |
      | Sevierville      | TN    | 37876    | 1092 Towering Oaks Dr542712.00 |
      | SAINT AUGUSTINE  | FL    | 32080    | 200 Ocean Hibiscus Dr UNIT 103 |
      | Lago Vista       | TX    | 78645    | 3404 American Dr               |
      | PARK CITY        | UT    | 84060    | 1484 Woodside Ave              |
      | SANTA ROSA BEACH | FL    | 32459    | 145 Beachfront Trl UNIT 106A   |