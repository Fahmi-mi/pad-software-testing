package steps;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Assumptions;
import pages.ReservationPage;
import support.TestContext;

public class ReservationSteps {
    private final ReservationPage reservationPage;

    public ReservationSteps(TestContext context) {
        this.reservationPage = new ReservationPage(context.getDriver());
    }

    @Given("pending section has at least {int} reservation")
    public void pendingSectionHasAtLeastReservation(int minCount) {
        boolean present = reservationPage.isSectionPresent("Menunggu");
        Assumptions.assumeTrue(present, "SKIP: Tidak ada data pending reservation.");
        int count = reservationPage.getSectionCardCount("Menunggu");
        Assumptions.assumeTrue(count >= minCount, "SKIP: Jumlah pending kurang dari " + minCount);
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
        reservationPage.waitForFormToLoad();
    }

    @When("admin validates the reservation in dialog")
    public void adminValidatesTheReservationInDialog() {
        reservationPage.validateReservationOnly();
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
        Assertions.assertTrue(reservationPage.isSectionPresent("Tervalidasi"),
                "Validated section tidak ada.");
        int count = reservationPage.getSectionCardCount("Tervalidasi");
        Assertions.assertTrue(count >= minCount,
                "Validated section hanya punya " + count + " reservation.");
    }

    @Then("completed section should have at least {int} reservation")
    public void completedSectionShouldHaveAtLeastReservation(int minCount) {
        Assertions.assertTrue(reservationPage.isSectionPresent("Selesai"),
                "Completed section tidak ada.");
        int count = reservationPage.getSectionCardCount("Selesai");
        Assertions.assertTrue(count >= minCount,
                "Completed section hanya punya " + count + " reservation.");
    }

    @Then("cancelled section should have at least {int} reservation")
    public void cancelledSectionShouldHaveAtLeastReservation(int minCount) {
        Assertions.assertTrue(reservationPage.isSectionPresent("Dibatalkan"),
                "Cancelled section tidak ada.");
        int count = reservationPage.getSectionCardCount("Dibatalkan");
        Assertions.assertTrue(count >= minCount,
                "Cancelled section hanya punya " + count + " reservation.");
    }

    @And("reservation detail should move to completed section")
    public void reservationDetailShouldMoveToCompletedSection() {
        completedSectionShouldHaveAtLeastReservation(1);
    }
}