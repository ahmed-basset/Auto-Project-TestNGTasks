package Pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class AccountSettingsPage extends BasePage {

    public AccountSettingsPage(WebDriver driver) {
        super(driver);
    }

    // The avatar in the header carries the user's initial; its style classes change with each build,
    // so the component class the site keeps is what identifies it.
    By ProfileIcon = By.cssSelector("header div[class*='e1j4u6fw0']");
    By AccountSettingsLink = By.cssSelector("a[href*='/settings/account']");
    By DeleteMyAccountButton = By.xpath("//button[contains(normalize-space(.), 'Delete My Account')]");
    By DeletionReason = By.cssSelector("input[name='reason']");
    By ConfirmDeletionCheckbox = By.xpath("//label[contains(normalize-space(.), 'I confirm')]");
    // Confirming happens in a dialog the site appends to the end of the page, so its copy of the
    // button is the last one.
    By ConfirmDeleteMyAccount = By.xpath("(//button[contains(normalize-space(.), 'Delete My Account')])[last()]");
    By SuccessMessage = By.xpath("//*[contains(normalize-space(.), 'deleted') or contains(normalize-space(.), 'Deleted')]"
            + "[not(.//*[contains(normalize-space(.), 'deleted') or contains(normalize-space(.), 'Deleted')])]");

    public void OpenProfileMenu()
    {
        ClickVisibleElement(ProfileIcon);
        wait.until(ExpectedConditions.visibilityOfElementLocated(AccountSettingsLink));
    }

    public void OpenAccountSettings()
    {
        ClickVisibleElement(AccountSettingsLink);
        wait.until(ExpectedConditions.urlContains("/settings/account"));
    }

    // The site asks why the account is going before it will delete it, and it will not act on the
    // dialog until the confirmation box is ticked.
    public void DeleteAccount(String reason)
    {
        ClickVisibleElement(DeleteMyAccountButton);
        wait.until(ExpectedConditions.visibilityOfElementLocated(DeletionReason));
        Typing(DeletionReason, reason);
        ClickVisibleElement(ConfirmDeletionCheckbox);
        ClickVisibleElement(ConfirmDeleteMyAccount);
    }

    public String GetSuccessMessage()
    {
        return GetText(SuccessMessage);
    }
}
