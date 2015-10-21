package com.disney.wdpr.jenkins.manager.report;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import junit.framework.Assert;

import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;

import com.disney.wdpr.jenkins.AbstractTestCase;
import com.disney.wdpr.jenkins.dto.GenericDto;
import com.disney.wdpr.jenkins.dto.extract.comments.Comment;
import com.disney.wdpr.jenkins.dto.extract.comments.CommentCreator;
import com.disney.wdpr.jenkins.dto.report.archive.Discussion;
import com.disney.wdpr.jenkins.dto.report.archive.Employee;
import com.disney.wdpr.jenkins.dto.report.archive.Group;
import com.disney.wdpr.jenkins.dto.report.archive.JamData;
import com.disney.wdpr.jenkins.dto.report.archive.TaskAssignment;
import com.disney.wdpr.jenkins.integration.report.ODataIntegration;
import com.disney.wdpr.jenkins.manager.LoadReport;
import com.disney.wdpr.jenkins.manager.ManagerUtils;
import com.disney.wdpr.jenkins.manager.report.ReportManagerImpl;
import com.jcraft.jsch.JSchException;

public class ReportManagerImplTest extends AbstractTestCase {

//    private ReportManagerImpl reportManager;
//    private FtpIntegration ftpIntegration;
//    private ManagerUtils managerUtils;
//    private LoadReport loadEmployeeReport;
//    private SortedSet<JamData> tasks;
//    private SortedSet<JamData> discussions;
//    private SortedSet<JamData> comments;
//    private List<Exception> failures;
//    private EmailIntegration emailIntegration;
//    private File file;
//    private ODataIntegration oDataIntegration;
//    private String employeeReportFilename;
//    private BufferedWriter writer;
//    private String commitReportFilename;
//    private String discussionReportFilename;
//    private String commentsReportFilename;
//    private String completeStatusFilename;
//    private String dateFormat;

//    @Before
//    public void initializeTest()
//    {
//        ftpIntegration = EasyMock.createMock(FtpIntegration.class);
//        managerUtils = EasyMock.createMock(ManagerUtils.class);
//        loadEmployeeReport = EasyMock.createMock(LoadReport.class);
//        emailIntegration = EasyMock.createMock(EmailIntegration.class);
//        file = EasyMock.createMock(File.class);
//        oDataIntegration = EasyMock.createMock(ODataIntegration.class);
//        writer = EasyMock.createMock(BufferedWriter.class);
//
//        employeeReportFilename = "member_report.csv";
//        commitReportFilename = "commit.csv";
//        discussionReportFilename = "discussion.csv";
//        commentsReportFilename = "comments.csv";
//        completeStatusFilename = "completeStatus.txt";
//        dateFormat = "YYYYMMdd";
//    }
//
//    @Test
//    public void testProcess() {
//        final String userId1 = "user1";
//        final String firstname1 = "firstname1";
//        final String lastname1 = "lastname1";
//        final String userId2 = "user2";
//        final String firstname2 = "firstname2";
//        final String lastname2 = "lastname2";
//        final String userId3 = "user3";
//        final String firstname3 = "firstname3";
//        final String lastname3 = "lastname3";
//        final String userId4 = "user4";
//        final String firstname4 = "firstname4";
//        final String lastname4 = "lastname4";
//        final String userId5 = "user5";
//        final String firstname5 = "firstname5";
//        final String lastname5 = "lastname5";
//
//        final Map<String, GenericDto> employeeMap = new HashMap<String, GenericDto>();
//        final Employee employee1 = new Employee(userId1, firstname1, lastname1, "P");
//        final Employee employee2 = new Employee(userId2, firstname2, lastname2, "P");
//        final Employee employee3 = new Employee(userId3, firstname3, lastname3, "P");
//        final Employee employee4 = new Employee(userId4, firstname4, lastname4, "P");
//        final Employee employee5 = new Employee(userId5, firstname5, lastname5, "P");
//
//        employeeMap.put(userId1, employee1);
//        employeeMap.put(userId2, employee2);
//        employeeMap.put(userId3, employee3);
//        employeeMap.put(userId4, employee4);
//        employeeMap.put(userId5, employee5);
//
//        final Date date = new Date();
//
//
//        try {
//            reportManager =
//                    EasyMock.createMockBuilder(ReportManagerImpl.class)
//                        .addMockedMethod("getNewEmployeeMap")
//                        .addMockedMethod("getNewJamSet")
//                        .addMockedMethod("getNewExceptionList")
//                        .addMockedMethod("getDate")
//                        .addMockedMethod("downloadEmployeeReport")
//                        .addMockedMethod("executeApiRequests")
//                        .addMockedMethod("createReports")
//                        .addMockedMethod("uploadReports")
//                        .addMockedMethod("checkExceptions")
//                        .addMockedMethod("archiveFiles")
//                        .createMock();
//
//            EasyMock.expect(reportManager.getNewEmployeeMap()).andReturn(employeeMap);
//            EasyMock.expect(reportManager.getNewJamSet()).andReturn(tasks);
//            EasyMock.expect(reportManager.getNewJamSet()).andReturn(discussions);
//            EasyMock.expect(reportManager.getNewJamSet()).andReturn(comments);
//            EasyMock.expect(reportManager.getNewExceptionList()).andReturn(failures);
//            EasyMock.expect(reportManager.getDate()).andReturn(date);
//            EasyMock.expect(reportManager.downloadEmployeeReport()).andReturn(true);
//            reportManager.executeApiRequests(employee1, tasks, discussions, comments, failures);
//            reportManager.executeApiRequests(employee2, tasks, discussions, comments, failures);
//            reportManager.executeApiRequests(employee3, tasks, discussions, comments, failures);
//            reportManager.executeApiRequests(employee4, tasks, discussions, comments, failures);
//            reportManager.executeApiRequests(employee5, tasks, discussions, comments, failures);
//
//            reportManager.createReports(tasks, discussions, comments);
//            reportManager.uploadReports(date);
//            reportManager.checkExceptions(date, failures);
//            reportManager.archiveFiles(true, date);
//
//            replayAll();
//
//            reportManager.process();
//
//            verifyAll();
//        } catch (final Exception e) {
//            this.failure(e);
//        }
//    }
//
//    private Set<Group> getGroups(final String groupId1, final String groupId2){
//        final Group group1 = new Group();
//        group1.setId(groupId1);
//        group1.setName("groupName1");
//        group1.setActive(true);
//        final Group group2 = new Group();
//        group2.setId(groupId2);
//        group2.setName("groupName2");
//        group2.setActive(true);
//
//        final Set<Group> groups = new HashSet<Group>();
//        groups.add(group1);
//        groups.add(group2);
//        return groups;
//    }
//
//    @Test
//    public void testExecuteApiRequests() {
//        final String userId1 = "user1";
//        final String firstname1 = "firstname1";
//        final String lastname1 = "lastname1";
//        final Employee employee1 = new Employee(userId1, firstname1, lastname1, "P");
//        final String token = "token1";
//
//        final Date date = new Date();
//        tasks = new TreeSet<JamData>();
//        discussions = new TreeSet<JamData>();
//        comments= new TreeSet<JamData>();
//        failures = new ArrayList<Exception>();
//
//        final String groupId1 ="group1";
//        final String groupId2 ="group2";
//        final String discussion1 = "disc1";
//        final String discussion2 = "disc2";
//        final String discussion3 = "disc3";
//        final String discussion4 = "disc4";
//
//        final String commentId1 = "commentId1";
//        final String commentId2 = "commentId2";
//        final String commentId3 = "commentId3";
//        final String commentId4 = "commentId4";
//        final String commentId5 = "commentId5";
//        final String commentId6 = "commentId6";
//        final String commentId7 = "commentId7";
//        final String commentId8 = "commentId8";
//
//        final String taskId1 = "taskId1";
//        final String taskId2 = "taskId2";
//        final String taskId3 = "taskId3";
//        final String taskId4 = "taskId4";
//
//        final Set<Group> groups = getGroups(groupId1, groupId2);
//        final List<TaskAssignment> returnedTasks1 = getTasks(userId1, taskId1, taskId2);
//        final List<TaskAssignment> returnedTasks2 = getTasks(userId1, taskId3, taskId4);
//        final SortedSet<Discussion> returnedDiscussions1 = getDiscussions(discussion1,discussion2);
//        final SortedSet<Discussion> returnedDiscussions2 = getDiscussions(discussion3,discussion4);
//        final SortedSet<Comment> comments1 = getComments(commentId1, commentId2);
//        final SortedSet<Comment> comments2 = getComments(commentId3, commentId4);
//        final SortedSet<Comment> comments3 = getComments(commentId5, commentId6);
//        final SortedSet<Comment> comments4 = getComments(commentId7, commentId8);
//
//
//        try {
//            reportManager =
//                    EasyMock.createMockBuilder(ReportManagerImpl.class)
//                        .addMockedMethod("getNewJamSet")
//                        .createMock();
//
//            reportManager.setoDataIntegration(oDataIntegration);
//
//            EasyMock.expect(reportManager.getNewJamSet()).andReturn(discussions);
//            EasyMock.expect(oDataIntegration.getToken(userId1)).andReturn(token);
//            EasyMock.expect(oDataIntegration.getGroups(token, employee1)).andReturn(groups);
//            EasyMock.expect(oDataIntegration.getTasks(token, userId1)).andReturn(returnedTasks1);
////            EasyMock.expect(oDataIntegration.getTasks(token, userId1)).andReturn(returnedTasks2);
//            EasyMock.expect(oDataIntegration.getDiscussions(token, userId1, groupId1)).andReturn(returnedDiscussions1);
//            EasyMock.expect(oDataIntegration.getDiscussions(token, userId1, groupId2)).andReturn(returnedDiscussions2);
//            EasyMock.expect(oDataIntegration.getComments(token, userId1, discussion1)).andReturn(comments1);
//            EasyMock.expect(oDataIntegration.getComments(token, userId1, discussion2)).andReturn(comments2);
//            EasyMock.expect(oDataIntegration.getComments(token, userId1, discussion3)).andReturn(comments3);
//            EasyMock.expect(oDataIntegration.getComments(token, userId1, discussion4)).andReturn(comments4);
//
//            replayAll();
//
//            reportManager.executeApiRequests(employee1, tasks, discussions, comments, failures);
//
//            Assert.assertEquals(0, failures.size());
//
//            verifyAll();
//        } catch (final Exception e) {
//            this.failure(e);
//        }
//    }
//
//    @Test
//    public void testDownloadEmployeeReport() {
//        try {
//            final String userId1 = "userId1";
//            final String firstname1 = "firstname1";
//            final String lastname1 = "lastname1";
//            final String userId2 = "userId2";
//            final String firstname2 = "firstname2";
//            final String lastname2 = "lastname2";
//            final Employee employee1 = new Employee(userId1, firstname1, lastname1, "P");
//            final Employee employee2 = new Employee(userId2, firstname2, lastname2, "P");
//
//            final Map<String, GenericDto> employeeMap = new HashMap<String, GenericDto>();
//            employeeMap.put(userId1, employee1);
//            employeeMap.put(userId2, employee2);
//
//
//            reportManager = EasyMock.createMockBuilder(ReportManagerImpl.class).createMock();
//            reportManager.setFtpIntegrationInput(ftpIntegration);
//            reportManager.setManagerUtils(managerUtils);
//            reportManager.setEmployeeReportFilename(employeeReportFilename);
//            reportManager.setLoadEmployeeReport(loadEmployeeReport);
//            reportManager.setEmployeeMap(employeeMap);
//
//            EasyMock.expect(ftpIntegration.downloadFile(employeeReportFilename)).andReturn(true);
//            EasyMock.expect(managerUtils.loadReport(employeeReportFilename, loadEmployeeReport, employeeMap)).andReturn(2);
//
//            replayAll();
//
//            final boolean downloaded = reportManager.downloadEmployeeReport();
//
//            Assert.assertTrue(downloaded);
//
//            verifyAll();
//
//        } catch (final Exception e) {
//            this.failure(e);
//        }
//    }
//
//
//    @Test
//    public void testCheckExceptions() {
//        try {
//            final String error1 = "failed calling discussions";
//            final String error2 = "failed calling commits";
//
//
//            final List<Exception> failures = new ArrayList<Exception>();
//            failures.add(new RuntimeException(error1));
//            failures.add(new RuntimeException(error2));
//
//            reportManager = EasyMock.createMockBuilder(ReportManagerImpl.class).createMock();
//            reportManager.setEmailIntegration(emailIntegration);
//
//
//            final Date date = new Date();
//            final String exceptionMessage = "<br><br>"+error1+"<br>"+error2+"<br>";
//
//            emailIntegration.sendFailureReport(exceptionMessage, date);
//            replayAll();
//
//            reportManager.checkExceptions(date, failures);
//
//            verifyAll();
//
//        } catch (final Exception e) {
//            this.failure(e);
//        }
//    }
//
//
//    @Test
//    public void testCreateReport() {
//        try {
//            final String error1 = "failed calling discussions";
//            final String error2 = "failed calling commits";
//
//
//            final List<Exception> failures = new ArrayList<Exception>();
//            failures.add(new RuntimeException(error1));
//            failures.add(new RuntimeException(error2));
//
//            reportManager = EasyMock.createMockBuilder(ReportManagerImpl.class).createMock();
//            reportManager.setEmailIntegration(emailIntegration);
//
//
//            final Date date = new Date();
//            final String exceptionMessage = "<br><br>"+error1+"<br>"+error2+"<br>";
//
//            emailIntegration.sendFailureReport(exceptionMessage, date);
//            replayAll();
//
//            reportManager.checkExceptions(date, failures);
//
//            verifyAll();
//
//        } catch (final Exception e) {
//            this.failure(e);
//        }
//    }
//
//
//
//
//    private SortedSet<Comment> getComments(final String commentId1, final String commentId2){
//        final SortedSet<Comment> comments = new TreeSet<Comment>();
//        Comment comment = new Comment();
//        comment.setId(commentId1);
//        comment.setUserId("userId1");
//        comment.setDiscussionId("disc1");
//
//        comments.add(comment);
//        comment = new Comment();
//        comment.setId(commentId2);
//        comment.setUserId("userId1");
//        comment.setDiscussionId("disc1");
//        comments.add(comment);
//        return comments;
//    }
//
//    private List<TaskAssignment> getTasks(final String userId, final String taskId1, final String taskId2) {
//        final String taskTitle = "taskTitle";
//        final String taskDescription = "taskDescription";
//
//        final List<TaskAssignment> tasks= new ArrayList<TaskAssignment>();
//        TaskAssignment task = new TaskAssignment();
//        task.setUserId(userId);
//        task.setId(taskId1);
//        task.setTitle(taskTitle);
//        task.setDescription(taskDescription);
//
//        task = new TaskAssignment();
//        task.setUserId(userId);
//        task.setId(taskId2);
//        task.setTitle(taskTitle);
//        task.setDescription(taskDescription);
//
//        tasks.add(task);
//       return tasks;
//    }
//
//    private SortedSet<Discussion> getDiscussions(final String discId1, final String discId2) {
//        final SortedSet<Discussion> discussions = new TreeSet<Discussion>();
//        final Discussion discussion1 = new Discussion();
//        discussion1.setId(discId1);
//        discussion1.setUserId("userid1");
//
//        final Discussion discussion2 = new Discussion();
//        discussion2.setId(discId2);
//        discussion2.setUserId("userid1");
//
//        discussions.add(discussion1);
//        discussions.add(discussion2);
//
//        return discussions;
//    }
//
//
//    @Test(expected=JSchException.class)
//    public void testProcessException() throws Exception {
//        final String userId1 = "user1";
//        final String firstname1 = "firstname1";
//        final String lastname1 = "lastname1";
//        final String userId2 = "user2";
//        final String firstname2 = "firstname2";
//        final String lastname2 = "lastname2";
//        final String userId3 = "user3";
//        final String firstname3 = "firstname3";
//        final String lastname3 = "lastname3";
//        final String userId4 = "user4";
//        final String firstname4 = "firstname4";
//        final String lastname4 = "lastname4";
//        final String userId5 = "user5";
//        final String firstname5 = "firstname5";
//        final String lastname5 = "lastname5";
//
//        final Map<String, GenericDto> employeeMap = new HashMap<String, GenericDto>();
//        final Employee employee1 = new Employee(userId1, firstname1, lastname1, "P");
//        final Employee employee2 = new Employee(userId2, firstname2, lastname2, "P");
//        final Employee employee3 = new Employee(userId3, firstname3, lastname3, "P");
//        final Employee employee4 = new Employee(userId4, firstname4, lastname4, "P");
//        final Employee employee5 = new Employee(userId5, firstname5, lastname5, "P");
//
//        employeeMap.put(userId1, employee1);
//        employeeMap.put(userId2, employee2);
//        employeeMap.put(userId3, employee3);
//        employeeMap.put(userId4, employee4);
//        employeeMap.put(userId5, employee5);
//
//        final Date date = new Date();
//        tasks = new TreeSet<JamData>();
//        discussions = new TreeSet<JamData>();
//        comments= new TreeSet<JamData>();
//        failures = new ArrayList<Exception>();
//
//
//        reportManager =
//                EasyMock.createMockBuilder(ReportManagerImpl.class)
//                    .addMockedMethod("getNewEmployeeMap")
//                    .addMockedMethod("getNewJamSet")
//                    .addMockedMethod("getNewExceptionList")
//                    .addMockedMethod("getDate")
//                    .addMockedMethod("downloadEmployeeReport")
//                    .addMockedMethod("archiveFiles")
//                    .createMock();
//        reportManager.setEmailIntegration(emailIntegration);
//
//        EasyMock.expect(reportManager.getNewEmployeeMap()).andReturn(employeeMap);
//        EasyMock.expect(reportManager.getNewJamSet()).andReturn(tasks);
//        EasyMock.expect(reportManager.getNewJamSet()).andReturn(discussions);
//        EasyMock.expect(reportManager.getNewJamSet()).andReturn(comments);
//        EasyMock.expect(reportManager.getNewExceptionList()).andReturn(failures);
//        EasyMock.expect(reportManager.getDate()).andReturn(date);
//        EasyMock.expect(reportManager.downloadEmployeeReport()).andThrow(new JSchException());
//        emailIntegration.sendFailureReport(EasyMock.isA(String.class), EasyMock.eq(date));
//        reportManager.archiveFiles(false, date);
//
//
//        replayAll();
//
//        reportManager.process();
//
//        verifyAll();
//
//    }
//
//    @Test
//    public void testArchiveFiles() {
//
//        final Date date = new Date();
//        final String empFilename = "basefile";
//        final String commitFilename = "commitFilename";
//        final String disFilename = "disfile";
//        final String comFilename = "comfile";
//        final String archDir = "archiveDir";
//
//
//        try {
//            reportManager =
//                    EasyMock.createMockBuilder(ReportManagerImpl.class)
//                        .addMockedMethod("getFile")
//                        .createMock();
//
//
//            reportManager.setEmployeeReportFilename(empFilename);
//            reportManager.setCommitmentsReportFilename(commitFilename);
//            reportManager.setDiscussionReportFilename(disFilename);
//            reportManager.setCommentsReportFilename(comFilename);
//            reportManager.setLocalArchiveDirectory(archDir);
//            reportManager.setManagerUtils(managerUtils);
//
//            managerUtils.moveFileToArchive(empFilename, date);
//            managerUtils.moveFileToArchive(commitFilename, date);
//            managerUtils.moveFileToArchive(disFilename, date);
//            managerUtils.moveFileToArchive(comFilename, date);
//
//            EasyMock.expect(reportManager.getFile(archDir)).andReturn(file).times(4);
//            managerUtils.cleanupOldfiles(file);
//            managerUtils.cleanupOldfiles(file);
//            managerUtils.compressOldArchives(file);
//            managerUtils.compressOldArchives(file);
//
//
//
//            replayAll();
//
//            reportManager.archiveFiles(true, date);
//            reportManager.archiveFiles(false, date);
//
//            verifyAll();
//        } catch (final Exception e) {
//            this.failure(e);
//        }
//    }
//
//    @Test
//    public void testCreateReports() {
//        try {
//            reportManager =
//                    EasyMock.createMockBuilder(ReportManagerImpl.class)
//                        .createMock();
//
//            reportManager.setManagerUtils(managerUtils);
//            reportManager.setCommitmentsReportFilename(commitReportFilename);
//            reportManager.setDiscussionReportFilename(discussionReportFilename);
//            reportManager.setCommentsReportFilename(commentsReportFilename);
//
//            final String userId1 = "userId1";
//
//            final String taskId = "taskId1";
//            final String taskTitle = "taskTitle";
//            final String taskDescription = "taskDescription";
//
//            final SortedSet<JamData> tasks= new TreeSet<JamData>();
//            final TaskAssignment task = new TaskAssignment();
//            task.setUserId(userId1);
//            task.setId(taskId);
//            task.setTitle(taskTitle);
//            task.setDescription(taskDescription);
//            tasks.add(task);
//
//            final String discussionId1 = "discussionId1";
//            final String discussionName1 = "discussionName1";
//
//            final SortedSet<JamData> discussions= new TreeSet<JamData>();
//            final Discussion discussion1 = new Discussion();
//            discussion1.setId(discussionId1);
//            discussion1.setDiscussionName(discussionName1);
//            discussion1.setUserId(userId1);
//            discussions.add(discussion1);
//
//            final String commentId1 = "commentId1";
//            final SortedSet<JamData> comments = new TreeSet<JamData>();
//            final Comment comment = new Comment();
//            comment.setId(commentId1);
//            comment.setUserId(userId1);
//            comment.setDiscussionId(discussionId1);
//            final CommentCreator creator = new CommentCreator();
//            creator.setFirstName("first");
//            creator.setLastName("first");
//            comment.setCommentCreator(creator);
//            comments.add(comment);
//
//
//            EasyMock.expect(managerUtils.getWriter(commitReportFilename)).andReturn(writer);
//            writer.write(TaskAssignment.getHeader());
//            writer.newLine();
//            writer.write(task.getRecord());
//            writer.newLine();
//            writer.flush();
//            writer.close();
//
//
//            EasyMock.expect(managerUtils.getWriter(discussionReportFilename)).andReturn(writer);
//            writer.write(Discussion.getHeader());
//            writer.newLine();
//            writer.write(discussion1.getRecord());
//            writer.newLine();
//            writer.flush();
//            writer.close();
//
//            EasyMock.expect(managerUtils.getWriter(commentsReportFilename)).andReturn(writer);
//            writer.write(Comment.getHeader());
//            writer.newLine();
//            writer.write(comment.getRecord());
//            writer.newLine();
//            writer.flush();
//            writer.close();
//
//            replayAll();
//
//            reportManager.createReports(tasks, discussions, comments);
//
//            verifyAll();
//        } catch (final Exception e) {
//            this.failure(e);
//        }
//
//    }
//
//
//    @Test
//    public void testCreateReportsEmpty() {
//        try {
//            reportManager =
//                    EasyMock.createMockBuilder(ReportManagerImpl.class)
//                        .createMock();
//
//            reportManager.setManagerUtils(managerUtils);
//            reportManager.setCommitmentsReportFilename(commitReportFilename);
//            reportManager.setDiscussionReportFilename(discussionReportFilename);
//            reportManager.setCommentsReportFilename(commentsReportFilename);
//
//            final String userId1 = "userId1";
//
//            final String discussionId1 = "discussionId1";
//            final String discussionName1 = "discussionName1";
//
//            final String taskId = "taskId1";
//            final String taskTitle = "taskTitle";
//            final String taskDescription = "taskDescription";
//
//            final SortedSet<JamData> tasks= new TreeSet<JamData>();
//            final TaskAssignment task = new TaskAssignment();
//            task.setUserId(userId1);
//            task.setId(taskId);
//            task.setTitle(taskTitle);
//            task.setDescription(taskDescription);
//            tasks.add(task);
//
//            final SortedSet<JamData> discussions= new TreeSet<JamData>();
//            final Discussion discussion1 = new Discussion();
//            discussion1.setId(discussionId1);
//            discussion1.setDiscussionName(discussionName1);
//            discussion1.setUserId(userId1);
//            discussions.add(discussion1);
//
//            final String commentId1 = "commentId1";
//            final SortedSet<JamData> comments = new TreeSet<JamData>();
//            final Comment comment = new Comment();
//            comment.setId(commentId1);
//            comment.setUserId(userId1);
//            comment.setDiscussionId(discussionId1);
//            final CommentCreator creator = new CommentCreator();
//            creator.setFirstName("first");
//            creator.setLastName("first");
//            comment.setCommentCreator(creator);
//
//            EasyMock.expect(managerUtils.getWriter(commitReportFilename)).andReturn(writer);
//            writer.write(TaskAssignment.getHeader());
//            writer.newLine();
//            writer.write(task.getRecord());
//            writer.newLine();
//            writer.flush();
//            writer.close();
//
//
//            EasyMock.expect(managerUtils.getWriter(discussionReportFilename)).andReturn(writer);
//            writer.write(Discussion.getHeader());
//            writer.newLine();
//            writer.write(discussion1.getRecord());
//            writer.newLine();
//            writer.flush();
//            writer.close();
//
//            EasyMock.expect(managerUtils.getWriter(commentsReportFilename)).andReturn(writer);
//            writer.write(Comment.getHeader());
//            writer.newLine();
//            writer.flush();
//            writer.close();
//
//            replayAll();
//
//            reportManager.createReports(tasks, discussions, comments);
//
//            verifyAll();
//        } catch (final Exception e) {
//            this.failure(e);
//        }
//
//    }
//
//    @Test(expected=IOException.class)
//    public void testCreateReportsException() throws IOException {
//        reportManager =
//                EasyMock.createMockBuilder(ReportManagerImpl.class)
//                    .createMock();
//
//        reportManager.setManagerUtils(managerUtils);
//        reportManager.setCommitmentsReportFilename(commitReportFilename);
//        reportManager.setDiscussionReportFilename(discussionReportFilename);
//        reportManager.setCommentsReportFilename(commentsReportFilename);
//
//        final String userId1 = "userId1";
//
//        final String taskId = "taskId1";
//        final String taskTitle = "taskTitle";
//        final String taskDescription = "taskDescription";
//
//        final SortedSet<JamData> tasks= new TreeSet<JamData>();
//        final TaskAssignment task = new TaskAssignment();
//        task.setUserId(userId1);
//        task.setId(taskId);
//        task.setTitle(taskTitle);
//        task.setDescription(taskDescription);
//        tasks.add(task);
//
//
//        final String discussionId1 = "discussionId1";
//        final String discussionName1 = "discussionName1";
//
//        final SortedSet<JamData> discussions= new TreeSet<JamData>();
//        final Discussion discussion1 = new Discussion();
//        discussion1.setId(discussionId1);
//        discussion1.setDiscussionName(discussionName1);
//        discussion1.setUserId(userId1);
//        discussions.add(discussion1);
//
//        final String commentId1 = "commentId1";
//        final SortedSet<JamData> comments = new TreeSet<JamData>();
//        final Comment comment = new Comment();
//        comment.setId(commentId1);
//        comment.setUserId(userId1);
//        comment.setDiscussionId(discussionId1);
//        final CommentCreator creator = new CommentCreator();
//        creator.setFirstName("first");
//        creator.setLastName("first");
//        comment.setCommentCreator(creator);
//
//        EasyMock.expect(managerUtils.getWriter(commitReportFilename)).andThrow(new IOException());
//
//        replayAll();
//
//        reportManager.createReports(tasks, discussions, comments);
//
//        verifyAll();
//
//    }
//
//
//    @Test
//    public void testUploadReports() {
//
//        try {
//            reportManager =
//                    EasyMock.createMockBuilder(ReportManagerImpl.class)
//                        .addMockedMethod("createCompleteStatusFile")
//                        .createMock();
//
//            final ManagerUtils manUtils = new ManagerUtils();
//            reportManager.setFtpIntegrationOutput(ftpIntegration);
//            reportManager.setManagerUtils(manUtils);
//            reportManager.setOutputReportDateFormat(dateFormat);
//
//            final String commitFilename = "commit.csv";
//            final String discussionFilename = "discussion.csv";
//            final String commentFilename = "comment.csv";
//            final String completeFilename = "complete.txt";
//
//            reportManager.setCommitmentsReportFilename(commitFilename);
//            reportManager.setDiscussionReportFilename(discussionFilename);
//            reportManager.setCommentsReportFilename(commentFilename);
//            reportManager.setCompleteStatusFilename(completeFilename);
//
//            final Calendar calendar = new GregorianCalendar(2015,5,1);
//            final Date date = new Date(calendar.getTimeInMillis());
//
//
//            final String commitFilenameDate = "commit_20150601.csv";
//            final String discussionFilenameDate = "discussion_20150601.csv";
//            final String commentFilenameDate = "comment_20150601.csv";
//            final String completeFilenameDate = "complete_20150601.txt";
//
//            EasyMock.expect(ftpIntegration.uploadFile(commitFilename, commitFilenameDate)).andReturn(true);
//            EasyMock.expect(ftpIntegration.uploadFile(discussionFilename, discussionFilenameDate)).andReturn(true);
//            EasyMock.expect(ftpIntegration.uploadFile(commentFilename, commentFilenameDate)).andReturn(true);
//
//            reportManager.createCompleteStatusFile();
//            EasyMock.expect(ftpIntegration.uploadFile(completeFilename, completeFilenameDate)).andReturn(true);
//
//            replayAll();
//
//            reportManager.uploadReports(date);
//
//            verifyAll();
//        } catch (final Exception e) {
//            this.failure(e);
//        }
//    }



    @Override
    protected void replayAll() {
//        EasyMock.replay(reportManager);
//        EasyMock.replay(ftpIntegration);
//        EasyMock.replay(managerUtils);
//        EasyMock.replay(loadEmployeeReport);
//        EasyMock.replay(emailIntegration);
//        EasyMock.replay(file);
//        EasyMock.replay(oDataIntegration);
//        EasyMock.replay(writer);

    }

    @Override
    protected void verifyAll() {
//        EasyMock.verify(reportManager);
//        EasyMock.verify(ftpIntegration);
//        EasyMock.verify(managerUtils);
//        EasyMock.verify(loadEmployeeReport);
//        EasyMock.verify(emailIntegration);
//        EasyMock.verify(file);
//        EasyMock.verify(oDataIntegration);
//        EasyMock.verify(writer);
    }

}
