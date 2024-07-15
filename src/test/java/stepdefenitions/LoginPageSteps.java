package stepdefenitions;

import com.sw.pages.LoginPage;
import com.sw.factory.DriverFactory;
import com.sw.utils.ConfigReader;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;

import java.util.Properties;

@Slf4j
public class LoginPageSteps {

    Properties prop;
    private ConfigReader configReader;
    private String title;
    private LoginPage loginPage;

    public LoginPageSteps() {
        this.configReader = new ConfigReader();
        this.prop = configReader.init_prop();
        this.loginPage = new LoginPage(DriverFactory.getDriver());
    }

    @Given("user is on login page")
    public void user_is_on_login_page() {
        DriverFactory.getDriver().get(prop.getProperty("login_url"));
    }

    @When("user gets the title of the page")
    public void user_gets_the_title_of_the_page(){
        title = loginPage.getLoginPageTitle();
        log.info("login page title is: {}", title);
    }

    @When("user enters username {string}")
    public void user_enters_username(String username) {
        loginPage.enterUserName(username);

    }
    @When("user enters password {string}")
    public void user_enters_password(String password) {
        loginPage.enterPassword(password);

    }
    @When("user clicks on Login button")
    public void user_clicks_on_login_button() {
        loginPage.clickOnLogin();

    }

    @Then("page title should be {string}")
    public void page_title_should_be(String expectedTitleName) {
        Assert.assertTrue(title.contains(expectedTitleName));

    }


}
