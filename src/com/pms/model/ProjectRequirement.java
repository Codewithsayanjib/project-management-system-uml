package com.pms.model;

import java.util.ArrayList;
import java.util.List;

/**
 * The requirement of a project as raised by the client, owned by a general
 * department. Holds a list of individual requirement items.
 */
public class ProjectRequirement {
    private final String department;      // general dept that owns the requirement
    private final String summary;
    private final List<String> items = new ArrayList<>();
    private boolean feasible = false;     // set once feasibility is confirmed

    public ProjectRequirement(String department, String summary) {
        this.department = department;
        this.summary = summary;
    }

    public void addItem(String item) {
        items.add(item);
    }

    public void markFeasible(boolean feasible) {
        this.feasible = feasible;
    }

    public boolean isFeasible() {
        return feasible;
    }

    public String getDepartment() {
        return department;
    }

    public String getSummary() {
        return summary;
    }

    public List<String> getItems() {
        return items;
    }

    @Override
    public String toString() {
        return String.format("Requirement[dept=%s, feasible=%s]: %s %s",
                department, feasible, summary, items);
    }
}
