package com.pms.model;

import com.pms.enums.SkillLevel;

/**
 * A skill possessed by an employee, e.g. ("Java", EXPERT).
 * Used when assigning the right employee to a project requirement.
 */
public class Skill {
    private final String name;
    private final SkillLevel level;

    public Skill(String name, SkillLevel level) {
        this.name = name;
        this.level = level;
    }

    public String getName() {
        return name;
    }

    public SkillLevel getLevel() {
        return level;
    }

    @Override
    public String toString() {
        return name + " (" + level + ")";
    }
}
