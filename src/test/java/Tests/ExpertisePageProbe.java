package Tests;

import Pages.RegisterPage;
import Utils.DriverFactory;
import com.github.javafaker.Faker;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.annotations.Test;

import java.text.SimpleDateFormat;
import java.util.UUID;

public class ExpertisePageProbe extends BaseTest {
    Faker faker = new Faker();

    @Override
    protected String getUrl() {
        return "https://wuzzuf.net/jobs/egypt";
    }

    @Test
    public void DumpExpertisePageControls() {
        RegisterPage registerPage = new RegisterPage(DriverFactory.GetDriver());
        registerPage.BeginRegisteration();
        registerPage.EnterFN(faker.name().firstName());
        registerPage.EnterLN(faker.name().lastName());
        registerPage.ENterEM("qa_" + UUID.randomUUID() + "@mail.com");
        registerPage.SetPass(faker.internet().password(10, 14, true, true, true));
        registerPage.CreateAccount();
        registerPage.Continue_1();
        registerPage.SetDateOfBirth(new SimpleDateFormat("dd/MM/yyyy").format(faker.date().birthday(18, 50)));
        registerPage.SetGender("female");
        registerPage.SetNationality("Egypt");
        registerPage.SetContry("Egypt");
        registerPage.SetCity("Alexandria");
        registerPage.SetArea("Montaza");
        registerPage.SetMobileNum(faker.regexify("01[0125][0-9]{8}"));
        registerPage.Continue_2();
        registerPage.SetEducationLeVel("Bachelor's Degree");
        registerPage.SetFieldStudy("Accounting");
        registerPage.SetUniversity("Cairo University");
        registerPage.SetYearOfGraduation("2018");
        registerPage.Continue_3();
        registerPage.SetYearsOfExperience("No experience");
        registerPage.SetCareerLevel("Student");
        registerPage.Continue_4();
        registerPage.CheckuserOnHisExpertisePage();

        registerPage.AddSkill("Software Testing");
        registerPage.AddSkill("Java");
        registerPage.SetLanguage("English");
        registerPage.SetLanguageProficiency("Fluent");
        DumpPage("EXPERTISE_FILLED");
        try {
            registerPage.SaveAndContinue();
        } catch (RuntimeException e) {
            System.out.println("PROBE_SAVE_FAILED: " + e.getClass().getSimpleName());
        }
        try { Thread.sleep(25000); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
        DumpPage("AFTER_SAVE_CONTINUE");
        for (org.openqa.selenium.logging.LogEntry entry : DriverFactory.GetDriver().manage().logs().get("browser")) {
            System.out.println("PROBE_CONSOLE: " + entry.getLevel() + " " + entry.getMessage());
        }

        try {
            registerPage.StartUsingTheSite();
        } catch (RuntimeException e) {
            System.out.println("PROBE_GETSTARTED_FAILED: " + e.getClass().getSimpleName());
        }
        try { Thread.sleep(8000); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
        DumpPage("AFTER_GET_STARTED");
    }

    private void DumpPage(String stage) {
        System.out.println("PROBE_URL[" + stage + "]: " + DriverFactory.GetDriver().getCurrentUrl());
        for (WebElement button : DriverFactory.GetDriver().findElements(By.tagName("button"))) {
            System.out.println("PROBE_BUTTON[" + stage + "]: text=[" + button.getText().trim()
                    + "] displayed=" + button.isDisplayed() + " enabled=" + button.isEnabled()
                    + " class=[" + button.getAttribute("class") + "]");
        }
        for (WebElement link : DriverFactory.GetDriver().findElements(By.tagName("a"))) {
            String text = link.getText().trim();
            if (!text.isEmpty()) {
                System.out.println("PROBE_LINK[" + stage + "]: text=[" + text + "] href=" + link.getAttribute("href"));
            }
        }
        for (WebElement input : DriverFactory.GetDriver().findElements(By.tagName("input"))) {
            System.out.println("PROBE_INPUT[" + stage + "]: name=[" + input.getAttribute("name")
                    + "] id=[" + input.getAttribute("id") + "] placeholder=[" + input.getAttribute("placeholder")
                    + "] displayed=" + input.isDisplayed());
        }
        System.out.println("PROBE_ERRORS[" + stage + "]: " + DriverFactory.GetDriver().findElements(
                By.xpath("//*[contains(text(),'required') or contains(text(),'must')]")).size());
        for (WebElement label : DriverFactory.GetDriver().findElements(By.tagName("label"))) {
            String text = label.getText().trim();
            if (!text.isEmpty()) {
                System.out.println("PROBE_LABEL[" + stage + "]: [" + text + "]");
            }
        }
        Object bodyText = ((org.openqa.selenium.JavascriptExecutor) DriverFactory.GetDriver())
                .executeScript("return document.body.innerText;");
        System.out.println("PROBE_TEXT[" + stage + "]: " + String.valueOf(bodyText).replace("\n", " | "));
        for (WebElement chip : DriverFactory.GetDriver().findElements(By.cssSelector("div[class*='multiValue']"))) {
            System.out.println("PROBE_CHIP[" + stage + "]: [" + chip.getText().trim() + "]");
        }
    }
}
