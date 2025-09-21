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
    private final List<Badge> earnedBadges;
    private final Map<String, Badge> badgeIndex;

    public BadgeService() {
        this.availableBadges = new ArrayList<>();
        this.earnedBadges = new ArrayList<>();
        this.badgeIndex = new HashMap<>();
        initializeDefaultBadges();
    }

    /** Initialize default badges for the system */
    private void initializeDefaultBadges() {
        addBadge("git-starter", "Git Starter", "Complete your first Git quest", 10, null);
        addBadge("quest-master", "Quest Master", "Complete 3 quests", 50, null);
        addBadge("glossary-guru", "Glossary Guru", "Look up 10 commands in glossary", 25, null);
        addBadge("branch-expert", "Branch Expert", "Complete branching quest", 30, null);
        addBadge("merge-master", "Merge Master", "Complete merging quest", 40, null);
        addBadge("remote-pro", "Remote Pro", "Complete remote operations quest", 45, null);
    }

    /** Add a new badge to the system */
    public void addBadge(String id, String name, String description, int points, String questId) {
        Badge badge = new Badge(id, name, description, points, questId);
        availableBadges.add(badge);
        badgeIndex.put(id, badge);
    }

    /** Award a badge to the user */
    public boolean awardBadge(String badgeId) {
        Badge badge = badgeIndex.get(badgeId);
        if (badge != null && !earnedBadges.contains(badge)) {
            Badge earnedBadge =
                    new Badge(
                            badge.getId(),
                            badge.getName(),
                            badge.getDescription(),
                            badge.getPointsEarned(),
                            badge.getQuestId());
            earnedBadge.setDateEarned(LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE));
            earnedBadges.add(earnedBadge);
            return true;
        }
        return false;
    }

    /** Check if a badge has been earned */
    public boolean hasBadge(String badgeId) {
        return earnedBadges.stream().anyMatch(badge -> badge.getId().equals(badgeId));
    }

    /** Get all earned badges */
    public List<Badge> getEarnedBadges() {
        return new ArrayList<>(earnedBadges);
    }

    /** Get all available badges */
    public List<Badge> getAllBadges() {
        return new ArrayList<>(availableBadges);
    }

    /** Get badge by ID */
    public Badge getBadgeById(String badgeId) {
        return badgeIndex.get(badgeId);
    }

    /** Get total points earned */
    public int getTotalPointsEarned() {
        return earnedBadges.stream().mapToInt(Badge::getPointsEarned).sum();
    }

    /** Get badges that could be earned by completing a specific quest */
    public List<Badge> getBadgesForQuest(String questId) {
        return availableBadges.stream()
                .filter(badge -> questId.equals(badge.getQuestId()))
                .filter(badge -> !earnedBadges.contains(badge))
                .collect(ArrayList::new, (list, badge) -> list.add(badge), ArrayList::addAll);
    }

    /** Check for badge eligibility based on quest completion */
    public List<Badge> checkBadgeEligibility(int completedQuests, int glossaryLookups) {
        List<Badge> eligibleBadges = new ArrayList<>();

        // Check quest completion badges
        if (completedQuests >= 1 && !hasBadge("git-starter")) {
            Badge starter = getBadgeById("git-starter");
            if (starter != null) eligibleBadges.add(starter);
        }

        if (completedQuests >= 3 && !hasBadge("quest-master")) {
            Badge master = getBadgeById("quest-master");
            if (master != null) eligibleBadges.add(master);
        }

        // Check glossary usage badge
        if (glossaryLookups >= 10 && !hasBadge("glossary-guru")) {
            Badge guru = getBadgeById("glossary-guru");
            if (guru != null) eligibleBadges.add(guru);
        }

        return eligibleBadges;
    }

    // Overload methods expected by tests (gracefully ignore session specifics for now)
    public List<Badge> getEarnedBadges(UserSession session) {
        if (session != null) {
            return new ArrayList<>(session.getEarnedBadges());
        }
        return getEarnedBadges();
    }

    public List<Badge> getAvailableBadges(UserSession session) {
        return getAllBadges();
    }

    public List<Badge> checkForNewBadges(UserSession session, String questId) {
        List<Badge> newlyAwarded = new ArrayList<>();
        if (session == null || questId == null || questId.trim().isEmpty()) {
            return newlyAwarded; // empty
        }
        // Simple heuristic: if quest completed in session, attempt awarding quest-specific badges
        int completed = session.getCompletedQuestIds().size();
        int glossaryLookups = session.getGlossaryLookupCount();
        for (Badge badge : checkBadgeEligibility(completed, glossaryLookups)) {
            if (!hasBadge(badge.getId())) {
                if (awardBadge(badge.getId())) {
                    newlyAwarded.add(getBadgeById(badge.getId()));
                }
            }
        }
        return newlyAwarded;
    }
}
