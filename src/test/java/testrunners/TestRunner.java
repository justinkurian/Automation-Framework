package testrunners;

import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;
import io.cucumber.junit.Cucumber;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import com.sw.extentReports.ExtentManager;

@RunWith(Cucumber.class)
@CucumberOptions(
        features = {"src/test/resources/appFeatures"},
        glue = {"stepdefenitions","appHooks"},
        plugin = {
                "pretty",
                "html:target/cucumber-reports.html",
                "json:target/cucumber-reports/cucumber.json",
                "junit:target/cucumber-reports/cucumber.xml"
//                "tech.grasshopper.extentreports.cucumber.adapter.ExtentCucumberAdapter:"
        },tags = "@activeEvent"
)

public class TestRunner {


    @BeforeClass
    public static void setup() {
        // Initialize the ExtentReports instance
        ExtentManager.getInstance();
    }

    @AfterClass
    public static void teardown() {
        // Flush the reports
        ExtentManager.getInstance().flush();
    }
}
