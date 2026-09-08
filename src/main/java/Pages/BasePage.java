package Pages;

import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

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
        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(Locator));
        js.executeScript("arguments[0].scrollIntoView({block:'center'});", element);

        // The site drops third-party overlays (e.g. the <us-widget> badge) on top of the page after
        // it becomes interactive, so a plain click is sometimes intercepted. A JS click is not
        // blocked by whatever happens to sit over the element.
        try
        {
            element.click();
        }
        catch (ElementClickInterceptedException e)
        {
            js.executeScript("arguments[0].click();", element);
        }
    }
    public void Typing(By locator , String str)
    {
        wait.until(ExpectedConditions.visibilityOfElementLocated(locator)).sendKeys(str);
    }

   public  void Enter()
   {
       actions.sendKeys(Keys.ENTER).perform();
   }

    // react-select widgets are plain divs, so org.openqa.selenium.support.ui.Select cannot drive them.
    // Fields are located by their visible label so the locators survive markup changes.
    public void SelectFromDropDownList(String label, String value)
    {
        String field = "//*[normalize-space(text())='" + label + "']"
                     + "/following::input[contains(@id,'react-select')][1]";

        ClickElement(By.xpath(field + "/ancestor::div[contains(@class,'control')][1]"));
        Typing(By.xpath(field), value);

        By options = By.cssSelector("div[id*='-option-']");
        for (WebElement option : wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(options)))
        {
            if (option.getText().trim().equalsIgnoreCase(value))
            {
                option.click();
                return;
            }
        }
        throw new NoSuchElementException("Option '" + value + "' was not found under the '" + label + "' field");
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
