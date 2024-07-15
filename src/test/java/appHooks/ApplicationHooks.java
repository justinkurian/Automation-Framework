package appHooks;

import com.sw.factory.DriverFactory;
import com.sw.utils.ConfigReader;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.util.Properties;

import static com.sw.utils.Constants.ErrorMessage.SCREENSHOT_ERROR_MSG;

@Slf4j
public class ApplicationHooks {

    Properties prop;
    private DriverFactory driverFactory;
    private WebDriver driver;
    private ConfigReader configReader;

    @Before(order = 0)
    public void getProperty() {
        configReader = new ConfigReader();
        prop = configReader.init_prop();
    }

    @Before(order = 1)
    public void launchBrowser() throws Exception {
        String browserName = prop.getProperty("browser");
        driverFactory = new DriverFactory();
        driver = driverFactory.init_driver(browserName);
    }

    @After(order = 0)
    public void quitBrowser() {
        driver.quit();
    }

    @After(order = 1)
    public void tearDown(Scenario scenario) {
        try {
            if (scenario.isFailed()) {
                //take screenshot:
                String screenshotName = scenario.getName().replaceAll("" , "_");
                byte[] sourcePath = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
                scenario.attach(sourcePath , "image/png" , screenshotName);
            }
        }catch (Exception e){
            log.error(SCREENSHOT_ERROR_MSG,e.getMessage());
        }
    }
}
