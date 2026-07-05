package com.pms.model;

import java.time.LocalDate;

/**
 * Association class that records WHICH EMPLOYEE HANDLES WHICH PROJECT.
 * It links an Employee to a Project together with the role the employee plays
 * on that project and the date the assignment was made.
 */
public class Assignment {
    private final Employee employee;
    private final Project project;
    private final String roleOnProject;
    private final LocalDate assignedOn;

    public Assignment(Employee employee, Project project, String roleOnProject) {
        this.employee = employee;
        this.project = project;
        this.roleOnProject = roleOnProject;
        this.assignedOn = LocalDate.now();
        // keep both sides of the association consistent
        employee.addAssignment(this);
    }

    public Employee getEmployee() {
        return employee;
    }

    public Project getProject() {
        return project;
    }

    public String getRoleOnProject() {
        return roleOnProject;
    }

    public LocalDate getAssignedOn() {
        return assignedOn;
    }

    @Override
    public String toString() {
        return String.format("%s handles \"%s\" as %s (since %s)",
                employee.getName(), project.getName(), roleOnProject, assignedOn);
    }
}
