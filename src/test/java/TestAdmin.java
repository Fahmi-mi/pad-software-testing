import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import pages.LoginPage;

import java.time.Duration;

public class TestAdmin {
    WebDriver driver;
    LoginPage loginPage;

    @BeforeEach
    void setup() {
        // Selenium 4.29.0 built-in Selenium Manager mengelola driver otomatis
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-notifications");
        options.addArguments("--start-maximized");
        driver = new ChromeDriver(options);

        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
        driver.get("https://tentangdental.netlify.app/login");

        loginPage = new LoginPage(driver);
    }

    @AfterEach
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    public void testLoginWithCorrectPassword() {
        loginPage.login("admin@tentangdental.com", "password");
        // Form GET submission — URL stays on /login, force navigate to /admin
        driver.get("https://tentangdental.netlify.app/admin");
        String actualText = driver.findElement(By.cssSelector("div[data-slot='sidebar-header'] span")).getText();

        Assertions.assertEquals("Tentang Dental", actualText);
    }

    @Test
    public void testLoginWithWrongPassword() {
        // Berikan email terdaftar, tapi password asal-asalan
        loginPage.loginWithoutRedirectCheck("admin@tentangdental.com", "passwordSALAH123");

        // Ambil teks error yang muncul di web
        String actualError = loginPage.getErrorMessageText();
        String expectedError = "Email atau password yang Anda masukkan salah."; // Sesuaikan dengan spek web Anda

        // Validasi apakah pesan error-nya benar muncul
        Assertions.assertEquals(expectedError, actualError);
    }

    @Test
    public void testLoginWithInvalidEmailFormat() {
        // Format email sengaja disalahkan (tanpa @ atau .com)
        loginPage.loginWithoutRedirectCheck("adminbukanemail", "password123");

        // 2. Ambil pesan validasi dari browser
        String actualValidationMessage = loginPage.getEmailValidationMessage();

        // 3. Sesuaikan dengan pesan yang muncul di gambar Anda
        String expectedMessage = "Please include an '@' in the email address. 'adminbukanemail' is missing an '@'.";

        // 4. Lakukan pengecekan
        Assertions.assertEquals(expectedMessage, actualValidationMessage);
    }

    @Test
    public void testLoginWithEmptyEmail() {
        // Email dikosongkan, password diisi
        loginPage.loginWithoutRedirectCheck("", "password123");

        String actualError = loginPage.getErrorMessageText();
        String expectedError = "Email wajib diisi.";

        Assertions.assertEquals(expectedError, actualError);
    }
}
