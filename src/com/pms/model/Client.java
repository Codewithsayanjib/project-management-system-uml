package com.pms.model;

import java.util.ArrayList;
import java.util.List;

/**
 * A client of the company. One client may own several projects. Clients raise
 * requirements and give feedback at each stage of a project.
 */
public class Client {
    private final String clientId;
    private final String name;
    private final String contactEmail;
    private final List<Project> projects = new ArrayList<>();

    public Client(String clientId, String name, String contactEmail) {
        this.clientId = clientId;
        this.name = name;
        this.contactEmail = contactEmail;
    }

    void addProject(Project project) {
        projects.add(project);
    }

    public String getClientId() {
        return clientId;
    }

    public String getName() {
        return name;
    }

    public String getContactEmail() {
        return contactEmail;
    }

    public List<Project> getProjects() {
        return projects;
    }

    @Override
    public String toString() {
        return String.format("%s [%s] <%s>", name, clientId, contactEmail);
    }
}
