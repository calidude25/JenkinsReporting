package com.disney.wdpr.jenkins.dto.report.archive;

import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

/**
 * @author matt.b.carson
 *
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class TaskWrapper {

    @JsonProperty("results")
    private List<Task> tasks = new ArrayList<Task>();

    public void setTasks(final List<Task> tasks) {
        this.tasks = tasks;
    }

    public List<Task> getTasks() {
        return tasks;
    }

}
