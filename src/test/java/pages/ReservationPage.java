package pages;

import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class ReservationPage {
    private final WebDriver driver;
    private final WebDriverWait wait;
    private final WebDriverWait longWait;

    public ReservationPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        this.longWait = new WebDriverWait(driver, Duration.ofSeconds(20));
    }

    public void open(String baseUrl) {
        driver.get(baseUrl + "/admin/reservasi");
        wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//h1[normalize-space()='Antrian Pasien']")));
    }

    private void waitForSectionHeader(String label) {
        By locator = By.xpath("//h2[contains(normalize-space(),'" + label + "')]");
        try {
            longWait.until(ExpectedConditions.visibilityOfElementLocated(locator));
        } catch (org.openqa.selenium.TimeoutException e) {
            List<WebElement> allH2 = driver.findElements(By.xpath("//h2"));
            System.out.println("[DEBUG] h2 on page:");
            allH2.forEach(h -> System.out.println("  -> '" + h.getText().trim() + "'"));
            throw new org.openqa.selenium.TimeoutException(
                    "Section '" + label + "' tidak ditemukan. Cek [DEBUG] di atas.", e);
        }
    }

    public boolean isSectionPresent(String label) {
        List<WebElement> headers = driver.findElements(
                By.xpath("//h2[contains(normalize-space(),'" + label + "')]"));
        return !headers.isEmpty();
    }

    public int getSectionCardCount(String label) {
        waitForSectionHeader(label);
        List<WebElement> cards = driver.findElements(By.xpath(
                "//h2[contains(normalize-space(),'" + label + "')]" +
                        "/parent::div" +
                        "/following-sibling::div" +
                        "//div[contains(@class,'rounded-lg') and contains(@class,'bg-[#E0F4FB]')]"));
        System.out.println("[DEBUG] Cards in '" + label + "': " + cards.size());
        return cards.size();
    }

    public boolean sectionHasAtLeastOneCard(String label) {
        if (!isSectionPresent(label)) return false;
        return getSectionCardCount(label) > 0;
    }

    public void openFirstCardAction(String sectionLabel, String buttonText) {
        waitForSectionHeader(sectionLabel);

        wait.until(ExpectedConditions.invisibilityOfElementLocated(
                By.xpath("//div[@data-slot='dialog-overlay' and @data-state='open']")));

        By buttonLocator = By.xpath(
                "//h2[contains(normalize-space(),'" + sectionLabel + "')]" +
                        "/parent::div" +
                        "/following-sibling::div" +
                        "//div[contains(@class,'rounded-lg')]" +
                        "[1]" +
                        "//button[normalize-space()='" + buttonText + "']");

        WebElement button = wait.until(ExpectedConditions.elementToBeClickable(buttonLocator));
        scrollDialogIntoView(button);
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", button);

        waitForDetailDialog();
        waitForFormToLoad();
    }

    public void waitForDetailDialog() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//*[normalize-space()='Catatan Dokter']")));
    }

    public void waitForFormToLoad() {
        longWait.until(driver -> {
            List<WebElement> inputs = driver.findElements(
                    By.xpath("//div[@role='dialog']//label[contains(text(),'Nama Pasien')]/following::input[1]"));
            if (inputs.isEmpty()) return false;
            String val = inputs.get(0).getAttribute("value");
            return val != null && !val.trim().isEmpty();
        });
    }

    public void validateReservation() {
        By validateBtn = By.xpath(
                "//div[@role='dialog']//button[normalize-space()='Validasi' and not(@disabled)]");
        WebElement button = wait.until(ExpectedConditions.elementToBeClickable(validateBtn));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", button);

        By alertTitle = By.xpath("//*[normalize-space()='Validasi Reservasi']");
        wait.until(ExpectedConditions.visibilityOfElementLocated(alertTitle));

        By alertAction = By.xpath(
                "//div[@role='alertdialog']//button[normalize-space()='Validasi']");
        WebElement confirmBtn = wait.until(ExpectedConditions.elementToBeClickable(alertAction));
        confirmBtn.click();

        wait.until(ExpectedConditions.invisibilityOfElementLocated(alertTitle));

        try {
            WebElement error = new WebDriverWait(driver, Duration.ofSeconds(3))
                    .until(ExpectedConditions.visibilityOfElementLocated(
                            By.xpath("//div[@role='dialog']//p[contains(@class,'text-destructive')]")));
            throw new RuntimeException("Validasi gagal: " + error.getText().trim());
        } catch (org.openqa.selenium.TimeoutException ignored) {
        }

        longWait.until(ExpectedConditions.invisibilityOfElementLocated(
                By.xpath("//div[@role='dialog'][@data-state='open']")));

        try { Thread.sleep(1000); } catch (InterruptedException ignored) {}
    }

    public void completeReservation() {
        By selesaiBtn = By.xpath(
                "//div[@role='dialog']//button[normalize-space()='Selesai' and not(@disabled)]");
        WebElement button = wait.until(ExpectedConditions.elementToBeClickable(selesaiBtn));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", button);

        By alertTitle = By.xpath("//*[normalize-space()='Selesaikan Reservasi']");
        wait.until(ExpectedConditions.visibilityOfElementLocated(alertTitle));

        By alertAction = By.xpath(
                "//div[@role='alertdialog']//button[normalize-space()='Selesai']");
        WebElement confirmBtn = wait.until(ExpectedConditions.elementToBeClickable(alertAction));
        confirmBtn.click();

        wait.until(ExpectedConditions.invisibilityOfElementLocated(alertTitle));

        try {
            WebElement error = new WebDriverWait(driver, Duration.ofSeconds(3))
                    .until(ExpectedConditions.visibilityOfElementLocated(
                            By.xpath("//div[@role='dialog']//p[contains(@class,'text-destructive')]")));
            throw new RuntimeException("Selesaikan gagal: " + error.getText().trim());
        } catch (org.openqa.selenium.TimeoutException ignored) {}

        longWait.until(ExpectedConditions.invisibilityOfElementLocated(
                By.xpath("//div[@role='dialog'][@data-state='open']")));

        try { Thread.sleep(1000); } catch (InterruptedException ignored) {}
    }

    public void cancelReservation() {
        By batalBtn = By.xpath(
                "//div[@role='dialog']//button[normalize-space()='Batalkan Reservasi' and not(@disabled)]");
        WebElement button = wait.until(ExpectedConditions.elementToBeClickable(batalBtn));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", button);

        By alertTitle = By.xpath("//*[normalize-space()='Batalkan Reservasi']");
        wait.until(ExpectedConditions.visibilityOfElementLocated(alertTitle));

        By alertAction = By.xpath(
                "//div[@role='alertdialog']//button[normalize-space()='Batalkan']");
        WebElement confirmBtn = wait.until(ExpectedConditions.elementToBeClickable(alertAction));
        confirmBtn.click();

        wait.until(ExpectedConditions.invisibilityOfElementLocated(alertTitle));

        try {
            WebElement error = new WebDriverWait(driver, Duration.ofSeconds(3))
                    .until(ExpectedConditions.visibilityOfElementLocated(
                            By.xpath("//div[@role='dialog']//p[contains(@class,'text-destructive')]")));
            throw new RuntimeException("Batalkan gagal: " + error.getText().trim());
        } catch (org.openqa.selenium.TimeoutException ignored) {}

        longWait.until(ExpectedConditions.invisibilityOfElementLocated(
                By.xpath("//div[@role='dialog'][@data-state='open']")));

        try { Thread.sleep(1000); } catch (InterruptedException ignored) {}
    }

    private void clickDialogFooterButton(String buttonText) {
        By locator = By.xpath(
                "//div[@role='dialog']//button[normalize-space()='" + buttonText + "']");
        WebElement button = wait.until(ExpectedConditions.elementToBeClickable(locator));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", button);
    }

    private void confirmAlertAction(String titleText, String actionText) {
        By titleLocator = By.xpath("//*[normalize-space()='" + titleText + "']");
        wait.until(ExpectedConditions.visibilityOfElementLocated(titleLocator));

        By actionLocator = By.xpath(
                "//*[normalize-space()='" + titleText + "']" +
                        "/ancestor::div[@role='alertdialog'][1]" +
                        "//button[normalize-space()='" + actionText + "']");
        WebElement button = wait.until(ExpectedConditions.elementToBeClickable(actionLocator));
        button.click();

        wait.until(ExpectedConditions.invisibilityOfElementLocated(titleLocator));

        try {
            WebElement errorMsg = new WebDriverWait(driver, Duration.ofSeconds(3))
                    .until(ExpectedConditions.visibilityOfElementLocated(
                            By.xpath("//div[@role='dialog']//p[contains(@class,'text-destructive')]")));
            String errorText = errorMsg.getText().trim();
            if (!errorText.isEmpty()) {
                throw new RuntimeException("Aksi gagal dengan error: " + errorText);
            }
        } catch (org.openqa.selenium.TimeoutException ignored) {
        }

        longWait.until(ExpectedConditions.or(
                ExpectedConditions.invisibilityOfElementLocated(
                        By.xpath("//div[@role='dialog' and @data-state='open']")),
                ExpectedConditions.invisibilityOfElementLocated(
                        By.xpath("//div[@data-slot='dialog-overlay'][@data-state='open']"))
        ));

        try { Thread.sleep(800); } catch (InterruptedException ignored) {}
    }

    private void setReactInputValue(WebElement input, String value) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].focus();", input);
        input.sendKeys(Keys.chord(Keys.CONTROL, "a"));
        input.sendKeys(Keys.DELETE);
        input.sendKeys(value);
        ((JavascriptExecutor) driver).executeScript(
                "var nativeInputValueSetter = Object.getOwnPropertyDescriptor(window.HTMLInputElement.prototype, 'value').set;" +
                        "nativeInputValueSetter.call(arguments[0], arguments[1]);" +
                        "arguments[0].dispatchEvent(new Event('input', { bubbles: true }));",
                input, value);
    }

    private void setReactTextareaValue(WebElement textarea, String value) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].focus();", textarea);
        textarea.sendKeys(Keys.chord(Keys.CONTROL, "a"));
        textarea.sendKeys(Keys.DELETE);
        textarea.sendKeys(value);
        ((JavascriptExecutor) driver).executeScript(
                "var setter = Object.getOwnPropertyDescriptor(window.HTMLTextAreaElement.prototype, 'value').set;" +
                        "setter.call(arguments[0], arguments[1]);" +
                        "arguments[0].dispatchEvent(new Event('input', { bubbles: true }));",
                textarea, value);
    }

    public void fillPatientName(String name) {
        WebElement input = wait.until(ExpectedConditions.visibilityOfElementLocated(
                getDialogInputByLabel("Nama Pasien")));
        scrollDialogIntoView(input);
        setReactInputValue(input, name);
    }

    public void fillNickname(String nickname) {
        WebElement input = wait.until(ExpectedConditions.visibilityOfElementLocated(
                getDialogInputByLabel("Nama Panggilan")));
        scrollDialogIntoView(input);
        setReactInputValue(input, nickname);
    }

    public void selectGender(String gender) {
        WebElement trigger = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//div[@role='dialog']//label[contains(text(),'Jenis Kelamin')]/following::button[1]")));
        scrollDialogIntoView(trigger);
        trigger.click();
        WebElement option = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//div[@role='menuitem'][normalize-space()='" + gender + "']")));
        option.click();
    }

    public void fillPhone(String phone) {
        WebElement input = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("(//div[@role='dialog']//label[contains(text(),'Nomor Handphone')]/following::input[1])[1]")));
        scrollDialogIntoView(input);

        ((JavascriptExecutor) driver).executeScript(
                "var setter = Object.getOwnPropertyDescriptor(window.HTMLInputElement.prototype, 'value').set;" +
                        "setter.call(arguments[0], '');" +
                        "arguments[0].dispatchEvent(new Event('input', { bubbles: true }));",
                input);
        try { Thread.sleep(100); } catch (InterruptedException ignored) {}

        setReactInputValue(input, phone);
    }

    public void fillAge(String age) {
        WebElement input = wait.until(ExpectedConditions.visibilityOfElementLocated(
                getDialogInputByLabel("Umur")));
        scrollDialogIntoView(input);
        setReactInputValue(input, age);
    }

    public void fillOccupation(String occupation) {
        WebElement input = wait.until(ExpectedConditions.visibilityOfElementLocated(
                getDialogInputByLabel("Pekerjaan")));
        scrollDialogIntoView(input);
        setReactInputValue(input, occupation);
    }

    public void fillBirthDate(String dateString) {
        WebElement input = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//div[@role='dialog']//label[contains(text(),'Tanggal Lahir')]/following::input[1]")));
        scrollDialogIntoView(input);
        input.click();
        input.sendKeys(dateString);
    }

    public void fillParentName(String parentName) {
        WebElement input = wait.until(ExpectedConditions.visibilityOfElementLocated(
                getDialogInputByLabel("Nama Orang Tua")));
        scrollDialogIntoView(input);
        setReactInputValue(input, parentName);
    }

    public void fillCity(String city) {
        WebElement input = wait.until(ExpectedConditions.visibilityOfElementLocated(
                getDialogInputByLabel("Kota/Kabupaten")));
        scrollDialogIntoView(input);
        setReactInputValue(input, city);
    }

    public void fillDistrict(String district) {
        WebElement input = wait.until(ExpectedConditions.visibilityOfElementLocated(
                getDialogInputByLabel("Kecamatan")));
        scrollDialogIntoView(input);
        setReactInputValue(input, district);
    }

    public void fillVillage(String village) {
        WebElement input = wait.until(ExpectedConditions.visibilityOfElementLocated(
                getDialogInputByLabel("Kelurahan")));
        scrollDialogIntoView(input);
        setReactInputValue(input, village);
    }

    public void fillAddress(String address) {
        WebElement input = wait.until(ExpectedConditions.visibilityOfElementLocated(
                getDialogInputByLabel("Alamat")));
        scrollDialogIntoView(input);
        setReactInputValue(input, address);
    }

    public void fillHeight(String height) {
        WebElement input = wait.until(ExpectedConditions.visibilityOfElementLocated(
                getDialogInputByLabel("Tinggi Badan")));
        scrollDialogIntoView(input);
        setReactInputValue(input, height);
    }

    public void fillWeight(String weight) {
        WebElement input = wait.until(ExpectedConditions.visibilityOfElementLocated(
                getDialogInputByLabel("Berat Badan")));
        scrollDialogIntoView(input);
        setReactInputValue(input, weight);
    }

    public void fillComplaint(String complaint) {
        WebElement textarea = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//div[@role='dialog']//label[contains(text(),'Keluhan')]/following::textarea[1]")));
        scrollDialogIntoView(textarea);
        setReactTextareaValue(textarea, complaint);
    }

    public void toggleMedicalCheckbox(String label) {
        WebElement cb = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//div[@role='dialog']//label[contains(text(),'" + label + "')]/following::button[@role='checkbox'][1]")));
        scrollDialogIntoView(cb);
        cb.click();
    }

    public void fillMedicalDetail(String question, String detail) {
        WebElement input = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//div[@role='dialog']//label[contains(text(),'" + question + "')]/following::input[1]")));
        scrollDialogIntoView(input);
        setReactInputValue(input, detail);
    }

    public void toggleDentalCheckbox(String label) {
        WebElement cb = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//div[@role='dialog']//label[contains(text(),'" + label + "')]/following::button[@role='checkbox'][1]")));
        scrollDialogIntoView(cb);
        cb.click();
    }

    public void fillDentalDetail(String question, String detail) {
        WebElement input = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//div[@role='dialog']//label[contains(text(),'" + question + "')]/following::input[1]")));
        scrollDialogIntoView(input);
        setReactInputValue(input, detail);
    }

    public void selectBrushingFrequency(String frequency) {
        WebElement trigger = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//div[@role='dialog']//label[contains(text(),'menyikat gigi')]/following::button[1]")));
        scrollDialogIntoView(trigger);
        trigger.click();
        wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//div[@role='option'][contains(.,'" + frequency + "')]"))).click();
    }

    public void selectCheckupFrequency(String frequency) {
        WebElement trigger = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//div[@role='dialog']//label[contains(text(),'checkup')]/following::button[1]")));
        scrollDialogIntoView(trigger);
        trigger.click();
        wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//div[@role='option'][contains(.,'" + frequency + "')]"))).click();
    }

    public void selectDoctor(String doctorName) {
        WebElement trigger = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//div[@role='dialog']//label[contains(text(),'Pilihan Dokter')]/following::button[1]")));
        scrollDialogIntoView(trigger);
        trigger.click();
        wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//div[@role='menuitem'][normalize-space()='" + doctorName + "']"))).click();
    }

    public void selectServices(String serviceNames) {
        WebElement multiSelectContainer = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//div[@role='dialog']//label[normalize-space()='Layanan']/following-sibling::*[1]")));
        scrollDialogIntoView(multiSelectContainer);
        multiSelectContainer.click();
        try { Thread.sleep(400); } catch (InterruptedException ignored) {}

        for (String s : serviceNames.split(",")) {
            String name = s.trim();
            WebElement option = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//div[@role='dialog']//div[contains(@class,'cursor-pointer') " +
                            "and normalize-space(text())='" + name + "']")));
            scrollDialogIntoView(option);
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", option);
            try { Thread.sleep(300); } catch (InterruptedException ignored) {}
        }

        try {
            driver.findElement(By.xpath("//div[@role='dialog']//label[normalize-space()='Layanan']")).click();
        } catch (Exception ignored) {}
        try { Thread.sleep(200); } catch (InterruptedException ignored) {}
    }

    public void deselectService(String serviceName) {
        selectServices(serviceName);
    }

    public void fillDoctorNotes(String notes) {
        WebElement textarea = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//div[@role='dialog']//label[contains(text(),'Catatan')]/following::textarea[1]")));
        scrollDialogIntoView(textarea);
        setReactTextareaValue(textarea, notes);
    }

    public void clearDoctorNotes() {
        fillDoctorNotes("");
    }

    public String getInputValue(String labelText) {
        WebElement input = wait.until(ExpectedConditions.visibilityOfElementLocated(
                getDialogInputByLabel(labelText)));
        return input.getAttribute("value");
    }

    public boolean isCheckboxChecked(String label) {
        WebElement cb = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//div[@role='dialog']//label[contains(text(),'" + label + "')]/following::button[@role='checkbox'][1]")));
        return "true".equals(cb.getAttribute("aria-checked"));
    }

    public void assertErrorMessage(String expectedError) {
        WebElement el = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//p[contains(@class,'text-destructive') and contains(text(),'" + expectedError + "')]")));
        Assertions.assertNotNull(el, "Error message not found: " + expectedError);
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
        selectDoctor("Testing");
        selectServices("Scaling");
        fillDoctorNotes("Lakukan scaling dan fluoride");
    }

    private By getDialogInputByLabel(String labelText) {
        return By.xpath(
                "//div[@role='dialog']//label[contains(text(),'" + labelText + "')]/following::input[1]");
    }

    private void scrollDialogIntoView(WebElement element) {
        ((JavascriptExecutor) driver).executeScript(
                "var e = arguments[0];" +
                        "var dialog = e.closest('[role=\"dialog\"]') || e.closest('[data-state=\"open\"]');" +
                        "if (dialog) {" +
                        "  var rect = e.getBoundingClientRect();" +
                        "  var dRect = dialog.getBoundingClientRect();" +
                        "  if (rect.bottom > dRect.bottom || rect.top < dRect.top) {" +
                        "    dialog.scrollTop += (rect.top - dRect.top) - 100;" +
                        "  }" +
                        "} else {" +
                        "  e.scrollIntoView({block: 'center'});" +
                        "}", element);
    }

    public void validateReservationOnly() {
        clickDialogFooterButton("Validasi");
        confirmAlertAction("Validasi Reservasi", "Validasi");
    }
}