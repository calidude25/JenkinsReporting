package com.disney.wdpr.jenkins.dto.report.archive;

import com.disney.wdpr.jenkins.dto.GenericDto;

/**
 * @author matt.b.carson
 *
 */
public class Employee implements GenericDto {

    private final String userId;
    private final String performancePopulation;
    private final String firstname;
    private final String lastname;

    public static final String PERFORMANCE_POPULATION = "P";

    public Employee(final String userId, final String firstname, final String lastname, final String performancePopulation) {
        super();
        this.userId = userId;
        this.firstname = firstname;
        this.lastname = lastname;
        this.performancePopulation = performancePopulation;
    }

    public String getUserId() {
        return userId;
    }
    public String getFirstname() {
        return firstname;
    }

    public String getLastname() {
        return lastname;
    }
    public boolean isPerformancePopulation() {
        if(performancePopulation.toUpperCase().equals(PERFORMANCE_POPULATION)){
            return true;
        }
        return false;
    }

}
