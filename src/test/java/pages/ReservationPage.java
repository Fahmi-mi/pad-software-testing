package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class ReservationPage {
    private final WebDriver driver;
    private final WebDriverWait wait;

    public ReservationPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public void open(String baseUrl) {
        driver.get(baseUrl + "/admin/reservasi");
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//h1[normalize-space()='Antrian Pasien']")));
    }

    public int getSectionCardCount(String sectionLabel) {
        waitForSectionHeader(sectionLabel);
        List<WebElement> cards = driver.findElements(sectionCards(sectionLabel));
        return cards.size();
    }

    public void openFirstCardAction(String sectionLabel, String actionText) {
        waitForSectionHeader(sectionLabel);
        WebElement card = wait.until(ExpectedConditions.visibilityOfElementLocated(sectionCards(sectionLabel)));
        WebElement actionButton = card.findElement(By.xpath(".//button[normalize-space()='" + actionText + "']"));
        actionButton.click();
        waitForDetailDialog();
    }

    public void waitForDetailDialog() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[normalize-space()='Catatan Dokter']")));
    }

    public void validateReservation() {
        clickDialogFooterButton("Validasi");
        confirmAlertAction("Validasi Reservasi", "Validasi");
    }

    public void completeReservation() {
        clickDialogFooterButton("Selesai");
        confirmAlertAction("Selesaikan Reservasi", "Selesai");
    }

    public void cancelReservation() {
        clickDialogFooterButton("Batalkan Reservasi");
        confirmAlertAction("Batalkan Reservasi", "Batalkan");
    }

    private void waitForSectionHeader(String label) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//h2[contains(normalize-space(), '" + label + "')]")));
    }

    private By sectionCards(String label) {
        return By.xpath("//h2[contains(normalize-space(), '" + label + "')]/parent::div/following-sibling::div//div[contains(@class,'rounded-lg')]");
    }

    private void clickDialogFooterButton(String buttonText) {
        WebElement button = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[normalize-space()='" + buttonText + "']")));
        button.click();
    }

    private void confirmAlertAction(String titleText, String actionText) {
        By dialogTitle = By.xpath("//*[normalize-space()='" + titleText + "']");
        wait.until(ExpectedConditions.visibilityOfElementLocated(dialogTitle));

        By actionButton = By.xpath("//*[normalize-space()='" + titleText + "']/ancestor::div[@role='alertdialog' or @data-state='open'][1]//button[normalize-space()='" + actionText + "']");
        WebElement button = wait.until(ExpectedConditions.elementToBeClickable(actionButton));
        button.click();

        wait.until(ExpectedConditions.invisibilityOfElementLocated(dialogTitle));
    }
}
