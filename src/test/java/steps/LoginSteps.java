package steps;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import pages.LoginPage;
import support.TestContext;

import java.time.Duration;
import java.util.ArrayList;

public class LoginSteps {
    private final WebDriver driver;
    private final WebDriverWait wait;
    private final LoginPage loginPage;
    private final JavascriptExecutor js;
    private static final String BASE_URL = "https://tentangdental.netlify.app";

    public LoginSteps(TestContext context) {
        this.driver = context.getDriver();
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        this.loginPage = new LoginPage(driver);
        this.js = (JavascriptExecutor) driver;
    }

    private void setReactInputValue(WebElement element, String value) {
        js.executeScript("arguments[0].focus();", element);
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

    @When("admin mengisi email dengan {string}")
    public void adminMengisiEmailDengan(String email) {
        WebElement emailEl = wait.until(
            ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector("[data-testid='login-email-input']")));
        setReactInputValue(emailEl, email);
    }

    @And("admin mengisi password dengan {string}")
    public void adminMengisiPasswordDengan(String password) {
        WebElement passEl = wait.until(
            ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector("[data-testid='login-password-input']")));
        setReactInputValue(passEl, password);
    }

    @And("admin mengklik tombol Login")
    public void adminMengklikTombolLogin() {
        WebElement btn = wait.until(
            ExpectedConditions.elementToBeClickable(
                By.cssSelector("[data-testid='login-submit-button']")));
        btn.click();
    }

    @Then("admin harus diarahkan ke halaman dashboard")
    public void adminHarusDiarahkanKeHalamanDashboard() {
        Assertions.assertTrue(loginPage.isLoginSuccessful(),
            "Admin tidak diarahkan ke halaman dashboard setelah login.");
    }

    @And("nama admin harus ditampilkan pada header dashboard")
    public void namaAdminHarusDitampilkanPadaHeaderDashboard() {
        wait.until(ExpectedConditions.urlContains("/admin"));
        WebElement header = wait.until(
            ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector("[data-slot='sidebar-header'] span")));
        String text = header.getText();
        Assertions.assertFalse(text.isEmpty(),
            "Nama admin tidak ditampilkan pada header dashboard.");
    }

    @Given("admin sudah login dengan kredensial valid")
    public void adminSudahLoginDenganKredensialValid() {
        driver.get(BASE_URL + "/login");
        wait.until(ExpectedConditions.visibilityOfElementLocated(
            By.cssSelector("[data-testid='login-submit-button']")));
        loginPage.login("admin@tentangdental.com", "password");
    }

    @When("admin membuka halaman admin di tab baru")
    public void adminMembukaHalamanAdminDiTabBaru() {
        js.executeScript("window.open()");
        ArrayList<String> tabs = new ArrayList<>(driver.getWindowHandles());
        driver.switchTo().window(tabs.get(tabs.size() - 1));
        driver.get(BASE_URL + "/admin");
    }

    @Then("halaman dashboard admin harus ditampilkan")
    public void halamanDashboardAdminHarusDitampilkan() {
        Assertions.assertTrue(loginPage.isLoginSuccessful(),
            "Admin tidak diarahkan ke halaman dashboard setelah login.");
        WebElement header = wait.until(
            ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector("[data-slot='sidebar-header'] span")));
        String text = header.getText();
        Assertions.assertFalse(text.isEmpty(),
            "Nama admin tidak ditampilkan pada header dashboard.");
    }

    @Then("pesan error {string} harus ditampilkan")
    public void pesanErrorHarusDitampilkan(String expectedMessage) {
        String actual = loginPage.getErrorMessage();
        Assertions.assertTrue(actual.contains(expectedMessage),
            "Pesan error tidak sesuai. Diharapkan mengandung: '" + expectedMessage
                + "', tetapi: '" + actual + "'");
    }

    @And("admin harus tetap berada di halaman login")
    public void adminHarusTetapBeradaDiHalamanLogin() {
        String currentUrl = driver.getCurrentUrl();
        Assertions.assertTrue(currentUrl.contains("/login"),
            "Seharusnya tetap di halaman login, tetapi URL: " + currentUrl);
    }

    @Then("field email harus menampilkan indikator error format tidak valid")
    public void fieldEmailHarusMenampilkanIndikatorErrorFormatTidakValid() {
        String validationMsg = loginPage.getEmailValidationMessage();
        Assertions.assertFalse(validationMsg.isEmpty(),
            "Field email tidak menampilkan indikator error format tidak valid.");
    }

    @Then("field {string} harus menampilkan indikator error")
    public void fieldHarusMenampilkanIndikatorError(String fieldName) {
        String selector = switch (fieldName) {
            case "email" -> "[data-testid='login-email-input']";
            case "password" -> "[data-testid='login-password-input']";
            default -> throw new IllegalArgumentException("Unknown field: " + fieldName);
        };
        WebElement field = driver.findElement(By.cssSelector(selector));
        String validationMsg = (String) js
            .executeScript("return arguments[0].validationMessage;", field);
        boolean hasErrorStyle = Boolean.TRUE.equals(js
            .executeScript(
                "const el = arguments[0];" +
                "return el.matches(':invalid') || " +
                "el.getAttribute('aria-invalid') === 'true' || " +
                "window.getComputedStyle(el).borderColor === 'rgb(255, 0, 0)';",
                field));
        Assertions.assertTrue(!validationMsg.isEmpty() || hasErrorStyle,
            "Field '" + fieldName + "' tidak menampilkan indikator error.");
    }

    @Then("tombol Login harus menampilkan status loading")
    public void tombolLoginHarusMenampilkanStatusLoading() {
        WebElement btn = driver.findElement(
            By.cssSelector("[data-testid='login-submit-button']"));
        String text = btn.getText().toLowerCase();
        boolean isLoading = text.contains("loading") || text.contains("memproses")
            || btn.getAttribute("disabled") != null;
        Assertions.assertTrue(isLoading,
            "Tombol Login tidak menampilkan status loading.");
    }

    @And("tombol Login harus nonaktif selama proses autentikasi")
    public void tombolLoginHarusNonaktifSelamaProsesAutentikasi() {
        WebElement btn = driver.findElement(
            By.cssSelector("[data-testid='login-submit-button']"));
        boolean isDisabled = btn.getAttribute("disabled") != null
            || btn.getAttribute("aria-disabled") != null;
        Assertions.assertTrue(isDisabled,
            "Tombol Login masih aktif selama proses autentikasi.");
    }
}
