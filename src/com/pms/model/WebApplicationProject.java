package com.pms.model;

/** A web-application project. Moderate complexity. */
public class WebApplicationProject extends Project {

    public WebApplicationProject(String projectId, String name, Client client) {
        super(projectId, name, client);
    }

    @Override
    public double complexityFactor() {
        return 1.2;
    }

    @Override
    public String projectType() {
        return "Web Application";
    }
}
