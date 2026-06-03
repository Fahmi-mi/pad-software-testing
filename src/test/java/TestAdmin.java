import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import pages.LoginPage;

import java.time.Duration;

public class TestAdmin {
    WebDriver driver;
    LoginPage loginPage;

    @BeforeEach
    void setup() {
        driver = new ChromeDriver();

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
        String actualText = driver.findElement(By.cssSelector("div[data-slot='sidebar-header'] span")).getText();

// 2. Bandingkan String teks vs String teks (Benar)
        Assertions.assertEquals("Tentang Dental", actualText);
    }

    @Test
    public void testLoginWithWrongPassword() {
        // Berikan email terdaftar, tapi password asal-asalan
        loginPage.login("admin@dental.com", "passwordSALAH123");

        // Ambil teks error yang muncul di web
        String actualError = loginPage.getErrorMessageText();
        String expectedError = "Validasi gagal. Periksa input Anda."; // Sesuaikan dengan spek web Anda

        // Validasi apakah pesan error-nya benar muncul
        Assertions.assertEquals(expectedError, actualError);
    }

    @Test
    public void testLoginWithInvalidEmailFormat() {
        // Format email sengaja disalahkan (tanpa @ atau .com)
        loginPage.login("adminbukanemail", "password123");

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
        loginPage.login("", "password123");

        String actualError = loginPage.getErrorMessageText();
        String expectedError = "Email wajib diisi.";

        Assertions.assertEquals(expectedError, actualError);
    }
}
