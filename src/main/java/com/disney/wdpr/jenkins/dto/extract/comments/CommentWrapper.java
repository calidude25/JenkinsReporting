package com.disney.wdpr.jenkins.dto.extract.comments;

import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

/**
 * @author matt.b.carson
 *
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class CommentWrapper {

    @JsonProperty("results")
    private List<Comment> comments = new ArrayList<Comment>();

    public void setComments(final List<Comment> Comments) {
        this.comments = Comments;
    }

    public List<Comment> getComments() {
        return comments;
    }

}
