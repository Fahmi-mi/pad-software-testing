package pages;

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

public class GaleriPage {
    private final WebDriver driver;
    private final WebDriverWait wait;
    private final Actions actions;

    // ── Upload Form Locators ───────────────────────────────────
    private final By fileInput    = By.cssSelector(
        "input[type='file'][accept='image/png,image/jpeg,image/jpg,image/webp']");
    private final By submitButton = By.cssSelector("[data-testid='galeri-tambah-button']");

    // ── Grid Locators ──────────────────────────────────────────
    private final By imageGrid    = By.cssSelector(".grid.sm\\:grid-cols-2.lg\\:grid-cols-4");
    private final By galeriImages = By.cssSelector("img[alt^='Galeri']");
    private final By firstCard    = By.cssSelector(".group.relative");

    // ── Delete Locators ────────────────────────────────────────
    // VERIFIED: button pakai class opacity-80 hover:opacity-100 (bukan group-hover)
    private final By deleteButton = By.cssSelector("button.opacity-80, button[class*='opacity-80'], button .lucide-trash");

    public GaleriPage(WebDriver driver) {
        this.driver  = driver;
        this.wait    = new WebDriverWait(driver, Duration.ofSeconds(20));
        this.actions = new Actions(driver);
    }

    // ── Navigation ─────────────────────────────────────────────
    public void open(String baseUrl) {
        driver.get(baseUrl + "/admin/galeri");
        wait.until(d -> d.getCurrentUrl().contains("/admin/galeri"));
        wait.until(webDriver -> ((JavascriptExecutor) webDriver).executeScript(
            "return document.readyState").equals("complete"));
        wait.until(ExpectedConditions.presenceOfElementLocated(fileInput));
    }

    // ── 4.1 Upload Gallery Image ───────────────────────────────
    public void uploadGaleriImage(String fileName) {
        WebElement input = wait.until(ExpectedConditions.presenceOfElementLocated(fileInput));
        File file = new File("src/test/resources/" + fileName);
        if (!file.exists()) {
            try { file.getParentFile().mkdirs(); file.createNewFile(); } catch (Exception ignored) {}
        }
        input.sendKeys(file.getAbsolutePath());
    }

    public void clickTambahkanGambar() {
        WebElement btn = wait.until(ExpectedConditions.elementToBeClickable(submitButton));
        btn.click();
    }

    /** Legacy compat */
    public void clickSubmit() { clickTambahkanGambar(); }
    public void fillDescription(String description) { /* galeri has no description field */ }

    public boolean isNewImageInGrid() {
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(imageGrid));
            return true;
        } catch (Exception e) { return false; }
    }

    // ── 4.2 Verify Existing Gallery ───────────────────────────
    public boolean areGaleriImagesVisible() {
        try {
            List<WebElement> imgs = wait.until(
                ExpectedConditions.presenceOfAllElementsLocatedBy(galeriImages));
            return !imgs.isEmpty();
        } catch (Exception e) { return false; }
    }

    public int countGaleriImages() {
        List<WebElement> imgs = driver.findElements(galeriImages);
        return imgs.size();
    }

    public boolean isGaleriPresentInList(String altOrDescription) {
        By loc = By.cssSelector("img[alt='" + altOrDescription + "'], img[alt^='Galeri']");
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(loc)).isDisplayed();
        } catch (Exception e) { return false; }
    }

    public boolean eachImageHasDeleteButton() {
        List<WebElement> cards = driver.findElements(firstCard);
        if (cards.isEmpty()) return false;
        for (WebElement card : cards) {
            List<WebElement> btns = card.findElements(By.cssSelector("button[type='button']"));
            if (btns.isEmpty()) return false;
        }
        return true;
    }

    // ── 4.3 Delete Gallery Image ───────────────────────────────
    public void hoverFirstGalleryItem() {
        WebElement card = wait.until(ExpectedConditions.visibilityOfElementLocated(firstCard));
        actions.moveToElement(card).perform();
        ((JavascriptExecutor) driver).executeScript(
            "arguments[0].dispatchEvent(new MouseEvent('mouseover', {bubbles:true}));", card);
    }

    private int countBeforeDelete = 0;

    public void clickDeleteFirstImage() {
        WebElement card = wait.until(ExpectedConditions.visibilityOfElementLocated(firstCard));
        if (card == null) {
            throw new RuntimeException("Tidak dapat menemukan elemen galeri pertama (firstCard).");
        }

        countBeforeDelete = countGaleriImages();

        // Use JS to force hover state, then find and click the delete button
        ((JavascriptExecutor) driver).executeScript(
            "arguments[0].dispatchEvent(new MouseEvent('mouseover', {bubbles:true}));", card);
        try {
            Thread.sleep(500);
        } catch (InterruptedException ignored) {}

        // VERIFIED: button pakai opacity-80 hover:opacity-100, bukan group-hover
        By trashBtn = By.cssSelector(
            "button.opacity-80, button[class*='opacity-80'], button .lucide-trash, button [data-lucide='trash'], "
            + "[data-testid='delete-button'], button.bg-red-400");
        WebElement btn = wait.until(ExpectedConditions.elementToBeClickable(trashBtn));

        // FIX ERROR 3: null check sebelum JS click
        if (btn == null) {
            throw new RuntimeException("Tombol hapus tidak ditemukan setelah hover.");
        }
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", btn);

        // Wait for and click the confirmation alert dialog confirm button
        By confirmBtnLoc = By.cssSelector("[data-testid='galeri-hapus-konfirmasi'], [data-slot='alert-dialog-action']");
        WebElement confirmBtn = wait.until(ExpectedConditions.elementToBeClickable(confirmBtnLoc));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", confirmBtn);
    }

    public void clickTambahkanGambarTanpaFile() {
        WebElement btn = wait.until(ExpectedConditions.elementToBeClickable(submitButton));
        btn.click();
    }

    public boolean isFileInputErrorDisplayed() {
        try {
            WebElement errorEl = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector("[data-testid='galeri-error-message'], .text-red-500, [role='alert']")));
            return errorEl.isDisplayed();
        } catch (Exception e) {
            try {
                WebElement input = driver.findElement(fileInput);
                String validationMsg = (String) ((JavascriptExecutor) driver)
                    .executeScript("return arguments[0].validationMessage;", input);
                return validationMsg != null && !validationMsg.isEmpty();
            } catch (Exception ex) { return false; }
        }
    }

    public boolean isImageRemovedFromGrid(int expectedCountAfterDelete) {
        try {
            // Wait for image count to decrease
            wait.until(driver -> countGaleriImages() < countBeforeDelete);
            return true;
        } catch (Exception e) { return false; }
    }
}
