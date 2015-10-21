package com.disney.wdpr.jenkins.dto.report.archive;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

import com.disney.wdpr.jenkins.manager.ManagerUtils;

/**
 * @author matt.b.carson
 *
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Discussion implements JamData, Comparable<Discussion> {

    private String userId;

    @JsonProperty("Id")
    private String id;
    @JsonProperty("Name")
    private String discussionName;
    @JsonProperty("LastActivity")
    private String lastActivity;
    @JsonProperty("LikesCount")
    private String likesCount;
    @JsonProperty("CommentsCount")
    private String commentCount;



    public Discussion() {
        commentCount="";
        discussionName="";
        id="";
        lastActivity="";
        likesCount="";
        userId="";
    }
    public static final String EMPLOYEE_USER_ID ="EMPLOYEE_USER_ID";
    public static final String DISCUSSION_ID ="DISCUSSION_ID";
    public static final String DISCUSSION_NAME ="DISCUSSION_NAME";
    public static final String LAST_ACTIVITY ="LAST_ACTIVITY";
    public static final String LIKES_COUNT ="LIKES_COUNT";
    public static final String COMMENT_COUNT ="COMMENT_COUNT";


    @Override
    public int compareTo(final Discussion discussion) {
        final int userCompare=userId.compareTo(discussion.getUserId());
        final int discussionCompare=id.compareTo(discussion.getId());

        if(userCompare == 0) {
            return discussionCompare;
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
    public String getDiscussionName() {
        return discussionName;
    }
    public String getLastActivity() {
        return lastActivity;
    }
    public String getLikesCount() {
        return likesCount;
    }
    public String getCommentCount() {
        return commentCount;
    }

    public void setUserId(final String userId) {
        this.userId = userId;
    }
    public void setId(final String discussionId) {
        id = discussionId;
    }
    public void setDiscussionName(final String discussionName) {
        this.discussionName = discussionName;
    }
    public void setLastActivity(final String lastActivity) {
        final long millis = ManagerUtils.parseUnixEpochDate(lastActivity);
        this.lastActivity = ManagerUtils.formatDate(millis, DATE_FORMAT);
    }
    public void setLikesCount(final String likesCount) {
        this.likesCount = likesCount;
    }
    public void setCommentCount(final String commentCount) {
        this.commentCount = commentCount;
    }


    @Override
    public String getRecord() {
        return "\""+userId + "\""+JamData.OUTPUT_DELIMITER+"\"" + id + "\""+JamData.OUTPUT_DELIMITER+"\"" + discussionName
                + "\""+JamData.OUTPUT_DELIMITER+"\"" + lastActivity + "\""+JamData.OUTPUT_DELIMITER+"\""
                + likesCount + "\""+JamData.OUTPUT_DELIMITER+"\"" + commentCount+"\"";
    }
    public static String getHeader() {
        return "\""+EMPLOYEE_USER_ID+"\""+JamData.OUTPUT_DELIMITER+"\""+DISCUSSION_ID+"\""+JamData.OUTPUT_DELIMITER+"\""+DISCUSSION_NAME+"\""
                +JamData.OUTPUT_DELIMITER+"\""+LAST_ACTIVITY+"\""+JamData.OUTPUT_DELIMITER+"\""+LIKES_COUNT+"\""+JamData.OUTPUT_DELIMITER+"\""
                +COMMENT_COUNT+"\"";
    }


}
