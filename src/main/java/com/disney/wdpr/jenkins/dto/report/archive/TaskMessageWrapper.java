package com.disney.wdpr.jenkins.dto.report.archive;

import org.codehaus.jackson.annotate.JsonProperty;

public class TaskMessageWrapper {

    @JsonProperty("d")
    private TaskWrapper taskWrapper;

    public TaskWrapper getTaskWrapper() {
        return taskWrapper;
    }

    public void setTaskWrapper(final TaskWrapper taskWrapper) {
        this.taskWrapper = taskWrapper;
    }


}
