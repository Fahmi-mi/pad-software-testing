package steps;

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

    @Then("reservation detail dialog should be visible")
    public void reservationDetailDialogShouldBeVisible() {
        reservationPage.waitForDetailDialog();
    }

    @When("admin validates the reservation in dialog")
    public void adminValidatesTheReservationInDialog() {
        reservationPage.validateReservation();
    }

    @Then("validated section should have at least {int} reservation")
    public void validatedSectionShouldHaveAtLeastReservation(int minCount) {
        int count = reservationPage.getSectionCardCount("Tervalidasi");
        Assertions.assertTrue(count >= minCount, "Validated section is empty.");
    }
}
