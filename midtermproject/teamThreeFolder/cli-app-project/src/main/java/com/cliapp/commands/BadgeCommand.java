package com.cliapp.commands;

import com.cliapp.domain.Badge;
import com.cliapp.services.BadgeManager;
import com.cliapp.services.BadgeService;
import java.util.ArrayList;
import java.util.List;

/**
 * Command to display user badges and achievements SOLID: Single Responsibility - handles only badge
 * display
 */
public class BadgeCommand implements Command {

    private final BadgeService badgeService;
    private final BadgeManager badgeManager;

    public BadgeCommand(BadgeService badgeService) {
        this.badgeService = badgeService;
        this.badgeManager = null;
    }

    public BadgeCommand(BadgeService badgeService, BadgeManager badgeManager) {
        this.badgeService = badgeService;
        this.badgeManager = badgeManager;
    }

    @Override
    public void execute(String[] args) {
        try {
            System.out.println("\n=== Your Achievement Badges ===");

            List<Badge> allBadges = badgeService.getAllBadges();
            if (allBadges == null) {
                allBadges = new ArrayList<>();
            }

            if (allBadges.isEmpty()) {
                System.out.println("🏆 No badges available yet!");
                System.out.println(
                        "Complete quests and explore the glossary to earn your first badge!");
            } else {
                System.out.println("🏆 BADGES:");
                for (Badge badge : allBadges) {
                    if (badge != null) {
                        double pointsEarned = badge.getPointsEarned();
                        double maxPoints = badge.getMaxPoints();
                        double progress =
                                maxPoints > 0 ? (double) pointsEarned / maxPoints * 100 : 0.0;
                        System.out.printf(
                                "%s: %.1f/%.1f points (%.1f%% complete)\n",
                                badge.getName(), pointsEarned, maxPoints, progress);
                        System.out.println("   " + badge.getDescription());
                        System.out.println();
                    }
                }
            }

            // Show overall progress motivation
            double totalPoints = 0;
            double totalMaxPoints = 0;
            for (Badge badge : allBadges) {
                if (badge != null) {
                    totalPoints += badge.getPointsEarned();
                    totalMaxPoints += badge.getMaxPoints();
                }
            }
            double overallProgress =
                    totalMaxPoints > 0 ? (double) totalPoints / totalMaxPoints * 100 : 0.0;
            System.out.printf(
                    "Overall Progress: %.1f/%.1f points (%.1f%% complete)\n",
                    totalPoints, totalMaxPoints, overallProgress);
            if (overallProgress < 100.0) {
                System.out.println(
                        "Keep going! Complete more quests to unlock additional points and badges! 🚀");
            } else {
                System.out.println("🎉 Congratulations! You've earned all available badge points!");
            }
        } catch (Exception e) {
            System.err.println("Error displaying badges: " + e.getMessage());
            System.out.println("Unable to load badge information at this time.");
        }
    }

    @Override
    public String getDescription() {
        return "View your earned badges and achievements";
    }

    @Override
    public String getName() {
        return "badges";
    }

    @Override
    public String getUsage() {
        return "badges";
    }

    @Override
    public boolean validateArgs(String[] args) {
        return true; // Badge command doesn't require arguments
    }
}
