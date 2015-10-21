package com.disney.wdpr.jenkins.dto.extract.comments;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

/**
 * @author matt.b.carson
 *
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class CommentCreatorWrapper {

    @JsonProperty("results")
    private CommentCreator commentCreator;

    public void setCommentCreator(final CommentCreator commentCreator) {
        this.commentCreator = commentCreator;
    }

    public CommentCreator getCommentCreator() {
        return commentCreator;
    }

}
