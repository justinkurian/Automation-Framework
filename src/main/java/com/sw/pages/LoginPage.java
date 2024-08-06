package com.sw.pages;

import com.sw.factory.DriverFactory;
import com.sw.utils.ConfigReader;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.Properties;

import static com.sw.utils.utilities.Wait;

@Slf4j

public class LoginPage extends DriverFactory {
    
    private final WebDriverWait wait=new WebDriverWait(getDriver(), Duration.ofSeconds(20));

    //1.By Locators:
    private final By emailAddress = By.xpath("//input[@id=\"email\"]");
    private final By powerBiSubmitButton = By.id("submitBtn");
    private final By password = By.xpath("//*[@id=\"i0118\"]");
    private final By signinButton = By.xpath("//*[@id=\"idSIButton9\"]");
    //private By noButton = By.xpath("//input[@id='idBtn_Back']");
    private final By yesButton = By.xpath("//*[@id=\"idSIButton9\"]");

    public void doLogin() {
        getDriver().get(getProperties().getProperty("login_url"));
        WebElement emailElement = wait.until(ExpectedConditions.visibilityOfElementLocated(emailAddress));
        //getDriver()findElement(emailAddress).sendKeys("kurianju@scottishwater.co.uk");
        getDriver().findElement(emailAddress).sendKeys(getProperties().getProperty("user_name"));
        getDriver().findElement(powerBiSubmitButton).click();
        WebElement passwordElement = wait.until(ExpectedConditions.visibilityOfElementLocated(password));
        //getDriver()findElement(password).sendKeys("BarcelonaMessi10*");
        getDriver().findElement(password).sendKeys(getProperties().getProperty("password"));
        getDriver().findElement(signinButton).click();
        WebElement yesElement = wait.until(ExpectedConditions.elementToBeClickable(yesButton));
        getDriver().findElement(yesButton).submit();

        log.info("login successful");

    }
}




