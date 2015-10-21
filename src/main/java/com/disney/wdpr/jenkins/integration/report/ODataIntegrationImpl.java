package com.disney.wdpr.jenkins.integration.report;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.apache.log4j.Logger;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpRequest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.InterceptingClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJacksonHttpMessageConverter;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.disney.wdpr.jenkins.dto.extract.comments.Comment;
import com.disney.wdpr.jenkins.dto.extract.comments.CommentCreator;
import com.disney.wdpr.jenkins.dto.extract.comments.CommentCreatorMessageWrapper;
import com.disney.wdpr.jenkins.dto.extract.comments.CommentMessageWrapper;
import com.disney.wdpr.jenkins.dto.report.archive.AuthTokenResponse;
import com.disney.wdpr.jenkins.dto.report.archive.Discussion;
import com.disney.wdpr.jenkins.dto.report.archive.DiscussionMessageWrapper;
import com.disney.wdpr.jenkins.dto.report.archive.Employee;
import com.disney.wdpr.jenkins.dto.report.archive.Group;
import com.disney.wdpr.jenkins.dto.report.archive.GroupMessageWrapper;
import com.disney.wdpr.jenkins.dto.report.archive.Task;
import com.disney.wdpr.jenkins.dto.report.archive.TaskAssignment;
import com.disney.wdpr.jenkins.dto.report.archive.TaskAssignmentMessageWrapper;
import com.disney.wdpr.jenkins.dto.report.archive.TaskMessageWrapper;

public class ODataIntegrationImpl implements ODataIntegration {
    private static Logger log = Logger.getLogger(ODataIntegrationImpl.class);

    private String idpHost;
    private String idpPath;
    private String idpCommand;
    private String idpClientId;

    private String jamHost;
    private String jamPath;
    private String jamGroupCommand;
    private String jamDiscussionsCommand;
    private String jamCommentCommand;
    private String jamCommentCreatorCommand;
    private String jamTasksCommand;
    private String jamTaskAssignmentCommand;

    public final static String AUTH_CLIENT_ID_HEADER = "Auth-Client-ID";
    public final static String ODATA_TOKEN_HEADER = "Authorization: Bearer";
    public final static int PAGE_INTERVAL = 100;
    public final static String PAGE ="&$skip=<<skip>>";
    public final static String SKIP_TOKEN ="<<skip>>";
    public final static String KEY_TOKEN ="<<key>>";
    public final static String KEY2_TOKEN ="<<key2>>";
    public final static String PERFORMANCE ="Performance";

    @Override
    public String getToken(final String userId) {

        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.add(AUTH_CLIENT_ID_HEADER, idpClientId);
        final HttpEntity<String> request = getHttpEntity(headers);

        final RestTemplate restTemplate = getRestTemplate();
        final String uri = idpHost+idpPath+idpCommand+userId;
        AuthTokenResponse authTokenResponse = null;
        try {
            authTokenResponse = restTemplate.postForObject(uri, request, AuthTokenResponse.class);
        } catch (final HttpClientErrorException e) {
            convertException(e, uri);
        }

        return authTokenResponse.getAccess_token();
    }

    protected HttpEntity<String> getHttpEntity(final HttpHeaders headers){
        return new HttpEntity<String>(headers);
    }

    protected RestTemplate getRestTemplate() {
        final RestTemplate restTemplate = new RestTemplate();

        //Add the Jackson Message converter
        final List<HttpMessageConverter<?>> messageConverters = new ArrayList<HttpMessageConverter<?>>();
        messageConverters.add(new MappingJacksonHttpMessageConverter());
        restTemplate.setMessageConverters(messageConverters);

        if(log.isDebugEnabled()){
            addLoggingInterceptor(restTemplate);
        }

        return restTemplate;

    }

    @Override
    public Set<Group> getGroups(final String token, final Employee employee) {
        final Set<Group> returnSet = new HashSet<Group>();

        final RestTemplate restTemplate = getRestTemplate();

        int skip = 0;

        while (true) {
            final String currentPage = PAGE.replace(SKIP_TOKEN, String.valueOf(skip));

            ResponseEntity<GroupMessageWrapper> response = null;
            final String uri = jamHost+jamPath+jamGroupCommand+currentPage;
            try {
                response = restTemplate.exchange(uri,
                        HttpMethod.GET, getRequest(token), GroupMessageWrapper.class);
            } catch (final HttpClientErrorException e) {
                convertException(e, uri);
            }

            final GroupMessageWrapper message = response.getBody();

            if (message.getGroupWrapper().getGroups().size() > 0) {
                for (final Group group : message.getGroupWrapper().getGroups()) {
                    if (group.isActive() && isEmployeesGroup(group, employee)) {
                        log.info("group: "+group.getId());
                        returnSet.add(group);
                    }
                }
                log.info("groups set size: "+returnSet.size());
            } else {
                break;
            }
            skip += PAGE_INTERVAL;
        }

        return returnSet;
    }


    private boolean isEmployeesGroup(final Group group, final Employee employee){
        boolean retVal = false;

        if(group.getName().contains(employee.getLastname())
                && group.getName().contains(PERFORMANCE)) {
            retVal = true;
            log.debug("Not Filtering groupName: "+group.getName()+" - Employee: "+employee.getLastname());
        } else {
            log.debug("Filtering groupName: "+group.getName()+" - Employee: "+employee.getLastname());
        }
        return retVal;
    }

    protected void convertException(final HttpClientErrorException clientException, final String uri) {
        final String body = clientException.getResponseBodyAsString();
        final String messageText = clientException.getMessage();
        final RuntimeException runtime = new RuntimeException(uri+" - "+messageText + " - "+body);
        log.fatal(runtime.getMessage(),clientException);
        throw runtime;
    }


    protected HttpEntity<String> getRequest(final String token) {
        final HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization","Bearer "+token);

        final HttpEntity<String> request = new HttpEntity<String>(headers);
        return request;
    }



    protected void addLoggingInterceptor(final RestTemplate restTemplate){
        final ClientHttpRequestInterceptor ri = new LoggingRequestInterceptor();
        final List<ClientHttpRequestInterceptor> ris = new ArrayList<ClientHttpRequestInterceptor>();
        ris.add(ri);
        restTemplate.setInterceptors(ris);
        restTemplate.setRequestFactory(new InterceptingClientHttpRequestFactory(
        new BufferingClientHttpRequestFactory(new SimpleClientHttpRequestFactory()), ris));
    }

    @Override
    public SortedSet<Discussion> getDiscussions(final String token, final String userId, final String groupId) {
        final SortedSet<Discussion> returnList = new TreeSet<Discussion>();

        final RestTemplate restTemplate = getRestTemplate();

        int skip = 0;

        final String jamCommand = jamDiscussionsCommand.replace(KEY_TOKEN, groupId);

        while (true) {
            final String currentPage = PAGE.replace(SKIP_TOKEN, String.valueOf(skip));

            ResponseEntity<DiscussionMessageWrapper> response=null;
            final String uri = jamHost+jamPath+jamCommand+currentPage;
            try {
                response = restTemplate.exchange(uri,
                        HttpMethod.GET, getRequest(token), DiscussionMessageWrapper.class);
            } catch (final HttpClientErrorException e) {
                convertException(e, uri);
            }
            final DiscussionMessageWrapper message = response.getBody();

            if (message.getDiscussionWrapper().getDiscussions().size() > 0) {
                for (final Discussion discussion : message.getDiscussionWrapper().getDiscussions()) {
                    log.info("Discussion: "+discussion.getId());
                    discussion.setUserId(userId);
                    returnList.add(discussion);
                }
                log.info("Discussions set size: "+returnList.size());
            } else {
                break;
            }
            skip += PAGE_INTERVAL;
        }

        return returnList;
    }

    @Override
    public SortedSet<Comment> getComments(final String token, final String userId, final String discussionId) {

        final SortedSet<Comment> returnSet = new TreeSet<Comment>();

        final RestTemplate restTemplate = getRestTemplate();

        int skip = 0;

        final String jamCommand = jamCommentCommand.replace(KEY_TOKEN, discussionId);

        while (true) {
            final String currentPage = PAGE.replace(SKIP_TOKEN, String.valueOf(skip));

            ResponseEntity<CommentMessageWrapper> response=null;
            final String uri = jamHost+jamPath+jamCommand+currentPage;
            try {
                response = restTemplate.exchange(uri,
                        HttpMethod.GET, getRequest(token), CommentMessageWrapper.class);
            } catch (final HttpClientErrorException e) {
                convertException(e, uri);
            }


            final CommentMessageWrapper message = response.getBody();

            if (message.getCommentWrapper().getComments().size() > 0) {
                for (final Comment comment : message.getCommentWrapper().getComments()) {
                    log.info("Comment: "+comment.getId());
                    comment.setUserId(userId);
                    comment.setDiscussionId(discussionId);
                    comment.setCommentCreator(getCommentCreator(token, comment.getId()));
                    final boolean added = returnSet.add(comment);
                    log.debug(added);
                    log.debug(comment.hashCode());
                }
                log.info("Comment list size: "+returnSet.size());
            } else {
                break;
            }
            skip += PAGE_INTERVAL;
        }

        return returnSet;
    }

    @Override
    public List<TaskAssignment> getTasks(final String token, final String userId) {
        final List<TaskAssignment> taskAssignments = new ArrayList<TaskAssignment>();

        final RestTemplate restTemplate = getRestTemplate();

        int skip = 0;

        while (true) {
            final String currentPage = PAGE.replace(SKIP_TOKEN, String.valueOf(skip));

            ResponseEntity<TaskMessageWrapper> response = null;
            final String uri = jamHost+jamPath+jamTasksCommand+currentPage;
            try {
                response = restTemplate.exchange(uri,
                        HttpMethod.GET, getRequest(token), TaskMessageWrapper.class);
            } catch (final HttpClientErrorException e) {
                convertException(e, uri);
            }

            final TaskMessageWrapper message = response.getBody();

            if (message.getTaskWrapper().getTasks().size() > 0) {
                for (final Task task: message.getTaskWrapper().getTasks()) {
                    log.info("taskId: "+task.getId());
                    taskAssignments.add(getTaskAssignment(token, userId, task));
                }
                log.info("tasks list size: "+taskAssignments.size());
            } else {
                break;
            }
            skip += PAGE_INTERVAL;
        }

        return taskAssignments;
    }

    protected TaskAssignment getTaskAssignment(final String token, final String userId, final Task task) {

        final RestTemplate restTemplate = getRestTemplate();

        final int skip = 0;

        String jamCommand = jamTaskAssignmentCommand.replace(KEY_TOKEN, task.getAssigneeId());
        jamCommand = jamCommand.replace(KEY2_TOKEN, task.getId());

        ResponseEntity<TaskAssignmentMessageWrapper> response=null;
        final String uri=jamHost+jamPath+jamCommand;
        try {
            response = restTemplate.exchange(uri,
                    HttpMethod.GET, getRequest(token), TaskAssignmentMessageWrapper.class);
        } catch (final HttpClientErrorException e) {
            convertException(e, uri);
        }

        final TaskAssignment taskAssignment = response.getBody().getTaskAssignmentWrapper().getTaskAssignment();
        taskAssignment.setUserId(userId);

        return taskAssignment;
    }

    protected CommentCreator getCommentCreator(final String token, final String commentId) {

        final RestTemplate restTemplate = getRestTemplate();

        final String jamCommand = jamCommentCreatorCommand.replace(KEY_TOKEN, commentId);

        ResponseEntity<CommentCreatorMessageWrapper> response=null;
        final String uri=jamHost+jamPath+jamCommand;
        try {
            response = restTemplate.exchange(uri,
                    HttpMethod.GET, getRequest(token), CommentCreatorMessageWrapper.class);
        } catch (final HttpClientErrorException e) {
            convertException(e, uri);
        }
        final CommentCreatorMessageWrapper message = response.getBody();

        return message.getCommentCreatorWrapper().getCommentCreator();
    }

    public void setIdpClientId(final String idpClientId) {
        this.idpClientId = idpClientId;
    }

    public void setIdpHost(final String idpHost) {
        this.idpHost = idpHost;
    }

    public void setIdpPath(final String idpPath) {
        this.idpPath = idpPath;
    }

    public void setIdpCommand(final String idpCommand) {
        this.idpCommand = idpCommand;
    }

    public void setJamHost(final String jamHost) {
        this.jamHost = jamHost;
    }

    public void setJamPath(final String jamPath) {
        this.jamPath = jamPath;
    }

    public void setJamGroupCommand(final String jamGroupCommand) {
        this.jamGroupCommand = jamGroupCommand;
    }

    public void setJamDiscussionsCommand(final String jamDiscussionsCommand) {
        this.jamDiscussionsCommand = jamDiscussionsCommand;
    }

    public void setJamCommentCommand(final String jamCommentCommand) {
        this.jamCommentCommand = jamCommentCommand;
    }

    public void setJamCommentCreatorCommand(final String jamCommentCreatorCommand) {
        this.jamCommentCreatorCommand = jamCommentCreatorCommand;
    }

    public void setJamTasksCommand(final String jamTasksCommand) {
        this.jamTasksCommand = jamTasksCommand;
    }

    public void setJamTaskAssignmentCommand(final String jamTaskAssignmentCommand) {
        this.jamTaskAssignmentCommand = jamTaskAssignmentCommand;
    }





}

class LoggingRequestInterceptor implements ClientHttpRequestInterceptor {

//    private static final Logger log = log.getLogger(LoggingRequestInterceptor.class);
    private static Logger log = Logger.getLogger(LoggingRequestInterceptor.class);

    @Override
    public ClientHttpResponse intercept(final HttpRequest request, final byte[] body, final ClientHttpRequestExecution execution) throws IOException {

        final ClientHttpResponse response = execution.execute(request, body);

        log.debug(request.getHeaders().toString());
        log.debug(request.getURI().toString());
        log.debug(response.getStatusCode());
        log.debug(response.getStatusCode().getReasonPhrase());
        log.debug(getStringFromInputStream(response.getBody()));

        return response;
    }


    private String getStringFromInputStream(final InputStream is) {

        BufferedReader br = null;
        final StringBuilder sb = new StringBuilder();

        String line;
        try {

            br = getReader(is);
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

        } catch (final IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (final IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return sb.toString();

    }

    protected BufferedReader getReader(final InputStream is){
        return new BufferedReader(new InputStreamReader(is));
    }


}
