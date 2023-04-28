# language: en

@example
  Feature: Search in Google

    Scenario: Search phrase "Cucumber framework"
      Given open url "https://www.google.com/"
      And accept cookies
      When set value "Cucumber framework"
      Then check link "https://cucumber.io/"