package steps;

import io.cucumber.java.en.Given;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import pages.LoginPage;
import support.TestContext;

import java.time.Duration;

public class AdminLoginSteps {
    private static final String BASE_URL = "https://tentangdental.netlify.app";

    private final TestContext context;
    private final WebDriverWait wait;
    private final WebDriverWait longWait;

    public AdminLoginSteps(TestContext context) {
        this.context = context;
        this.wait = new WebDriverWait(context.getDriver(), Duration.ofSeconds(10));
        this.longWait = new WebDriverWait(context.getDriver(), Duration.ofSeconds(30));
    }

    @Given("admin opens login page")
    public void adminOpensLoginPage() {
        context.getDriver().get(BASE_URL + "/login");
        wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//input[@type='email' or @type='text']")));
    }

    @Given("admin logs in with valid credentials")
    public void adminLogsInWithValidCredentials() {
        WebDriver driver = context.getDriver();
        LoginPage loginPage = new LoginPage(driver);
        loginPage.login("admin@tentangdental.com", "password");

        longWait.until(ExpectedConditions.urlContains("/admin"));

        longWait.until(ExpectedConditions.or(
                ExpectedConditions.visibilityOfElementLocated(
                        By.cssSelector("div[data-slot='sidebar-header'] span")),
                ExpectedConditions.visibilityOfElementLocated(
                        By.cssSelector("nav, aside, [data-slot='sidebar']")),
                ExpectedConditions.visibilityOfElementLocated(
                        By.xpath("//h1 | //h2 | //main"))
        ));
    }

    @Given("admin opens reservation page")
    public void adminOpensReservationPage() {
        context.getDriver().get(BASE_URL + "/admin/reservasi");
        wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//h1[normalize-space()='Antrian Pasien']")));
    }
}