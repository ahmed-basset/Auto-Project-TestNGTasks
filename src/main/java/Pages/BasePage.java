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

   
    public void SelectRadioButton(By labelLocator, By inputLocator)
    {
        ClickElement(labelLocator);
        WebElement input = wait.until(ExpectedConditions.presenceOfElementLocated(inputLocator));
        if (!input.isSelected())
        {
            js.executeScript("arguments[0].click();", input);
        }
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

    // Some labels wrap part of their text in a child element (the languages block renders its index as
    // "Language 1"), so the exact text-node match above cannot reach them.
    public void SelectFromDropDownListByLabelTag(String labelText, String value)
    {
        String field = "//label[contains(normalize-space(.), '" + labelText + "')]"
                     + "/following::input[contains(@id,'react-select')][1]";
        SelectFromReactSelect(field, value, labelText);
    }

    // A react-select menu left open sits over the wizard buttons, so the first click only dismisses it
    // and the form is never submitted. Dismissing the menu first and retrying while the page has not
    // moved makes the step reliable regardless of which widget was touched last.
    public void ClickAndLeavePage(By locator)
    {
        String before = driver.getCurrentUrl();
        WebDriverWait navigation = new WebDriverWait(driver, Duration.ofSeconds(60));

        for (int attempt = 1; attempt <= 3; attempt++)
        {
            // A slow redirect can land after the wait below gives up, so re-check before clicking again:
            // the next page has no such button and looking for one would simply time out.
            if (!driver.getCurrentUrl().equals(before))
            {
                return;
            }
            actions.sendKeys(Keys.ESCAPE).perform();
            try
            {
                // Once submitted the button shows a spinner in place of its label, so it stops matching
                // its own locator. That is submission in progress, not a failure: fall through and keep
                // waiting for the redirect rather than clicking a second time.
                ClickElement(locator);
            }
            catch (TimeoutException e)
            {
                if (driver.getCurrentUrl().equals(before) && attempt == 3)
                {
                    throw e;
                }
            }
            try
            {
                navigation.until(ExpectedConditions.not(ExpectedConditions.urlToBe(before)));
                return;
            }
            catch (TimeoutException e)
            {
                // Still on the same page; dismiss whatever swallowed the click and try again.
            }
        }
        throw new TimeoutException("The page did not change after clicking " + locator);
    }


    private void SelectFromReactSelect(String fieldXpath, String value, String fieldDescription)
    {
        By field = By.xpath(fieldXpath);
        By control = By.xpath(fieldXpath + "/ancestor::div[contains(@class,'control')][1]");
        By options = By.cssSelector("div[id*='-option-']");


        WebDriverWait menuWait = new WebDriverWait(driver, Duration.ofSeconds(15));
        List<String> available = new ArrayList<>();

        for (int attempt = 1; attempt <= 3; attempt++)
        {
            try
            {
                ClickElement(control);
                WebElement input = wait.until(ExpectedConditions.visibilityOfElementLocated(field));
                // Only clear text the widget is still holding: on a multi-select an empty input treats
                // BACK_SPACE as "remove the last chosen value", which would undo earlier selections.
                String typed = input.getAttribute("value");
                if (typed != null && !typed.isEmpty())
                {
                    for (int i = 0; i < typed.length(); i++)
                    {
                        input.sendKeys(Keys.BACK_SPACE);
                    }
                }
                input.sendKeys(value);

                menuWait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(options));
                available.clear();
                available.addAll(WaitForSettledOptions(options));

                int index = IndexOfOption(available, value);
                if (index >= 0)
                {
                    // Re-read the menu instead of reusing the elements the texts came from: the list
                    // above is only known to have stopped changing, not to have kept its nodes.
                    List<WebElement> current = driver.findElements(options);
                    if (index < current.size())
                    {
                        ClickOption(current.get(index));
                        return;
                    }
                }
                else if (!available.isEmpty())
                {
                    break;
                }
            }
            catch (StaleElementReferenceException e)
            {
                // Same re-render race as ClickElement; reopen and try again.
            }
            catch (ElementClickInterceptedException e)
            {
                // The menu re-rendered under the cursor and a neighbouring option took the click;
                // reopening the widget and re-reading the list picks the right one up again.
            }
            catch (TimeoutException e)
            {
                // The lookup returned nothing in time; reopening the widget re-issues it.
            }
        }

        throw new NoSuchElementException("Option '" + value + "' was not found under the '"
                + fieldDescription + "' field. Available options: " + available);
    }

    // The skills and job-title menus fetch their suggestions, so the first list to become visible is
    // usually a leftover that is replaced a moment later. Reading until two consecutive looks agree
    // means the match is picked from the list the user would actually see.
    private List<String> WaitForSettledOptions(By options)
    {
        List<String> previous = new ArrayList<>();
        for (int poll = 1; poll <= 20; poll++)
        {
            List<String> current = new ArrayList<>();
            try
            {
                for (WebElement option : driver.findElements(options))
                {
                    current.add(option.getText().trim());
                }
            }
            catch (StaleElementReferenceException e)
            {
                // The list changed while it was being read, which is itself a sign it has not settled.
                previous.clear();
                continue;
            }
            if (!current.isEmpty() && current.equals(previous))
            {
                return current;
            }
            previous = current;
            SleepBriefly();
        }
        return previous;
    }

    // Options are often longer than what identifies them ("Cairo University (CU)" for "Cairo
    // University"), so fall back to a prefix match once no option matches outright.
    private int IndexOfOption(List<String> available, String value)
    {
        for (int i = 0; i < available.size(); i++)
        {
            if (available.get(i).equalsIgnoreCase(value))
            {
                return i;
            }
        }
        for (int i = 0; i < available.size(); i++)
        {
            if (available.get(i).toLowerCase().startsWith(value.toLowerCase()))
            {
                return i;
            }
        }
        return -1;
    }

    private void SleepBriefly()
    {
        try
        {
            Thread.sleep(250);
        }
        catch (InterruptedException e)
        {
            Thread.currentThread().interrupt();
        }
    }

    // The menu list shifts as react-select filters, so an option found a moment ago can end up behind
    // its neighbour by the time Selenium reaches it. Centring it first fixes most of that, and a
    // scripted click gets past the rest without waiting for another render.
    private void ClickOption(WebElement option)
    {
        js.executeScript("arguments[0].scrollIntoView({block:'center'});", option);
        try
        {
            option.click();
        }
        catch (ElementClickInterceptedException e)
        {
            js.executeScript("arguments[0].click();", option);
        }
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
    // Search results are a list, so the assertions need every matching element's text at once.
    public List<String> GetTexts(By locator)
    {
        List<String> texts = new ArrayList<>();
        for (WebElement element : wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(locator)))
        {
            texts.add(element.getText().trim());
        }
        return texts;
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
