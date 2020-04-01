package com.reporter.jenkins.manager;

import java.util.Map;

/**
 * @author 
 *
 */
public interface JobManager {

    public void process(Map<String, String> paramMap) throws Exception;


}
