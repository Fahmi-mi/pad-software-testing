package pages;

import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class FaqPage {
    private final WebDriver driver;
    private final WebDriverWait wait;

    public FaqPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public void open(String baseUrl) {
        driver.get(baseUrl + "/admin/faq");
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@placeholder='Masukkan pertanyaan']")));
    }

    public void fillQuestion(String question) {
        WebElement input = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//input[@placeholder='Masukkan pertanyaan']")
        ));
        scrollElementIntoView(input);
        input.clear();
        input.sendKeys(question);
    }

    public void fillAnswer(String answer) {
        WebElement editor = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//div[@contenteditable='true']")
        ));
        scrollElementIntoView(editor);
        editor.clear();
        editor.sendKeys(answer);
    }

    public void clickAddFaq() {
        WebElement button = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[normalize-space()='Tambah FAQ']")
        ));
        button.click();
    }

    public void clickEditFaq(int index) {
        WebElement editButton = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("(//div[contains(@class,'border rounded-lg')]//button[contains(@class,'p-1.5')])[1]")
        ));
        List<WebElement> editButtons = driver.findElements(By.xpath("//button[.//svg[contains(@class, 'lucide-pencil')]]"));
        if (index < editButtons.size()) {
            editButtons.get(index).click();
        }
    }

    public void updateFaq(String question, String answer) {
        WebElement qInput = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//div[contains(@class,'p-4 space-y-3')]//input")
        ));
        qInput.clear();
        qInput.sendKeys(question);

        WebElement aEditor = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//div[contains(@class,'p-4 space-y-3')]//div[@contenteditable='true']")
        ));
        aEditor.clear();
        aEditor.sendKeys(answer);

        clickSave();
    }

    private void clickSave() {
        WebElement saveButton = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[normalize-space()='Simpan']")
        ));
        saveButton.click();
    }

    public void clickDeleteFaq(int index) {
        List<WebElement> deleteButtons = driver.findElements(By.xpath("//button[.//svg[contains(@class, 'lucide-trash')]]"));
        if (index < deleteButtons.size()) {
            deleteButtons.get(index).click();
        }

        try {
            WebElement confirmButton = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//div[@role='alertdialog']//button[normalize-space()='Hapus']")
            ));
            confirmButton.click();
        } catch (Exception e) {
        }
    }

    public void assertErrorMessage(String expectedError) {
        WebElement errorElement = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//p[contains(@class, 'text-destructive') and contains(text(), '" + expectedError + "')]")
        ));
        Assertions.assertNotNull(errorElement, "Error message not found: " + expectedError);
    }

    public boolean isFaqPresent(String question) {
        return driver.findElements(By.xpath("//span[contains(text(), '" + question + "')]")).size() > 0;
    }

    private void scrollElementIntoView(WebElement element) {
        ((org.openqa.selenium.JavascriptExecutor) driver).executeScript(
                "arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});",
                element
        );
    }
}
