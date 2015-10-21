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
public class GroupWrapper {

    @JsonProperty("results")
    private List<Group> groups = new ArrayList<Group>();

    public void setGroups(final List<Group> groups) {
        this.groups = groups;
    }

    public List<Group> getGroups() {
        return groups;
    }

}
