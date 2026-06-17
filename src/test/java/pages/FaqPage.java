package pages;

import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.interactions.Actions;

import java.time.Duration;
import java.util.List;

public class FaqPage {
    private final WebDriver driver;
    private final WebDriverWait wait;
    private final WebDriverWait longWait;

    public FaqPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        this.longWait = new WebDriverWait(driver, Duration.ofSeconds(20));
    }

    public void open(String baseUrl) {
        driver.get(baseUrl + "/admin/faq");
        wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//input[@placeholder='Masukkan pertanyaan']")));
    }

    public void fillQuestion(String question) {
        WebElement input = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//input[@placeholder='Masukkan pertanyaan']")));
        scrollElementIntoView(input);
        input.clear();
        input.sendKeys(question);
    }

    public void fillAnswer(String answer) {
        WebElement editor = wait.until(ExpectedConditions.presenceOfElementLocated(
                By.xpath("(//div[@contenteditable='true'])[1]")));

        scrollElementIntoView(editor);

        ((JavascriptExecutor) driver).executeScript("arguments[0].focus();", editor);
        try { Thread.sleep(300); } catch (InterruptedException ignored) {}

        new org.openqa.selenium.interactions.Actions(driver)
                .click(editor)
                .keyDown(Keys.CONTROL).sendKeys("a").keyUp(Keys.CONTROL)
                .pause(Duration.ofMillis(200))
                .sendKeys(answer)
                .perform();
    }

    public void clickAddFaq() {
        WebElement button = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[normalize-space()='Tambah FAQ']")));
        button.click();
    }

    public void clickEditFaq(int index) {
        By editButtonsLocator = By.xpath(
                "//div[contains(@class,'flex gap-1') and contains(@class,'p-3')]//button[1]");

        List<WebElement> editButtons = wait.until(
                ExpectedConditions.visibilityOfAllElementsLocatedBy(editButtonsLocator));

        if (index >= editButtons.size()) {
            throw new RuntimeException(
                    "Edit button at index " + index + " not found. Total: " + editButtons.size());
        }
        scrollElementIntoView(editButtons.get(index));
        editButtons.get(index).click();
    }

    public void updateFaq(String question, String answer) {
        WebElement qInput = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//div[contains(@class,'p-4') and contains(@class,'space-y-3')]//input")));
        qInput.clear();
        qInput.sendKeys(question);

        WebElement aEditor = wait.until(ExpectedConditions.presenceOfElementLocated(
                By.xpath("//div[contains(@class,'p-4') and contains(@class,'space-y-3')]//div[@contenteditable='true']")));

        scrollElementIntoView(aEditor);
        ((JavascriptExecutor) driver).executeScript("arguments[0].focus();", aEditor);
        try { Thread.sleep(300); } catch (InterruptedException ignored) {}

        new Actions(driver)
                .click(aEditor)
                .keyDown(Keys.CONTROL).sendKeys("a").keyUp(Keys.CONTROL)
                .pause(Duration.ofMillis(200))
                .sendKeys(answer)
                .perform();

        clickSave();
    }

    private void clickSave() {
        WebElement saveButton = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[normalize-space()='Simpan']")));
        saveButton.click();
    }

    public void clickDeleteFaq(int index) {
        By deleteButtonsLocator = By.xpath(
                "//div[contains(@class,'flex gap-1') and contains(@class,'p-3')]//button[2]");

        List<WebElement> deleteButtons = wait.until(
                ExpectedConditions.visibilityOfAllElementsLocatedBy(deleteButtonsLocator));

        if (index >= deleteButtons.size()) {
            throw new RuntimeException(
                    "Delete button at index " + index + " not found. Total: " + deleteButtons.size());
        }
        scrollElementIntoView(deleteButtons.get(index));
        deleteButtons.get(index).click();

        try {
            WebElement confirmButton = new WebDriverWait(driver, Duration.ofSeconds(3))
                    .until(ExpectedConditions.elementToBeClickable(
                            By.xpath("//div[@role='alertdialog']//button[normalize-space()='Hapus']")));
            confirmButton.click();
        } catch (TimeoutException e) {
        }
    }

    public void assertErrorMessage(String expectedError) {
        WebElement errorElement = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//p[contains(@class,'text-destructive') and contains(text(),'" + expectedError + "')]")));
        Assertions.assertNotNull(errorElement, "Error message not found: " + expectedError);
    }

    public boolean isFaqPresent(String question) {
        try {
            longWait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//*[starts-with(@data-testid,'faq-question-') and contains(text(),'" + question + "')]")));
            return true;
        } catch (TimeoutException e) {
            return false;
        }
    }

    public boolean isFaqAbsent(String question) {
        try {
            longWait.until(ExpectedConditions.invisibilityOfElementLocated(
                    By.xpath("//*[starts-with(@data-testid,'faq-question-') and contains(text(),'" + question + "')]")));
            return true;
        } catch (TimeoutException e) {
            return false;
        }
    }

    private void scrollElementIntoView(WebElement element) {
        ((JavascriptExecutor) driver).executeScript(
                "arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});",
                element);
    }

    public void deleteFaqByQuestion(String question) {
        List<WebElement> faqItems = wait.until(
                ExpectedConditions.visibilityOfAllElementsLocatedBy(
                        By.xpath("//div[contains(@class,'border rounded-lg overflow-hidden')]")));

        for (int i = 0; i < faqItems.size(); i++) {
            List<WebElement> questionElements = faqItems.get(i).findElements(
                    By.xpath(".//*[starts-with(@data-testid,'faq-question-')]"));

            if (!questionElements.isEmpty() &&
                    questionElements.get(0).getText().contains(question)) {

                WebElement deleteBtn = faqItems.get(i).findElement(
                        By.xpath(".//div[contains(@class,'flex gap-1')]//button[2]"));
                scrollElementIntoView(deleteBtn);
                deleteBtn.click();

                try {
                    WebElement confirmButton = new WebDriverWait(driver, Duration.ofSeconds(3))
                            .until(ExpectedConditions.elementToBeClickable(
                                    By.xpath("//div[@role='alertdialog']//button[normalize-space()='Hapus']")));
                    confirmButton.click();
                } catch (TimeoutException e) {
                }
                return;
            }
        }
        throw new RuntimeException("FAQ dengan pertanyaan '" + question + "' tidak ditemukan untuk dihapus.");
    }
}