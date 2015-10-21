package com.disney.wdpr.jenkins.dto.extract.comments;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

import com.disney.wdpr.jenkins.dto.report.archive.JamData;
import com.disney.wdpr.jenkins.manager.ManagerUtils;

/**
 * @author matt.b.carson
 *
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Comment implements JamData, Comparable<Comment> {
    @JsonProperty("Id")
    private  String id;

    private String userId;
    private  String discussionId;

    @JsonProperty("Text")
    private  String comment;

    private CommentCreator commentCreator;

    @JsonProperty("CreatedAt")
    private  String created;
    @JsonProperty("LastModifiedAt")
    private  String lastModified;

    private long lastModRawDate;


    public Comment() {
        id = "";
        userId = "";
        discussionId = "";
        comment = "";
        created = "";
        lastModified = "";
    }

    public final static String USER_ID="USER_ID";
    public final static String DISCUSSION_ID="DISCUSSION_ID";
    public final static String COMMENT="COMMENT";
    public final static String AUTHOR="AUTHOR";
    public final static String CREATED="CREATED";
    public final static String LAST_MODIFIED="LAST_MODIFIED";

    @Override
    public int compareTo(final Comment comment) {
        final int userCompare=userId.compareTo(comment.getUserId());
        final int discussionCompare=discussionId.compareTo(comment.getDiscussionId());
        final long lastModCompare=lastModRawDate - comment.getLastModRawDate();

        if(userCompare == 0) {
            if(discussionCompare==0){
                return (int) lastModCompare;
            } else {
                return discussionCompare;
            }
        } else {
            return userCompare;
        }
    }



    public String getUserId() {
        return userId;
    }
    @Override
    public String getId() {
        return id;
    }
    public String getDiscussionId() {
        return discussionId;
    }
    public String getComment() {
        return comment;
    }
    public CommentCreator getCommentCreator() {
        return commentCreator;
    }
    public String getCreated() {
        return created;
    }
    public String getLastModified() {
        return lastModified;
    }
    public void setUserId(final String userId) {
        this.userId = userId;
    }
    public void setDiscussionId(final String discussionId) {
        this.discussionId = discussionId;
    }
    public void setId(final String commentId) {
        id = commentId;
    }
    public void setComment(final String comment) {
        this.comment = comment.replaceAll("(\\r|\\n)", "");
    }
    public void setCreated(final String created) {
        final long millis = ManagerUtils.parseUnixEpochDate(created);
        this.created = ManagerUtils.formatDate(millis, DATE_FORMAT);
    }
    public void setLastModified(final String lastModified) {
        lastModRawDate = ManagerUtils.parseUnixEpochDate(lastModified);
        this.lastModified = ManagerUtils.formatDate(lastModRawDate, DATE_FORMAT);
    }

    public void setCommentCreator(final CommentCreator commentCreator) {
        this.commentCreator = commentCreator;
    }


    public long getLastModRawDate() {
        return lastModRawDate;
    }



    public static String getHeader() {
        return "\""+USER_ID+ "\""+JamData.OUTPUT_DELIMITER+"\"" +DISCUSSION_ID+ "\""+JamData.OUTPUT_DELIMITER+"\""
                +COMMENT+ "\""+JamData.OUTPUT_DELIMITER+"\"" +AUTHOR+ "\""+JamData.OUTPUT_DELIMITER+"\""
                +CREATED+ "\""+JamData.OUTPUT_DELIMITER+"\"" +LAST_MODIFIED+"\"";
    }

    @Override
    public String getRecord() {
        return "\""+userId + "\""+JamData.OUTPUT_DELIMITER+"\"" + discussionId + "\""+JamData.OUTPUT_DELIMITER+"\""
                + comment + "\""+JamData.OUTPUT_DELIMITER+"\"" + commentCreator.getFirstName()+" "
                +commentCreator.getLastName()  +"\""+JamData.OUTPUT_DELIMITER+"\""+ created + "\""+JamData.OUTPUT_DELIMITER+"\""
                + lastModified+"\"";
    }

}
