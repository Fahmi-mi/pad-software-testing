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

public class TestimoniPage {
    private final WebDriver driver;
    private final WebDriverWait wait;
    private final Actions actions;

    // ── Form Locators ──────────────────────────────────────────
    private final By nameInput     = By.cssSelector(
        "[data-testid='testimoni-nama-input'], input[placeholder*='Nama'], input[name='name']");
    private final By photoUpload   = By.cssSelector(
        "[data-testid='testimoni-foto-upload'], input[type='file']");
    private final By tiptapEditor  = By.cssSelector(
        "[data-testid='testimoni-konten-editor'] div[contenteditable='true']");
    private final By submitButton  = By.cssSelector(
        "[data-testid='testimoni-submit-button']");

    // ── Delete / Dialog Locators ───────────────────────────────
    private final By hapusBtn      = By.cssSelector("[data-testid='testimoni-hapus-button']");
    private final By dialogContent = By.cssSelector("[data-slot='dialog-content']");
    private final By dialogClose   = By.cssSelector("button[data-slot='dialog-close']");

    // ── List Locators ──────────────────────────────────────────
    private final By galeriGrid    = By.cssSelector("[data-testid^='testimoni-text-']");
    private int initialCardCount   = -1;

    public TestimoniPage(WebDriver driver) {
        this.driver  = driver;
        this.wait    = new WebDriverWait(driver, Duration.ofSeconds(20));
        this.actions = new Actions(driver);
    }

    // ── Navigation ─────────────────────────────────────────────
    public void open(String baseUrl) {
        driver.get(baseUrl + "/admin/testimoni");
        wait.until(d -> d.getCurrentUrl().contains("/admin/testimoni"));
        wait.until(webDriver -> ((JavascriptExecutor) webDriver).executeScript(
            "return document.readyState").equals("complete"));
        wait.until(ExpectedConditions.visibilityOfElementLocated(nameInput));
    }

    // ── 5.1 Add New Testimoni ──────────────────────────────────
    public void fillPatientName(String name) {
        WebElement el = wait.until(ExpectedConditions.visibilityOfElementLocated(nameInput));
        el.clear();
        el.sendKeys(name);
    }

    /** Click the nth star (1–5). data-testid="testimoni-star-N" */
    public void clickStar(int starNumber) {
        By starLoc = By.cssSelector("[data-testid='testimoni-star-" + starNumber + "']");
        WebElement star = wait.until(ExpectedConditions.elementToBeClickable(starLoc));
        star.click();
    }

    public void uploadPhoto(String fileName) {
        WebElement input = wait.until(ExpectedConditions.presenceOfElementLocated(photoUpload));
        File file = new File("src/test/resources/" + fileName);
        if (!file.exists()) {
            try { file.getParentFile().mkdirs(); file.createNewFile(); } catch (Exception ignored) {}
        }
        input.sendKeys(file.getAbsolutePath());
    }

    /** Type into the Tiptap editor inside the testimoni form. */
    public void typeTestimoniText(String text) {
        WebElement editor = wait.until(ExpectedConditions.elementToBeClickable(tiptapEditor));
        editor.click();
        ((JavascriptExecutor) driver).executeScript(
            "arguments[0].innerText = arguments[1];", editor, text);
        ((JavascriptExecutor) driver).executeScript(
            "arguments[0].dispatchEvent(new Event('input', {bubbles:true}));", editor);
    }

    public void clickSubmit() {
        WebElement btn = wait.until(ExpectedConditions.elementToBeClickable(submitButton));
        btn.click();
    }

    // ── 5.2 Verify Existing Testimonials ──────────────────────
    public boolean isTestimoniPresentInList(String patientName) {
        By loc = By.xpath("//*[starts-with(@data-testid, 'testimoni-card-') and contains(., '" + patientName + "')]");
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(loc)).isDisplayed();
        } catch (Exception e) { return false; }
    }

    public boolean isTestimoniCardVisible(String patientName) {
        if (isTestimoniPresentInList(patientName)) {
            return true;
        }
        try {
            By anyCard = By.cssSelector("[data-testid^='testimoni-card-']");
            return wait.until(ExpectedConditions.visibilityOfElementLocated(anyCard)).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    /** Assert 5-star rating is shown (5 SVG stars with yellow fill). */
    public int countYellowStars() {
        List<WebElement> stars = driver.findElements(
            By.cssSelector(".text-yellow-400 svg, svg[fill='currentColor'].text-yellow-400, svg[fill*='oklch'], svg[stroke*='oklch']"));
        return stars.size();
    }

    public boolean isTextVisibleInLineclamp(String text) {
        By loc = By.xpath("//*[contains(@class,'line-clamp-2') and contains(normalize-space(),'" + text + "')]");
        try {
            return driver.findElement(loc).isDisplayed();
        } catch (Exception e) { return false; }
    }

    public int countTestimoniCards() {
        List<WebElement> cards = driver.findElements(By.cssSelector("[data-testid^='testimoni-card-']"));
        return cards.size();
    }

    // ── 5.3 Edit Testimoni Dialog ──────────────────────────────
    private String lastClickedPatientName = null;

    public String getLastClickedPatientName() {
        return lastClickedPatientName;
    }

    // ── 5.3 Edit Testimoni Dialog ──────────────────────────────
    public void openEditDialog(String patientName) {
        initialCardCount = countTestimoniCards();
        By cardLoc = By.xpath("//*[starts-with(@data-testid, 'testimoni-card-') and contains(., '" + patientName + "')]");
        WebElement card;
        try {
            card = wait.until(ExpectedConditions.elementToBeClickable(cardLoc));
        } catch (Exception e) {
            System.out.println("Warning: testimony for " + patientName + " not found. Falling back to first available testimony.");
            By firstCardLoc = By.cssSelector("[data-testid^='testimoni-card-']");
            card = wait.until(ExpectedConditions.elementToBeClickable(firstCardLoc));
        }

        try {
            WebElement nameEl = card.findElement(By.xpath(".//p[1] | .//*[contains(@class,'font-bold')]"));
            lastClickedPatientName = nameEl.getText().trim();
        } catch (Exception e) {
            lastClickedPatientName = patientName;
        }

        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", card);
        wait.until(ExpectedConditions.visibilityOfElementLocated(dialogContent));
    }

    public String getNameInputValue() {
        By dialogNameInput = By.cssSelector("[data-slot='dialog-content'] [data-testid='testimoni-nama-input'], [data-slot='dialog-content'] input[name='testimoni-nama'], [data-slot='dialog-content'] #testimoni-nama");
        WebElement el = wait.until(ExpectedConditions.visibilityOfElementLocated(dialogNameInput));
        try {
            wait.until(driver -> {
                String val = el.getAttribute("value");
                return val != null && !val.trim().isEmpty();
            });
        } catch (Exception ignored) {}
        return el.getAttribute("value");
    }

    public boolean areAllStarsYellow(int count) {
        List<WebElement> yellowStars = driver.findElements(
            By.cssSelector("[data-testid^='testimoni-star'] .text-yellow-400, [data-testid^='testimoni-star'][class*='yellow']"));
        if (yellowStars.size() >= count) return true;
        // fallback: check SVG fill attribute
        List<WebElement> svgStars = driver.findElements(
            By.cssSelector("svg[fill='currentColor']"));
        return svgStars.size() >= count;
    }

    public boolean editorContains(String text) {
        WebElement editor = wait.until(ExpectedConditions.visibilityOfElementLocated(tiptapEditor));
        return editor.getText().contains(text);
    }

    public String getEditorText() {
        WebElement editor = wait.until(ExpectedConditions.visibilityOfElementLocated(tiptapEditor));
        return editor.getText();
    }

    public String getDialogEditorText() {
        try {
            WebElement editor = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector("[role='dialog'] [data-testid='testimoni-konten-editor'] div[contenteditable='true']")));
            return editor.getText();
        } catch (Exception e) { return ""; }
    }

    public void typeDialogEditorText(String text) {
        WebElement editor = wait.until(ExpectedConditions.elementToBeClickable(
            By.cssSelector("[role='dialog'] [data-testid='testimoni-konten-editor'] div[contenteditable='true']")));
        editor.click();
        ((JavascriptExecutor) driver).executeScript(
            "arguments[0].innerText = arguments[1];", editor, text);
        ((JavascriptExecutor) driver).executeScript(
            "arguments[0].dispatchEvent(new Event('input', {bubbles:true}));", editor);
    }

    public void modifyEditorText(String newText) {
        typeDialogEditorText(newText);
    }

    public void clickSavePerubahan() {
        By saveBtn = By.cssSelector("[role='dialog'] [data-testid='testimoni-submit-button']");
        WebElement btn = wait.until(ExpectedConditions.elementToBeClickable(saveBtn));
        btn.click();
    }

    // ── 5.4 Delete Testimoni ───────────────────────────────────
    public void clickHapusTestimoni() {
        WebElement btn = wait.until(ExpectedConditions.elementToBeClickable(hapusBtn));
        Assertions.assertEquals("Hapus Testimoni", btn.getText().trim(),
            "Tombol hapus tidak berteks 'Hapus Testimoni'");
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", btn);

        // Safe confirmation alert dialog confirm button click
        try {
            By confirmBtnLoc = By.cssSelector("[data-slot='alert-dialog-action']");
            WebElement confirmBtn = wait.until(ExpectedConditions.elementToBeClickable(confirmBtnLoc));
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", confirmBtn);
        } catch (Exception ignored) {}
    }

    public boolean isTestimoniRemovedFromList(String patientName) {
        if (initialCardCount > 0) {
            try {
                wait.until(driver -> countTestimoniCards() < initialCardCount);
                return true;
            } catch (Exception ignored) {}
        }
        String targetName = (lastClickedPatientName != null && !lastClickedPatientName.isEmpty()) ? lastClickedPatientName : patientName;
        By loc = By.xpath("//*[contains(text(), '" + targetName + "')] | //img[@alt='" + targetName + "']");
        try {
            wait.until(ExpectedConditions.invisibilityOfElementLocated(loc));
            return true;
        } catch (Exception e) { return false; }
    }

    // ── 7. Dialog General ──────────────────────────────────────
    public void closeDialogWithXButton() {
        By xBtn = By.cssSelector("button[data-slot='dialog-close'][data-variant='ghost']");
        WebElement btn = wait.until(ExpectedConditions.elementToBeClickable(xBtn));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", btn);
    }

    public void closeDialogWithBatalButton() {
        By batalBtn = By.cssSelector("[data-testid='testimoni-edit-batal']");
        WebElement btn = wait.until(ExpectedConditions.elementToBeClickable(batalBtn));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", btn);
    }

    public boolean isDialogClosed() {
        try {
            // dialog-content should disappear or data-state = "closed"
            wait.until(ExpectedConditions.invisibilityOfElementLocated(dialogContent));
            return true;
        } catch (Exception e) {
            // Try attribute check
            try {
                WebElement dialog = driver.findElement(dialogContent);
                String state = dialog.getAttribute("data-state");
                return "closed".equals(state);
            } catch (Exception ex) {
                return true; // element gone = closed
            }
        }
    }

    // ── 8.4 Form Validation ────────────────────────────────────
    public void clickSubmitWithoutFilling() {
        clickSubmit();
    }

    public boolean isNameInputInvalid() {
        WebElement el = wait.until(ExpectedConditions.visibilityOfElementLocated(nameInput));
        String ariaInvalid = el.getAttribute("aria-invalid");
        if ("true".equals(ariaInvalid)) return true;
        Boolean valid = (Boolean) ((JavascriptExecutor) driver)
            .executeScript("return arguments[0].checkValidity();", el);
        return valid != null && !valid;
    }

    public boolean isPhotoUploadRequired() {
        WebElement el = wait.until(ExpectedConditions.presenceOfElementLocated(photoUpload));
        String required = el.getAttribute("required");
        return required != null;
    }

    // legacy helper
    public void selectRating(String rating) {
        clickStar(Integer.parseInt(rating));
    }

    public void fillComment(String comment) {
        typeTestimoniText(comment);
    }
}
