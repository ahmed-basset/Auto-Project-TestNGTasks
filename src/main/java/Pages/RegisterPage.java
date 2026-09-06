package Pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class RegisterPage extends BasePage {

    public RegisterPage(WebDriver driver) {
        super(driver);
    }

    By GetStarted = By.cssSelector("a[aria-label=\"navigate to sign up page\"]");
    By FirstName = By.id("firstname");
    By LastName = By.id("lastname");
    By Email = By.id("email");
    By Password =By.cssSelector("input[name=\"password\"]");
    By CreateAccButton = By.cssSelector("button[class=\"css-19xra8v ezfki8j0\"]");
    By fillManaul = By.cssSelector("a[class=\"css-1qhkksc ezfki8j0\"]");
    By Assert1 = By.xpath("//div[@class=\"css-p14xnm exkztdf0\"]/p[@class=\"css-qqj41n\"]");

    // Tell about yourself


    By BDate = By.cssSelector("input[class=\"css-1oy2ayn e1n2h7jb1\"]");
    By GenderM = By.xpath("//label[.//input[@value='male']]");
    By GenderF =By.xpath("//label[.//input[@value='female']]");
    By MobileNum = By.cssSelector("input[class=\"css-e3onam e1n2h7jb1\"]");
    By ContinueB = By.cssSelector("button[class=\"css-lfgv4q ezfki8j0\"]");
    By Assert2 = By.xpath("//div[@class=\"css-p14xnm exkztdf0\"]/p[@class=\"css-qqj41n\"]");

    public void BeginRegisteration()
    {
        ClickElement(GetStarted);
    }
    public void EnterFN (String fn)
    {
        Typing(FirstName,fn);
    }
    public void EnterLN(String ln)
    {
        Typing(LastName,ln);
    }
    public void ENterEM(String email)
    {
        Typing(Email,email);
    }
    public void SetPass(String pass)
    {
        Typing(Password,pass);
    }
    public void CreateAccount()
    {
        ClickElement(CreateAccButton);
    }
    public void Continue_1()
    {
        ClickElement(fillManaul);
    }
    public  void SetDateOfBirth(String date)
    {
        Typing(BDate,date);
    }
    public  void SetGender(String gen)
    {
        if(gen.equalsIgnoreCase("male") || gen.equalsIgnoreCase("m"))
        {
          ClickElement(GenderM);
        }
        else if (gen.equalsIgnoreCase("female") || gen.equalsIgnoreCase("f"))
        {
            ClickElement(GenderF);
        }

    }
    public void SetNationality(String str)
    {
        SelectFromDropDownList("Nationality",str);
    }
    public void SetContry(String strr)
    {
        SelectFromDropDownList("Country",strr);
    }
    public void SetCity(String city)
    {
        SelectFromDropDownList("City",city);
    }
    public void SetArea(String area)
    {
        SelectFromDropDownList("Area",area);
    }
    public void SetMobileNum(String mobile)
    {
        Typing(MobileNum,mobile);
    }
    public void Continue_2()
    {
        ClickElement(ContinueB);
    }
    public void Register_1(String fn,String ln , String em ,String pass ,String DOB,String gender,String Na,String co,String Cit,String Ar,String Mob)
    {
        BeginRegisteration();
        EnterFN(fn);
        EnterLN(ln);
        ENterEM(em);
        SetPass(pass);
        CreateAccount();
        Continue_1();
        SetDateOfBirth(DOB);
        SetGender(gender);
        SetNationality(Na);
        SetContry(co);
        SetCity(Cit);
        SetArea(Ar);
        SetMobileNum(Mob);
        Continue_2();
    }


}
