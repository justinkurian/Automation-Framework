Feature: Verify Operational Site Count in WWIN Application
  #@loginscenario
  #Scenario: Verify operational site count in summary page matches with site DB
  #  Given I am logged into the WWIN application with valid credentials
  #  Then verify operational site count against database on the summary page

  @activeEvent
  Scenario: Verify active event tracker page
    Given user is logged into the WWIN application with valid credentials
    When user selects active event tracker page
    Then user gets the WWIN status with the counts

  @activeEvent
  Scenario: Verify active tracker types
    Given user is logged into the WWIN application with valid credentials
    When user selects active event tracker page
    Then verify WWIN event type count against database






