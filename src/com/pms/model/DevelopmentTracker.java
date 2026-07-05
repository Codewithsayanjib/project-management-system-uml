package com.pms.model;

import com.pms.enums.LifeCyclePhase;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Tracks the development life cycle of a project: requirement feasibility ->
 * development -> testing -> deployment -> maintenance. Stores a completion
 * percentage per phase and knows the current active phase.
 */
public class DevelopmentTracker {
    private final Map<LifeCyclePhase, Integer> phaseCompletion = new LinkedHashMap<>();
    private LifeCyclePhase currentPhase = LifeCyclePhase.REQUIREMENT_FEASIBILITY;

    public DevelopmentTracker() {
        for (LifeCyclePhase phase : LifeCyclePhase.values()) {
            phaseCompletion.put(phase, 0);
        }
    }

    /** Set completion (0-100) for the current phase. */
    public void updateProgress(int percent) {
        if (percent < 0 || percent > 100) {
            throw new IllegalArgumentException("Percent must be 0..100");
        }
        phaseCompletion.put(currentPhase, percent);
    }

    /**
     * Mark the current phase complete and move to the next one.
     * @return true if advanced, false if already at the final phase.
     */
    public boolean advancePhase() {
        phaseCompletion.put(currentPhase, 100);
        LifeCyclePhase[] all = LifeCyclePhase.values();
        int next = currentPhase.ordinal() + 1;
        if (next < all.length) {
            currentPhase = all[next];
            return true;
        }
        return false; // already at MAINTENANCE
    }

    public LifeCyclePhase getCurrentPhase() {
        return currentPhase;
    }

    /** Overall completion averaged across all phases. */
    public double overallCompletion() {
        return phaseCompletion.values().stream()
                .mapToInt(Integer::intValue).average().orElse(0);
    }

    public Map<LifeCyclePhase, Integer> getPhaseCompletion() {
        return phaseCompletion;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Tracker[current=" + currentPhase.getLabel() + "] ");
        phaseCompletion.forEach((phase, pct) ->
                sb.append(String.format("%s=%d%% ", phase.getLabel(), pct)));
        return sb.toString().trim();
    }
}
