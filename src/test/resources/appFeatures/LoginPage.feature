Feature: Login page feature

  Scenario: Login with correct credentials
    Given user is on login page
    When user gets the title of the page
    When user enters username "justintkurian1010@gmail.com"
    And user enters password "Automation123"
    And user clicks on Login button
    Then user gets the title of the page
    And page title should be "My account"