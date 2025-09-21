package com.cliapp.commands;

import com.cliapp.domain.Badge;
import com.cliapp.services.BadgeService;
import java.util.ArrayList;
import java.util.List;

/**
 * Command to display user badges and achievements SOLID: Single Responsibility - handles only badge
 * display
 */
public class BadgeCommand implements Command {

    private final BadgeService badgeService;

    public BadgeCommand(BadgeService badgeService) {
        this.badgeService = badgeService;
    }

    @Override
    public void execute(String[] args) {
        try {
            System.out.println("\n=== Your Achievement Badges ===");

            List<Badge> earnedBadges = badgeService.getEarnedBadges();
            List<Badge> allBadges = badgeService.getAllBadges();
            int totalPoints = badgeService.getTotalPointsEarned();

            // Handle null badge lists
            if (earnedBadges == null) {
                earnedBadges = new ArrayList<>();
            }
            if (allBadges == null) {
                allBadges = new ArrayList<>();
            }

            // Display summary
            System.out.printf("Badges Earned: %d/%d\n", earnedBadges.size(), allBadges.size());
            System.out.printf("Total Points: %d\n", totalPoints);
            System.out.println("─".repeat(50));

            if (earnedBadges.isEmpty()) {
                System.out.println("🏆 No badges earned yet!");
                System.out.println(
                        "Complete quests and explore the glossary to earn your first badge!");
            } else {
                System.out.println("🏆 EARNED BADGES:");
                for (Badge badge : earnedBadges) {
                    if (badge != null) {
                        System.out.println("✅ " + badge.formatForDisplay());
                        System.out.println();
                    }
                }
            }

            // Show available badges not yet earned
            List<Badge> availableBadges = new ArrayList<>();
            for (Badge badge : allBadges) {
                if (badge != null && !earnedBadges.contains(badge)) {
                    availableBadges.add(badge);
                }
            }

            if (!availableBadges.isEmpty()) {
                System.out.println("🎯 AVAILABLE BADGES:");
                for (Badge badge : availableBadges) {
                    if (badge != null) {
                        System.out.println(
                                "⬜ "
                                        + badge.getName()
                                        + " - "
                                        + badge.getPointsEarned()
                                        + " points");
                        System.out.println("   " + badge.getDescription());
                        System.out.println();
                    }
                }
            }

            // Show progress motivation
            if (earnedBadges.size() < allBadges.size()) {
                double progress = (double) earnedBadges.size() / allBadges.size() * 100;
                System.out.printf("Progress: %.1f%% complete\n", progress);
                System.out.println(
                        "Keep going! Complete more quests to unlock additional badges! 🚀");
            } else {
                System.out.println("🎉 Congratulations! You've earned all available badges!");
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
