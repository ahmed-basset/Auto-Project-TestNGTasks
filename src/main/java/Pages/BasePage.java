package Pages;

import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class BasePage {

    protected WebDriver driver;
    protected WebDriverWait wait;
    protected JavascriptExecutor js;
    protected Actions actions;
    protected Select select;


    public BasePage(WebDriver driver) {
        this.driver = driver;
        js = (JavascriptExecutor) driver;
        actions = new Actions(driver);
        wait = new WebDriverWait(driver, Duration.ofSeconds(50));
    }

    //Actions
    public void ClickElement(By Locator)
    {
        for (int attempt = 1; attempt < 3; attempt++)
        {
            try
            {
                WebElement element = wait.until(ExpectedConditions.elementToBeClickable(Locator));
                js.executeScript("arguments[0].scrollIntoView({block:'center'});", element);
                element.click();
                return;
            }
            catch (ElementClickInterceptedException e)
            {
                js.executeScript("arguments[0].click();",
                        wait.until(ExpectedConditions.presenceOfElementLocated(Locator)));
                return;
            }
            catch (StaleElementReferenceException e)
            {
                // React re-renders after hydration, so the element can detach between being found
                // and being clicked. Looking it up again on the next pass picks up the new node.
            }
        }
        wait.until(ExpectedConditions.elementToBeClickable(Locator)).click();
    }
    public void Typing(By locator , String str)
    {
        for (int attempt = 1; attempt < 3; attempt++)
        {
            try
            {
                wait.until(ExpectedConditions.visibilityOfElementLocated(locator)).sendKeys(str);
                return;
            }
            catch (StaleElementReferenceException e)
            {
                // Same re-render race as ClickElement.
            }
        }
        wait.until(ExpectedConditions.visibilityOfElementLocated(locator)).sendKeys(str);
    }

   public  void Enter()
   {
       actions.sendKeys(Keys.ENTER).perform();
   }


    // Fields located by their visible label. Prefer SelectFromDropDownListByName where the field has
    // a hidden input, since some react-selects (the graduation year) carry no label at all.
    public void SelectFromDropDownList(String label, String value)
    {
        String field = "//*[normalize-space(text())='" + label + "']"
                     + "/following::input[contains(@id,'react-select')][1]";
        SelectFromReactSelect(field, value, label);
    }

    // Each react-select renders a hidden input holding the submitted value, and the widget's own
    // input sits directly before it, so the stable form field name identifies the widget.
    public void SelectFromDropDownListByName(String fieldName, String value)
    {
        String field = "//input[@name='" + fieldName + "']"
                     + "/preceding::input[contains(@id,'react-select')][1]";
        SelectFromReactSelect(field, value, fieldName);
    }

    // react-select widgets are plain divs, so org.openqa.selenium.support.ui.Select cannot drive them.
    private void SelectFromReactSelect(String fieldXpath, String value, String fieldDescription)
    {
        ClickElement(By.xpath(fieldXpath + "/ancestor::div[contains(@class,'control')][1]"));
        Typing(By.xpath(fieldXpath), value);

        By options = By.cssSelector("div[id*='-option-']");
        List<WebElement> found = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(options));

        // Options are often longer than what identifies them ("Cairo University (CU)" for "Cairo
        // University"), so fall back to a prefix match once no option matches outright.
        for (WebElement option : found)
        {
            if (option.getText().trim().equalsIgnoreCase(value))
            {
                option.click();
                return;
            }
        }
        for (WebElement option : found)
        {
            if (option.getText().trim().toLowerCase().startsWith(value.toLowerCase()))
            {
                option.click();
                return;
            }
        }

        List<String> available = new ArrayList<>();
        for (WebElement option : found)
        {
            available.add(option.getText().trim());
        }
        throw new NoSuchElementException("Option '" + value + "' was not found under the '"
                + fieldDescription + "' field. Available options: " + available);
    }
    public  void SelectElementByText(By loc,String Text)
    {
        WebElement ele = driver.findElement(loc);
        select = new Select(ele);
        select.selectByVisibleText(Text);
    }
    public void SelectElementByIndex(By loc,int index)
    {
        WebElement ele = driver.findElement(loc);
        select = new Select(ele);
        select.selectByIndex(index);
    }


    public String GetText(By loctor)
    {
        return  wait.until(ExpectedConditions.visibilityOfElementLocated(loctor)).getText();
        // return  driver.findElement(loctor).getText();
    }
    public String GetTextElement(WebElement ele)
    {
        return ele.getText();
    }
    public boolean equalText(String actual,String exprcted)
    {
        return  exprcted.equals(actual);

    }
}
