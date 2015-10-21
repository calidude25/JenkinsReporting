package com.disney.wdpr.jenkins.dto.report.archive;

import org.codehaus.jackson.annotate.JsonProperty;

public class GroupMessageWrapper {

    @JsonProperty("d")
    private GroupWrapper groupWrapper;

    public GroupWrapper getGroupWrapper() {
        return groupWrapper;
    }

    public void setGroupWrapper(final GroupWrapper groupWrapper) {
        this.groupWrapper = groupWrapper;
    }
    
    
}
