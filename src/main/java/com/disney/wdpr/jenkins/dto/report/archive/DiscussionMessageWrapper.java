package com.disney.wdpr.jenkins.dto.report.archive;

import org.codehaus.jackson.annotate.JsonProperty;

public class DiscussionMessageWrapper {

    @JsonProperty("d")
    private DiscussionWrapper discussionWrapper;

    public DiscussionWrapper getDiscussionWrapper() {
        return discussionWrapper;
    }

    public void setDiscussionWrapper(final DiscussionWrapper discussionWrapper) {
        this.discussionWrapper = discussionWrapper;
    }
    
    
}
