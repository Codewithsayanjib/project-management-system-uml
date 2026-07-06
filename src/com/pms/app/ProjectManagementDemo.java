package com.pms.app;

import com.pms.enums.Designation;
import com.pms.enums.LifeCyclePhase;
import com.pms.enums.ProjectStatus;
import com.pms.enums.SkillLevel;
import com.pms.model.*;

/**
 * Driver program for the Project Management System.
 *
 * Part A  - builds a realistic scenario and prints a full report (the
 *           "output screenshot" deliverable).
 * Part B  - a set of self-checking TEST CASES that assert the behaviour of
 *           the domain model and print PASS/FAIL for each.
 */
public class ProjectManagementDemo {

    public static void main(String[] args) {
        runScenario();
        System.out.println();
        TestRunner.runAll();
    }

    // ------------------------------------------------------------------ Part A
    private static void runScenario() {
        banner("PROJECT MANAGEMENT SYSTEM - DEMO SCENARIO");

        Company company = new Company("Acme Software Pvt. Ltd.");

        // --- Clients
        Client acme = company.registerClient(new Client("C-01", "RetailKart", "cto@retailkart.com"));
        Client medico = company.registerClient(new Client("C-02", "MediCare Labs", "it@medicare.com"));

        // --- Employees (with designations + skills)
        Employee alice = company.hireEmployee(new Employee("E-01", "Alice", Designation.PROJECT_MANAGER));
        alice.addSkill(new Skill("Planning", SkillLevel.EXPERT));

        Employee bob = company.hireEmployee(new Employee("E-02", "Bob", Designation.SENIOR_DEVELOPER));
        bob.addSkill(new Skill("Java", SkillLevel.EXPERT));
        bob.addSkill(new Skill("React", SkillLevel.ADVANCED));

        Employee carol = company.hireEmployee(new Employee("E-03", "Carol", Designation.DEVELOPER));
        carol.addSkill(new Skill("Kotlin", SkillLevel.ADVANCED));

        Employee dan = company.hireEmployee(new Employee("E-04", "Dan", Designation.TESTER));
        dan.addSkill(new Skill("Selenium", SkillLevel.INTERMEDIATE));

        Employee eve = company.hireEmployee(new Employee("E-05", "Eve", Designation.DEVOPS_ENGINEER));
        eve.addSkill(new Skill("AWS", SkillLevel.EXPERT));

        // --- Project 1: Web application for RetailKart
        WebApplicationProject shop = company.addProject(
                new WebApplicationProject("P-01", "RetailKart Storefront", acme));
        ProjectRequirement req1 = new ProjectRequirement("E-Commerce", "Online storefront with cart & payments");
        req1.addItem("Product catalogue");
        req1.addItem("Shopping cart");
        req1.addItem("Payment gateway");
        req1.markFeasible(true);
        shop.setRequirement(req1);
        shop.setBilling(new Billing(5000.0, 55.0, 0.18));

        // assign employees (which employee handles which project)
        company.assign(alice, shop, "Project Manager");
        company.assign(bob, shop, "Lead Developer");
        company.assign(dan, shop, "QA Engineer");

        // walk the life cycle with client feedback at each stage
        shop.getTracker().updateProgress(100);
        shop.addFeedback(new ClientFeedback(LifeCyclePhase.REQUIREMENT_FEASIBILITY, 5, "Scope looks great"));
        shop.advanceLifeCycle(); // -> DEVELOPMENT
        shop.getBilling().logHours(120);
        shop.addFeedback(new ClientFeedback(LifeCyclePhase.DEVELOPMENT, 4, "Good progress on cart"));
        shop.advanceLifeCycle(); // -> TESTING
        shop.getBilling().logHours(40);
        shop.addFeedback(new ClientFeedback(LifeCyclePhase.TESTING, 4, "Few bugs, fixed quickly"));
        shop.advanceLifeCycle(); // -> DEPLOYMENT
        shop.getBilling().logHours(20);
        shop.addFeedback(new ClientFeedback(LifeCyclePhase.DEPLOYMENT, 5, "Smooth go-live"));
        shop.advanceLifeCycle(); // -> MAINTENANCE (COMPLETED)

        // --- Project 2: Mobile app for MediCare
        MobileAppProject app = company.addProject(
                new MobileAppProject("P-02", "MediCare Patient App", medico));
        ProjectRequirement req2 = new ProjectRequirement("Healthcare", "Patient appointment & records app");
        req2.addItem("Appointment booking");
        req2.addItem("Lab report viewer");
        req2.markFeasible(true);
        app.setRequirement(req2);
        app.setBilling(new Billing(7000.0, 60.0, 0.18));
        company.assign(carol, app, "Android Developer");
        company.assign(eve, app, "DevOps");
        app.getBilling().logHours(90);
        app.addFeedback(new ClientFeedback(LifeCyclePhase.REQUIREMENT_FEASIBILITY, 4, "Clear requirements"));

        // ----- print report
        printReport(company);
    }

    private static void printReport(Company company) {
        System.out.println("Company: " + company.getName());
        System.out.println("Total clients : " + company.getClients().size());
        System.out.println("Total employees: " + company.getEmployees().size());
        System.out.println("Total projects : " + company.getProjects().size());

        for (Project p : company.getProjects()) {
            System.out.println("\n" + "-".repeat(70));
            System.out.println(p);
            System.out.println("  Requirement : " + p.getRequirement());
            System.out.println("  Life cycle  : " + p.getTracker());
            System.out.printf ("  Completion  : %.0f%%%n", p.getTracker().overallCompletion());
            System.out.println("  Assignments (which employee handles this project):");
            for (Assignment a : p.getAssignments()) {
                System.out.println("      - " + a);
            }
            System.out.println("  Client feedback at each stage:");
            for (ClientFeedback f : p.getFeedbackLog()) {
                System.out.println("      - " + f);
            }
            System.out.printf ("  Avg feedback: %.2f / 5%n", p.averageFeedbackRating());
            System.out.println("  " + p.getBilling());
            System.out.printf ("  Invoice (incl. tax, complexity x%.1f): %.2f%n",
                    p.complexityFactor(), p.invoiceAmount());
        }

        System.out.println("\n" + "=".repeat(70));
        System.out.printf("TOTAL COMPANY REVENUE: %.2f%n", company.totalRevenue());
        System.out.println("=".repeat(70));
    }

    private static void banner(String title) {
        System.out.println("=".repeat(70));
        System.out.println(title);
        System.out.println("=".repeat(70));
    }
}
