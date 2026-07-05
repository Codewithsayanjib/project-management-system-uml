package com.pms.model;

import java.util.ArrayList;
import java.util.List;

/**
 * The company. It handles MULTIPLE CLIENTS (and their projects) and maintains
 * a pool of employees which it ASSIGNS to particular projects. This is the
 * top-level aggregate root of the system.
 */
public class Company {
    private final String name;
    private final List<Client> clients = new ArrayList<>();
    private final List<Employee> employees = new ArrayList<>();
    private final List<Project> projects = new ArrayList<>();

    public Company(String name) {
        this.name = name;
    }

    public Client registerClient(Client client) {
        clients.add(client);
        return client;
    }

    public Employee hireEmployee(Employee employee) {
        employees.add(employee);
        return employee;
    }

    public <T extends Project> T addProject(T project) {
        projects.add(project);
        return project;
    }

    /** Assign an employee from the pool to a specific project. */
    public Assignment assign(Employee employee, Project project, String role) {
        if (!employees.contains(employee)) {
            throw new IllegalStateException("Employee not part of company: " + employee.getName());
        }
        if (!projects.contains(project)) {
            throw new IllegalStateException("Unknown project: " + project.getName());
        }
        return project.assignEmployee(employee, role);
    }

    /** Find employees that possess a given skill (used to staff a project). */
    public List<Employee> findEmployeesWithSkill(String skillName) {
        List<Employee> matches = new ArrayList<>();
        for (Employee e : employees) {
            if (e.hasSkill(skillName)) {
                matches.add(e);
            }
        }
        return matches;
    }

    /** Total billable amount across every project the company runs. */
    public double totalRevenue() {
        return projects.stream().mapToDouble(Project::invoiceAmount).sum();
    }

    public String getName() { return name; }
    public List<Client> getClients() { return clients; }
    public List<Employee> getEmployees() { return employees; }
    public List<Project> getProjects() { return projects; }
}
