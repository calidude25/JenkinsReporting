package com.disney.wdpr.jenkins.dto.report.archive;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

import com.disney.wdpr.jenkins.manager.ManagerUtils;

/**
 * @author matt.b.carson
 *
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class TaskAssignment implements JamData, Comparable<TaskAssignment> {

    private String userId;

    @JsonProperty("Id")
    private String id;
    @JsonProperty("Title")
    private String title;
    @JsonProperty("Description")
    private String description;
    @JsonProperty("CreatedAt")
    private String created;
    @JsonProperty("LastModifiedAt")
    private String lastModifiedAt;
    @JsonProperty("DueAt")
    private String dueAt;


    public TaskAssignment() {
        created="";
        description="";
        dueAt="";
        id="";
        lastModifiedAt="";
        title="";
        userId="";
    }



    public static final String EMPLOYEE_USER_ID ="EMPLOYEE_USER_ID";
    public static final String COMMITMENT_ID ="COMMITMENT_ID";
    public static final String TITLE ="TITLE";
    public static final String DESCRIPTION ="DESCRIPTION";
    public static final String CREATED ="CREATED";
    public static final String LAST_MODIFIED ="LAST_MODIFIED";
    public static final String DUE ="DUE";



    @Override
    public int compareTo(final TaskAssignment taskAssignment) {
        final int userCompare=userId.compareTo(taskAssignment.getUserId());
        final int idCompare=id.compareTo(taskAssignment.getId());

        if(userCompare == 0) {
            return idCompare;
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

    public void setUserId(final String userId) {
        this.userId = userId;
    }
    public void setId(final String taskId) {
        id = taskId;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getCreated() {
        return created;
    }

    public String getLastModifiedAt() {
        return lastModifiedAt;
    }

    public String getDueAt() {
        return dueAt;
    }

    public void setTitle(final String title) {
        this.title = title;
    }


    public void setDescription(final String description) {
        this.description = description;
    }


    public void setCreated(final String created) {
        final long millis = ManagerUtils.parseUnixEpochDate(created);
        this.created = ManagerUtils.formatDate(millis, DATE_FORMAT);
    }


    public void setLastModifiedAt(final String lastModifiedAt) {
        final long millis = ManagerUtils.parseUnixEpochDate(lastModifiedAt);
        this.lastModifiedAt = ManagerUtils.formatDate(millis, DATE_FORMAT);
    }


    public void setDueAt(final String dueAt) {
        if(dueAt!=null) {
            final long millis = ManagerUtils.parseUnixEpochDate(dueAt);
            this.dueAt = ManagerUtils.formatDate(millis, DATE_FORMAT);
        }
    }


    @Override
    public String getRecord() {
        return "\""+userId + "\""+JamData.OUTPUT_DELIMITER+"\"" + id + "\""+JamData.OUTPUT_DELIMITER+"\"" + title
                + "\""+JamData.OUTPUT_DELIMITER+"\"" + description + "\""+JamData.OUTPUT_DELIMITER+"\""
                + created + "\""+JamData.OUTPUT_DELIMITER+"\"" + lastModifiedAt+ "\""+JamData.OUTPUT_DELIMITER+"\"" + dueAt+"\"";
    }
    public static String getHeader() {
        return "\""+EMPLOYEE_USER_ID+"\""+JamData.OUTPUT_DELIMITER+"\""+COMMITMENT_ID+"\""+JamData.OUTPUT_DELIMITER+"\""
                +TITLE+"\""+JamData.OUTPUT_DELIMITER+"\""+DESCRIPTION+"\""+JamData.OUTPUT_DELIMITER+"\""
                +CREATED+"\""+JamData.OUTPUT_DELIMITER+"\""+LAST_MODIFIED+"\""+JamData.OUTPUT_DELIMITER+"\""+DUE+"\"";
    }


}
