package com.scottishwater.pages;

import com.scottishwater.factory.DriverFactory;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class LoginPage {

    private WebDriver driver;

    //1.By Locators:
    private By emailID = By.id("email");
    private By password = By.id("passwd");
    private By signInButton = By.id("SubmitLogin");
    private By forgotPwdLink = By.linkText("Forgot your password?");

    //2. Constructor of the page class:
    public LoginPage(WebDriver driver){
        this.driver=driver;
    }

    //3. page actions: features(behaviour) of the page in the form of methods:
    public String getLoginPageTitle(){
        return driver.getTitle();
    }

    public boolean isForgotPwdLinkExist() {
        return driver.findElement(forgotPwdLink).isDisplayed();
    }

    public void enterUserName(String username){
        driver.findElement(emailID).sendKeys(username);
    }

    public void enterPassword(String pwd){
        driver.findElement(password).sendKeys(pwd);
    }

    public void clickOnLogin(){
        driver.findElement(signInButton).click();
    }
}
