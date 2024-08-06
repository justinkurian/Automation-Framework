package stepdefenitions;

import com.sw.extentReports.ExtentTestManager;
import com.sw.pages.ActiveEventTrackerPage;
import com.sw.pages.LoginPage;
import com.sw.pages.SummaryPage;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;

@Slf4j
public class LoginPageSteps {

    private final LoginPage loginPage;
    private final SummaryPage summaryPage;
    private final ActiveEventTrackerPage activeEventTrackerPage;

    public LoginPageSteps() {
        this.loginPage = new LoginPage();
        this.summaryPage = new SummaryPage();
        this.activeEventTrackerPage = new ActiveEventTrackerPage();
    }

    @Given("I am logged into the WWIN application with valid credentials")
    public void i_am_logged_into_the_wwin_application_with_valid_credentials() {
        loginPage.doLogin();
        ExtentTestManager.getTest().log(Status.INFO, "Login Successful");
    }

    @Then("verify operational site count against database on the summary page")
    public void verify_operational_site_count_against_database_on_the_summary_page() {
        Assert.assertTrue(summaryPage.verifySiteStatusCountWithDatabase("Operational_Site_Status"));
    }

    @Given("user is logged into the WWIN application with valid credentials")
    public void user_is_logged_into_the_wwin_application_with_valid_credentials() {
        loginPage.doLogin();
        ExtentTestManager.getTest().log(Status.INFO, "Login Successful");
    }

    @When("user selects active event tracker page")
    public void user_selects_active_event_tracker_page() {
        activeEventTrackerPage.selectActiveEventTracker();
        ExtentTestManager.getTest().log(Status.INFO, "Navigated to event tracker");

    }

    @Then("user gets the WWIN status with the counts")
    public void user_gets_the_wwin_status_with_the_counts() {
        activeEventTrackerPage.verifyActiveEventTrackerStatus();
        ExtentTestManager.getTest().log(Status.INFO, "Event tracker status verified");
    }


    @Then("verify WWIN event type count against database")
    public void verifyWWINEventTypeCountAgainstDatabase() {
        activeEventTrackerPage.verifyActiveEventTypeStatus();
        ExtentTestManager.getTest().log(Status.INFO, "Event type status verified against DB");
    }
}
