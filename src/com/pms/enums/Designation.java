package com.pms.enums;

/**
 * Designation of an employee inside the company. Each designation carries a
 * standard hourly cost that the billing module uses to compute project cost.
 */
public enum Designation {
    PROJECT_MANAGER(80.0),
    BUSINESS_ANALYST(60.0),
    SENIOR_DEVELOPER(70.0),
    DEVELOPER(45.0),
    TESTER(40.0),
    DEVOPS_ENGINEER(65.0);

    private final double hourlyRate;

    Designation(double hourlyRate) {
        this.hourlyRate = hourlyRate;
    }

    public double getHourlyRate() {
        return hourlyRate;
    }
}
