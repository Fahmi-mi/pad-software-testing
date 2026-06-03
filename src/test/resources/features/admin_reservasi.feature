Feature: Admin Reservasi
  As an admin
  I want to manage reservation status
  So that I can validate and confirm patient appointments

  Background:
    Given admin opens login page
    And admin logs in with valid credentials
    And admin opens reservation page

  Scenario: Open pending reservation detail
    Given pending section has at least 1 reservation
    When admin opens first pending reservation detail
    Then reservation detail dialog should be visible

  Scenario: Validate first pending reservation
    Given pending section has at least 1 reservation
    When admin opens first pending reservation detail
    And admin validates the reservation in dialog
    Then validated section should have at least 1 reservation
