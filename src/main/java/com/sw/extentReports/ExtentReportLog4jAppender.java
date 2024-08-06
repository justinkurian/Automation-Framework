package com.sw.extentReports;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.AppenderBase;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;

public class ExtentReportLog4jAppender extends AppenderBase<ILoggingEvent> {
    private static final ExtentReports extentReports = ExtentManager.getInstance();
    private static final ExtentTest extentTest = ExtentTestManager.getTest();

    @Override
    protected void append(ILoggingEvent eventObject) {
        if (extentTest != null) {
            switch (eventObject.getLevel().toString()) {
                case "ERROR": case "FAIL":
                    extentTest.log(Status.FAIL, eventObject.getFormattedMessage());
                    break;
                case "WARN":
                    extentTest.log(Status.WARNING, eventObject.getFormattedMessage());
                    break;
                default:
                    extentTest.log(Status.INFO, eventObject.getFormattedMessage());
                    break;
            }
        }
    }
}
