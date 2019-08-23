package com.project.test.testrailintegration;

import static com.project.test.testrailintegration.api.model.ResultStatus.BLOCKED;
import static com.project.test.testrailintegration.api.model.ResultStatus.FAILED;
import static com.project.test.testrailintegration.api.model.ResultStatus.PASSED;
import static com.project.test.testrailintegration.api.model.TestCaseAutomation.AUTOMATED;
import static com.project.test.testrailintegration.core.ProductProperties.getEnv;
import static java.lang.Integer.max;
import static java.lang.Integer.parseInt;
import static java.lang.String.format;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.testng.ISuite;
import org.testng.ISuiteListener;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import com.project.test.testrailintegration.api.client.method.AddResultsRequest;
import com.project.test.testrailintegration.api.client.method.AddTestRunRequest;
import com.project.test.testrailintegration.api.client.method.CloseTestRunRequest;
import com.project.test.testrailintegration.api.client.method.GetCasesRequest;
import com.project.test.testrailintegration.api.client.method.UpdateTestCaseRequest;
import com.project.test.testrailintegration.api.model.ResultStatus;
import com.project.test.testrailintegration.api.model.TestCase;
import com.project.test.testrailintegration.api.model.TestResults;
import com.project.test.testrailintegration.api.model.TestResults.TestResult;
import com.project.test.testrailintegration.api.model.TestRun;
import com.project.test.testrailintegration.config.Config;
import com.project.test.testrailintegration.core.CommentReporter;
import com.project.test.testrailintegration.core.LogUtilities;
import com.project.test.testrailintegration.core.ProductProperties;

public class TestRailListener implements ITestListener, ISuiteListener {

	private static final Pattern TEST_NAME = Pattern.compile("^C(\\d+)_.+");
	private TestRun run;
	private TestResults testResults;
	private TestCase[] projectCases;
	private static String appVersion = "";

	@Override
	public void onStart(ITestContext testContext) {
		Config.init();
		if (Config.isListenerEnabled()) {
			ProductProperties.initEnv(testContext.getSuite());
			run = new TestRun(
					format("Automated run on %s for ver. %s at %s", getEnv(), appVersion, getFormattedDateTime()));
			run.project_id = Integer.parseInt(Config.PROJECT_ID);
			testResults = new TestResults();
		}
	}

	private String getFormattedDateTime() {
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd 'at' h:mm a, z");
		return dateFormat.format(new Date());
	}

//  private String getHostName() {
//    try {
//      return InetAddress.getLocalHost().getHostName();
//    } catch (UnknownHostException e) {
//      return "Unknown";
//    }
//  }

	@Override
	public void onTestStart(ITestResult result) {
		if (Config.isListenerEnabled()) {
			CommentReporter.clear();
		}
	}

	@Override
	public void onTestSuccess(ITestResult tr) {
		reportResult(PASSED, tr);
	}

	@Override
	public void onTestFailure(ITestResult tr) {
		reportResult(FAILED, tr);
	}

	@Override
	public void onTestSkipped(ITestResult tr) {
		CommentReporter.clear();
		reportResult(BLOCKED, tr);
	}

	@Override
	public void onTestFailedButWithinSuccessPercentage(ITestResult tr) {
		reportResult(FAILED, tr);
	}

	private void reportResult(ResultStatus status, ITestResult tr) {
		if (Config.isListenerEnabled()) {
			String testName = tr.getMethod().getMethodName();
			if (!TEST_NAME.matcher(testName).matches()) {
				testName = tr.getTestClass().getRealClass().getSimpleName();
			}
			int caseId = getCaseId(testName);
			if (caseId > 0) {
				LogUtilities.logInfoMessage(
						format("RailsIntegrator: save to memory result %s, for test case C%d", status.name(), caseId));
				TestResult result = testResults.new TestResult(status, caseId, getTestDuration(tr), appVersion);
				testResults.results.add(result);
				run.case_ids.add(caseId);
			} else {
				LogUtilities.logWarningMessage("Can not parse test case id in " + testName);
			}
		}
	}

	@Override
	public void onFinish(ITestContext context) {
		if (Config.isListenerEnabled()) {
			run.id = new AddTestRunRequest().createRun(run).id;
			projectCases = new GetCasesRequest().getProjectCases();
			addResultsToTestRun();
			updateTestCasesStatuses();
			new CloseTestRunRequest().closeRun(run);
		}
	}

	public void addResultsToTestRun() {
		Arrays.sort(projectCases);
		Collections.sort(testResults.results);
		Iterator<TestResult> resultsIterator = testResults.results.iterator();
		// Remove test cases that does not have ID in test rail.
		int projectCasesIndex = 0;
		TestResult previousTR = null;
		while (resultsIterator.hasNext()) {
			TestResult currentTR = resultsIterator.next();
			// Since both arrays are sorted, we may iterate through both of them
			// simultaneously.
			while (projectCasesIndex < projectCases.length
					&& projectCases[projectCasesIndex].id < currentTR.getCase_id()) {
				projectCasesIndex++;
			}
			if (projectCases[projectCasesIndex].id != currentTR.getCase_id()) {
				LogUtilities
						.logWarningMessage(format("There is no case with id=C%s in TestRail!", currentTR.getCase_id()));
				resultsIterator.remove();
				continue;
			}
			// Remove duplicates, update status to the worst, if at least test fail, fail
			// the whole TC.
			if (previousTR != null && previousTR.getCase_id() == currentTR.getCase_id()) {
				previousTR.setStatus_id(max(previousTR.getStatus_id(), currentTR.getStatus_id()));
				resultsIterator.remove();
				continue;
			}
			previousTR = currentTR;
		}

		if (testResults.results.size() > 0) {
			new AddResultsRequest().addResultsToRun(testResults, run.id);
		} else {
			LogUtilities.logWarningMessage("Cases not yet described in TestRail");
		}
	}

	private void updateTestCasesStatuses() {
		// Since both arrays are sorted, we may iterate through both of them
		// simultaneously.
		int projectCasesIndex = 0;
		UpdateTestCaseRequest updateTestCaseRequest = new UpdateTestCaseRequest();
		for (TestResult testResult : testResults.results) {
			// Since both arrays are sorted, we may iterate through both of them
			// simultaneously.
			while (projectCasesIndex < projectCases.length
					&& projectCases[projectCasesIndex].id < testResult.getCase_id()) {
				projectCasesIndex++;
			}
			// At this point we can ensure that projectCases[projectCasesIndex].id ==
			// testResult.case_id
			// since we deleted not matching records in previous method.
			if (projectCases[projectCasesIndex].custom_automation != AUTOMATED.getValue()) {
				updateTestCaseRequest.markCaseAsAutomated(testResult.getCase_id());
			}
		}
	}

	public int getCaseId(String testName) {
		Matcher matcher = TEST_NAME.matcher(testName);
		if (!matcher.matches()) {
			return -1;
		}
		return parseInt(matcher.group(1));
	}

	private String getTestDuration(ITestResult result) {
		try {
			long duration = (result.getEndMillis() - result.getStartMillis());
			long ms2mins = TimeUnit.MILLISECONDS.toMinutes(duration);
			long ms2secs = TimeUnit.MILLISECONDS.toSeconds(duration);
			long mins2secs = TimeUnit.MINUTES.toSeconds(ms2mins);
			String durationString = String.format("%dm %ds", ms2mins, ms2secs - mins2secs);

			if (durationString.equals("0m 0s")) {
				durationString = "0m 1s";
			}

			return durationString;
		} catch (Exception ex) {
			return "0m 1s";
		}
	}

	public static void setApplicationVersion(String appVer) {
		appVersion = appVer;
	}

	private String getApplicationVersion() {
		return appVersion;
	}

	@Override
	public void onStart(ISuite suite) {
		appVersion = getApplicationVersion();
		if (appVersion == null || appVersion.isEmpty()) {
			appVersion = "0.0.0";
		}
	}

	@Override
	public void onFinish(ISuite suite) {
		// No work to be done here.
	}
};