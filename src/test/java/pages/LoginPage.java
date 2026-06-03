package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class LoginPage {
    private WebDriver driver;
    private WebDriverWait wait;

    // Locators
    private By emailField = By.cssSelector("[data-testid='login-email-input']");
    private By passwordField = By.cssSelector("[data-testid='login-password-input']");
    private By loginButton = By.cssSelector("[data-testid='login-submit-button']");
    private By errorMessage = By.cssSelector("[data-testid='login-error-message']");

    public LoginPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    // Actions
    public void login(String email, String password) {
        WebElement emailEl = wait.until(ExpectedConditions.visibilityOfElementLocated(emailField));
        emailEl.clear();
        emailEl.sendKeys(email);

        WebElement passEl = wait.until(ExpectedConditions.visibilityOfElementLocated(passwordField));
        passEl.clear();
        passEl.sendKeys(password);

        // Menunggu sampai tombol bisa di-klik
        WebElement btnEl = wait.until(ExpectedConditions.elementToBeClickable(loginButton));
        btnEl.click();
    }

    public String getErrorMessageText() {
        WebElement errorEl = wait.until(ExpectedConditions.visibilityOfElementLocated(errorMessage));
        return errorEl.getText();
    }

    // Tambahkan fungsi ini di dalam class LoginPage Anda
    public String getEmailValidationMessage() {
        // Menunggu sampai field email muncul
        WebElement emailEl = wait.until(ExpectedConditions.visibilityOfElementLocated(emailField));

        // Mengambil pesan validasi bawaan HTML5 menggunakan attribute "validationMessage"
        return emailEl.getAttribute("validationMessage");
    }

}