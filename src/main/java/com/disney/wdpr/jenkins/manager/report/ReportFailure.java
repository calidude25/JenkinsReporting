package com.disney.wdpr.jenkins.manager.report;

/**
 * @author matt.b.carson
 *
 */
public class ReportFailure {

    private String filename;
    private String exception;
    
    public ReportFailure(final String filename, final String exception) {
        super();
        this.filename = filename;
        this.exception = exception;
    }
    
    public void setFilename(final String filename) {
        this.filename = filename;
    }
    public void setException(final String exception) {
        this.exception = exception;
    }
}
