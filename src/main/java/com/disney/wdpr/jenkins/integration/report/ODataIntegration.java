package com.disney.wdpr.jenkins.integration.report;

import java.util.List;
import java.util.Set;
import java.util.SortedSet;

import com.disney.wdpr.jenkins.dto.extract.comments.Comment;
import com.disney.wdpr.jenkins.dto.report.archive.Discussion;
import com.disney.wdpr.jenkins.dto.report.archive.Employee;
import com.disney.wdpr.jenkins.dto.report.archive.Group;
import com.disney.wdpr.jenkins.dto.report.archive.TaskAssignment;

public interface ODataIntegration {

    public String getToken(String userId);

    public Set<Group> getGroups(String token, Employee employee);

    public SortedSet<Discussion> getDiscussions(String token, String userId, String groupId);

    public SortedSet<Comment> getComments(String token, String userId, String discussionId);

    public List<TaskAssignment> getTasks(String token, String userId);

}
