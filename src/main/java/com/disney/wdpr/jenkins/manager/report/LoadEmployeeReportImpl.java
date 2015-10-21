package com.disney.wdpr.jenkins.manager.report;

import java.util.Map;

import com.disney.wdpr.jenkins.dto.GenericDto;
import com.disney.wdpr.jenkins.dto.report.archive.Employee;
import com.disney.wdpr.jenkins.manager.LoadReport;

public class LoadEmployeeReportImpl implements LoadReport {

    public final static String ACTIVE = "active";
    public final static String PERFORMANCE_POPULATION = "P";
    public final static String LEFT_PAD_CHARACTERS = "00000000";

    @Override
    public void loadLine(final String line, final Map<String, GenericDto> employeeMap) {
        if (line != null && !line.isEmpty()) {
            final Employee emp = createEmployeeObject(line);

            if (emp.isPerformancePopulation()) {
                employeeMap.put(emp.getUserId(), emp);
            }
        }
    }

    protected Employee createEmployeeObject(final String recordLine) {

        final String[] lineArray = recordLine.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)");
        String userId = lineArray[0].replaceAll("\"", "");
        final String firstname = lineArray[1].replaceAll("\"", "");
        final String lastname = lineArray[3].replaceAll("\"", "");
        final String perfPopulation = lineArray[7].replaceAll("\"", "");

        userId = LEFT_PAD_CHARACTERS.substring(userId.length()) + userId;
        final Employee emp = new Employee(userId, firstname, lastname, perfPopulation);

        return emp;
    }

}
