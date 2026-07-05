package com.pms.enums;

/**
 * The life-cycle phases a project moves through in order. The development
 * tracker advances a project from REQUIREMENT_FEASIBILITY to MAINTENANCE.
 */
public enum LifeCyclePhase {
    REQUIREMENT_FEASIBILITY("Requirement & Feasibility"),
    DEVELOPMENT("Development"),
    TESTING("Testing"),
    DEPLOYMENT("Deployment"),
    MAINTENANCE("Maintenance");

    private final String label;

    LifeCyclePhase(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
