package com.cliapp.services;

import com.cliapp.domain.Badge;
import com.cliapp.models.UserSession;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * Service class for managing badge operations SOLID: Single Responsibility - handles badge business
 * logic
 */
public class BadgeService {

    private final List<Badge> availableBadges;
    private final Map<String, Badge> badgeIndex;

    public BadgeService() {
        this.availableBadges = new ArrayList<>();
        this.badgeIndex = new HashMap<>();
        initializeDefaultBadges();
    }

    /** Initialize default badges for the system */
    private void initializeDefaultBadges() {
        addBadge(
                "git-basics",
                "Git Fundamentals",
                "Complete Git Fundamnetals Lesson Module",
                0,
                20,
                "git-basics");
        addBadge(
                "git-branching",
                "Git Branching & Merging",
                "Complete Git Branching & Merginging Lesson Module",
                0,
                30,
                "git-branching");
        addBadge(
                "git-remote",
                "Remote Repository Operations",
                "Complete Remote Repository Operations Lesson Module",
                0,
                50,
                "git-remote");
    }

    /** Add a new badge to the system */
    public void addBadge(
            String id, String name, String description, int points, int maxPoints, String questId) {
        Badge badge = new Badge(id, name, description, points, maxPoints, questId);
        availableBadges.add(badge);
        badgeIndex.put(id, badge);
    }

    public void addPointsToBadge(String badgeId, double points) {
        if (badgeId == null || points <= 0) {
            return;
        }
        Badge badge = badgeIndex.get(badgeId);
        if (badge != null) {
            double newPoints = Math.min(badge.getPointsEarned() + points, badge.getMaxPoints());
            badge.addPoints(newPoints);
            if (newPoints >= badge.getMaxPoints()) {
                badge.setDateEarned(LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE));
            }
        }
    }

    /** Get all available badges */
    public List<Badge> getAllBadges() {
        return new ArrayList<>(availableBadges);
    }

    /** Get badge by ID */
    public Badge getBadgeById(String badgeId) {
        return badgeIndex.get(badgeId);
    }

    public List<Badge> getAvailableBadges(UserSession session) {
        return getAllBadges();
    }
}
