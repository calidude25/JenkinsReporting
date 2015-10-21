package com.disney.wdpr.jenkins.integration.report;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.URI;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;

import junit.framework.Assert;

import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.disney.wdpr.jenkins.AbstractTestCase;
import com.disney.wdpr.jenkins.dto.extract.comments.Comment;
import com.disney.wdpr.jenkins.dto.extract.comments.CommentCreator;
import com.disney.wdpr.jenkins.dto.extract.comments.CommentCreatorMessageWrapper;
import com.disney.wdpr.jenkins.dto.extract.comments.CommentCreatorWrapper;
import com.disney.wdpr.jenkins.dto.extract.comments.CommentMessageWrapper;
import com.disney.wdpr.jenkins.dto.extract.comments.CommentWrapper;
import com.disney.wdpr.jenkins.dto.report.archive.AuthTokenResponse;
import com.disney.wdpr.jenkins.dto.report.archive.Discussion;
import com.disney.wdpr.jenkins.dto.report.archive.DiscussionMessageWrapper;
import com.disney.wdpr.jenkins.dto.report.archive.DiscussionWrapper;
import com.disney.wdpr.jenkins.dto.report.archive.Employee;
import com.disney.wdpr.jenkins.dto.report.archive.Group;
import com.disney.wdpr.jenkins.dto.report.archive.GroupMessageWrapper;
import com.disney.wdpr.jenkins.dto.report.archive.GroupWrapper;
import com.disney.wdpr.jenkins.integration.report.LoggingRequestInterceptor;
import com.disney.wdpr.jenkins.integration.report.ODataIntegrationImpl;

public class ODataIntegrationImplTest extends AbstractTestCase {

    private ODataIntegrationImpl oDataIntegrationImpl;
    private RestTemplate restTemplate;
    private HttpEntity<String> httpEntity;
    private AuthTokenResponse authTokenResponse;
    private HttpClientErrorException httpClientErrorException;
    ResponseEntity<GroupMessageWrapper> groupResponse;
    ResponseEntity<DiscussionMessageWrapper> discussionResponse;
    ResponseEntity<CommentMessageWrapper> commentResponse;
    ResponseEntity<CommentCreatorMessageWrapper> commentCreatorResponse;
    HttpRequest httpRequest;
    ClientHttpRequestExecution execution;
    ClientHttpResponse httpResponse;
    InputStream inputStream;
    BufferedReader bufferedReader;
    LoggingRequestInterceptor requestInterceptor;

    @SuppressWarnings("unchecked")
    @Before
    public void initializeTest(){
        restTemplate = EasyMock.createMock(RestTemplate.class);
        httpEntity = EasyMock.createMock(HttpEntity.class);
        authTokenResponse = EasyMock.createMock(AuthTokenResponse.class);
        httpClientErrorException = EasyMock.createMock(HttpClientErrorException.class);
        groupResponse = EasyMock.createMock(ResponseEntity.class);
        discussionResponse = EasyMock.createMock(ResponseEntity.class);
        commentResponse = EasyMock.createMock(ResponseEntity.class);
        commentCreatorResponse = EasyMock.createMock(ResponseEntity.class);
        httpRequest = EasyMock.createMock(HttpRequest.class);
        execution = EasyMock.createMock(ClientHttpRequestExecution.class);
        httpResponse = EasyMock.createMock(ClientHttpResponse.class);
        inputStream = EasyMock.createMock(InputStream.class);
        bufferedReader = EasyMock.createMock(BufferedReader.class);
        requestInterceptor = EasyMock.createMockBuilder(LoggingRequestInterceptor.class)
                .addMockedMethod("getReader")
                .createMock();

    }



    @Test
    public void testGetToken() {

        final String userId = "userId";
        final String host = "http://www.test.com";
        final String path = "/member-update";
        final String command = "/authenticate/";
        final String url = host+path+command+userId;
        final String token = "token";

        final String exceptionBody ="Http failure - body";
        final String exceptionMessage ="Http failure - message";

        try {
            oDataIntegrationImpl =
                    EasyMock.createMockBuilder(ODataIntegrationImpl.class)
                        .addMockedMethod("getHttpEntity")
                        .addMockedMethod("getRestTemplate")
                        .createMock();

            oDataIntegrationImpl.setIdpHost(host);
            oDataIntegrationImpl.setIdpPath(path);
            oDataIntegrationImpl.setIdpCommand(command);

            EasyMock.expect(oDataIntegrationImpl.getHttpEntity(EasyMock.isA(HttpHeaders.class))).andReturn(httpEntity).times(2);
            EasyMock.expect(oDataIntegrationImpl.getRestTemplate()).andReturn(restTemplate).times(2);
            EasyMock.expect(restTemplate.postForObject(url, httpEntity, AuthTokenResponse.class))
                .andReturn(authTokenResponse)
                .andThrow(httpClientErrorException);
            EasyMock.expect(authTokenResponse.getAccess_token()).andReturn(token);

            EasyMock.expect(httpClientErrorException.getResponseBodyAsString()).andReturn(exceptionBody);
            EasyMock.expect(httpClientErrorException.getMessage()).andReturn(exceptionMessage);

            httpClientErrorException.printStackTrace(EasyMock.isA(PrintWriter.class));

            replayAll();

            String tokenResult = oDataIntegrationImpl.getToken(userId);
            Assert.assertEquals(token, tokenResult);

            boolean success = false;

            try {
                tokenResult = oDataIntegrationImpl.getToken(userId);
            } catch (final RuntimeException e) {
                if(e.getMessage().equals(url+" - "+exceptionMessage+" - "+exceptionBody)){
                    success = true;
                }
            }
            Assert.assertTrue(success);


            verifyAll();
        } catch (final Exception e) {
            this.failure(e);
        }
    }

    @Test
    public void testGetGroups() {
        final String host = "https://jam8.sapjam.com";
        final String path = "/api/v1/OData";
        final String command = "/Groups?$format=json";
        final String url = host+path+command;
        final String skip = "&$skip=";
        final String token = "token";
        final String userId = "userId";
        final String firstname = "Bart";
        final String lastname = "Simpson";
        final Employee employee = new Employee(userId, firstname, lastname, "P");

        final Group group1 = new Group();
        group1.setId("groupId1");
        group1.setName(firstname +" "+lastname +"'s "+ODataIntegrationImpl.PERFORMANCE);
        group1.setActive(true);

        final Group group2 = new Group();
        group2.setId("groupId2");
        group2.setName("Jimmy John's "+ODataIntegrationImpl.PERFORMANCE);
        group2.setActive(false);

        final Group group3 = new Group();
        group3.setId("groupId3");
        group3.setName("Ricky Ricardo's "+ODataIntegrationImpl.PERFORMANCE);
        group3.setActive(true);

        final List<Group> groups = new ArrayList<Group>();
        groups.add(group1);
        groups.add(group2);

        final GroupWrapper groupWrapper1 = new GroupWrapper();
        groupWrapper1.setGroups(groups);
        final GroupMessageWrapper groupMessageWrapper1 = new GroupMessageWrapper();
        groupMessageWrapper1.setGroupWrapper(groupWrapper1);

        final GroupWrapper groupWrapper2 = new GroupWrapper();
        groupWrapper2.setGroups(new ArrayList<Group>());
        final GroupMessageWrapper groupMessageWrapper2 = new GroupMessageWrapper();
        groupMessageWrapper2.setGroupWrapper(groupWrapper2);

        try {
            oDataIntegrationImpl =
                    EasyMock.createMockBuilder(ODataIntegrationImpl.class)
                        .addMockedMethod("getRestTemplate")
                        .addMockedMethod("getRequest")
                        .createMock();

            oDataIntegrationImpl.setJamHost(host);
            oDataIntegrationImpl.setJamPath(path);
            oDataIntegrationImpl.setJamGroupCommand(command);

            EasyMock.expect(oDataIntegrationImpl.getRestTemplate()).andReturn(restTemplate);

            EasyMock.expect(oDataIntegrationImpl.getRequest(token)).andReturn(httpEntity).times(2);
            EasyMock.expect(restTemplate.exchange(url+skip+"0", HttpMethod.GET, httpEntity, GroupMessageWrapper.class)).andReturn(groupResponse);
            EasyMock.expect(restTemplate.exchange(url+skip+"100", HttpMethod.GET, httpEntity, GroupMessageWrapper.class)).andReturn(groupResponse);
            EasyMock.expect(groupResponse.getBody())
                .andReturn(groupMessageWrapper1)
                .andReturn(groupMessageWrapper2);

            replayAll();


            final Set<Group> retGroups = oDataIntegrationImpl.getGroups(token, employee);
            Assert.assertEquals(1, retGroups.size());
            final Iterator<Group> groupsIterator = retGroups.iterator();
            Assert.assertEquals(group1, groupsIterator.next());

            verifyAll();

        } catch (final Exception e) {
            this.failure(e);
        }
    }


    @Test
    public void testGetDiscussions() {
        final String groupId = "groupId";
        final String userId = "userId";

        final String host = "https://jam8.sapjam.com";
        final String path = "/api/v1/OData";
        final String command = "/Discussions('"+groupId+"')/Comments?$format=json";
        final String url = host+path+command;
        final String skip = "&$skip=";
        final String token = "token";


        final Discussion discussion1 = new Discussion();
        discussion1.setId("discussionId");
        discussion1.setDiscussionName("discussionName");
        discussion1.setUserId("userId");
        discussion1.setLikesCount("1");
        discussion1.setCommentCount("2");
        discussion1.setLastActivity("/Date("+String.valueOf(new Date().getTime())+")");

        final Discussion discussion2 = new Discussion();
        discussion2.setId("discussionId2");
        discussion2.setDiscussionName("discussionName2");
        discussion2.setUserId("userId");
        discussion2.setLikesCount("0");
        discussion2.setCommentCount("3");
        discussion2.setLastActivity("/Date("+String.valueOf(new Date().getTime())+")");

        final List<Discussion> discussions = new ArrayList<Discussion>();
        discussions.add(discussion1);
        discussions.add(discussion2);

        final DiscussionWrapper discussionWrapper1 = new DiscussionWrapper();
        discussionWrapper1.setDiscussions(discussions);
        final DiscussionMessageWrapper discussionMessageWrapper1 = new DiscussionMessageWrapper();
        discussionMessageWrapper1.setDiscussionWrapper(discussionWrapper1);

        final DiscussionWrapper discussionWrapper2 = new DiscussionWrapper();
        discussionWrapper2.setDiscussions(new ArrayList<Discussion>());
        final DiscussionMessageWrapper discussionMessageWrapper2 = new DiscussionMessageWrapper();
        discussionMessageWrapper2.setDiscussionWrapper(discussionWrapper2);

        try {
            oDataIntegrationImpl =
                    EasyMock.createMockBuilder(ODataIntegrationImpl.class)
                        .addMockedMethod("getRestTemplate")
                        .addMockedMethod("getRequest")
                        .createMock();

            oDataIntegrationImpl.setJamHost(host);
            oDataIntegrationImpl.setJamPath(path);
            oDataIntegrationImpl.setJamDiscussionsCommand(command);

            EasyMock.expect(oDataIntegrationImpl.getRestTemplate()).andReturn(restTemplate);

            EasyMock.expect(oDataIntegrationImpl.getRequest(token)).andReturn(httpEntity).times(2);
            EasyMock.expect(restTemplate.exchange(url+skip+"0", HttpMethod.GET, httpEntity, DiscussionMessageWrapper.class)).andReturn(discussionResponse);
            EasyMock.expect(restTemplate.exchange(url+skip+"100", HttpMethod.GET, httpEntity, DiscussionMessageWrapper.class)).andReturn(discussionResponse);
            EasyMock.expect(discussionResponse.getBody())
                .andReturn(discussionMessageWrapper1)
                .andReturn(discussionMessageWrapper2);

            replayAll();


            final SortedSet<Discussion> retDiscussions = oDataIntegrationImpl.getDiscussions(token, userId, groupId);
            Assert.assertEquals(2, retDiscussions.size());
            final Iterator<Discussion> discussionsIterator = retDiscussions.iterator();
            Assert.assertEquals(discussion1, discussionsIterator.next());
            Assert.assertEquals(discussion2, discussionsIterator.next());

            verifyAll();



        } catch (final Exception e) {
            this.failure(e);
        }
    }

    @Test
    public void testGetComments() {
        final String discussionId = "discussionId";
        final String userId = "userId";

        final String host = "https://jam8.sapjam.com";
        final String path = "/api/v1/OData";
        final String command = "/Comments('"+discussionId+"')/Creator?$format=json";
        final String url = host+path+command;
        final String skip = "&$skip=";
        final String token = "token";
        final String commentId1 = "comment Id1";
        final String commentId2 = "comment Id2";

        final long date1 = new GregorianCalendar(2014,1,1,2,14,2).getTimeInMillis();
        final long date2 = new GregorianCalendar(2014,1,2,2,14,2).getTimeInMillis();

        final CommentCreator commentCreator = new CommentCreator();
        commentCreator.setFirstName("firstName");
        commentCreator.setLastName("lastName");

        final Comment comment1 = new Comment();
        comment1.setComment("comment 1");
        comment1.setDiscussionId(discussionId);
        comment1.setId(commentId1);
        comment1.setUserId(userId);
        comment1.setCommentCreator(commentCreator);
        comment1.setLastModified("/Date("+date1+")");

        final Comment comment2 = new Comment();
        comment2.setComment("comment 2");
        comment2.setDiscussionId(discussionId);
        comment2.setId(commentId2);
        comment2.setUserId(userId);
        comment2.setCommentCreator(commentCreator);
        comment2.setLastModified("/Date("+date2+")");

        final List<Comment> comments = new ArrayList<Comment>();
        comments.add(comment1);
        comments.add(comment2);

        final CommentWrapper commentWrapper1 = new CommentWrapper();
        commentWrapper1.setComments(comments);
        final CommentMessageWrapper commentMessageWrapper1 = new CommentMessageWrapper();
        commentMessageWrapper1.setCommentWrapper(commentWrapper1);

        final CommentWrapper commentWrapper2 = new CommentWrapper();
        commentWrapper2.setComments(new ArrayList<Comment>());
        final CommentMessageWrapper commentMessageWrapper2 = new CommentMessageWrapper();
        commentMessageWrapper2.setCommentWrapper(commentWrapper2);


        try {
            oDataIntegrationImpl =
                    EasyMock.createMockBuilder(ODataIntegrationImpl.class)
                        .addMockedMethod("getRestTemplate")
                        .addMockedMethod("getRequest")
                        .addMockedMethod("getCommentCreator")
                        .createMock();

            oDataIntegrationImpl.setJamHost(host);
            oDataIntegrationImpl.setJamPath(path);
            oDataIntegrationImpl.setJamCommentCommand(command);

            EasyMock.expect(oDataIntegrationImpl.getRestTemplate()).andReturn(restTemplate);

            EasyMock.expect(oDataIntegrationImpl.getRequest(token)).andReturn(httpEntity).times(2);
            EasyMock.expect(restTemplate.exchange(url+skip+"0", HttpMethod.GET, httpEntity, CommentMessageWrapper.class)).andReturn(commentResponse);
            EasyMock.expect(oDataIntegrationImpl.getCommentCreator(token, commentId1)).andReturn(commentCreator);
            EasyMock.expect(oDataIntegrationImpl.getCommentCreator(token, commentId2)).andReturn(commentCreator);
            EasyMock.expect(restTemplate.exchange(url+skip+"100", HttpMethod.GET, httpEntity, CommentMessageWrapper.class)).andReturn(commentResponse);
            EasyMock.expect(commentResponse.getBody())
                .andReturn(commentMessageWrapper1)
                .andReturn(commentMessageWrapper2);

            replayAll();


            final SortedSet<Comment> retComments = oDataIntegrationImpl.getComments(token, userId, discussionId);
            Assert.assertEquals(2, retComments.size());
            final Iterator<Comment> commentIterator = retComments.iterator();
            Assert.assertEquals(comment1, commentIterator.next());
            Assert.assertEquals(comment2, commentIterator.next());

            verifyAll();



        } catch (final Exception e) {
            this.failure(e);
        }
    }


    @Test
    public void testGetCommentCreator() {
        final String commentId = "groupId";
        final String userId = "userId";

        final String host = "https://jam8.sapjam.com";
        final String path = "/api/v1/OData";
        final String command = "/Comments('"+commentId+"')/Creator?$format=json";
        final String url = host+path+command;
        final String skip = "&$skip=";
        final String token = "token";


        final CommentCreator commentCreator = new CommentCreator();
        commentCreator.setFirstName("firstName");
        commentCreator.setLastName("lastName");

        final CommentCreatorWrapper commentCreatorWrapper = new CommentCreatorWrapper();
        commentCreatorWrapper.setCommentCreator(commentCreator);

        final CommentCreatorMessageWrapper commentCreatorMessageWrapper = new CommentCreatorMessageWrapper();
        commentCreatorMessageWrapper.setCommentCreatorWrapper(commentCreatorWrapper);


        try {
            oDataIntegrationImpl =
                    EasyMock.createMockBuilder(ODataIntegrationImpl.class)
                        .addMockedMethod("getRestTemplate")
                        .addMockedMethod("getRequest")
                        .createMock();

            oDataIntegrationImpl.setJamHost(host);
            oDataIntegrationImpl.setJamPath(path);
            oDataIntegrationImpl.setJamCommentCreatorCommand(command);

            EasyMock.expect(oDataIntegrationImpl.getRestTemplate()).andReturn(restTemplate);

            EasyMock.expect(oDataIntegrationImpl.getRequest(token)).andReturn(httpEntity);
            EasyMock.expect(restTemplate.exchange(url, HttpMethod.GET, httpEntity, CommentCreatorMessageWrapper.class)).andReturn(commentCreatorResponse);
            EasyMock.expect(commentCreatorResponse.getBody())
                .andReturn(commentCreatorMessageWrapper);

            replayAll();


            final CommentCreator retCommentCreator = oDataIntegrationImpl.getCommentCreator(token, commentId);
            Assert.assertEquals(commentCreator, retCommentCreator);

            verifyAll();



        } catch (final Exception e) {
            this.failure(e);
        }
    }

    @Test
    public void testLoggingRequestInterceptor() {

        try {
            oDataIntegrationImpl =
                    EasyMock.createMockBuilder(ODataIntegrationImpl.class)
                        .createMock();

//            String headers = "headers";
            final HttpHeaders headers = new HttpHeaders();
            headers.add("Authorization","Bearer: token");
            final byte[] requestByteArray = "request string".getBytes();
            final String responseBody1 = "response string 1";
            final String responseBody2 = "response string 2";
            final URI uri = new URI("uri");
            final HttpStatus httpStatus = HttpStatus.ACCEPTED;

            EasyMock.expect(execution.execute(httpRequest, requestByteArray)).andReturn(httpResponse);
            EasyMock.expect(httpRequest.getHeaders()).andReturn(headers);
            EasyMock.expect(httpRequest.getURI()).andReturn(uri);
            EasyMock.expect(httpResponse.getStatusCode()).andReturn(httpStatus).times(2);
            EasyMock.expect(httpResponse.getBody()).andReturn(inputStream);
            EasyMock.expect(requestInterceptor.getReader(inputStream)).andReturn(bufferedReader);
            EasyMock.expect(bufferedReader.readLine())
                .andReturn(responseBody1)
                .andReturn(responseBody2)
                .andReturn(null);
            bufferedReader.close();

            replayAll();

            requestInterceptor.intercept(httpRequest, requestByteArray, execution);

            verifyAll();

        } catch (final Exception e) {
            this.failure(e);
        }
    }


    @Override
    protected void replayAll() {
        EasyMock.replay(oDataIntegrationImpl);
        EasyMock.replay(restTemplate);
        EasyMock.replay(httpEntity);
        EasyMock.replay(authTokenResponse);
        EasyMock.replay(httpClientErrorException);
        EasyMock.replay(groupResponse);
        EasyMock.replay(discussionResponse);
        EasyMock.replay(commentResponse);
        EasyMock.replay(commentCreatorResponse);
        EasyMock.replay(httpRequest);
        EasyMock.replay(execution);
        EasyMock.replay(httpResponse);
        EasyMock.replay(inputStream);
        EasyMock.replay(bufferedReader);
        EasyMock.replay(requestInterceptor);
    }



    @Override
    protected void verifyAll() {
        EasyMock.verify(oDataIntegrationImpl);
        EasyMock.verify(restTemplate);
        EasyMock.verify(httpEntity);
        EasyMock.verify(authTokenResponse);
        EasyMock.verify(httpClientErrorException);
        EasyMock.verify(groupResponse);
        EasyMock.verify(discussionResponse);
        EasyMock.verify(commentResponse);
        EasyMock.verify(commentCreatorResponse);
        EasyMock.verify(httpRequest);
        EasyMock.verify(execution);
        EasyMock.verify(httpResponse);
        EasyMock.verify(inputStream);
        EasyMock.verify(bufferedReader);
        EasyMock.verify(requestInterceptor);
    }

}
