package com.pms.model;

/** A mobile-application project (iOS/Android). Higher UI/QA overhead. */
public class MobileAppProject extends Project {

    public MobileAppProject(String projectId, String name, Client client) {
        super(projectId, name, client);
    }

    @Override
    public double complexityFactor() {
        return 1.4;
    }

    @Override
    public String projectType() {
        return "Mobile Application";
    }
}
