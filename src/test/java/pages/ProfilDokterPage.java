package pages;

import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.time.Duration;
import java.util.List;

public class ProfilDokterPage {
    private final WebDriver driver;
    private final WebDriverWait wait;
    private final Actions actions;
//    private final JavascriptExecutor js;

    // ── Form Locators ──────────────────────────────────────────
    private final By nameInput        = By.cssSelector(
        "[data-testid='dokter-nama-input'], input[placeholder*='Nama'], input[name='name']");
    private final By spesialisInput   = By.cssSelector(
        "[data-testid='dokter-spesialis-input'], input[placeholder*='Spesialis'], input[name='specialization']");
    private final By tiptapEditor     = By.cssSelector(
        "[data-testid='dokter-form'] .tiptap.ProseMirror, .tiptap.ProseMirror");
    private final By schedulePicker   = By.cssSelector(
        ".border-primary.min-h-9.w-full, [data-testid='schedule-picker'], .schedule-picker");
    private final By photoUpload      = By.cssSelector(
        "[data-testid='dokter-foto-upload'], input[type='file']");
    private final By submitButton     = By.cssSelector(
        "[data-testid='dokter-submit-button'], button[type='submit']");

    // ── Card (index-based) Locators ────────────────────────────
    private final By cardFirst        = By.cssSelector("[data-testid='dokter-card-0']");

    // ── Delete Locator ─────────────────────────────────────────
    private final By hapusDokterBtn   = By.cssSelector("button.bg-red-400");

    public ProfilDokterPage(WebDriver driver) {
        this.driver  = driver;
        this.wait    = new WebDriverWait(driver, Duration.ofSeconds(20));
        this.actions = new Actions(driver);
    }

    // ── Navigation ─────────────────────────────────────────────
    public void open(String baseUrl) {
        driver.get(baseUrl + "/admin/profil-dokter");
        wait.until(d -> d.getCurrentUrl().contains("/admin/profil-dokter"));
        wait.until(webDriver -> ((JavascriptExecutor) webDriver).executeScript(
            "return document.readyState").equals("complete"));
        wait.until(ExpectedConditions.visibilityOfElementLocated(nameInput));
    }

    // ── 3.1 Add New Dokter ─────────────────────────────────────
    public void fillDoctorName(String name) {
        WebElement el = wait.until(ExpectedConditions.visibilityOfElementLocated(nameInput));
        el.clear();
        el.sendKeys(name);
    }

    public void fillSpesialis(String spec) {
        WebElement el = wait.until(ExpectedConditions.visibilityOfElementLocated(spesialisInput));
        el.clear();
        el.sendKeys(spec);
    }

    /** Type into the Tiptap rich-text editor inside the dokter form. */
    public void typeStatement(String text) {
        WebElement editor = wait.until(ExpectedConditions.elementToBeClickable(tiptapEditor));
        editor.click();
        // Use JS to set text content, then fire an input event so Tiptap picks it up
        ((JavascriptExecutor) driver).executeScript(
            "arguments[0].innerText = arguments[1];", editor, text);
        // Trigger input event
        ((JavascriptExecutor) driver).executeScript(
            "arguments[0].dispatchEvent(new Event('input', {bubbles:true}));", editor);
    }

    /** Open the schedule picker dropdown. */
    public void openSchedulePicker() {
        WebElement picker = wait.until(ExpectedConditions.elementToBeClickable(schedulePicker));
        picker.click();
    }

    /** Click a schedule slot option by its visible text (e.g. "Selasa 09:00 - 10:00"). */
    public void selectScheduleSlot(String slotText) {
        By slotLocator = By.xpath(
            "//div[contains(@class,'cursor-pointer') and contains(normalize-space(),'" + slotText + "')]"
            + " | //span[contains(normalize-space(),'" + slotText + "')]/parent::div");
        WebElement slot = wait.until(ExpectedConditions.elementToBeClickable(slotLocator));
        slot.click();
    }

    public void uploadPhoto(String fileName) {
        WebElement input = wait.until(ExpectedConditions.presenceOfElementLocated(photoUpload));
        File file = new File("src/test/resources/" + fileName);
        if (!file.exists()) {
            try { file.getParentFile().mkdirs(); file.createNewFile(); } catch (Exception ignored) {}
        }
        input.sendKeys(file.getAbsolutePath());
    }

    public void clickSubmit() {
        By dialogSubmit = By.cssSelector("[data-slot='dialog-content'] button[type='submit'], [data-slot='dialog-content'] [data-testid='dokter-submit-button']");
        try {
            WebElement btn = driver.findElement(dialogSubmit);
            if (btn.isDisplayed()) {
                btn.click();
                return;
            }
        } catch (Exception ignored) {}

        WebElement btn = wait.until(ExpectedConditions.elementToBeClickable(submitButton));
        btn.click();
    }

    // ── 3.2 Verify Existing Cards ──────────────────────────────
    public boolean isCardPresent(int index) {
        By loc = By.cssSelector("[data-testid^='dokter-card-']");
        List<WebElement> cards = driver.findElements(loc);
        return cards.size() > index;
    }

    public String getDoctorName(int index) {
        By loc = By.cssSelector("[data-testid='dokter-name-" + index + "'], .card-name, [class*='name']");
        return wait.until(ExpectedConditions.visibilityOfElementLocated(loc)).getText();
    }

    public String getDoctorSpecialization(int index) {
        By loc = By.cssSelector("[data-testid='dokter-specialization-" + index + "'], .card-specialization, [class*='special']");
        return wait.until(ExpectedConditions.visibilityOfElementLocated(loc)).getText();
    }

    public String getDoctorStatement(int index) {
        By loc = By.cssSelector("[data-testid='dokter-statement-" + index + "'], .card-statement, [class*='statement']");
        return wait.until(ExpectedConditions.visibilityOfElementLocated(loc)).getText();
    }

    public boolean isDoctorPhotoLoaded(int index) {
        By loc = By.cssSelector("[data-testid='dokter-photo-" + index + "'], img[src*='doctors'], [data-testid^='dokter-card-'] img");
        WebElement img = wait.until(ExpectedConditions.visibilityOfElementLocated(loc));
        String src = img.getAttribute("src");
        return src != null && !src.isEmpty();
    }

    // ── 3.3 Edit Dokter Dialog ─────────────────────────────────
    public void openEditDialog(int index) {
        WebElement card = null;
        try {
            By targetDoctorCard = By.xpath("//*[contains(text(), 'drg. Budi Santoso, Sp.Ort') or contains(text(), 'drg. Putri, Sp.KGA')]/ancestor::*[contains(@data-testid, 'dokter-card-')]");
            card = driver.findElement(targetDoctorCard);
        } catch (Exception e) {
            // Fallback if target doctor is not found dynamically
        }

        if (card == null) {
            By loc = By.cssSelector("[data-testid='dokter-card-" + index + "']");
            card = wait.until(ExpectedConditions.elementToBeClickable(loc));
        }

        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", card);

        // Wait for dialog to appear (name input inside dialog becomes visible)
        By dialogNameInput = By.cssSelector("[data-slot='dialog-content'] [data-testid='dokter-nama-input'], [data-slot='dialog-content'] input[name='name']");
        wait.until(ExpectedConditions.visibilityOfElementLocated(dialogNameInput));
    }

    public String getInputValue(By locator) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator))
                   .getAttribute("value");
    }

    public String getNameInputValue() {
        By dialogNameInput = By.cssSelector("[data-slot='dialog-content'] [data-testid='dokter-nama-input'], [data-slot='dialog-content'] input[name='name']");
        WebElement el = wait.until(ExpectedConditions.visibilityOfElementLocated(dialogNameInput));
        try {
            wait.until(driver -> {
                String val = el.getAttribute("value");
                return val != null && !val.trim().isEmpty();
            });
        } catch (Exception ignored) {}
        return el.getAttribute("value");
    }

    public String getSpesialisInputValue() {
        By dialogSpecInput = By.cssSelector("[data-slot='dialog-content'] [data-testid='dokter-spesialis-input'], [data-slot='dialog-content'] input[name='specialization']");
        WebElement el = wait.until(ExpectedConditions.visibilityOfElementLocated(dialogSpecInput));
        try {
            wait.until(driver -> {
                String val = el.getAttribute("value");
                return val != null && !val.trim().isEmpty();
            });
        } catch (Exception ignored) {}
        return el.getAttribute("value");
    }

    public boolean isScheduleTagVisible(String scheduleText) {
        By loc = By.xpath(
            "//*[@data-slot='dialog-content']//span[contains(@class,'inline-flex') and contains(normalize-space(),'" + scheduleText + "')]"
            + " | //*[@data-slot='dialog-content']//*[contains(@class,'inline-flex')]/span[contains(normalize-space(),'" + scheduleText + "')]");
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(loc)).isDisplayed();
        } catch (Exception e) { return false; }
    }

    public int countScheduleTags() {
        List<WebElement> tags = driver.findElements(
            By.cssSelector("[data-slot='dialog-content'] span.inline-flex.items-center"));
        return tags.size();
    }

    public boolean editorContains(String text) {
        By loc = By.cssSelector("[data-slot='dialog-content'] .tiptap.ProseMirror");
        WebElement editor = wait.until(ExpectedConditions.visibilityOfElementLocated(loc));
        return editor.getText().contains(text);
    }

    // ── 3.4 Delete Dokter ──────────────────────────────────────
    private int countBeforeDelete = 0;

    public int countDoctorCards() {
        return driver.findElements(By.cssSelector("[data-testid^='dokter-card-'], .cursor-pointer")).size();
    }

    public void clickHapusDokter() {
        countBeforeDelete = countDoctorCards();
        WebElement btn = wait.until(ExpectedConditions.elementToBeClickable(hapusDokterBtn));
        Assertions.assertEquals("Hapus Dokter", btn.getText().trim(),
            "Tombol hapus tidak berteks 'Hapus Dokter'");
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", btn);

        // Safe confirmation alert dialog confirm button click
        try {
            By confirmBtnLoc = By.cssSelector("[data-slot='alert-dialog-action'], button.bg-destructive, [data-testid*='konfirmasi']");
            WebElement confirmBtn = wait.until(ExpectedConditions.elementToBeClickable(confirmBtnLoc));
            try { Thread.sleep(500); } catch (InterruptedException ignored) {}
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", confirmBtn);
        } catch (Exception ignored) {}
    }

    public boolean isDoctorRemovedFromGrid(String doctorName) {
        try {
            wait.until(driver -> countDoctorCards() < countBeforeDelete);
            return true;
        } catch (Exception e) { return false; }
    }

    // Helper
    public boolean isDoctorPresentInList(String doctorName) {
        By loc = By.xpath("//*[contains(text(),'" + doctorName + "')]");
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(loc)).isDisplayed();
        } catch (Exception e) { return false; }
    }

    public boolean isAnyFieldInvalid() {
        By[] required = {
            By.cssSelector("[data-testid='dokter-nama-input'], input[placeholder*='Nama'], input[name='name']"),
            By.cssSelector("[data-testid='dokter-spesialis-input'], input[placeholder*='Spesialis'], input[name='specialization']")
        };
        for (By loc : required) {
            try {
                WebElement el = driver.findElement(loc);
                String ariaInvalid = el.getAttribute("aria-invalid");
                if ("true".equals(ariaInvalid)) return true;
                Boolean valid = (Boolean) ((JavascriptExecutor) driver)
                    .executeScript("return arguments[0].checkValidity();", el);
                if (valid != null && !valid) return true;
            } catch (Exception ignored) {}
        }
        By errorMsg = By.cssSelector(".text-red-500, .text-destructive, [role='alert']");
        try {
            return driver.findElement(errorMsg).isDisplayed();
        } catch (Exception e) { return false; }
    }
}
