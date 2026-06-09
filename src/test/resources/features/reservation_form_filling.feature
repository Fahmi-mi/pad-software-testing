Feature: Admin Reservasi Form Filling
  As an admin
  I want to fill patient form with various data
  So that patient reservation details are properly recorded

  Background:
    Given admin opens login page
    And admin logs in with valid credentials
    And admin opens reservation page
    And pending section has at least 1 reservation
    And admin opens first pending reservation detail
    And reservation detail dialog should be visible

  Scenario: Fill all form fields with complete data then validate
    When admin fills patient name with "Budi Santoso"
    And admin fills nickname with "Budi"
    And admin selects gender "Laki-laki"
    And admin fills phone with "081234567890"
    And admin fills age with "25"
    And admin fills occupation with "Software Engineer"
    And admin fills parent name with "Siti Nurhaliza"
    And admin fills city with "Jakarta"
    And admin fills district with "Senayan"
    And admin fills village with "Kuningan"
    And admin fills address with "Jl. Sudirman No. 100, Blok A"
    And admin fills height with "170"
    And admin fills weight with "70"
    And admin fills complaint with "Gigi belakang kanan sakit saat mengunyah"
    And admin toggles medical checkbox "Apakah ada alergi obat atau makanan?"
    And admin fills medical detail for "Apakah ada alergi obat atau makanan?" with "Alergi Penisilin"
    And admin toggles dental checkbox "Apakah Anda sering mengalami sakit gigi?"
    And admin fills dental detail for "Apakah Anda sering mengalami sakit gigi?" with "Sakit saat minum dingin"
    And admin selects doctor "Dr. Adi Suryanto"
    And admin selects services "Konsultasi, Scaling"
    And admin fills doctor notes with "Lakukan scaling dan fluoride"
    And admin validates the reservation in dialog
    Then validated section should have at least 1 reservation

  Scenario: Update form and complete reservation
    When admin fills doctor notes with "Lakukan scaling"
    And admin validates the reservation in dialog
    Then validated section should have at least 1 reservation
    When admin opens first validated reservation detail
    And admin fills doctor notes with "Scaling selesai, fluoride aplikasi"
    And admin completes the reservation
    Then reservation detail should move to completed section
