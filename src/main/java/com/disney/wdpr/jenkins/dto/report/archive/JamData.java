package com.disney.wdpr.jenkins.dto.report.archive;

public interface JamData {

    public String getId();

    public String getRecord();

    public final static String DATE_FORMAT ="MM-dd-yyyy:hh:mma";
    public final static String OUTPUT_DELIMITER ="|";

}
