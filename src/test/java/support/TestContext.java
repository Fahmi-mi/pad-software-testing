package support;

import org.openqa.selenium.WebDriver;

/**
 * TestContext digunakan sebagai shared state antara Hooks dan StepDefinitions
 * melalui PicoContainer Dependency Injection Cucumber.
 * Satu instance TestContext per skenario (Cucumber membuat instance baru).
 */
public class TestContext {
    private WebDriver driver;

    public WebDriver getDriver() {
        return driver;
    }

    public void setDriver(WebDriver driver) {
        this.driver = driver;
    }
}
