package Pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;

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
    By Assert1 = By.xpath("" +
            "//*[contains(normalize-space(.), 'Tell us about yourself') or contains(normalize-space(.), 'Tell Us About Yourself')]");

    // Tell about yourself

    By BDate = By.cssSelector("input[class=\"css-1oy2ayn e1n2h7jb1\"]");
    By GenderM = By.xpath("//label[.//input[@value='male']]");
    By GenderF =By.xpath("//label[.//input[@value='female']]");
    By MobileNum = By.cssSelector("input[class=\"css-e3onam e1n2h7jb1\"]");
    By ContinueB = By.cssSelector("button[class=\"css-lfgv4q ezfki8j0\"]");
    By Assert2 = By.xpath("//*[contains(normalize-space(.), 'Tell us about your education') or contains(normalize-space(.), 'Tell Us About Your Education')]");

     //Tell about Education
     By FieldOfStud = By.cssSelector("input[class=\"css-1a96k50 ek82ord0\"]");
     By University = By.xpath("//*[@id=\"education-form\"]/div[1]/div[3]/div[2]/div[1]/div/div[2]/div/div[1]/div[2]");
     By ContinueC = By.cssSelector("button[class=\"css-lfgv4q ezfki8j0\"]");
     By Assert3 = By.xpath("//*[contains(normalize-space(.), 'Tell us about your experience') or contains(normalize-space(.), 'Tell Us About Your Experience')]");

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
    public void SetEducationLeVel(String level)
    {
      SelectFromDropDownListByName("currentEducationLevel", level);
    }
    public void SetFieldStudy(String study)
    {
        Typing(FieldOfStud,study);
    }

    public void SetUniversity(String Univers)
    {
        SelectFromDropDownListByName("schoolName", Univers);
    }
    public void SetYearOfGraduation(String Year)
    {
        SelectFromDropDownListByName("endingYear", Year);
    }

    public void Continue_3()
    {
        ClickElement(ContinueC);
    }
    //Assertions
    public String CheckuserOnHisInformationPage()
    {
        wait.until(ExpectedConditions.visibilityOfElementLocated(Assert1));
        return GetText(Assert1);
    }
    public String CheckuserOnHisEducationPage()
    {
        wait.until(ExpectedConditions.visibilityOfElementLocated(Assert2));
        return GetText(Assert2);
    }

    public String CheckuserOnHisExperiencePage()
    {
        wait.until(ExpectedConditions.visibilityOfElementLocated(Assert3));
        return GetText(Assert3);
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
        String infoPageText = CheckuserOnHisInformationPage();
        String educationPageText = CheckuserOnHisEducationPage();
        System.out.println(infoPageText);
        System.out.println(educationPageText);

    }


}
