package com.pms.model;

import com.pms.enums.LifeCyclePhase;
import com.pms.enums.ProjectStatus;
import java.util.ArrayList;
import java.util.List;

/**
 * Abstract SUPER CLASS of the system (as required by the assignment).
 *
 * A Project belongs to one Client, has a requirement, a development tracker,
 * a billing record, a set of employee assignments and client feedback captured
 * at each life-cycle stage.
 *
 * Concrete project types (Web / Mobile / DataAnalytics ...) extend this class
 * and supply their own complexity factor, which drives the billing amount.
 */
public abstract class Project {
    protected final String projectId;
    protected final String name;
    protected final Client client;
    protected ProjectStatus status = ProjectStatus.NOT_STARTED;

    protected ProjectRequirement requirement;
    protected final DevelopmentTracker tracker = new DevelopmentTracker();
    protected Billing billing;

    protected final List<Assignment> assignments = new ArrayList<>();
    protected final List<ClientFeedback> feedbackLog = new ArrayList<>();

    protected Project(String projectId, String name, Client client) {
        this.projectId = projectId;
        this.name = name;
        this.client = client;
        client.addProject(this); // keep the association consistent
    }

    /**
     * Complexity multiplier for this kind of project. Subclasses decide the
     * value; billing scales the cost by it. This is the polymorphic hook.
     */
    public abstract double complexityFactor();

    /** Human-readable category of the project. */
    public abstract String projectType();

    // ---- domain operations -------------------------------------------------

    public void setRequirement(ProjectRequirement requirement) {
        this.requirement = requirement;
    }

    public void setBilling(Billing billing) {
        this.billing = billing;
    }

    public Assignment assignEmployee(Employee employee, String roleOnProject) {
        Assignment assignment = new Assignment(employee, this, roleOnProject);
        assignments.add(assignment);
        if (status == ProjectStatus.NOT_STARTED) {
            status = ProjectStatus.IN_PROGRESS;
        }
        return assignment;
    }

    public void addFeedback(ClientFeedback feedback) {
        feedbackLog.add(feedback);
    }

    /** Advance the life cycle; mark the project COMPLETED after deployment. */
    public void advanceLifeCycle() {
        boolean advanced = tracker.advancePhase();
        if (!advanced || tracker.getCurrentPhase() == LifeCyclePhase.MAINTENANCE) {
            status = ProjectStatus.COMPLETED;
        }
    }

    public double invoiceAmount() {
        if (billing == null) {
            return 0.0;
        }
        return billing.computeTotal(complexityFactor());
    }

    public double averageFeedbackRating() {
        return feedbackLog.stream().mapToInt(ClientFeedback::getRating)
                .average().orElse(0.0);
    }

    // ---- getters -----------------------------------------------------------

    public String getProjectId() { return projectId; }
    public String getName() { return name; }
    public Client getClient() { return client; }
    public ProjectStatus getStatus() { return status; }
    public ProjectRequirement getRequirement() { return requirement; }
    public DevelopmentTracker getTracker() { return tracker; }
    public Billing getBilling() { return billing; }
    public List<Assignment> getAssignments() { return assignments; }
    public List<ClientFeedback> getFeedbackLog() { return feedbackLog; }

    @Override
    public String toString() {
        return String.format("%s \"%s\" [%s] client=%s status=%s type=%s",
                getClass().getSimpleName(), name, projectId,
                client.getName(), status, projectType());
    }
}
