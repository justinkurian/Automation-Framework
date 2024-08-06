package appHooks;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.sw.extentReports.ExtentTestManager;
import com.sw.factory.DriverFactory;
import com.sw.utils.ConfigReader;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.util.Properties;
import com.sw.extentReports.ExtentManager;

import static com.sw.extentReports.ExtentTestManager.test;
import static com.sw.utils.Constants.ErrorMessage.SCREENSHOT_ERROR_MSG;

@Slf4j
public class ApplicationHooks extends DriverFactory {

    Properties prop;
    private WebDriver driver;
    private ConfigReader configReader;

    @Before
    public void launchBrowser(Scenario scenario) throws Exception {
        this.prop=getProperties();
        ExtentTestManager.startTest(scenario.getName());
        driver = init_driver(prop.getProperty("browser"));
        ExtentTestManager.getTest().log(Status.INFO,"Browser Launched");
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
                File screenshotFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
                byte[] fileContent = Files.readAllBytes(screenshotFile.toPath());
                scenario.attach(fileContent , "image/png" , screenshotName);

                File destinationPath = new File("./screenshots/" + screenshotName +"-"+System.currentTimeMillis()+ ".png");
                  Files.copy(screenshotFile.toPath(), destinationPath.toPath());
                // Add screenshot to Extent Report

                ExtentTestManager.getTest().fail("Screenshot attached")
                        .addScreenCaptureFromPath(destinationPath.getAbsolutePath());

            }else{
                log.info("Scenario Passed ");
            }
        }catch (Exception e){
            log.error(SCREENSHOT_ERROR_MSG+"- {}",e.getMessage());
            ExtentTestManager.getTest().log(Status.FAIL, SCREENSHOT_ERROR_MSG  + e.getMessage());
        }
    }
}
