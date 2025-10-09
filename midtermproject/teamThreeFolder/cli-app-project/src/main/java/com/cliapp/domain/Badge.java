package com.cliapp.domain;

/**
 * Badge domain object representing user achievements SOLID: Single Responsibility - represents user
 * achievements and points
 */
public class Badge {

    private String id;
    private String name;
    private String description;
    private double pointsEarned;
    private double maxPoints;
    private String questId;
    private String dateEarned;

    public Badge() {
        // Default constructor
    }

    public Badge(
            String id,
            String name,
            String description,
            double pointsEarned,
            double maxPoints,
            String questId) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.pointsEarned = pointsEarned;
        this.maxPoints = maxPoints;
        this.questId = questId;
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPointsEarned() {
        return pointsEarned;
    }

    public void setPointsEarned(double pointsEarned) {
        this.pointsEarned = pointsEarned;
    }

    public void addPoints(double points) {
        this.pointsEarned = Math.min(this.pointsEarned + points, this.maxPoints);
    }

    public double getMaxPoints() {
        return maxPoints;
    }

    public String getQuestId() {
        return questId;
    }

    public void setQuestId(String questId) {
        this.questId = questId;
    }

    public String getDateEarned() {
        return dateEarned;
    }

    public void setDateEarned(String dateEarned) {
        this.dateEarned = dateEarned;
    }

    /** Format badge for display */
    public String formatForDisplay() {
        // Use %.1f for double points, or %s for string points
        String pointsStr =
                pointsEarned == (int) pointsEarned
                        ? String.format("%d", (int) pointsEarned)
                        : String.format("%.1f", pointsEarned);
        return String.format("%s - %s points (%s)", name, pointsStr, description);
    }

    @Override
    public String toString() {
        return "Badge{"
                + "id='"
                + id
                + '\''
                + ", name='"
                + name
                + '\''
                + ", description='"
                + description
                + '\''
                + ", pointsEarned="
                + pointsEarned
                + ", questId='"
                + questId
                + '\''
                + ", dateEarned='"
                + dateEarned
                + '\''
                + '}';
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Badge badge = (Badge) obj;
        return id != null ? id.equals(badge.id) : badge.id == null;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
