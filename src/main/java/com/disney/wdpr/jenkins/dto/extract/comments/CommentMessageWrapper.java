package com.disney.wdpr.jenkins.dto.extract.comments;

import org.codehaus.jackson.annotate.JsonProperty;

public class CommentMessageWrapper {

    @JsonProperty("d")
    private CommentWrapper commentWrapper;

    public CommentWrapper getCommentWrapper() {
        return commentWrapper;
    }

    public void setCommentWrapper(final CommentWrapper commentWrapper) {
        this.commentWrapper = commentWrapper;
    }
    
}
