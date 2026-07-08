package com.pms.ui;

import com.pms.enums.Designation;
import com.pms.enums.LifeCyclePhase;
import com.pms.enums.SkillLevel;
import com.pms.model.*;

/** Builds the demo company used to populate the GUI. */
public final class SampleData {
    private SampleData() {}

    public static Company build() {
        Company company = new Company("Acme Software Pvt. Ltd.");

        Client retail = company.registerClient(new Client("C-01", "RetailKart", "cto@retailkart.com"));
        Client medico = company.registerClient(new Client("C-02", "MediCare Labs", "it@medicare.com"));
        Client fin    = company.registerClient(new Client("C-03", "FinEdge", "cto@finedge.io"));

        Employee alice = company.hireEmployee(emp("E-01", "Alice", Designation.PROJECT_MANAGER,  new Skill("Planning", SkillLevel.EXPERT)));
        Employee bob   = company.hireEmployee(emp("E-02", "Bob", Designation.SENIOR_DEVELOPER,   new Skill("Java", SkillLevel.EXPERT), new Skill("React", SkillLevel.ADVANCED)));
        Employee carol = company.hireEmployee(emp("E-03", "Carol", Designation.DEVELOPER,        new Skill("Kotlin", SkillLevel.ADVANCED)));
        Employee dan   = company.hireEmployee(emp("E-04", "Dan", Designation.TESTER,             new Skill("Selenium", SkillLevel.INTERMEDIATE)));
        Employee eve   = company.hireEmployee(emp("E-05", "Eve", Designation.DEVOPS_ENGINEER,    new Skill("AWS", SkillLevel.EXPERT)));
        Employee finn  = company.hireEmployee(emp("E-06", "Finn", Designation.BUSINESS_ANALYST,  new Skill("Analytics", SkillLevel.ADVANCED)));

        // Project 1: completed web app
        WebApplicationProject shop = company.addProject(new WebApplicationProject("P-01", "RetailKart Storefront", retail));
        ProjectRequirement r1 = new ProjectRequirement("E-Commerce", "Online storefront with cart & payments");
        r1.addItem("Product catalogue"); r1.addItem("Shopping cart"); r1.addItem("Payment gateway");
        r1.markFeasible(true);
        shop.setRequirement(r1);
        shop.setBilling(new Billing(5000.0, 55.0, 0.18));
        company.assign(alice, shop, "Project Manager");
        company.assign(bob, shop, "Lead Developer");
        company.assign(dan, shop, "QA Engineer");
        shop.getTracker().updateProgress(100);
        shop.addFeedback(new ClientFeedback(LifeCyclePhase.REQUIREMENT_FEASIBILITY, 5, "Scope looks great"));
        shop.advanceLifeCycle();
        shop.getBilling().logHours(120);
        shop.addFeedback(new ClientFeedback(LifeCyclePhase.DEVELOPMENT, 4, "Good progress on cart"));
        shop.advanceLifeCycle();
        shop.getBilling().logHours(40);
        shop.addFeedback(new ClientFeedback(LifeCyclePhase.TESTING, 4, "Few bugs, fixed quickly"));
        shop.advanceLifeCycle();
        shop.getBilling().logHours(20);
        shop.addFeedback(new ClientFeedback(LifeCyclePhase.DEPLOYMENT, 5, "Smooth go-live"));
        shop.advanceLifeCycle();

        // Project 2: in-progress mobile app
        MobileAppProject app = company.addProject(new MobileAppProject("P-02", "MediCare Patient App", medico));
        ProjectRequirement r2 = new ProjectRequirement("Healthcare", "Patient appointment & records app");
        r2.addItem("Appointment booking"); r2.addItem("Lab report viewer");
        r2.markFeasible(true);
        app.setRequirement(r2);
        app.setBilling(new Billing(7000.0, 60.0, 0.18));
        company.assign(carol, app, "Android Developer");
        company.assign(eve, app, "DevOps");
        app.getTracker().updateProgress(100);
        app.addFeedback(new ClientFeedback(LifeCyclePhase.REQUIREMENT_FEASIBILITY, 4, "Clear requirements"));
        app.advanceLifeCycle();
        app.getBilling().logHours(90);
        app.getTracker().updateProgress(60);
        app.addFeedback(new ClientFeedback(LifeCyclePhase.DEVELOPMENT, 4, "Booking flow looks good"));

        // Project 3: early data-analytics project
        DataAnalyticsProject dash = company.addProject(new DataAnalyticsProject("P-03", "FinEdge Risk Dashboard", fin));
        ProjectRequirement r3 = new ProjectRequirement("Finance", "Real-time credit risk analytics dashboard");
        r3.addItem("Data ingestion"); r3.addItem("Risk scoring model"); r3.addItem("Interactive dashboard");
        r3.markFeasible(true);
        dash.setRequirement(r3);
        dash.setBilling(new Billing(9000.0, 70.0, 0.18));
        company.assign(finn, dash, "Business Analyst");
        company.assign(bob, dash, "Data Engineer");
        dash.getTracker().updateProgress(40);
        dash.addFeedback(new ClientFeedback(LifeCyclePhase.REQUIREMENT_FEASIBILITY, 3, "Need clearer KPIs"));

        return company;
    }

    private static Employee emp(String id, String name, Designation d, Skill... skills) {
        Employee e = new Employee(id, name, d);
        for (Skill s : skills) e.addSkill(s);
        return e;
    }
}
