package com.pms.model;

import com.pms.enums.Designation;
import java.util.ArrayList;
import java.util.List;

/**
 * An employee of the company. Employees are assigned to projects to work on
 * them. An employee has a designation and a set of skills.
 */
public class Employee {
    private final String employeeId;
    private final String name;
    private final Designation designation;
    private final List<Skill> skills = new ArrayList<>();
    private final List<Assignment> assignments = new ArrayList<>();

    public Employee(String employeeId, String name, Designation designation) {
        this.employeeId = employeeId;
        this.name = name;
        this.designation = designation;
    }

    public void addSkill(Skill skill) {
        skills.add(skill);
    }

    /** Package-internal: called by Assignment so the link stays consistent. */
    void addAssignment(Assignment assignment) {
        assignments.add(assignment);
    }

    public boolean hasSkill(String skillName) {
        return skills.stream().anyMatch(s -> s.getName().equalsIgnoreCase(skillName));
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public String getName() {
        return name;
    }

    public Designation getDesignation() {
        return designation;
    }

    public List<Skill> getSkills() {
        return skills;
    }

    public List<Assignment> getAssignments() {
        return assignments;
    }

    @Override
    public String toString() {
        return String.format("%s [%s] - %s | skills=%s",
                name, employeeId, designation, skills);
    }
}
