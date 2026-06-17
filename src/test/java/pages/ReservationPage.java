package pages;

import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
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

    public void fillCompleteReservationForm() {
        fillPatientName("Budi Santoso");
        fillNickname("Budi");
        selectGender("Laki-laki");
        fillPhone("081234567890");
        fillAge("25");
        fillOccupation("Software Engineer");
        fillParentName("Siti Nurhaliza");
        fillCity("Jakarta");
        fillDistrict("Senayan");
        fillVillage("Kuningan");
        fillAddress("Jl. Sudirman No. 100, Blok A");
        fillHeight("170");
        fillWeight("70");
        fillComplaint("Gigi belakang kanan sakit saat mengunyah");
        toggleMedicalCheckbox("Apakah ada alergi obat atau makanan?");
        fillMedicalDetail("Apakah ada alergi obat atau makanan?", "Alergi Penisilin");
        toggleDentalCheckbox("Apakah Anda sering mengalami sakit gigi?");
        fillDentalDetail("Apakah Anda sering mengalami sakit gigi?", "Sakit saat minum dingin");
        selectDoctor("Dr. Adi Suryanto");
        selectServices("Konsultasi, Scaling");
        fillDoctorNotes("Lakukan scaling dan fluoride");
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
        By dialogButton = By.xpath(
                "//div[@role='dialog' or @data-state='open']//button[normalize-space()='" + buttonText + "']"
        );
        WebElement button = wait.until(ExpectedConditions.elementToBeClickable(dialogButton));
        ((org.openqa.selenium.JavascriptExecutor) driver).executeScript("arguments[0].click();", button);
    }

    private void confirmAlertAction(String titleText, String actionText) {
        By dialogTitle = By.xpath("//*[normalize-space()='" + titleText + "']");
        wait.until(ExpectedConditions.visibilityOfElementLocated(dialogTitle));

        By actionButton = By.xpath("//*[normalize-space()='" + titleText + "']/ancestor::div[@role='alertdialog' or @data-state='open'][1]//button[normalize-space()='" + actionText + "']");
        WebElement button = wait.until(ExpectedConditions.elementToBeClickable(actionButton));
        button.click();

        wait.until(ExpectedConditions.invisibilityOfElementLocated(dialogTitle));
    }

    public void fillPatientName(String name) {
        WebElement input = wait.until(ExpectedConditions.visibilityOfElementLocated(getFormInputByLabel("Nama Pasien")));
        scrollElementIntoView(input);
        input.clear();
        input.sendKeys(name);
    }

    public void fillNickname(String nickname) {
        WebElement input = wait.until(ExpectedConditions.visibilityOfElementLocated(getFormInputByLabel("Nama Panggilan")));
        scrollElementIntoView(input);
        input.clear();
        input.sendKeys(nickname);
    }

    public void selectGender(String gender) {
        WebElement dropdownButton = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//label[contains(text(), 'Jenis Kelamin')]/following::button[1]")
        ));
        scrollElementIntoView(dropdownButton);
        dropdownButton.click();

        WebElement option = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//div[@role='menuitem'][contains(text(), '" + gender + "')]")
        ));
        option.click();
    }

    public void fillPhone(String phone) {
        WebElement input = wait.until(ExpectedConditions.visibilityOfElementLocated(getFormInputByLabel("Nomor Handphone")));
        scrollElementIntoView(input);
        input.clear();
        input.sendKeys(phone);
    }

    public void fillAge(String age) {
        WebElement input = wait.until(ExpectedConditions.visibilityOfElementLocated(getFormInputByLabel("Umur")));
        scrollElementIntoView(input);
        input.clear();
        input.sendKeys(age);
    }

    public void fillOccupation(String occupation) {
        WebElement input = wait.until(ExpectedConditions.visibilityOfElementLocated(getFormInputByLabel("Pekerjaan")));
        scrollElementIntoView(input);
        input.clear();
        input.sendKeys(occupation);
    }

    public void fillBirthDate(String dateString) {
        WebElement datePickerBtn = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//label[contains(text(), 'Tanggal Lahir')]/following::input[1]")
        ));
        scrollElementIntoView(datePickerBtn);
        datePickerBtn.click();
        datePickerBtn.sendKeys(dateString);
    }

    public void fillParentName(String parentName) {
        WebElement input = wait.until(ExpectedConditions.visibilityOfElementLocated(getFormInputByLabel("Nama Orang Tua")));
        scrollElementIntoView(input);
        input.clear();
        input.sendKeys(parentName);
    }

    public void fillCity(String city) {
        WebElement input = wait.until(ExpectedConditions.visibilityOfElementLocated(getFormInputByLabel("Kota/Kabupaten")));
        scrollElementIntoView(input);
        input.clear();
        input.sendKeys(city);
    }

    public void fillDistrict(String district) {
        WebElement input = wait.until(ExpectedConditions.visibilityOfElementLocated(getFormInputByLabel("Kecamatan")));
        scrollElementIntoView(input);
        input.clear();
        input.sendKeys(district);
    }

    public void fillVillage(String village) {
        WebElement input = wait.until(ExpectedConditions.visibilityOfElementLocated(getFormInputByLabel("Kelurahan")));
        scrollElementIntoView(input);
        input.clear();
        input.sendKeys(village);
    }

    public void fillAddress(String address) {
        WebElement input = wait.until(ExpectedConditions.visibilityOfElementLocated(getFormInputByLabel("Alamat")));
        scrollElementIntoView(input);
        input.clear();
        input.sendKeys(address);
    }

    public void fillHeight(String height) {
        WebElement input = wait.until(ExpectedConditions.visibilityOfElementLocated(getFormInputByLabel("Tinggi Badan")));
        scrollElementIntoView(input);
        input.clear();
        input.sendKeys(height);
    }

    public void fillWeight(String weight) {
        WebElement input = wait.until(ExpectedConditions.visibilityOfElementLocated(getFormInputByLabel("Berat Badan")));
        scrollElementIntoView(input);
        input.clear();
        input.sendKeys(weight);
    }

    public void fillComplaint(String complaint) {
        WebElement textarea = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//label[contains(text(), 'Keluhan')]/following::textarea[1]")));
        scrollElementIntoView(textarea);
        textarea.clear();
        textarea.sendKeys(complaint);
    }

    public void toggleMedicalCheckbox(String label) {
        WebElement checkbox = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//label[contains(text(), '" + label + "')]/ancestor::div[1]//input[@type='checkbox']")
        ));
        scrollElementIntoView(checkbox);
        checkbox.click();
    }

    public void fillMedicalDetail(String question, String detail) {
        WebElement input = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//label[contains(text(), '" + question + "')]/following::input[@placeholder][1]")
        ));
        scrollElementIntoView(input);
        input.clear();
        input.sendKeys(detail);
    }

    public void toggleDentalCheckbox(String label) {
        WebElement checkbox = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//label[contains(text(), '" + label + "')]/ancestor::div[1]//input[@type='checkbox']")
        ));
        scrollElementIntoView(checkbox);
        checkbox.click();
    }

    public void fillDentalDetail(String question, String detail) {
        WebElement input = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//label[contains(text(), '" + question + "')]/following::input[@placeholder][1]")
        ));
        scrollElementIntoView(input);
        input.clear();
        input.sendKeys(detail);
    }

    public void selectBrushingFrequency(String frequency) {
        WebElement select = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//label[contains(text(), 'Frekuensi')]/following::select[1]")));
        scrollElementIntoView(select);
        new Select(select).selectByVisibleText(frequency);
    }

    public void selectCheckupFrequency(String frequency) {
        WebElement select = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//label[contains(text(), 'Frekuensi Kontrol')]/following::select[1]")));
        scrollElementIntoView(select);
        new Select(select).selectByVisibleText(frequency);
    }

    public void selectDoctor(String doctorName) {
        WebElement dropdownButton = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//label[contains(text(), 'Pilihan Dokter')]/following::button[1]")
        ));
        scrollElementIntoView(dropdownButton);
        dropdownButton.click();

        WebElement option = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//div[@role='menuitem'][contains(text(), '" + doctorName + "')]")
        ));
        option.click();
    }

    public void selectServices(String serviceNames) {
        String[] services = serviceNames.split(",");
        for (String serviceName : services) {
            String trimmedService = serviceName.trim();
            WebElement checkbox = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//label[contains(text(), '" + trimmedService + "')]/preceding::input[@type='checkbox'][1]")
            ));
            scrollElementIntoView(checkbox);
            checkbox.click();
        }
    }

    public void deselectService(String serviceName) {
        WebElement checkbox = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//label[contains(text(), '" + serviceName + "')]/preceding::input[@type='checkbox'][1]")
        ));
        scrollElementIntoView(checkbox);
        checkbox.click();
    }

    public void fillDoctorNotes(String notes) {
        WebElement textarea = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//label[contains(text(), 'Catatan')]/following::textarea[1]")));
        scrollElementIntoView(textarea);
        textarea.clear();
        textarea.sendKeys(notes);
    }

    public void clearDoctorNotes() {
        WebElement textarea = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//label[contains(text(), 'Catatan')]/following::textarea[1]")));
        scrollElementIntoView(textarea);
        textarea.clear();
    }

    private By getFormInputByLabel(String labelText) {
        return By.xpath("//label[contains(text(), '" + labelText + "')]/following::input[1]");
    }

    private void scrollElementIntoView(WebElement element) {
        ((org.openqa.selenium.JavascriptExecutor) driver).executeScript(
                "var element = arguments[0]; var parent = element.closest('[role=\"dialog\"]') || element.closest('[data-state=\"open\"]'); if (parent) { parent.scrollTop = element.offsetTop - parent.offsetTop - 100; } else { element.scrollIntoView(true); }",
                element
        );
    }

    public String getInputValue(String labelText) {
        WebElement input = wait.until(ExpectedConditions.visibilityOfElementLocated(getFormInputByLabel(labelText)));
        scrollElementIntoView(input);
        return input.getAttribute("value");
    }

    public boolean isCheckboxChecked(String label) {
        WebElement checkbox = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//label[contains(text(), '" + label + "')]/ancestor::div[1]//input[@type='checkbox']")
        ));
        scrollElementIntoView(checkbox);
        return checkbox.isSelected();
    }

    public void assertErrorMessage(String expectedError) {
        WebElement errorElement = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//p[contains(@class, 'text-destructive') and contains(text(), '" + expectedError + "')]")
        ));
        Assertions.assertNotNull(errorElement, "Error message not found: " + expectedError);
    }
}