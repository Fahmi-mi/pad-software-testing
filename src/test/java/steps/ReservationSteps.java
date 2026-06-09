package steps;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.jupiter.api.Assertions;
import pages.ReservationPage;
import support.TestContext;

public class ReservationSteps {
    private final ReservationPage reservationPage;

    public ReservationSteps(TestContext context) {
        this.reservationPage = new ReservationPage(context.getDriver());
    }

    @Given("pending section has at least {int} reservation")
    public void pendingSectionHasAtLeastReservation(int minCount) {
        int count = reservationPage.getSectionCardCount("Menunggu");
        Assertions.assertTrue(count >= minCount, "Pending section is empty.");
    }

    @When("admin opens first pending reservation detail")
    public void adminOpensFirstPendingReservationDetail() {
        reservationPage.openFirstCardAction("Menunggu", "Validasi");
    }

    @When("admin opens first validated reservation detail")
    public void adminOpensFirstValidatedReservationDetail() {
        reservationPage.openFirstCardAction("Tervalidasi", "Konfirmasi");
    }

    @Then("reservation detail dialog should be visible")
    public void reservationDetailDialogShouldBeVisible() {
        reservationPage.waitForDetailDialog();
    }

    @When("admin validates the reservation in dialog")
    public void adminValidatesTheReservationInDialog() {
        reservationPage.validateReservation();
    }

    @When("admin completes the reservation")
    public void adminCompletesTheReservation() {
        reservationPage.completeReservation();
    }

    @When("admin cancels the reservation")
    public void adminCancelsTheReservation() {
        reservationPage.cancelReservation();
    }

    @Then("validated section should have at least {int} reservation")
    public void validatedSectionShouldHaveAtLeastReservation(int minCount) {
        int count = reservationPage.getSectionCardCount("Tervalidasi");
        Assertions.assertTrue(count >= minCount, "Validated section is empty.");
    }

    @Then("completed section should have at least {int} reservation")
    public void completedSectionShouldHaveAtLeastReservation(int minCount) {
        int count = reservationPage.getSectionCardCount("Selesai");
        Assertions.assertTrue(count >= minCount, "Completed section is empty.");
    }

    @Then("cancelled section should have at least {int} reservation")
    public void cancelledSectionShouldHaveAtLeastReservation(int minCount) {
        int count = reservationPage.getSectionCardCount("Dibatalkan");
        Assertions.assertTrue(count >= minCount, "Cancelled section is empty.");
    }

    @And("reservation detail should move to completed section")
    public void reservationDetailShouldMoveToCompletedSection() {
        completedSectionShouldHaveAtLeastReservation(1);
    }
}
