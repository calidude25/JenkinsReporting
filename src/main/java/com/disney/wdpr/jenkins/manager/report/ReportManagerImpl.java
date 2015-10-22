package com.disney.wdpr.jenkins.manager.report;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.log4j.NDC;
import org.apache.poi.ss.examples.html.ToHtml;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.disney.wdpr.jenkins.Launch;
import com.disney.wdpr.jenkins.dto.report.Build;
import com.disney.wdpr.jenkins.dto.report.BuildListing;
import com.disney.wdpr.jenkins.dto.report.Job;
import com.disney.wdpr.jenkins.dto.report.JobListing;
import com.disney.wdpr.jenkins.dto.report.TestReport;
import com.disney.wdpr.jenkins.dto.report.View;
import com.disney.wdpr.jenkins.dto.report.issues.TestCase;
import com.disney.wdpr.jenkins.integration.report.JenkinsIntegration;
import com.disney.wdpr.jenkins.manager.JobManager;
import com.disney.wdpr.jenkins.vo.Totals;

/**
 * @author matt.b.carson
 */
public class ReportManagerImpl implements JobManager {

    private static Logger log = Logger.getLogger(ReportManagerImpl.class);
    private JenkinsIntegration jenkinsIntegration;

    protected final static String OUTPUT_FILE_NAME = "GQE_Automation_Summary_Report.xlsx";
    protected final static int JOB_CELL = 0;
    protected final static int COUNT_CELL = 1;
    protected final static int PASS_CELL = 2;
    protected final static int FAIL_CELL = 3;
    protected final static int SKIP_CELL = 4;
//    protected final static int ISSUE_CELL = 5;
    protected final static int COLUMN_COUNT = 5;

    @Override
    public void process(Map<String, String> paramMap) throws Exception {
        List<Totals> reportLines = this.executeApiRequests(paramMap.get(Launch.PARAM_VIEW_NAME), paramMap.get(Launch.PARAM_JOB_NAME));
        this.createReport(reportLines);
    }

    private void createReport(List<Totals> reportLines) throws IOException {
        int rowCount = 0;

        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet();

        XSSFRow row = sheet.createRow(rowCount++);
        this.createHeaderRow(workbook, row);

        for (Totals totals : reportLines) {
            row = sheet.createRow(rowCount++);
            this.createRow(workbook, row, totals);
        }

        for (int j = 0; j <= COLUMN_COUNT; ++j) {
            sheet.autoSizeColumn(j);
        }

        FileOutputStream fout = null;
        ByteArrayOutputStream outputStream = null;

        try {

            // Directory path where the xlsx file will be created is stored in the
            // json config file.
            fout = new FileOutputStream(OUTPUT_FILE_NAME);
            outputStream = new ByteArrayOutputStream();
            workbook.write(outputStream);
            outputStream.writeTo(fout);
            System.out.println("SPREADSHEET written to " + OUTPUT_FILE_NAME);

            int lastIndex = OUTPUT_FILE_NAME.lastIndexOf('.');

            if (lastIndex > 0) {
                String htmlFile = OUTPUT_FILE_NAME.substring(0, lastIndex) + ".html";
                ToHtml toHtml = ToHtml.create(workbook, new PrintWriter(new FileWriter(htmlFile)));
                toHtml.setCompleteHTML(true);
                toHtml.printPage();

                System.out.println("HTML written to " + htmlFile);
            }

        } catch (Exception ex) {
            System.out.println("FAILED TO WRITE TO FILE");
            ex.printStackTrace();

        } finally {

            if (outputStream != null) {
                outputStream.close();
            }

            if (fout != null) {
                fout.close();
            }
        }

    }

    private void createHeaderRow(XSSFWorkbook workbook, XSSFRow row){
        XSSFCell cell = row.createCell(JOB_CELL);
        decorateHeaderCell(workbook, cell);
        cell.setCellValue("Job");

        cell = row.createCell(COUNT_CELL);
        decorateHeaderCell(workbook, cell);
        cell.setCellValue("Test Count");

        cell = row.createCell(PASS_CELL);
        decorateHeaderCell(workbook, cell);
        cell.setCellValue("Pass");

        cell = row.createCell(FAIL_CELL);
        decorateHeaderCell(workbook, cell);
        cell.setCellValue("Fail");

        cell = row.createCell(SKIP_CELL);
        decorateHeaderCell(workbook, cell);
        cell.setCellValue("Skip");
    }

    private void createRow(XSSFWorkbook workbook, XSSFRow row, Totals totals){
        XSSFCell cell = row.createCell(JOB_CELL);
        cell.setCellValue(totals.getName());

        cell = row.createCell(COUNT_CELL);
        cell.setCellValue(totals.getTestTotal());

        cell = row.createCell(PASS_CELL);
        decorateSuccessCell(workbook, cell);
        cell.setCellValue(totals.getPassTotal());

        cell = row.createCell(FAIL_CELL);
        decorateFailureCell(workbook, cell);
        cell.setCellValue(totals.getFailTotal());

        cell = row.createCell(SKIP_CELL);
        decorateSkippedCell(workbook, cell);
        cell.setCellValue(totals.getSkipTotal());
    }


    protected File getFile(final String name){
        return new File(name);
    }


    protected List<Totals> executeApiRequests(String viewName, String jobName) {
        List<Totals> reportLines = new ArrayList<Totals>();

        try {

            log.info("starting process...");

            View view = jenkinsIntegration.getView(viewName);
            List<JobListing> jobListings = view.getJobs();

            Totals runningTotal = new Totals("Totals");
            Totals jobTotal = null;

            log.info("Executing API requests... ");
            for (JobListing jobListing : jobListings) {

                log.info("Job Name: "+jobListing.getName());

                if (!jobName.equals(jobListing.getName())) {
                    NDC.push("Job: " + jobListing.getName());
                    jobTotal = new Totals(jobListing.getName());
                    Job job = jenkinsIntegration.getJob(jobListing.getName());
                    BuildListing buildListing = job.getLastBuild();
                    TestReport testReport = null;
                    if (buildListing != null) {
                        log.info("last build: " + buildListing.getNumber());
                        NDC.push(" - build: " + buildListing.getNumber());
                        Build build = jenkinsIntegration.getBuild(job, buildListing.getNumber());
                        log.info("Result: " + build.getResult());
                        testReport = jenkinsIntegration.getTestReport(job, buildListing.getNumber());

                        if (testReport == null) {
                            log.info("No report for given build/job.");
                        } else {

                            List<TestCase> testCases = testReport.getChildReports().get(0).getResult().getSuites().get(0).getTestCases();

                            for (TestCase testCase : testCases) {
                                testCase.incrementCounts(jobTotal, runningTotal);
                            }

                            log.info("Test Report: Total=" + jobTotal.getTestTotal() + " - Passed=" + jobTotal.getPassTotal() + " - Failed=" + jobTotal.getFailTotal() + " - Skipped=" + jobTotal.getSkipTotal());

                        }
                    }
                }
                reportLines.add(jobTotal);
                NDC.pop();
                NDC.pop();
            }
            reportLines.add(runningTotal);
            log.info("Total Tests="+runningTotal.getTestTotal()+" - Total Passed="+runningTotal.getPassTotal()+" - Total Failed="+runningTotal.getFailTotal()+" - Total Skipped="+runningTotal.getSkipTotal());
            log.warn("API requests complete");

        } catch (final Exception e) {
             log.fatal("Failure in process method",e);
             throw(e);
        } finally {
            log.warn("process method complete...");
            NDC.remove();
        }
        return reportLines;
    }


    public static void decorateHeaderCell(XSSFWorkbook workbook, XSSFCell cell) {
        XSSFFont font = workbook.createFont();
        font.setBold(true);
        XSSFCellStyle style = workbook.createCellStyle();
        style.setFont(font);
        style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        style.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND);
        cell.setCellStyle(style);
    }

    public static void decorateSuccessCell(XSSFWorkbook workbook, XSSFCell cell) {
        XSSFCellStyle style = workbook.createCellStyle();
        style.setFillForegroundColor(IndexedColors.LIME.getIndex());
        style.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND);
        cell.setCellStyle(style);
    }

    public static void decorateFailureCell(XSSFWorkbook workbook, XSSFCell cell) {
        XSSFCellStyle style = workbook.createCellStyle();
        style.setFillForegroundColor(IndexedColors.RED.getIndex());
        //style.setFillForegroundColor(IndexedColors.CORAL.getIndex());
        style.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND);
        cell.setCellStyle(style);
    }

    public static void decorateSkippedCell(XSSFWorkbook workbook, XSSFCell cell) {
        XSSFCellStyle style = workbook.createCellStyle();
        style.setFillForegroundColor(IndexedColors.LIGHT_YELLOW.getIndex());
        style.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND);
        cell.setCellStyle(style);
    }

    public static void decorateBoldCell(XSSFWorkbook workbook, XSSFCell cell) {
        XSSFFont font = workbook.createFont();
        font.setBold(true);
        XSSFCellStyle style = workbook.createCellStyle();
        style.setFont(font);
        cell.setCellStyle(style);
    }

    protected void checkExceptions(final Date date, final List<Exception> failures) throws Exception{
        log.info("Checking for exceptions... ");
        final StringBuilder exceptionMessages = new StringBuilder("<br><br>");
        for (final Exception exception : failures) {
            exceptionMessages.append(exception.getMessage()+"<br>");
        }
    }

    protected List<Exception> getNewExceptionList() {
        return new ArrayList<Exception>();
    }

    protected Date getDate() {
        return new Date();
    }

    public void setJenkinsIntegration(JenkinsIntegration jenkinsApiIntegration) {
        this.jenkinsIntegration = jenkinsApiIntegration;
    }

}
