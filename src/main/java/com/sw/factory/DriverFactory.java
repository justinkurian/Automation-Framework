package com.sw.factory;

import io.github.bonigarcia.wdm.WebDriverManager;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;

import static com.sw.utils.Constants.ApplicationConstants.BROWSER_WARN_MESSAGE;

@Slf4j
public class DriverFactory {

    public WebDriver driver;

    public static ThreadLocal<WebDriver> tlDriver = new ThreadLocal<>();

    private static final String CHROME="chrome";

    /**
     * This method is used to initialize the threadlocal driver on the basis of given browser
     * @param browser
     * @return
     */

    public WebDriver init_driver(String browser) {
        log.info("browser value is: {} opened as {}",browser,browser.equals(CHROME));

        if (browser.equals(CHROME)) {
            WebDriverManager.chromedriver().setup();
            tlDriver.set(new ChromeDriver());
        } else if (browser.equals("edge")) {
            WebDriverManager.edgedriver().setup();
            tlDriver.set(new EdgeDriver());
        } else {
            log.error(BROWSER_WARN_MESSAGE + browser);
        }

        getDriver().manage().deleteAllCookies();
        getDriver().manage().window().maximize();
        return getDriver();

    }

    /**
     * This is used to get the driver with Thread local
     * @return
     */
    public static synchronized WebDriver getDriver(){
        return tlDriver.get();
    }


}
