package com.disney.wdpr.jenkins.dto.report.archive;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

/**
 * @author matt.b.carson
 *
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Task implements JamData, Comparable<Task> {

    @JsonProperty("TaskId")
    private String id;
    @JsonProperty("AssigneeId")
    private String assigneeId;

    @Override
    public int compareTo(final Task task) {
        final int idCompare=id.compareTo(task.getId());
        final int assigneeCompare=assigneeId.compareTo(task.getAssigneeId());

        if(idCompare == 0) {
            return assigneeCompare;
        } else {
            return idCompare;
        }
    }


    @Override
    public String getId() {
        return id;
    }

    public String getAssigneeId() {
        return assigneeId;
    }

    public void setAssigneeId(final String assigneeId) {
        this.assigneeId = assigneeId;
    }

    public void setId(final String id) {
        this.id = id;
    }

    @Override
    public String getRecord() {
        return null;
    }

}
