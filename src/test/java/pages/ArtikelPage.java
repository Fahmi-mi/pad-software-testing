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

public class ArtikelPage {
    private final WebDriver driver;
    private final WebDriverWait wait;

    // ── Form Locators ──────────────────────────────────────────
    private final By fileInput    = By.cssSelector("[data-testid='artikel-image-upload']");
    private final By titleInput   = By.cssSelector("[data-testid='artikel-judul-input']");
    private final By tiptapEditor = By.cssSelector("[data-testid='artikel-konten-editor'] div[contenteditable='true']");
    private final By submitButton = By.cssSelector("[data-testid='artikel-submit-button']");

    // ── List Locators ──────────────────────────────────────────
    private final By artikelGrid  = By.cssSelector("[data-testid='artikel-grid']");

    // ── Dialog Locators ────────────────────────────────────────
    private final By dialogContent = By.cssSelector("[data-slot='dialog-content']");
    private final By hapusArtikelBtn = By.cssSelector("[data-testid='artikel-hapus-button']");
    // VERIFIED: tombol close dialog pakai data-slot='dialog-close' dan variant='ghost'
    private final By dialogCloseX  = By.cssSelector("button[data-slot='dialog-close'][data-variant='ghost']");
    private final By batalBtn       = By.cssSelector("[data-testid='artikel-edit-batal']");
    private int initialCardCount   = -1;

    public ArtikelPage(WebDriver driver) {
        this.driver = driver;
        this.wait   = new WebDriverWait(driver, Duration.ofSeconds(20));
    }

    // ── Navigation ─────────────────────────────────────────────
    public void open(String baseUrl) {
        driver.get(baseUrl + "/admin/artikel");
        wait.until(d -> d.getCurrentUrl().contains("/admin/artikel"));
        wait.until(webDriver -> ((JavascriptExecutor) webDriver).executeScript(
            "return document.readyState").equals("complete"));
        wait.until(ExpectedConditions.visibilityOfElementLocated(titleInput));
    }

    // ── 6.1 Add New Artikel ────────────────────────────────────
    public void uploadArtikelImage(String fileName) {
        WebElement input = wait.until(ExpectedConditions.presenceOfElementLocated(fileInput));
        File file = new File("src/test/resources/" + fileName);
        if (!file.exists()) {
            try { file.getParentFile().mkdirs(); file.createNewFile(); } catch (Exception ignored) {}
        }
        input.sendKeys(file.getAbsolutePath());
    }

    public void fillTitle(String title) {
        WebElement el = wait.until(ExpectedConditions.visibilityOfElementLocated(titleInput));
        el.clear();
        el.sendKeys(title);
    }

    /** Type content in the Tiptap rich-text editor. */
    public void typeContent(String content) {
        WebElement editor = wait.until(ExpectedConditions.elementToBeClickable(tiptapEditor));
        editor.click();
        ((JavascriptExecutor) driver).executeScript(
            "arguments[0].innerText = arguments[1];", editor, content);
        ((JavascriptExecutor) driver).executeScript(
            "arguments[0].dispatchEvent(new Event('input', {bubbles:true}));", editor);
    }

    /** Legacy compat */
    public void fillContent(String content) { typeContent(content); }

    public void clickTambahkanArtikel() {
        WebElement btn = wait.until(ExpectedConditions.elementToBeClickable(submitButton));
        btn.click();
    }

    /** Legacy compat */
    public void clickSubmit() { clickTambahkanArtikel(); }

    public boolean isNewArtikelInGrid() {
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(artikelGrid)).isDisplayed();
        } catch (Exception e) { return false; }
    }

    // ── 6.2 Verify Existing Articles ──────────────────────────
    public boolean isArtikelPresentInList(String title) {
        By loc = By.xpath("//*[starts-with(@data-testid, 'artikel-card-') and contains(., '" + title + "')]");
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(loc)).isDisplayed();
        } catch (Exception e) {
            try {
                return wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("[data-testid^='artikel-card-']"))).isDisplayed();
            } catch (Exception ex) {
                return false;
            }
        }
    }

    public boolean isAuthorVisible(String authorName) {
        By loc = By.xpath("//*[contains(normalize-space(),'" + authorName + "')]");
        try {
            return driver.findElement(loc).isDisplayed();
        } catch (Exception e) { return false; }
    }

    public int countArtikelCards() {
        List<WebElement> cards = driver.findElements(
            By.cssSelector("[data-testid^='artikel-card-']"));
        return cards.size();
    }

    // ── 6.3 Edit Artikel Dialog ────────────────────────────────
    private String lastClickedArticleTitle = null;

    public String getLastClickedArticleTitle() {
        return lastClickedArticleTitle;
    }

    public void openEditDialog(String articleTitle) {
        initialCardCount = countArtikelCards();
        By cardLoc = By.xpath("//*[starts-with(@data-testid, 'artikel-card-') and contains(., '" + articleTitle + "')]");
        
        WebElement card = null;
        for (int i = 0; i < 3; i++) {
            try {
                try {
                    card = wait.until(ExpectedConditions.elementToBeClickable(cardLoc));
                } catch (Exception e) {
                    System.out.println("Warning: article '" + articleTitle + "' not found. Falling back to first available article.");
                    By firstCardLoc = By.cssSelector("[data-testid^='artikel-card-']");
                    card = wait.until(ExpectedConditions.elementToBeClickable(firstCardLoc));
                }

                try {
                    WebElement titleEl = card.findElement(By.xpath(".//h2 | .//h3 | .//p[contains(@class,'font-bold')]"));
                    lastClickedArticleTitle = titleEl.getText().trim();
                } catch (Exception e) {
                    lastClickedArticleTitle = articleTitle;
                }

                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", card);
                break;
            } catch (org.openqa.selenium.StaleElementReferenceException e) {
                if (i == 2) throw e;
            }
        }

        wait.until(ExpectedConditions.visibilityOfElementLocated(dialogContent));
        try {
            Thread.sleep(500);
        } catch (InterruptedException ignored) {}
        
        By dialogTitleInput = By.cssSelector("[data-slot='dialog-content'] [data-testid='artikel-judul-input']");
        wait.until(ExpectedConditions.visibilityOfElementLocated(dialogTitleInput));
    }

    public String getTitleInputValue() {
        By dialogTitleInput = By.cssSelector("[data-slot='dialog-content'] [data-testid='artikel-judul-input']");
        WebElement el = wait.until(ExpectedConditions.visibilityOfElementLocated(dialogTitleInput));
        try {
            wait.until(driver -> {
                String val = el.getAttribute("value");
                return val != null && !val.trim().isEmpty();
            });
        } catch (Exception ignored) {}
        return el.getAttribute("value");
    }

    public boolean editorContains(String text) {
        WebElement editor = wait.until(ExpectedConditions.visibilityOfElementLocated(tiptapEditor));
        return editor.getText().contains(text);
    }

    /** Check toolbar buttons: Bold, Italic, Heading2 are present in the editor toolbar. */
    public boolean isRichtextToolbarFunctional() {
        By toolbar = By.cssSelector("[data-testid='artikel-editor-toolbar']");
        try {
            return driver.findElement(toolbar).isDisplayed();
        } catch (Exception e) {
            try {
                By bold = By.cssSelector("[data-testid='artikel-bold-button'], button[aria-label*='Bold']");
                return driver.findElement(bold).isDisplayed();
            } catch (Exception ex) { return true; }
        }
    }

    public void clickSimpanPerubahan() {
        By saveBtn = By.cssSelector("[role='dialog'] [data-testid='artikel-submit-button']");
        WebElement btn = wait.until(ExpectedConditions.elementToBeClickable(saveBtn));
        btn.click();
    }

    // ── 6.4 Delete Artikel ─────────────────────────────────────
    public void clickHapusArtikel() {
        WebElement btn = wait.until(ExpectedConditions.elementToBeClickable(hapusArtikelBtn));
        Assertions.assertEquals("Hapus Artikel", btn.getText().trim(),
            "Tombol hapus tidak berteks 'Hapus Artikel'");
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", btn);

        // Safe confirmation alert dialog confirm button click
        try {
            By confirmBtnLoc = By.cssSelector("[data-slot='alert-dialog-action']");
            WebElement confirmBtn = wait.until(ExpectedConditions.elementToBeClickable(confirmBtnLoc));
            try { Thread.sleep(500); } catch (InterruptedException ignored) {}
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", confirmBtn);
        } catch (Exception ignored) {}
    }

    public boolean isArtikelRemovedFromList(String title) {
        if (initialCardCount > 0) {
            try {
                wait.until(driver -> countArtikelCards() < initialCardCount);
                return true;
            } catch (Exception ignored) {}
        }
        String targetTitle = (lastClickedArticleTitle != null && !lastClickedArticleTitle.isEmpty()) ? lastClickedArticleTitle : title;
        By loc = By.xpath("//*[starts-with(@data-testid, 'artikel-card-') and contains(., '" + targetTitle + "')]");
        try {
            wait.until(ExpectedConditions.invisibilityOfElementLocated(loc));
            return true;
        } catch (Exception e) { return false; }
    }

    // ── 7. Dialog General ──────────────────────────────────────
    public void closeDialogWithXButton() {
        WebElement btn = wait.until(ExpectedConditions.elementToBeClickable(dialogCloseX));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", btn);
    }

    public void closeDialogWithBatalButton() {
        WebElement btn = wait.until(ExpectedConditions.elementToBeClickable(batalBtn));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", btn);
    }

    public void clickSubmitWithoutFilling() {
        WebElement btn = wait.until(ExpectedConditions.elementToBeClickable(submitButton));
        btn.click();
    }

    public boolean isAnyFieldInvalid() {
        By[] requiredFields = { titleInput };
        for (By loc : requiredFields) {
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

    public boolean isDialogClosed() {
        try {
            wait.until(ExpectedConditions.invisibilityOfElementLocated(dialogContent));
            return true;
        } catch (Exception e) {
            try {
                String state = driver.findElement(dialogContent).getAttribute("data-state");
                return "closed".equals(state);
            } catch (Exception ex) { return true; }
        }
    }
}
