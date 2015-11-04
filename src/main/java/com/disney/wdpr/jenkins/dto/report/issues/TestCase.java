package com.disney.wdpr.jenkins.dto.report.issues;

import org.apache.log4j.Logger;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

import com.disney.wdpr.jenkins.manager.report.ReportManagerImpl;
import com.disney.wdpr.jenkins.vo.Totals;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TestCase {

    private static Logger log = Logger.getLogger(TestCase.class);
    protected final static String SETUP_METHOD ="setUp";
    protected final static String TEARDOWN_METHOD ="tearDown";
    protected final static String PASS ="PASSED";
    protected final static String FAIL ="FAILED";
    protected final static String SKIP ="SKIPPED";
    protected final static String FIXED ="FIXED";
    protected final static String REGRESSION ="REGRESSION";

    @JsonProperty("name")
    private String name;

    @JsonProperty("status")
    private String status;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void incrementCounts(Totals jobTotals, Totals runningTotals){
        if(!isConfigMethod(name)) {
            switch (status) {
            case PASS:
            case FIXED:
                jobTotals.incPass();
                runningTotals.incPass();
                break;
            case FAIL:
            case REGRESSION:
                jobTotals.incFail();
                runningTotals.incFail();
                break;
            case SKIP:
                jobTotals.incSkip();
                runningTotals.incSkip();
                break;
            default:
                log.error("STATUS: "+status+" is not recognized. This needs to be added and handled in the reporting code.");
                break;
            }
        }
    }


    private boolean isConfigMethod(String testName){
        if(testName.equals(SETUP_METHOD) || testName.equals(TEARDOWN_METHOD)) {
            return true;
        } else {
            return false;
        }
    }

}
