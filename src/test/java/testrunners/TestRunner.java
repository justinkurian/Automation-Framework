package testrunners;

import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;
import io.cucumber.junit.Cucumber;


@RunWith(Cucumber.class)
@CucumberOptions(
        features = {"src/test/resources/appFeatures"},
        glue = {"stepdefenitions", "appHooks"},
        plugin = {"pretty"}
)

public class TestRunner {


}
