package com.pms.model;

import com.pms.enums.LifeCyclePhase;
import java.time.LocalDate;

/**
 * Feedback given by the client at a particular life-cycle stage of the project.
 * Captures a 1-5 rating and free-text comments.
 */
public class ClientFeedback {
    private final LifeCyclePhase phase;
    private final int rating;         // 1..5
    private final String comments;
    private final LocalDate givenOn;

    public ClientFeedback(LifeCyclePhase phase, int rating, String comments) {
        if (rating < 1 || rating > 5) {
            throw new IllegalArgumentException("Rating must be between 1 and 5");
        }
        this.phase = phase;
        this.rating = rating;
        this.comments = comments;
        this.givenOn = LocalDate.now();
    }

    public LifeCyclePhase getPhase() {
        return phase;
    }

    public int getRating() {
        return rating;
    }

    public String getComments() {
        return comments;
    }

    @Override
    public String toString() {
        return String.format("[%s] %d/5 - \"%s\" (%s)",
                phase.getLabel(), rating, comments, givenOn);
    }
}
