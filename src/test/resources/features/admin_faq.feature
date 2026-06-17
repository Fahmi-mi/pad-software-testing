Feature: Admin FAQ Management
  As an admin
  I want to manage frequently asked questions (FAQ)
  So that I can provide accurate information to the users

  Background:
    Given admin opens login page
    And admin logs in with valid credentials
    And admin opens FAQ page

  Scenario: Successfully add a new FAQ
    When admin fills FAQ question with "Apa itu Tentang Dental?"
    And admin fills FAQ answer with "Tentang Dental adalah klinik gigi terpercaya."
    And admin clicks add FAQ button
    Then FAQ with question "Apa itu Tentang Dental?" should be visible in list

  Scenario: Fail to add FAQ due to empty question
    When admin fills FAQ question with ""
    And admin fills FAQ answer with "Jawaban ini tidak akan tersimpan."
    And admin clicks add FAQ button
    Then admin should see FAQ error message "Pertanyaan wajib diisi."

  Scenario: Successfully update an existing FAQ
    When admin edits the FAQ at index 0
    And admin updates FAQ with question "Apa itu Tentang Dental (Updated)?" and answer "Klinik gigi terbaik di kota."
    Then FAQ with question "Apa itu Tentang Dental (Updated)?" should be visible in list

  Scenario: Successfully delete an FAQ
    When admin fills FAQ question with "FAQ Yang Akan Dihapus"
    And admin fills FAQ answer with "Jawaban sementara untuk dihapus."
    And admin clicks add FAQ button
    Then FAQ with question "FAQ Yang Akan Dihapus" should be visible in list
    When admin deletes FAQ with question "FAQ Yang Akan Dihapus"
    Then FAQ with question "FAQ Yang Akan Dihapus" should not be visible in list
