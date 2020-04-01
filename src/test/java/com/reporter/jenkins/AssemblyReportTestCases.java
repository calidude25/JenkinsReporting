package com.reporter.jenkins;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.reporter.jenkins.Launch;
import com.reporter.jenkins.dto.report.Build;
import com.reporter.jenkins.dto.report.BuildListing;
import com.reporter.jenkins.dto.report.Job;
import com.reporter.jenkins.dto.report.JobListing;
import com.reporter.jenkins.dto.report.TestReport;
import com.reporter.jenkins.dto.report.View;
import com.reporter.jenkins.dto.report.issues.TestCase;
import com.reporter.jenkins.integration.report.JenkinsIntegration;
import com.reporter.jenkins.manager.report.ReportManagerImpl;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"file:src/main/resources/reportContext.xml", "file:src/main/resources/sharedContext.xml"})
public class AssemblyReportTestCases {
    private static Logger log = Logger.getLogger(AssemblyReportTestCases.class);

    ReportManagerImpl reportManager;
    private JenkinsIntegration jenkinsIntegration;

    @Test
    public void testProcess() throws Exception {
        String viewName = "03. Appium Mobile Tests";
//        String jobName = "Jenkins Reporting Mobile";
//        String viewName = "DVC Automation";
        String jobName = "Jenkins Reporting DVC Automation";

        Map<String, String> paramMap = new HashMap<String, String>();
        paramMap.put(Launch.PARAM_JOB_NAME, jobName);
        paramMap.put(Launch.PARAM_VIEW_NAME, viewName);

        reportManager.process(paramMap);
    }

    @Test
    public void testApi() {

        try {

            String viewName = "03. Appium Mobile Tests";
            View view = jenkinsIntegration.getView(viewName);
            List<JobListing> jobListings = view.getJobs();

            JobListing mdxJobListing = null;
            for (JobListing jobListing : jobListings) {
                String name = jobListing.getName();
                log.info(name);
                if(name.equals("MDX IOS Appium Smoke Test")) {
                    mdxJobListing = jobListing;
                }
            }


            Job job = jenkinsIntegration.getJob(mdxJobListing.getName());
            BuildListing buildListing = job.getLastBuild();

            log.info("Job: "+mdxJobListing.getName()+" - last build: "+buildListing.getNumber());

            Build build = jenkinsIntegration.getBuild(job, buildListing.getNumber());
            log.info("result: "+build.getResult());

            TestReport testReport = jenkinsIntegration.getTestReport(job, buildListing.getNumber());
            if(testReport == null) {
//                throw new RuntimeException("No report for given build/job.");
                // could be the job is running at the moment.
            } else {
                log.info("Test Report: Total="+testReport.getTotalCount()+" - Passed="+testReport.getPassCount()+" - Failed="+testReport.getFailCount()+" - Skipped="+testReport.getSkipCount());

                List<TestCase> testCases = testReport.getChildReports().get(0).getResult().getSuites().get(0).getTestCases();
                for (TestCase caseInstance : testCases) {
                    log.info("TestCase name="+caseInstance.getName());
                }

            }

        } catch (final RuntimeException e) {
            e.printStackTrace();
            throw e;
        }

    }

    @Resource
    public void setReportManagerImpl(final ReportManagerImpl reportManager) {
        this.reportManager = reportManager;
    }


    @Resource
    public void setJenkinsIntegration(final JenkinsIntegration jenkinsIntegration) {
        this.jenkinsIntegration = jenkinsIntegration;
    }

}
