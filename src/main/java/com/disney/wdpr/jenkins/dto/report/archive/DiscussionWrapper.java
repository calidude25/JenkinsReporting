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
public class DiscussionWrapper {

    @JsonProperty("results")
    private List<Discussion> discussions = new ArrayList<Discussion>();

    public void setDiscussions(final List<Discussion> discussions) {
        this.discussions = discussions;
    }

    public List<Discussion> getDiscussions() {
        return discussions;
    }

}
