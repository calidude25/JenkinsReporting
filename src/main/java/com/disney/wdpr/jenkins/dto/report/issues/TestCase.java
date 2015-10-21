package com.disney.wdpr.jenkins.dto.report.issues;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

import com.disney.wdpr.jenkins.vo.Totals;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TestCase {
    protected final static String SETUP_METHOD ="setUp";
    protected final static String TEARDOWN_METHOD ="tearDown";
    protected final static String PASS ="PASSED";
    protected final static String FAIL ="FAILED";
    protected final static String SKIP ="SKIPPED";

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
                jobTotals.incPass();
                runningTotals.incPass();
                break;
            case FAIL:
                jobTotals.incFail();
                runningTotals.incFail();
                break;
            case SKIP:
                jobTotals.incSkip();
                runningTotals.incSkip();
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
