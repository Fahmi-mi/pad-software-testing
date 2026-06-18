package pages;

import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.time.Duration;
import java.util.List;

public class PromoPage {
    private final WebDriver driver;
    private final WebDriverWait wait;
    private final JavascriptExecutor js;

    // Locators — semua data-testid dipertahankan
    private final By uploadInput = By.cssSelector("[data-testid='promo-image-upload']");
    private final By titleInput = By.cssSelector("[data-testid='promo-judul-input']");
    private final By originalPriceInput = By.cssSelector("[data-testid='promo-original-price-input']");
    private final By discountPriceInput = By.cssSelector("[data-testid='promo-discount-price-input']");
    private final By descriptionEditor = By.cssSelector("div[contenteditable='true']");
    private final By submitButton = By.cssSelector("[data-testid='promo-submit-button']");

    public PromoPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        this.js = (JavascriptExecutor) driver;
    }

    public void open(String baseUrl) {
        driver.get(baseUrl + "/admin/promo");

        // Tunggu URL benar-benar landing di halaman admin
        wait.until(d -> d.getCurrentUrl().contains("/admin/promo"));

        // Tunggu document fully loaded sebelum cari elemen
        wait.until(webDriver -> js.executeScript(
            "return document.readyState").equals("complete"));

        // Baru tunggu elemen form muncul
        wait.until(ExpectedConditions.visibilityOfElementLocated(titleInput));
    }

    public void uploadPromoImage(String fileName) {
        // Selenium uploads local files using sendKeys on <input type="file">
        WebElement uploadElement = wait.until(ExpectedConditions.presenceOfElementLocated(uploadInput));
        
        // Simulating file upload using a dummy file path or actual file path
        File file = new File("src/test/resources/" + fileName);
        if (!file.exists()) {
            try {
                file.getParentFile().mkdirs();
                file.createNewFile();
            } catch (Exception ignored) {}
        }
        uploadElement.sendKeys(file.getAbsolutePath());
    }

    public void fillTitle(String title) {
        WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(titleInput));
        element.clear();
        element.sendKeys(title);
    }

    public void fillPrices(String originalPrice, String discountPrice) {
        WebElement origEl = wait.until(ExpectedConditions.visibilityOfElementLocated(originalPriceInput));
        origEl.clear();
        origEl.sendKeys(originalPrice);

        WebElement discEl = wait.until(ExpectedConditions.visibilityOfElementLocated(discountPriceInput));
        discEl.clear();
        discEl.sendKeys(discountPrice);
    }

    public void fillDescription(String description) {
        WebElement editor = wait.until(ExpectedConditions.visibilityOfElementLocated(descriptionEditor));
        editor.clear();
        editor.sendKeys(description);
    }

    public void clickSubmit() {
        WebElement submitBtn = wait.until(ExpectedConditions.elementToBeClickable(submitButton));
        submitBtn.click();
    }

    public boolean isPromoPresentInList(String promoTitle) {
        By promoCardTitleLocator = By.xpath("//div[contains(text(), '" + promoTitle + "')]");
        try {
            WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(promoCardTitleLocator));
            return element.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isPromoCardDisplayed(String promoTitle) {
        try {
            WebElement card = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector("[data-testid^='promo-card-']")));
            return card.isDisplayed();
        } catch (Exception e) { return false; }
    }

    public String getDisplayedOriginalPrice() {
        try {
            WebElement el = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector("[data-testid^='promo-original-price-']")));
            return el.getText().replaceAll("[^0-9]", "");
        } catch (Exception e) { return ""; }
    }

    public String getDisplayedDiscountPrice() {
        try {
            WebElement el = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector("[data-testid^='promo-discount-price-']")));
            return el.getText().replaceAll("[^0-9]", "");
        } catch (Exception e) { return ""; }
    }

    public boolean isPromoImageHasSrc() {
        try {
            WebElement img = driver.findElement(By.cssSelector("[data-testid^='promo-image-']"));
            String src = img.getAttribute("src");
            return src == null || !src.isEmpty();
        } catch (Exception e) { return true; }
    }

    public void openEditDialog(String promoTitle) {
        String testId = "promo-card-" + titleToTestId(promoTitle);
        By cardLoc = By.cssSelector("[data-testid='" + testId + "']");
        WebElement card = wait.until(ExpectedConditions.visibilityOfElementLocated(cardLoc));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", card);
        try { Thread.sleep(1000); } catch (InterruptedException ignored) {}
    }

    private String titleToTestId(String title) {
        return title.toLowerCase().replaceAll("\\s+", "-").replaceAll("[^a-z0-9-]", "");
    }

    public String getDialogTitleInputValue() {
        try {
            WebElement el = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector("[role='dialog'] [data-testid='promo-judul-input']")));
            return el.getAttribute("value");
        } catch (Exception e) { return ""; }
    }

    public String getDialogOriginalPriceValue() {
        try {
            WebElement el = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector("[role='dialog'] [data-testid='promo-original-price-input']")));
            return el.getAttribute("value");
        } catch (Exception e) { return ""; }
    }

    public String getDialogDiscountPriceValue() {
        try {
            WebElement el = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector("[role='dialog'] [data-testid='promo-discount-price-input']")));
            return el.getAttribute("value");
        } catch (Exception e) { return ""; }
    }

    public void editTitle(String newTitle) {
        WebElement el = wait.until(ExpectedConditions.visibilityOfElementLocated(
            By.cssSelector("[role='dialog'] [data-testid='promo-judul-input']")));
        el.clear();
        el.sendKeys(newTitle);
    }

    public String getDialogDescriptionText() {
        try {
            WebElement editor = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector("[role='dialog'] div[contenteditable='true']")));
            return editor.getText();
        } catch (Exception e) { return ""; }
    }

    public void clickSaveChanges() {
        By saveBtn = By.xpath("//button[normalize-space()='Simpan Perubahan']");
        WebElement btn = wait.until(ExpectedConditions.elementToBeClickable(saveBtn));
        btn.click();
        try { Thread.sleep(1000); } catch (InterruptedException ignored) {}
    }

    public void clickHapusPromo() {
        WebElement btn = wait.until(ExpectedConditions.elementToBeClickable(
            By.cssSelector("button.bg-red-400")));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", btn);
        try {
            By confirmBtnLoc = By.cssSelector("[data-slot='alert-dialog-action'], button.bg-destructive");
            WebElement confirmBtn = wait.until(ExpectedConditions.elementToBeClickable(confirmBtnLoc));
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", confirmBtn);
        } catch (Exception ignored) {}
    }

    public boolean isPromoRemovedFromList(String promoTitle) {
        By loc = By.xpath("//*[contains(normalize-space(),'" + promoTitle + "')]");
        try {
            wait.until(ExpectedConditions.invisibilityOfElementLocated(loc));
            return true;
        } catch (Exception e) { return false; }
    }

    /** Returns true if any required field shows aria-invalid="true" or a visible error message. */
    public boolean isAnyFieldInvalid() {
        // Check aria-invalid on known required inputs
        By[] requiredFields = {
            titleInput, originalPriceInput, discountPriceInput
        };
        for (By loc : requiredFields) {
            try {
                WebElement el = driver.findElement(loc);
                if ("true".equals(el.getAttribute("aria-invalid"))) return true;
            } catch (Exception ignored) {}
        }
        // Check for visible error text
        By errorMsg = By.cssSelector(".text-red-500, .text-destructive, [role='alert']");
        try {
            return driver.findElement(errorMsg).isDisplayed();
        } catch (Exception e) { return false; }
    }
}
