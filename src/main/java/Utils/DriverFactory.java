package Utils;

import org.openqa.selenium.WebDriver;

public class DriverFactory {
    private static final ThreadLocal<WebDriver> drivers = new ThreadLocal<>();

    public static void SetDriver(WebDriver driver)
    {
        drivers.set(driver);
    }

    public static WebDriver GetDriver ()
    {
        return drivers.get();
    }
    public static void QuitDriver()
    {
        if(drivers.get() !=null)
        {
            drivers.get().quit();
            drivers.remove();
        }
    }
}
