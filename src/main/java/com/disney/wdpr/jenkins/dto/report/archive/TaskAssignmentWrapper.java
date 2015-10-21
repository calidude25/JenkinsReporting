package com.disney.wdpr.jenkins.dto.report.archive;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

/**
 * @author matt.b.carson
 *
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class TaskAssignmentWrapper {

    @JsonProperty("results")
    private TaskAssignment taskAssignment;

    public void setTaskAssignment(final TaskAssignment taskAssignment) {
        this.taskAssignment = taskAssignment;
    }

    public TaskAssignment getTaskAssignment() {
        return taskAssignment;
    }

}
