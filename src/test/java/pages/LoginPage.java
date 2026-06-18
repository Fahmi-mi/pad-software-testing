package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class LoginPage {
    private final WebDriver driver;
    private final WebDriverWait wait;
    private final JavascriptExecutor js;

    // Locators — data-testid sesuai dengan DOM React
    private final By emailField = By.cssSelector("[data-testid='login-email-input']");
    private final By passwordField = By.cssSelector("[data-testid='login-password-input']");
    private final By loginButton = By.cssSelector("[data-testid='login-submit-button']");
    private final By errorMessage = By.cssSelector("[data-testid='login-error-message']");

    public LoginPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        this.js = (JavascriptExecutor) driver;
    }

    /**
     * Helper: mengisi nilai input React via JS setter + dispatch synthetic event.
     * Pendekatan ini memastikan React onChange / onInput terpanggil.
     */
    private void setReactInputValue(WebElement element, String value) {
        // Fokus ke element terlebih dahulu
        js.executeScript("arguments[0].focus();", element);

        // Set nilai menggunakan native value setter (melewati React wrapper)
        js.executeScript(
            "const el = arguments[0];" +
            "const value = arguments[1];" +
            "const proto = Object.getPrototypeOf(el);" +
            "const nativeSetter = Object.getOwnPropertyDescriptor(proto, 'value').set;" +
            "nativeSetter.call(el, value);" +
            "el.dispatchEvent(new Event('input', { bubbles: true, cancelable: true }));" +
            "el.dispatchEvent(new Event('change', { bubbles: true, cancelable: true }));" +
            "el.dispatchEvent(new Event('blur', { bubbles: true, cancelable: true }));",
            element, value
        );
    }

    /**
     * Login dengan email dan password.
     * Setelah klik tombol submit, method ini MENUNGGU redirect ke /admin.
     * Jika redirect tidak terjadi dalam waktu tunggu, throw RuntimeException
     * dengan informasi URL dan pesan error dari form.
     *
     * FIX ERROR 5: retry mechanism — jika redirect gagal tanpa pesan error
     * (indikasi rate limiting / session expired), tunggu 3 detik lalu coba lagi.
     */
    public void login(String email, String password) {
        loginAttempt(email, password, 1);
    }

    public void loginWithoutRedirectCheck(String email, String password) {
        wait.until(webDriver -> js.executeScript(
            "return document.readyState").equals("complete"));
        WebElement emailEl = wait.until(
            ExpectedConditions.visibilityOfElementLocated(emailField));
        setReactInputValue(emailEl, email);
        WebElement passEl = wait.until(
            ExpectedConditions.visibilityOfElementLocated(passwordField));
        setReactInputValue(passEl, password);
        WebElement btnEl = wait.until(
            ExpectedConditions.elementToBeClickable(loginButton));
        btnEl.click();
        try { Thread.sleep(1000); } catch (InterruptedException ignored) {}
    }

    private void loginAttempt(String email, String password, int attempt) {
        // Tunggu page fully loaded (penting untuk React + TanStack Router)
        wait.until(webDriver -> js.executeScript(
            "return document.readyState").equals("complete"));

        // Isi email
        WebElement emailEl = wait.until(
            ExpectedConditions.visibilityOfElementLocated(emailField));
        setReactInputValue(emailEl, email);

        // Isi password
        WebElement passEl = wait.until(
            ExpectedConditions.visibilityOfElementLocated(passwordField));
        setReactInputValue(passEl, password);

        // Klik tombol submit — pastikan sudah enabled
        WebElement btnEl = wait.until(
            ExpectedConditions.elementToBeClickable(loginButton));
        btnEl.click();

        // === TUNGGU REDIRECT AGAR HALAMAN SIAP SEBELUM LANJUT ===
        boolean redirected = isLoginSuccessful();
        if (!redirected) {
            String currentUrl = driver.getCurrentUrl();
            String pageTitle = driver.getTitle();
            String errorMsg = getErrorMessage();

            // Retry jika tidak ada error message (kemungkinan rate limited / session expired)
            if (errorMsg.isEmpty() && attempt < 2) {
                System.out.println("Login percobaan " + attempt + " gagal tanpa pesan error. "
                    + "Retry dalam 3 detik... (URL: " + currentUrl + ")");
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException ignored) {}
                driver.get("https://tentangdental.netlify.app/login");
                wait.until(ExpectedConditions.visibilityOfElementLocated(loginButton));
                loginAttempt(email, password, attempt + 1);
                return;
            }

            throw new RuntimeException(
                "Login gagal — tidak redirect ke /admin.\n" +
                "  URL saat ini : " + currentUrl + "\n" +
                "  Page title   : " + pageTitle + "\n" +
                "  Error message: " + (errorMsg.isEmpty() ? "(tidak ada)" : errorMsg));
        }
    }

    /**
     * Memeriksa apakah login berhasil dengan mengecek redirect ke halaman /admin.
     * Method ini WAIT sampai URL mengandung "/admin".
     */
    public boolean isLoginSuccessful() {
        try {
            wait.until(d -> d.getCurrentUrl().contains("/admin"));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Mengambil teks pesan error yang muncul di form login.
     * Jika tidak ada pesan error, return string kosong.
     */
    public String getErrorMessage() {
        try {
            wait.until(driver -> {
                WebElement el = driver.findElement(errorMessage);
                return el.isDisplayed() && !el.getText().trim().isEmpty();
            });
            return driver.findElement(errorMessage).getText();
        } catch (Exception e) {
            try {
                WebElement fallback = driver.findElement(By.cssSelector(".text-red-500, .text-destructive, [role='alert']"));
                return fallback.getText();
            } catch (Exception ignored) {}
            return "";
        }
    }

    /**
     * (Legacy) Sama dengan getErrorMessage() — untuk backward compatibility.
     */
    public String getErrorMessageText() {
        return getErrorMessage();
    }

    /**
     * Mengambil pesan validasi HTML5 dari field email (native browser validation).
     */
    public String getEmailValidationMessage() {
        WebElement emailEl = wait.until(ExpectedConditions.visibilityOfElementLocated(emailField));
        return (String) js.executeScript("return arguments[0].validationMessage;", emailEl);
    }
}
