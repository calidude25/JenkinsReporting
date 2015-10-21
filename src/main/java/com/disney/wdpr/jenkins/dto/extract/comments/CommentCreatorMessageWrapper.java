package com.disney.wdpr.jenkins.dto.extract.comments;

import org.codehaus.jackson.annotate.JsonProperty;

public class CommentCreatorMessageWrapper {

    @JsonProperty("d")
    private CommentCreatorWrapper commentCreatorWrapper;

    public CommentCreatorWrapper getCommentCreatorWrapper() {
        return commentCreatorWrapper;
    }

    public void setCommentCreatorWrapper(final CommentCreatorWrapper commentCreatorWrapper) {
        this.commentCreatorWrapper = commentCreatorWrapper;
    }
    
}
