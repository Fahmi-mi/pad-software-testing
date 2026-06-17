package steps;

import io.cucumber.java.en.And;
import io.cucumber.java.en.When;
import pages.ReservationPage;
import support.TestContext;

public class FormFillingSteps {
    private final ReservationPage reservationPage;

    public FormFillingSteps(TestContext context) {
        this.reservationPage = new ReservationPage(context.getDriver());
    }

    @When("admin fills the reservation form completely with valid data")
    public void adminFillsTheReservationFormCompletelyWithValidData() {
        reservationPage.fillCompleteReservationForm();
    }

    @When("admin fills patient name with {string}")
    public void adminFillsPatientNameWith(String name) {
        reservationPage.fillPatientName(name);
    }

    @When("admin clears patient name field")
    public void adminClearsPatientNameField() {
        reservationPage.fillPatientName("");
    }

    @When("admin fills nickname with {string}")
    public void adminFillsNicknameWith(String nickname) {
        reservationPage.fillNickname(nickname);
    }

    @When("admin selects gender {string}")
    public void adminSelectsGender(String gender) {
        reservationPage.selectGender(gender);
    }

    @When("admin fills phone with {string}")
    public void adminFillsPhoneWith(String phone) {
        reservationPage.fillPhone(phone);
    }

    @When("admin clears phone field")
    public void adminClearsPhoneField() {
        reservationPage.fillPhone("");
    }

    @When("admin fills age with {string}")
    public void adminFillsAgeWith(String age) {
        reservationPage.fillAge(age);
    }

    @When("admin fills occupation with {string}")
    public void adminFillsOccupationWith(String occupation) {
        reservationPage.fillOccupation(occupation);
    }

    @When("admin fills birth date with {string}")
    public void adminFillsBirthDateWith(String dateString) {
        reservationPage.fillBirthDate(dateString);
    }

    @When("admin fills parent name with {string}")
    public void adminFillsParentNameWith(String parentName) {
        reservationPage.fillParentName(parentName);
    }

    @When("admin fills city with {string}")
    public void adminFillsCityWith(String city) {
        reservationPage.fillCity(city);
    }

    @When("admin fills district with {string}")
    public void adminFillsDistrictWith(String district) {
        reservationPage.fillDistrict(district);
    }

    @When("admin fills village with {string}")
    public void adminFillsVillageWith(String village) {
        reservationPage.fillVillage(village);
    }

    @When("admin fills address with {string}")
    public void adminFillsAddressWith(String address) {
        reservationPage.fillAddress(address);
    }

    @When("admin fills height with {string}")
    public void adminFillsHeightWith(String height) {
        reservationPage.fillHeight(height);
    }

    @When("admin fills weight with {string}")
    public void adminFillsWeightWith(String weight) {
        reservationPage.fillWeight(weight);
    }

    @When("admin fills complaint with {string}")
    public void adminFillsComplaintWith(String complaint) {
        reservationPage.fillComplaint(complaint);
    }

    @When("admin toggles medical checkbox {string}")
    public void adminTogglesMedicalCheckbox(String label) {
        reservationPage.toggleMedicalCheckbox(label);
    }

    @And("admin fills medical detail for {string} with {string}")
    public void adminFillsMedicalDetail(String question, String detail) {
        reservationPage.fillMedicalDetail(question, detail);
    }

    @When("admin toggles dental checkbox {string}")
    public void adminTogglesDentalCheckbox(String label) {
        reservationPage.toggleDentalCheckbox(label);
    }

    @And("admin fills dental detail for {string} with {string}")
    public void adminFillsDentalDetail(String question, String detail) {
        reservationPage.fillDentalDetail(question, detail);
    }

    @And("admin selects brushing frequency {string}")
    public void adminSelectsBrushingFrequency(String frequency) {
        reservationPage.selectBrushingFrequency(frequency);
    }

    @And("admin selects checkup frequency {string}")
    public void adminSelectsCheckupFrequency(String frequency) {
        reservationPage.selectCheckupFrequency(frequency);
    }

    @When("admin selects doctor {string}")
    public void adminSelectsDoctor(String doctorName) {
        reservationPage.selectDoctor(doctorName);
    }

    @When("admin selects services {string}")
    public void adminSelectsServices(String services) {
        reservationPage.selectServices(services);
    }

    @And("admin deselects service {string}")
    public void adminDeselectsService(String serviceName) {
        reservationPage.deselectService(serviceName);
    }

    @When("admin fills doctor notes with {string}")
    public void adminFillsDoctorNotesWith(String notes) {
        reservationPage.fillDoctorNotes(notes);
    }

    @And("admin clears doctor notes field")
    public void adminClearsDoctorNotesField() {
        reservationPage.clearDoctorNotes();
    }

    @And("admin should see error message {string}")
    public void adminShouldSeeErrorMessage(String errorMessage) {
        reservationPage.assertErrorMessage(errorMessage);
    }

    @And("patient name field should have value {string}")
    public void patientNameFieldShouldHaveValue(String expectedValue) {
        String actualValue = reservationPage.getInputValue("Nama Pasien");
        assert actualValue.equals(expectedValue) :
                "Expected: " + expectedValue + ", but got: " + actualValue;
    }

    @And("checkbox {string} should be checked")
    public void checkboxShouldBeChecked(String label) {
        boolean isChecked = reservationPage.isCheckboxChecked(label);
        assert isChecked : "Checkbox '" + label + "' is not checked";
    }

    @And("checkbox {string} should be unchecked")
    public void checkboxShouldBeUnchecked(String label) {
        boolean isChecked = reservationPage.isCheckboxChecked(label);
        assert !isChecked : "Checkbox '" + label + "' is still checked";
    }
}