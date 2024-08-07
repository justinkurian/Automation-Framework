package com.sw.pages;

import com.sw.extentReports.ExtentTestManager;
import com.sw.factory.DriverFactory;
import com.sw.utils.AppUtil;
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
import java.util.Objects;
import java.util.Properties;
import java.util.Map;
import org.junit.Assert;

@Slf4j
public class ActiveEventTrackerPage extends DriverFactory {

    public static final String REGEX = "[^0-9]";
    public static final String WWIN_COUNT = "WWIN_Count";
    public static final String WWIN = "WWIN";
    public static final String TYPE = "TYPE";
    public static final String Event_Count = "Event_Count";
    private final WebDriverWait wait= new WebDriverWait(getDriver(), Duration.ofSeconds(60));
    private final By activeEventTrackerLink = By.xpath("//*[contains(@aria-label, 'Navigate to page Active Event Tracker')]//*[@class='itemName']");
    private final By siteMaintenanceImage = By.xpath("//*[contains(@aria-label, 'ActiveEventSiteMaintenance')]");
    private final By siteInterventionImage = By.xpath("//*[contains(@aria-label, 'ActiveEventSiteIntervention')]");
    private final By suppressedImage = By.xpath("//*[contains(@aria-label, 'EventSuppressed')]");
    private final By alarmCountInfo = By.xpath("//*[contains(@aria-label, 'Active Alarms 8')]");
    private final By alertCountInfo = By.xpath("//*[contains(@aria-label, 'Active Alerts 4')]");
    private final By notificationCountInfo = By.xpath("//*[contains(@aria-label, 'Active Notification 5.')]");
    private final By infoCountInfo = By.xpath("//*[contains(@aria-label, 'Active Event Info 2.')]");

    public void selectActiveEventTracker() {

        getDriver().manage().timeouts().pageLoadTimeout(Duration.ofSeconds(20));
        getDriver().manage().timeouts().implicitlyWait(Duration.ofSeconds(20));
        WebElement activeElement = wait.until(ExpectedConditions.elementToBeClickable(activeEventTrackerLink));
        getDriver().findElement(activeEventTrackerLink).click();
    }

    public void verifyActiveEventTrackerStatus() {
        WebElement alarm = wait.until(ExpectedConditions.presenceOfElementLocated(siteMaintenanceImage));
        int siteMaintenanceCount = Integer.parseInt(AppUtil.getText(siteMaintenanceImage, getDriver()));
        log.info("siteMaintenanceCount => {}", siteMaintenanceCount);

        WebElement intervention = wait.until(ExpectedConditions.visibilityOfElementLocated(siteInterventionImage));
        int siteInterventionCount = Integer.parseInt(AppUtil.getText(siteInterventionImage, getDriver()));
        log.info("siteInterventionCount => {}", siteInterventionCount);

        WebElement suppressed = wait.until(ExpectedConditions.visibilityOfElementLocated(suppressedImage));
        int suppressedCount = Integer.parseInt(AppUtil.getText(suppressedImage, getDriver()));
        log.info("suppressedCount => {}", suppressedCount);

        String sqlQuery = """ 
                select
                w.WORKFLOW_STATUS_DESCRIPTION as WWIN, count(w.WORKFLOW_STATUS_DESCRIPTION) as
                WWIN_Count from [WWIN].[WORKFLOW_STATUS] w
                LEFT JOIN [WWIN].[EVENT] e on w.[WORKFLOW_STATUS_ID] =
                e.[WORKFLOW_STATUS_ID]
                LEFT JOIN [WWIN].[WWIN_EVENT_TYPE_VIEW] v on
                e.[EVENT_TYPE_ID] = v.[EVENT_TYPE_ID]
                LEFT JOIN [WWIN].[DEVICE] as d on e.[DEVICE_ID] =
                d.[DEVICE_ID]
                LEFT JOIN [WWIN].[SITE] as s on d.[SITE_ID] =
                s.[SITE_ID]
                group by w.WORKFLOW_STATUS_DESCRIPTION,
                        e.EVENT_STATUS_ID, d.[DEVICE_STATUS_ID], s.[SITE_STATUS_ID]
                having e.EVENT_STATUS_ID = 1
                and d.[DEVICE_STATUS_ID] = 1
                and s.[SITE_STATUS_ID] = 1
                ORDER BY w.WORKFLOW_STATUS_DESCRIPTION desc
                """;
        List<Map<String, Object>> resultData = DbUtils.getValueFromDB(sqlQuery, WWIN, WWIN_COUNT);
        for (Map<String, Object> data : Objects.requireNonNull(resultData)) {
            switch (data.get(WWIN).toString()) {
                case "Suppress": {
                    Assert.assertEquals(data.get(WWIN_COUNT), suppressedCount);
                    break;
                }
                case "Site Maintenance": {
                    Assert.assertEquals(data.get(WWIN_COUNT), siteMaintenanceCount);
                    break;
                }
                case "Site Intervention": {
                    Assert.assertEquals(data.get(WWIN_COUNT), siteInterventionCount);
                    break;
                }
            }
        }

    }

    public void verifyActiveEventTypeStatus() {

        WebElement alarm = wait.until(ExpectedConditions.presenceOfElementLocated(alarmCountInfo));
        int alarmCount = Integer.parseInt(AppUtil.getText(alarmCountInfo, getDriver()));
        log.info("alarmCount => {}", alarmCount);

        WebElement alert = wait.until(ExpectedConditions.visibilityOfElementLocated(alertCountInfo));
        int alertCount = Integer.parseInt(AppUtil.getText(alertCountInfo, getDriver()));
        log.info("alertCount => {}", alertCount);

        WebElement notification = wait.until(ExpectedConditions.visibilityOfElementLocated(notificationCountInfo));
        int notificationCount = Integer.parseInt(AppUtil.getText(notificationCountInfo, getDriver()));
        log.info("notificationCount => {}", notificationCount);

        WebElement information = wait.until(ExpectedConditions.visibilityOfElementLocated(infoCountInfo));
        int informationCount = Integer.parseInt(AppUtil.getText(infoCountInfo, getDriver()));
        log.info("informationCount => {}", informationCount);

        String sqlQuery2 = """
                select v.TYPE, count(v.TYPE) as Event_Count from [WWIN].[EVENT] e
                LEFT JOIN [WWIN].[WWIN_EVENT_TYPE_VIEW] v on e.[EVENT_TYPE_ID] = v.[EVENT_TYPE_ID]
                LEFT JOIN [WWIN].[WORKFLOW_STATUS] w on e.[WORKFLOW_STATUS_ID] =
                w.[WORKFLOW_STATUS_ID]
                LEFT JOIN [WWIN].[DEVICE] as d on e.[DEVICE_ID] = d.[DEVICE_ID]
                LEFT JOIN [WWIN].[SITE] as s on d.[SITE_ID] = s.[SITE_ID]
                group by v.TYPE,  e.EVENT_STATUS_ID, d.[DEVICE_STATUS_ID], s.[SITE_STATUS_ID]
                having e.EVENT_STATUS_ID = 1 -- Active event filter
                and d.[DEVICE_STATUS_ID] = 1 -- Active device filter
                and s.[SITE_STATUS_ID] = 1 -- Active site filter
                ORDER BY v.TYPE asc
                """;

        List<Map<String, Object>> resultData = DbUtils.getValueFromDB(sqlQuery2, TYPE, Event_Count);
        for (Map<String, Object> data : Objects.requireNonNull(resultData)) {
            switch (data.get(TYPE).toString()) {
                case "Alarm": {
                    Assert.assertEquals(data.get(Event_Count), alarmCount);
                    break;
                }
                case "Alert": {
                    Assert.assertEquals(data.get(Event_Count), alertCount);
                    break;
                }
                case "Notification": {
                    Assert.assertEquals(data.get(Event_Count), notificationCount);
                    break;
                }
                case "Information": {
                    Assert.assertEquals(data.get(Event_Count), informationCount);
                    break;
                }
            }
        }
    }
}
