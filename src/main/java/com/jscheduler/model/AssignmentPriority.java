package com.jscheduler.model;

public enum AssignmentPriority {
    LOW("Low"),
    MEDIUM("Medium"),
    HIGH("High"),
    URGENT("Urgent");

    private final String displayName;

    AssignmentPriority(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public static AssignmentPriority fromString(String text) {
        if (text == null) {
            return MEDIUM;
        }
        for (AssignmentPriority priority : AssignmentPriority.values()) {
            if (priority.displayName.equalsIgnoreCase(text) || priority.name().equalsIgnoreCase(text)) {
                return priority;
            }
        }
        return MEDIUM;
    }

    @Override
    public String toString() {
        return displayName;
    }
}

