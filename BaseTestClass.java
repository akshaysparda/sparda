package base;

import com.one97.paytm.appautomation.android.GenericFunctionsAndroid;
import com.one97.paytm.appautomation.driver.DriverManager;
import com.one97.paytm.appautomation.enums.LogLevel;
import com.one97.paytm.appautomation.enums.Platform;
import com.one97.paytm.appautomation.factory.GenericFactory;
import com.one97.paytm.appautomation.global.GlobalData;
import com.one97.paytm.appautomation.interfaces.IInvocationListener;
import com.one97.paytm.appautomation.interfaces.ITestStatusListener;
import com.one97.paytm.appautomation.ios.GenericFunctionsIOS;
import com.one97.paytm.appautomation.listeners.InvocationListener;
import com.one97.paytm.appautomation.listeners.TestNGReportListener;
import com.one97.paytm.appautomation.reporting.extentreports.ExtentManager;
import com.one97.paytm.appautomation.reporting.performance.PerformanceMonitor;
import com.one97.paytm.appautomation.utils.GenericFunctions;
import org.testng.IInvokedMethod;
import org.testng.ITestResult;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Listeners;
import pageobjects.shared.SharedActions;

import java.io.IOException;

import static com.one97.paytm.appautomation.global.GlobalData.*;
import static global.GlobalData.APPIUM_NEW_COMMAND_TIMEOUT;
import static global.GlobalData.DEFAULT_IMPLICITWAIT;
import static global.GlobalData.DOWNLOADING_TIMEOUT;
import static global.GlobalData.ELEMENT_TIMEOUT;
import static global.GlobalData.PAGELOAD_TIMEOUT;
import static global.GlobalData.*;


@Listeners({InvocationListener.class, TestNGReportListener.class})
public class BaseTestClass implements IInvocationListener, ITestStatusListener {
    private GenericFunctions genericFunctions;

    @BeforeSuite(alwaysRun = true)
    public void beforeSuite() {
        System.out.println("--------------------- INSIDE BEFORE SUITE ---------------------");
        initCustomListeners();
        initGenericFunctions();
        if (GlobalData.PLATFORM == Platform.IOS)
            initIOSSimulators();
        initApplication();
        initExtentReporter();
        initZephyrReporter();
        initKlovReporter();
        initAppiumDrivers();
    }

    @AfterSuite(alwaysRun = true)
    public void afterSuite() throws IOException {
        System.out.println("--------------------- INSIDE AFTER SUITE ---------------------");
        updateDetailsExtentReports_AfterSuite();
        updateResultsToDing();
        if (GlobalData.PLATFORM == Platform.ANDROID)
            sendBrowserStackEMail();
        pushResultsToDashboard();
        updatePerformanceData();
        quitAppiumDrivers();
    }

    private void initIOSSimulators() {
        ((GenericFunctionsIOS) genericFunctions).initIOSSimulators();
    }

    private void initZephyrReporter() {
        if (FLAG_UPDATEJIRA) {
            if (GlobalData.PLATFORM == Platform.ANDROID)
                initZephyrReporter_Android();
            else
                initZephyrReporter_IOS();
        }
    }

    private void initZephyrReporter_Android() {
        if (FLAG_UPDATEJIRA)
            genericFunctions.initZephyrReporter(JIRA_USER_NAME, JIRA_PASSWORD, JIRA_PROJECT_NAME, JIRA_RELEASE_NAME_PREFIX,
                    JIRA_TEST_REPO_RELEASE_NAME, JIRA_TEST_REPO_CYCLE_NAME, JIRA_NEW_CYCLE_NAME_PREFIX);
    }

    private void initZephyrReporter_IOS() {
        if (FLAG_UPDATEJIRA)
            genericFunctions.initZephyrReporter(JIRA_USER_NAME, JIRA_PASSWORD, JIRA_PROJECT_NAME, JIRA_RELEASE_NAME_PREFIX,
                    JIRA_TEST_REPO_RELEASE_NAME, JIRA_TEST_REPO_CYCLE_NAME, JIRA_NEW_CYCLE_NAME_PREFIX);
    }

    private void initKlovReporter() {
        if (FLAG_UPDATEKLOV) {
            if (GlobalData.PLATFORM == Platform.ANDROID)
                genericFunctions.initKlovReporter(KLOV_PROJECTNAME, KLOV_SERVERIP, KLOV_MONGODBPORT, KLOV_SERVERPORT);
            else
                genericFunctions.initKlovReporter(KLOV_PROJECTNAME_IOS, KLOV_SERVERIP, KLOV_MONGODBPORT, KLOV_SERVERPORT);
        }
    }

    private void initExtentReporter() {
        genericFunctions.initDirectories();
        if (GlobalData.PLATFORM == Platform.ANDROID)
            genericFunctions.initExtentReporter(EXTENTREPORT_DOCUMENTTITLE, EXTENTREPORT_REPORTNAME, FLAG_UPDATEJIRA, FLAG_CAPTURESCREENSHOTS,
                    FLAG_REMOVE_RETRIEDTESTS);
        else
            genericFunctions.initExtentReporter(EXTENTREPORT_DOCUMENTTITLE_IOS, EXTENTREPORT_REPORTNAME_IOS, FLAG_UPDATEJIRA, FLAG_CAPTURESCREENSHOTS,
                    FLAG_REMOVE_RETRIEDTESTS);
    }

    private void updateResultsToDing() {
        if (FLAG_UPDATEDING) {
            if (GlobalData.PLATFORM == Platform.ANDROID)
                updateResultsToDing_Android();
            else
                updateResultsToDing_IOS();
        }

    }

    private void updateResultsToDing_Android() {
        genericFunctions.pushResultsToDingGroup(DING_ACCESS_TOKEN_AUTOMATIONGROUP, DING_MESSAGETITLE_ANDROID, JENKINS_JOBNAME, BUILDNAME,
                DING_THRESHOLDPERCENTAGE, DING_ANDROIDLOGO_URL, JENKINS_REPORT_URL, APPSTAMP);
    }

    private void updateResultsToDing_IOS() {
        genericFunctions.pushResultsToDingGroup(DING_ACCESS_TOKEN_AUTOMATIONGROUP, DING_MESSAGETITLE_IOS, JENKINS_JOBNAME, BUILDNAME,
                DING_THRESHOLDPERCENTAGE_IOS, DING_IOS_LOGO_URL, JENKINS_REPORT_URL, APPSTAMP);
    }

    private void updatePerformanceData() throws IOException {
        if (FLAG_UPDATEPERFMON) {
            PerformanceMonitor.getInstance().pushToPrometheus();
        }
    }

    private void initAppiumDrivers() {
        try {
            genericFunctions.initAppiumServers_Drivers(LogLevel.ERROR);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(0);
        }
    }

    private void initApplication() {
        genericFunctions.downloadAndInstallApp();
    }

    private void quitAppiumDrivers() {
        genericFunctions.closeAppAllDevices();
        //genericFunctions.killAppiumServers();
        if (GlobalData.PLATFORM == Platform.IOS)
            ((GenericFunctionsIOS) genericFunctions).closeAllSimulatorApp();
    }

    private void updateDetailsExtentReports_AfterSuite() {
        genericFunctions.addExecutionDetails_ExtentReport();
        if (INFRA.equalsIgnoreCase(com.one97.paytm.appautomation.constants.Constants.INFRA_BROWSERSTACK))
            ExtentManager.addSystemInfo("BrowserStack Link", "<a target=\"_blank\" href=\"" + BROWSERSTACK_URL + BROWSERSTACK_SESSIONID + "\"> Click Here to open complete Browser Stack Report </a>");
    }

    private void initGenericFunctions() {
        if (GlobalData.PLATFORM == Platform.ANDROID)
            initGenericFunctions_Android();
        else
            initGenericFunctions_IOS();
    }

    private void initGenericFunctions_Android() {
        genericFunctions = GenericFactory.getGenericFunctions();
    }

    private void initGenericFunctions_IOS() {
        genericFunctions = (GenericFunctionsIOS) GenericFactory.getGenericFunctions();
        genericFunctions.initTimeOuts(ELEMENT_TIMEOUT, PAGELOAD_TIMEOUT, APPIUM_NEW_COMMAND_TIMEOUT, DEFAULT_IMPLICITWAIT, DOWNLOADING_TIMEOUT);
    }

    private void initCustomListeners() {
        InvocationListener.setInvocationListener(this);
        TestNGReportListener.initStatusListener(this);
    }

    private void pushResultsToDashboard() {
        if (FLAG_UPDATEDASHBOARD) {
            genericFunctions.pushResultsToDashboard(DASHBOARD_PROJECTNAME,  JENKINS_REPORT_URL,APPSTAMP);
        }
    }

    private void sendBrowserStackEMail() {
        if (FLAG_TRIGGEREMAIL) {
            String toEmail = "ankit8.gupta@paytm.com,prasenjit.chakraborty@paytm.com,varsha.gupta@paytm.com,nishith.tripathi@paytm.com";
            String text = genericFunctions.executionDetailsContent(DING_MESSAGETITLE_ANDROID, APPSTAMP, BROWSERSTACK_URL + BROWSERSTACK_SESSIONID);
            genericFunctions.initSendEmail("digital.apps.test@paytm.com", "qvkimfnkbytbmyxo", "digital.apps.test@paytm.com", toEmail, "Browserstack Automation Report", text);
        }
    }

    @Override
    public void performBeforeInvocation(IInvokedMethod iInvokedMethod, ITestResult result) {
    }

    @Override
    public void performAfterInvocation(IInvokedMethod iInvokedMethod, ITestResult result) {
        if (result.getStatus() == ITestResult.FAILURE && !result.getThrowable().toString().contains("exception.SkipTestException")) {
            if (GlobalData.PLATFORM == Platform.ANDROID) {
                SharedActions.getInstance().checkSessionAndPerformLogin();
                //if (INFRA.equalsIgnoreCase(com.one97.paytm.appautomation.constants.Constants.INFRA_LOCAL))
                ((GenericFunctionsAndroid) genericFunctions).addADBLogsInReport(result);
            }
        }

    }

    @Override
    public void performOnSuccess(ITestResult iTestResult) {

    }

    @Override
    public void performOnFailure(ITestResult iTestResult) {
        DriverManager.setLoggedIn(false);
    }

    @Override
    public void performOnSkip(ITestResult iTestResult) {

    }

}
