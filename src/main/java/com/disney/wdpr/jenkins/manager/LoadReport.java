package com.disney.wdpr.jenkins.manager;

import java.util.Map;

import com.disney.wdpr.jenkins.dto.GenericDto;


public interface LoadReport {

    
    public void loadLine(String line, Map<String, GenericDto> activityMap);
    

}
