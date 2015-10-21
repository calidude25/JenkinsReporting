package com.disney.wdpr.jenkins.dto.report.archive;

import org.codehaus.jackson.annotate.JsonProperty;

public class TaskAssignmentMessageWrapper {

    @JsonProperty("d")
    private TaskAssignmentWrapper taskAssignmentWrapper;

    public TaskAssignmentWrapper getTaskAssignmentWrapper() {
        return taskAssignmentWrapper;
    }

    public void setTaskAssignmentWrapper(final TaskAssignmentWrapper taskAssignmentWrapper) {
        this.taskAssignmentWrapper = taskAssignmentWrapper;
    }


}
