package com.pms.app;

import com.pms.enums.Designation;
import com.pms.enums.LifeCyclePhase;
import com.pms.enums.ProjectStatus;
import com.pms.enums.SkillLevel;
import com.pms.model.*;

/**
 * A tiny self-contained test harness (no external JUnit needed) that exercises
 * the domain model with explicit TEST CASES and prints PASS/FAIL for each.
 */
public class TestRunner {

    private static int passed = 0;
    private static int failed = 0;

    public static void runAll() {
        System.out.println("=".repeat(70));
        System.out.println("TEST CASES");
        System.out.println("=".repeat(70));

        testEmployeeSkillLookup();
        testAssignmentLinksBothSides();
        testAssignmentStartsProject();
        testLifeCycleAdvancesInOrder();
        testProjectCompletesAfterDeployment();
        testBillingScalesWithComplexity();
        testInvalidFeedbackRatingRejected();
        testAverageFeedback();
        testFindEmployeesWithSkill();
        testTotalRevenueAggregates();

        System.out.println("-".repeat(70));
        System.out.printf("RESULT: %d passed, %d failed, %d total%n",
                passed, failed, passed + failed);
        System.out.println("=".repeat(70));
    }

    // ---- individual test cases --------------------------------------------

    private static void testEmployeeSkillLookup() {
        Employee e = new Employee("E-99", "Test", Designation.DEVELOPER);
        e.addSkill(new Skill("Java", SkillLevel.EXPERT));
        check("Employee.hasSkill finds an existing skill", e.hasSkill("java"));
        check("Employee.hasSkill rejects a missing skill", !e.hasSkill("COBOL"));
    }

    private static void testAssignmentLinksBothSides() {
        Client c = new Client("C-T", "T", "t@t.com");
        Project p = new WebApplicationProject("P-T", "T", c);
        Employee e = new Employee("E-T", "T", Designation.DEVELOPER);
        Assignment a = p.assignEmployee(e, "Dev");
        check("Assignment appears on the project", p.getAssignments().contains(a));
        check("Assignment appears on the employee", e.getAssignments().contains(a));
    }

    private static void testAssignmentStartsProject() {
        Client c = new Client("C-T", "T", "t@t.com");
        Project p = new WebApplicationProject("P-T", "T", c);
        check("Project starts NOT_STARTED", p.getStatus() == ProjectStatus.NOT_STARTED);
        p.assignEmployee(new Employee("E-T", "T", Designation.DEVELOPER), "Dev");
        check("Project becomes IN_PROGRESS after first assignment",
                p.getStatus() == ProjectStatus.IN_PROGRESS);
    }

    private static void testLifeCycleAdvancesInOrder() {
        DevelopmentTracker t = new DevelopmentTracker();
        check("Tracker starts at REQUIREMENT_FEASIBILITY",
                t.getCurrentPhase() == LifeCyclePhase.REQUIREMENT_FEASIBILITY);
        t.advancePhase();
        check("Tracker advances to DEVELOPMENT",
                t.getCurrentPhase() == LifeCyclePhase.DEVELOPMENT);
    }

    private static void testProjectCompletesAfterDeployment() {
        Client c = new Client("C-T", "T", "t@t.com");
        Project p = new MobileAppProject("P-T", "T", c);
        for (int i = 0; i < LifeCyclePhase.values().length; i++) {
            p.advanceLifeCycle();
        }
        check("Project is COMPLETED after full life cycle",
                p.getStatus() == ProjectStatus.COMPLETED);
    }

    private static void testBillingScalesWithComplexity() {
        Billing b = new Billing(1000.0, 50.0, 0.0);
        b.logHours(10); // subtotal base = 1500 before complexity
        double web = b.computeTotal(1.2);
        double data = b.computeTotal(1.6);
        check("Higher complexity yields higher bill", data > web);
        check("Web invoice equals 1500 * 1.2 = 1800", Math.abs(web - 1800.0) < 0.001);
    }

    private static void testInvalidFeedbackRatingRejected() {
        boolean threw = false;
        try {
            new ClientFeedback(LifeCyclePhase.TESTING, 9, "bad");
        } catch (IllegalArgumentException ex) {
            threw = true;
        }
        check("Feedback rating outside 1..5 is rejected", threw);
    }

    private static void testAverageFeedback() {
        Client c = new Client("C-T", "T", "t@t.com");
        Project p = new WebApplicationProject("P-T", "T", c);
        p.addFeedback(new ClientFeedback(LifeCyclePhase.TESTING, 4, "ok"));
        p.addFeedback(new ClientFeedback(LifeCyclePhase.DEPLOYMENT, 2, "meh"));
        check("Average feedback is (4+2)/2 = 3.0",
                Math.abs(p.averageFeedbackRating() - 3.0) < 0.001);
    }

    private static void testFindEmployeesWithSkill() {
        Company co = new Company("Co");
        Employee e = co.hireEmployee(new Employee("E-1", "Sky", Designation.SENIOR_DEVELOPER));
        e.addSkill(new Skill("Go", SkillLevel.ADVANCED));
        co.hireEmployee(new Employee("E-2", "NoSkill", Designation.TESTER));
        check("findEmployeesWithSkill returns only matching employees",
                co.findEmployeesWithSkill("Go").size() == 1);
    }

    private static void testTotalRevenueAggregates() {
        Company co = new Company("Co");
        Client c = co.registerClient(new Client("C-1", "Cl", "c@c.com"));
        Project p1 = co.addProject(new WebApplicationProject("P-1", "A", c));
        p1.setBilling(new Billing(1000.0, 0.0, 0.0));
        Project p2 = co.addProject(new DataAnalyticsProject("P-2", "B", c));
        p2.setBilling(new Billing(1000.0, 0.0, 0.0));
        // 1000*1.2 + 1000*1.6 = 2800
        check("totalRevenue aggregates all projects",
                Math.abs(co.totalRevenue() - 2800.0) < 0.001);
    }

    // ---- helper ------------------------------------------------------------
    private static void check(String description, boolean condition) {
        if (condition) {
            passed++;
            System.out.println("  [PASS] " + description);
        } else {
            failed++;
            System.out.println("  [FAIL] " + description);
        }
    }
}
