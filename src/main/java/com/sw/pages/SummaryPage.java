package com.sw.pages;

import com.sw.factory.DriverFactory;
import com.sw.utils.ConfigReader;
import com.sw.utils.DbUtils;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.sql.ResultSet;

@Slf4j
public class SummaryPage extends DriverFactory {
    
    private final WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(60));;

    private final By operationalButton = By.xpath("(//*[@id=\"sandbox-host\"]/div/div[3]/div/div/div/div[1]/ul/li)[1]");
    private final By deviceOperationalButton = By.xpath("//*[@class=\"slicerHeader\"]//div[text()='DEVICE STATUS']/../..//span[text()='Operational']/../../../..");
    private final By countField = By.xpath("(//*[contains(@aria-label, 'No. of Sites')]//*[local-name()='tspan'])[1]");
    private final By alarmButton = By.xpath("//*[@id=\"sandbox-host\"]/div/div[3]//span[@class=\"slicerText\" and text()='Alarm']");
    
    public boolean verifySiteStatusCountWithDatabase(String siteInfo){

        switch (siteInfo){
            case "Operational_Site_Status": {
                /**
                 * These two lines are not required (only here), As application itself default selected the "OperationalButton"
                 *      as soon entered into "Summary" Page screen
                 *
                 *      These kind of same lines require in other case blocks
                 */
                //wait.until(ExpectedConditions.presenceOfElementLocated(operationalButton));
                getDriver().manage().timeouts().pageLoadTimeout(Duration.ofSeconds(20));
                getDriver().manage().timeouts().implicitlyWait(Duration.ofSeconds(20));

                //getDriver().findElement(operationalButton).click();
                //wait.until(ExpectedConditions.visibilityOfAllElements(getDriver().findElement(operationalButton)));
                if(!getDriver().findElement(operationalButton).isSelected()){
                    wait.until(ExpectedConditions.elementToBeClickable(operationalButton));
                    getDriver().findElement(operationalButton).click();
                }else{
                    log.warn("Operational button is already selected");
                }

                int siteCountFromApplication= getCountInfo();

                List<Map<String,Object>> resultData = DbUtils.getValueFromDB("select count(*) as OperationalCount FROM [WWIN].[SITE] WHERE SITE_STATUS_ID='1'","OperationalCount");
                int operationalCountFromDb = 0;
                for(Map<String,Object> data: resultData){
                    operationalCountFromDb=Integer.parseInt((String) data.get("OperationalCount"));
                }

                //TODO: Get the maximum reduction percentage to compare
                return (siteCountFromApplication<=operationalCountFromDb) ;
            }
            case "Operational_Device_Status": {

                WebElement operationalElement = wait.until(ExpectedConditions.elementToBeClickable(deviceOperationalButton));
                getDriver().findElement(deviceOperationalButton).click();
                int siteCountFromApplication= getCountInfo();
                List<Map<String,Object>> resultData= DbUtils.
                        getValueFromDB("select count(*) as DeviceOperationalCount FROM [WWIN].[DEVICE] WHERE DEVICE_STATUS_ID='1'","DeviceOperationalCount");
                int deviceOperationalCountFromDb=0 ;

                for(Map<String,Object> data: resultData){
                    deviceOperationalCountFromDb=Integer.parseInt((String) data.get("DeviceOperationalCount"));;
                }

                //TODO: Get the maximum reduction percentage to compare
                return (siteCountFromApplication<=deviceOperationalCountFromDb);

            } default:
                log.error("Invalid siteInfo has been passed, {}",siteInfo);
                return false;
        }

    }


//        WebElement alarmElement = wait.until(ExpectedConditions.elementToBeClickable(alarmButton));
//        getDriver().findElement(alarmButton).click();



    private int getCountInfo() {
        WebElement countElement = wait.until(ExpectedConditions.visibilityOfElementLocated(countField));
        getDriver().findElement(countField).isDisplayed();
        int siteCount = Integer.parseInt(countElement.getText());
        log.info("Get count for {}", siteCount);

        return siteCount;
    }
}
