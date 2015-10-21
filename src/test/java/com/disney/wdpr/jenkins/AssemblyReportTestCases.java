package com.disney.wdpr.jenkins;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.disney.wdpr.jenkins.dto.report.Build;
import com.disney.wdpr.jenkins.dto.report.BuildListing;
import com.disney.wdpr.jenkins.dto.report.Job;
import com.disney.wdpr.jenkins.dto.report.JobListing;
import com.disney.wdpr.jenkins.dto.report.TestReport;
import com.disney.wdpr.jenkins.dto.report.View;
import com.disney.wdpr.jenkins.dto.report.issues.TestCase;
import com.disney.wdpr.jenkins.integration.report.JenkinsIntegration;
import com.disney.wdpr.jenkins.manager.report.ReportManagerImpl;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"file:src/main/resources/reportContext.xml", "file:src/main/resources/sharedContext.xml"})
public class AssemblyReportTestCases {
    private static Logger log = Logger.getLogger(AssemblyReportTestCases.class);

    ReportManagerImpl reportManager;
    private JenkinsIntegration jenkinsIntegration;
//    private ODataIntegrationImpl oDataIntegrationImpl;

    @Test
    public void testProcess() throws Exception {
        String viewName = "03. Appium Mobile Tests";
        String jobName = "Jenkins Reporting Mobile";

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
                throw new RuntimeException("No report for given build/job.");
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

//    @Test
//    public void testApi() {
//
//        try {
//            final List<JamData> discussions = new ArrayList<JamData>();
//
//            final String firstname = "Reggie";
//            final String lastname = "Blanco";
//            String userId="92049268";
//
////            final String firstname = "Tiffany";
////            final String lastname = "Collis";
////            String userId="94000783";
//
////          final String firstname = "Pedro";
////          final String lastname = "Fernandez";
////          String userId="00132829";
//
//
//            final Employee employee = new Employee(userId, firstname, lastname, "P");
//
//
//            userId = "00000000".substring(userId.length()) + userId;
//
//            final String token = oDataIntegrationImpl.getToken(userId);
//            log.fatal(token);
//
//            final List<TaskAssignment> tasks = oDataIntegrationImpl.getTasks(token, userId);
//            for (final TaskAssignment taskAssignment : tasks) {
//                log.info(taskAssignment.getId());
//            }
//
//            final Set<Group> groups = oDataIntegrationImpl.getGroups(token, employee);
//            // oDataIntegrationImpl.getGroups("d5G8Gs9dWlfRV5atocx9PmidV4KxTf0MlwUq8kii");
//
//            log.info(groups.size());
//            SortedSet<Comment> comments = null;
//            for (final Group group : groups) {
//                discussions.addAll(oDataIntegrationImpl.getDiscussions(token, userId, group.getId()));
//                log.info("discussions size: " + discussions.size());
//                for (final JamData discussion : discussions) {
//                    log.info(discussion.getId());
//                    comments = oDataIntegrationImpl.getComments(token, userId, discussion.getId());
//
//                    for (final Comment comment : comments) {
//                        log.info("comment creator: " + comment.getCommentCreator().getLastName());
//                    }
//
//                }
//            }
//        } catch (final RuntimeException e) {
//            e.printStackTrace();
//            throw e;
//        }
//
//    }



    @Resource
    public void setReportManagerImpl(final ReportManagerImpl reportManager) {
        this.reportManager = reportManager;
    }


    @Resource
    public void setJenkinsIntegration(final JenkinsIntegration jenkinsIntegration) {
        this.jenkinsIntegration = jenkinsIntegration;
    }

}
