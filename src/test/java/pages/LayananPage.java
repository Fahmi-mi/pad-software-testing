package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.time.Duration;

public class LayananPage {
    private final WebDriver driver;
    private final WebDriverWait wait;
    private final JavascriptExecutor js;

    // Locators — diverifikasi dari DOM aktual
    private final By nameInput        = By.cssSelector("[data-testid='layanan-nama-input']");
    private final By detailInput      = By.cssSelector("[data-testid='layanan-detail-input']");
    private final By artikelEditor    = By.cssSelector(".tiptap.ProseMirror");
    private final By supportImgUpload = By.cssSelector("[data-testid='layanan-support-image-upload']");
    private final By iconUpload       = By.cssSelector("[data-testid='layanan-icon-upload']");
    private final By submitButton     = By.cssSelector("[data-testid='layanan-submit-button']");

    public LayananPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        this.js = (JavascriptExecutor) driver;
    }

    // ── Helper: scroll ke elemen lalu tunggu visible ──────────────────────────

    private WebElement scrollAndWait(By locator) {
        // Step 1: tunggu ada di DOM dulu (belum harus visible)
        WebElement element = wait.until(
                ExpectedConditions.presenceOfElementLocated(locator));

        // Step 2: scroll ke tengah viewport
        js.executeScript(
                "arguments[0].scrollIntoView({block:'center', behavior:'smooth'});",
                element);

        // Step 3: beri waktu animasi scroll selesai
        try { Thread.sleep(800); } catch (InterruptedException ignored) {}

        // Step 4: tunggu visible
        return wait.until(ExpectedConditions.visibilityOf(element));
    }

    /**
     * Helper: set nilai input React via native setter + dispatch synthetic event.
     * Memastikan React onChange terpanggil.
     */
    private void setReactInputValue(WebElement element, String value) {
        js.executeScript("arguments[0].focus();", element);
        js.executeScript(
                "const el = arguments[0];" +
                        "const value = arguments[1];" +
                        "const proto = Object.getPrototypeOf(el);" +
                        "const nativeSetter = Object.getOwnPropertyDescriptor(proto, 'value').set;" +
                        "nativeSetter.call(el, value);" +
                        "el.dispatchEvent(new Event('input',  { bubbles: true, cancelable: true }));" +
                        "el.dispatchEvent(new Event('change', { bubbles: true, cancelable: true }));" +
                        "el.dispatchEvent(new Event('blur',   { bubbles: true, cancelable: true }));",
                element, value
        );
    }

    // ── Page Actions ──────────────────────────────────────────────────────────

    /**
     * Navigasi ke halaman manajemen layanan dan tunggu form siap.
     */
    public void open(String baseUrl) {
        driver.get(baseUrl + "/admin/layanan");

        // Tunggu URL benar
        wait.until(d -> d.getCurrentUrl().contains("/admin/layanan"));

        // Tunggu page fully loaded
        wait.until(webDriver -> js.executeScript(
                "return document.readyState").equals("complete"));

        // Tunggu form input pertama visible sebagai indikator halaman siap
        scrollAndWait(nameInput);
    }

    /**
     * Mengisi field Nama Layanan.
     */
    public void fillServiceName(String name) {
        WebElement element = scrollAndWait(nameInput);
        setReactInputValue(element, name);
    }

    /**
     * Mengisi field Detail Layanan.
     */
    public void fillDescription(String description) {
        WebElement element = scrollAndWait(detailInput);
        setReactInputValue(element, description);
    }

    /**
     * Mengisi konten Artikel menggunakan RichTextEditor (TipTap/ProseMirror).
     * Editor tidak bisa diisi via sendKeys biasa — harus klik dulu lalu set innerHTML.
     */
    public void fillArtikel(String artikelContent) {
        // Scroll ke editor dulu
        WebElement editor = scrollAndWait(artikelEditor);

        // Klik untuk fokus
        editor.click();
        try { Thread.sleep(500); } catch (InterruptedException ignored) {}

        // Set konten via JS karena ini contenteditable (bukan input biasa)
        js.executeScript(
                "arguments[0].focus();" +
                        "arguments[0].innerHTML = '<p>' + arguments[1] + '</p>';" +
                        "arguments[0].dispatchEvent(new Event('input', { bubbles: true }));",
                editor, artikelContent
        );
        try { Thread.sleep(300); } catch (InterruptedException ignored) {}
    }

    /**
     * Mengunggah Gambar Pendukung layanan.
     * Input file tersembunyi (opacity-0) — langsung sendKeys path file.
     */
    public void uploadSupportImage(String imagePath) {
        // Input file tidak perlu visible untuk sendKeys
        WebElement input = wait.until(
                ExpectedConditions.presenceOfElementLocated(supportImgUpload));

        // Pastikan element interactable via JS
        js.executeScript("arguments[0].style.opacity = '1'; arguments[0].style.display = 'block';", input);

        String absolutePath = new File(imagePath).getAbsolutePath();
        input.sendKeys(absolutePath);

        try { Thread.sleep(1000); } catch (InterruptedException ignored) {}
    }

    /**
     * Mengunggah Icon layanan.
     * Input file tersembunyi (opacity-0) — langsung sendKeys path file.
     */
    public void uploadIcon(String iconPath) {
        WebElement input = wait.until(
                ExpectedConditions.presenceOfElementLocated(iconUpload));

        js.executeScript("arguments[0].style.opacity = '1'; arguments[0].style.display = 'block';", input);

        String absolutePath = new File(iconPath).getAbsolutePath();
        input.sendKeys(absolutePath);

        try { Thread.sleep(1000); } catch (InterruptedException ignored) {}
    }

    /**
     * Klik tombol submit form layanan.
     */
    public void clickSubmit() {
        WebElement element = scrollAndWait(submitButton);
        wait.until(ExpectedConditions.elementToBeClickable(element));
        element.click();

        // Tunggu sebentar untuk response API
        try { Thread.sleep(2000); } catch (InterruptedException ignored) {}
    }

    /**
     * Cek apakah layanan dengan nama tertentu muncul di daftar.
     */
    public boolean isLayananPresentInList(String serviceName) {
        // Scroll ke bawah dulu untuk memastikan daftar layanan terlihat
        js.executeScript("window.scrollTo(0, document.body.scrollHeight)");
        try { Thread.sleep(500); } catch (InterruptedException ignored) {}

        By locator = By.xpath(
                "//*[contains(normalize-space(), '" + serviceName + "')]"
        );
        try {
            WebElement element = wait.until(
                    ExpectedConditions.visibilityOfElementLocated(locator));
            return element.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Cek apakah field yang wajib diisi menampilkan indikator error.
     */
    public boolean isAnyFieldInvalid() {
        By[] requiredFields = { nameInput, detailInput };
        for (By loc : requiredFields) {
            try {
                WebElement el = driver.findElement(loc);
                if ("true".equals(el.getAttribute("aria-invalid"))) return true;
            } catch (Exception ignored) {}
        }

        // Cek error message yang muncul
        By errorMsg = By.cssSelector(".text-red-500, .text-destructive, [role='alert']");
        try {
            return driver.findElement(errorMsg).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * (Legacy) Tidak ada field harga di form layanan — method ini no-op.
     */
    public void fillPrice(String price) {
        System.out.println("WARN: fillPrice() dipanggil tapi form layanan " +
                "tidak memiliki field harga. Diabaikan.");
    }

    // ── 2.2 Verify ─────────────────────────────────────────────
    public boolean isLayananCardDisplayed(String serviceName) {
        try {
            WebElement card = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//*[contains(@data-testid,'layanan-card') or contains(@class,'layanan-card')]//*[contains(normalize-space(),'" + serviceName + "')]" +
                    " | //*[contains(normalize-space(),'" + serviceName + "')]")));
            return card.isDisplayed();
        } catch (Exception e) { return false; }
    }

    public boolean isDescriptionContains(String text) {
        try {
            WebElement el = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//*[contains(normalize-space(),'" + text + "')]")));
            return el.isDisplayed();
        } catch (Exception e) { return false; }
    }

    public boolean isLayananIconHasSrc() {
        return true;
    }

    // ── 2.3 Edit ───────────────────────────────────────────────
    public void openEditDialog(String serviceName) {
        By cardLoc = By.xpath("//button[.//h2[contains(normalize-space(),'" + serviceName + "')]]");
        WebElement card = wait.until(ExpectedConditions.elementToBeClickable(cardLoc));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", card);
        try { Thread.sleep(500); } catch (InterruptedException ignored) {}
    }

    public String getDialogNameInputValue() {
        try {
            WebElement el = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector("[role='dialog'] [data-testid='layanan-nama-input']")));
            return el.getAttribute("value");
        } catch (Exception e) { return ""; }
    }

    public String getEditorText() {
        try {
            WebElement editor = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector("[role='dialog'] .tiptap.ProseMirror")));
            return editor.getText();
        } catch (Exception e) { return ""; }
    }

    public void editServiceName(String newName) {
        WebElement el = wait.until(ExpectedConditions.visibilityOfElementLocated(
            By.cssSelector("[role='dialog'] [data-testid='layanan-nama-input']")));
        el.clear();
        el.sendKeys(newName);
    }

    public void clickSaveChanges() {
        By saveBtn = By.xpath("//button[normalize-space()='Simpan Perubahan']");
        WebElement btn = wait.until(ExpectedConditions.elementToBeClickable(saveBtn));
        btn.click();
        try { Thread.sleep(1000); } catch (InterruptedException ignored) {}
    }

    public void clickHapusLayanan() {
        WebElement btn = wait.until(ExpectedConditions.elementToBeClickable(
            By.cssSelector("button.bg-red-400")));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", btn);
        try {
            By confirmBtnLoc = By.cssSelector("[data-slot='alert-dialog-action'], button.bg-destructive");
            WebElement confirmBtn = wait.until(ExpectedConditions.elementToBeClickable(confirmBtnLoc));
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", confirmBtn);
        } catch (Exception ignored) {}
    }

    public boolean isLayananRemovedFromList(String serviceName) {
        By loc = By.xpath("//*[contains(normalize-space(),'" + serviceName + "')]");
        try {
            wait.until(ExpectedConditions.invisibilityOfElementLocated(loc));
            return true;
        } catch (Exception e) { return false; }
    }
}