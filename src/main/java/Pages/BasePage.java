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
        wait.until(ExpectedConditions.elementToBeClickable(Locator)).click();
    }
    public void Typing(By locator , String str)
    {
        wait.until(ExpectedConditions.visibilityOfElementLocated(locator)).sendKeys(str);
        actions.sendKeys(Keys.ENTER);

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
