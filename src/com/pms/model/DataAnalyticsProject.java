package com.pms.model;

/** A data-analytics / ML project. Highest complexity of the three. */
public class DataAnalyticsProject extends Project {

    public DataAnalyticsProject(String projectId, String name, Client client) {
        super(projectId, name, client);
    }

    @Override
    public double complexityFactor() {
        return 1.6;
    }

    @Override
    public String projectType() {
        return "Data Analytics";
    }
}
