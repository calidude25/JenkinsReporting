package com.disney.wdpr.jenkins.manager;

import java.util.Map;

/**
 * @author matt.b.carson
 *
 */
public interface JobManager {

    public void process(Map<String, String> paramMap) throws Exception;


}
