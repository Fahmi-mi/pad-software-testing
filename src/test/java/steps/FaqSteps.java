package steps;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.jupiter.api.Assertions;
import pages.FaqPage;
import support.TestContext;

public class FaqSteps {
    private final FaqPage faqPage;

    public FaqSteps(TestContext context) {
        this.faqPage = new FaqPage(context.getDriver());
    }

    @Given("admin opens FAQ page")
    public void adminOpensFaqPage() {
        faqPage.open("https://tentangdental.netlify.app");
    }

    @When("admin fills FAQ question with {string}")
    public void adminFillsFaqQuestionWith(String question) {
        faqPage.fillQuestion(question);
    }

    @When("admin fills FAQ answer with {string}")
    public void adminFillsFaqAnswerWith(String answer) {
        faqPage.fillAnswer(answer);
    }

    @When("admin clicks add FAQ button")
    public void adminClicksAddFaqButton() {
        faqPage.clickAddFaq();
    }

    @Then("FAQ with question {string} should be visible in list")
    public void faqWithQuestionShouldBeVisibleInList(String question) {
        Assertions.assertTrue(faqPage.isFaqPresent(question), "FAQ with question '" + question + "' not found in list.");
    }

    @Then("admin should see FAQ error message {string}")
    public void adminShouldSeeErrorMessage(String errorMessage) {
        faqPage.assertErrorMessage(errorMessage);
    }

    @When("admin edits the FAQ at index {int}")
    public void adminEditsTheFaqAtIndex(int index) {
        faqPage.clickEditFaq(index);
    }

    @And("admin updates FAQ with question {string} and answer {string}")
    public void adminUpdatesFaqWithQuestionAndAnswer(String question, String answer) {
        faqPage.updateFaq(question, answer);
    }

    @When("admin deletes the FAQ at index {int}")
    public void adminDeletesTheFaqAtIndex(int index) {
        faqPage.clickDeleteFaq(index);
    }

    @When("admin deletes FAQ with question {string}")
    public void adminDeletesFaqWithQuestion(String question) {
        faqPage.deleteFaqByQuestion(question);
    }

    @Then("FAQ with question {string} should not be visible in list")
    public void faqWithQuestionShouldNotBeVisibleInList(String question) {
        Assertions.assertTrue(
                faqPage.isFaqAbsent(question),
                "FAQ with question '" + question + "' is still visible."
        );
    }
}