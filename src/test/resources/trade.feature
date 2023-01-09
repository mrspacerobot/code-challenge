Feature: 00 Smoke Tests

  Scenario: Basic trading Buy Sell
    Given one security "WSB" and two users "Diamond" and "Paper" exist
    When user "Diamond" puts a "buy" order for security "WSB" with a price of 101 and quantity of 50
    And user "Paper" puts a "sell" order for security "WSB" with a price of 100 and a quantity of 100
    Then a trade occurs with the price of 100 and quantity of 50

  Scenario: Basic trading Sell Buy
    Given one security "SEC" and two users "User1" and "User2" exist
    When user "User2" puts a "sell" order for security "SEC" with a price of 100 and a quantity of 100
    And user "User1" puts a "buy" order for security "SEC" with a price of 101 and quantity of 50
    Then a trade occurs with the price of 100 and quantity of 50

  Scenario: No trades occur Sell Buy
    Given one security "NTR" and two users "User1" and "User2" exist
    When user "User2" puts a "sell" order for security "NTR" with a price of 100 and a quantity of 100
    And user "User1" puts a "buy" order for security "NTR" with a price of 99 and quantity of 50
    Then no trades occur

  Scenario: No trades occur Buy Sell
    Given one security "NTR" and two users "User1" and "User2" exist
    When user "User2" puts a "buy" order for security "NTR" with a price of 99 and a quantity of 50
    And user "User1" puts a "sell" order for security "NTR" with a price of 100 and quantity of 100
    Then no trades occur

  Scenario: No trades occur same user Sell Buy
    Given one security "SEC" and two users "User1" and "User2" exist
    When user "User1" puts a "sell" order for security "SEC" with a price of 100 and a quantity of 100
    And user "User1" puts a "buy" order for security "SEC" with a price of 101 and quantity of 50
    Then no trades occur

  Scenario: Trade occurs Sell Buy Buy
    Given one security "SEC" and three users "User1" and "User2" and "User3" exist
    When user "User2" puts a "sell" order for security "SEC" with a price of 100 and a quantity of 100
    And user "User1" puts a "buy" order for security "SEC" with a price of 99 and quantity of 50
    And user "User3" puts a "buy" order for security "SEC" with a price of 101 and quantity of 50
    Then a trade occurs with the price of 100 and quantity of 50

  Scenario: Trade occurs Sell Sell Buy Minimum price
    Given one security "SEC" and three users "User1" and "User2" and "User3" exist
    When user "User2" puts a "sell" order for security "SEC" with a price of 100 and a quantity of 100
    And user "User1" puts a "sell" order for security "SEC" with a price of 80 and quantity of 50
    And user "User3" puts a "buy" order for security "SEC" with a price of 100 and quantity of 50
    Then a trade occurs with the price of 80 and quantity of 50

  Scenario: Trade occurs Buy Sell Buy Minimum price
    Given one security "SEC" and three users "User1" and "User2" and "User3" exist
    When user "User2" puts a "buy" order for security "SEC" with a price of 100 and a quantity of 100
    And user "User1" puts a "sell" order for security "SEC" with a price of 80 and quantity of 50
    And user "User3" puts a "buy" order for security "SEC" with a price of 100 and quantity of 50
    Then a trade occurs with the price of 80 and quantity of 50
