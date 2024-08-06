package com.sw.factory;

import io.github.bonigarcia.wdm.WebDriverManager;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.io.File;
import java.time.Duration;
import java.util.Properties;

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


//

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
        getDriver().manage().timeouts().pageLoadTimeout(Duration.ofSeconds(20));
        getDriver().manage().timeouts().implicitlyWait(Duration.ofSeconds(20));
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
