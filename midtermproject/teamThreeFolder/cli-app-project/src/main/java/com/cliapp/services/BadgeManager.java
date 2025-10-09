package com.cliapp.services;

import com.cliapp.domain.Badge;

public class BadgeManager {
    private BadgeService badgeService;
    private QuestService questService;

    public BadgeManager(BadgeService badgeService, QuestService questService) {
        this.badgeService = badgeService;
        this.questService = questService;
    }

    public void onQuestCompleted(String questId) {
        // Check for new badges when a quest is completed
        boolean questCompleted = questService.markQuestAsCompleted(questId);

        if (questCompleted) {
            for (Badge badge : badgeService.getAllBadges()) {
                if (questId.equals(badge.getQuestId())) {
                    double pointsToAdd = 0.0;
                    if (questService.getQuestById(questId).getDifficultyLevel() == 1) {
                        pointsToAdd = 5.0;
                    } else if (questService.getQuestById(questId).getDifficultyLevel() == 3) {
                        pointsToAdd = 7.5;
                    } else if (questService.getQuestById(questId).getDifficultyLevel() == 5) {
                        pointsToAdd = 10.0;
                    }
                    badgeService.addPointsToBadge(badge.getId(), pointsToAdd);
                }
            }
        }
    }
}
