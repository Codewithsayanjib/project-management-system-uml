package com.pms.model;

/**
 * Billing for a project. Computes the invoice amount from a fixed base cost
 * plus labour (hours * blended hourly rate), scaled by the project's own
 * complexity factor, with tax applied on top.
 */
public class Billing {
    private final double baseCost;
    private double loggedHours;
    private double blendedHourlyRate;
    private final double taxRate;      // e.g. 0.18 for 18%
    private boolean paid = false;

    public Billing(double baseCost, double blendedHourlyRate, double taxRate) {
        this.baseCost = baseCost;
        this.blendedHourlyRate = blendedHourlyRate;
        this.taxRate = taxRate;
    }

    public void logHours(double hours) {
        this.loggedHours += hours;
    }

    public void markPaid() {
        this.paid = true;
    }

    public boolean isPaid() {
        return paid;
    }

    /** Amount before tax, scaled by the given project complexity factor. */
    public double computeSubtotal(double complexityFactor) {
        return (baseCost + loggedHours * blendedHourlyRate) * complexityFactor;
    }

    /** Final invoice amount including tax. */
    public double computeTotal(double complexityFactor) {
        double subtotal = computeSubtotal(complexityFactor);
        return subtotal + subtotal * taxRate;
    }

    public double getLoggedHours() {
        return loggedHours;
    }

    @Override
    public String toString() {
        return String.format("Billing[base=%.2f, hours=%.1f, rate=%.2f, tax=%.0f%%, paid=%s]",
                baseCost, loggedHours, blendedHourlyRate, taxRate * 100, paid);
    }
}
