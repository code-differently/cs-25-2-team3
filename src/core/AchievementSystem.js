/**
 * Achievement System - Manages badges, achievements, and gamification
 * 
 * Implements user story:
 * - B7: Earn experience points, achieve skill levels, and unlock achievement badges
 */

import fs from 'fs/promises';
import path from 'path';
import os from 'os';

export class AchievementSystem {
  constructor(config, logger) {
    this.config = config;
    this.logger = logger;
    this.achievementsPath = path.join(os.homedir(), '.git-trainer', 'achievements.json');
    this.definitions = [];
    this.userAchievements = [];
  }

  /**
   * Initialize achievement system
   */
  async initialize() {
    await this.loadAchievementDefinitions();
    await this.loadUserAchievements();
    this.logger.debug('Achievement system initialized');
  }

  /**
   * Load achievement definitions
   */
  async loadAchievementDefinitions() {
    this.definitions = [
      // Beginner Achievements
      {
        id: 'first_commit',
        name: 'Git Beginner',
        description: 'Complete your first commit',
        icon: '🌱',
        points: 10,
        category: 'basics',
        requirements: {
          type: 'command',
          command: 'git commit',
          count: 1
        }
      },
      {
        id: 'git_basics_complete',
        name: 'Foundation Master',
        description: 'Complete the Git Basics module',
        icon: '🏗️',
        points: 50,
        category: 'modules',
        requirements: {
          type: 'module_complete',
          module: 'git_basics'
        }
      },
      {
        id: 'ten_commits',
        name: 'Commit Champion',
        description: 'Make 10 successful commits',
        icon: '💪',
        points: 25,
        category: 'progression',
        requirements: {
          type: 'command',
          command: 'git commit',
          count: 10
        }
      },

      // Intermediate Achievements
      {
        id: 'first_branch',
        name: 'Branch Explorer',
        description: 'Create your first branch',
        icon: '🌿',
        points: 20,
        category: 'branching',
        requirements: {
          type: 'command',
          command: 'git branch',
          count: 1
        }
      },
      {
        id: 'first_merge',
        name: 'Merge Master',
        description: 'Successfully merge your first branch',
        icon: '🔗',
        points: 30,
        category: 'branching',
        requirements: {
          type: 'command',
          command: 'git merge',
          count: 1
        }
      },
      {
        id: 'working_changes_complete',
        name: 'Change Detective',
        description: 'Complete the Working with Changes module',
        icon: '🔍',
        points: 50,
        category: 'modules',
        requirements: {
          type: 'module_complete',
          module: 'working_with_changes'
        }
      },

      // Advanced Achievements
      {
        id: 'branching_complete',
        name: 'Branch Ninja',
        description: 'Complete the Branching Fundamentals module',
        icon: '🥷',
        points: 75,
        category: 'modules',
        requirements: {
          type: 'module_complete',
          module: 'branching_fundamentals'
        }
      },
      {
        id: 'all_modules_complete',
        name: 'Git Master',
        description: 'Complete all learning modules',
        icon: '👑',
        points: 200,
        category: 'completion',
        requirements: {
          type: 'all_modules_complete'
        }
      },

      // Special Achievements
      {
        id: 'speed_demon',
        name: 'Speed Demon',
        description: 'Complete a module in under 10 minutes',
        icon: '⚡',
        points: 40,
        category: 'special',
        requirements: {
          type: 'module_time',
          maxTime: 600000 // 10 minutes in milliseconds
        }
      },
      {
        id: 'perfectionist',
        name: 'Perfectionist',
        description: 'Complete a module with 100% accuracy',
        icon: '💎',
        points: 60,
        category: 'special',
        requirements: {
          type: 'module_accuracy',
          minAccuracy: 100
        }
      },
      {
        id: 'streak_master',
        name: 'Streak Master',
        description: 'Learn for 7 consecutive days',
        icon: '🔥',
        points: 100,
        category: 'engagement',
        requirements: {
          type: 'streak',
          days: 7
        }
      },
      {
        id: 'hundred_commands',
        name: 'Command Expert',
        description: 'Execute 100 Git commands successfully',
        icon: '⌨️',
        points: 80,
        category: 'progression',
        requirements: {
          type: 'total_commands',
          count: 100
        }
      }
    ];

    this.logger.debug(`Loaded ${this.definitions.length} achievement definitions`);
  }

  /**
   * Load user's earned achievements
   */
  async loadUserAchievements() {
    try {
      const data = await fs.readFile(this.achievementsPath, 'utf8');
      this.userAchievements = JSON.parse(data);
      this.logger.debug(`Loaded ${this.userAchievements.length} user achievements`);
    } catch (error) {
      if (error.code === 'ENOENT') {
        this.userAchievements = [];
        await this.saveUserAchievements();
      } else {
        this.logger.error('Failed to load user achievements:', error.message);
        this.userAchievements = [];
      }
    }
  }

  /**
   * Save user achievements
   */
  async saveUserAchievements() {
    try {
      await fs.mkdir(path.dirname(this.achievementsPath), { recursive: true });
      await fs.writeFile(this.achievementsPath, JSON.stringify(this.userAchievements, null, 2));
      this.logger.debug('User achievements saved');
    } catch (error) {
      this.logger.error('Failed to save user achievements:', error.message);
    }
  }

  /**
   * Check for new achievements based on activity
   */
  async checkAchievements(activityData) {
    const newAchievements = [];

    for (const achievement of this.definitions) {
      if (this.hasAchievement(achievement.id)) {
        continue; // Already earned
      }

      if (await this.checkAchievementRequirement(achievement, activityData)) {
        await this.awardAchievement(achievement.id);
        newAchievements.push(achievement);
      }
    }

    if (newAchievements.length > 0) {
      await this.notifyNewAchievements(newAchievements);
    }

    return newAchievements;
  }

  /**
   * Check if achievement requirement is met
   */
  async checkAchievementRequirement(achievement, activityData) {
    const req = achievement.requirements;

    switch (req.type) {
      case 'command':
        return this.checkCommandRequirement(req, activityData);
      
      case 'module_complete':
        return activityData.moduleCompleted === req.module;
      
      case 'all_modules_complete':
        return activityData.allModulesComplete;
      
      case 'module_time':
        return activityData.moduleTime && activityData.moduleTime <= req.maxTime;
      
      case 'module_accuracy':
        return activityData.accuracy && activityData.accuracy >= req.minAccuracy;
      
      case 'streak':
        return activityData.currentStreak >= req.days;
      
      case 'total_commands':
        return activityData.totalCommands >= req.count;
      
      default:
        this.logger.warn(`Unknown achievement requirement type: ${req.type}`);
        return false;
    }
  }

  /**
   * Check command-based achievement requirement
   */
  checkCommandRequirement(requirement, activityData) {
    if (activityData.commands && activityData.commands[requirement.command]) {
      return activityData.commands[requirement.command] >= requirement.count;
    }
    return false;
  }

  /**
   * Award achievement to user
   */
  async awardAchievement(achievementId) {
    if (this.hasAchievement(achievementId)) {
      return; // Already earned
    }

    const achievement = this.definitions.find(a => a.id === achievementId);
    if (!achievement) {
      this.logger.error(`Achievement not found: ${achievementId}`);
      return;
    }

    const userAchievement = {
      id: achievementId,
      earnedDate: new Date().toISOString(),
      points: achievement.points
    };

    this.userAchievements.push(userAchievement);
    await this.saveUserAchievements();

    this.logger.success(`Achievement earned: ${achievement.name} (${achievement.points} points)`);
  }

  /**
   * Check if user has specific achievement
   */
  hasAchievement(achievementId) {
    return this.userAchievements.some(a => a.id === achievementId);
  }

  /**
   * Get all achievement definitions with earned status
   */
  async getAllAchievements() {
    return this.definitions.map(achievement => ({
      ...achievement,
      earned: this.hasAchievement(achievement.id),
      earnedDate: this.getEarnedDate(achievement.id)
    }));
  }

  /**
   * Get user's earned achievements
   */
  getUserAchievements() {
    return this.userAchievements.map(userAch => {
      const definition = this.definitions.find(def => def.id === userAch.id);
      return {
        ...userAch,
        ...definition
      };
    });
  }

  /**
   * Get earned date for achievement
   */
  getEarnedDate(achievementId) {
    const userAchievement = this.userAchievements.find(a => a.id === achievementId);
    return userAchievement ? userAchievement.earnedDate : null;
  }

  /**
   * Get total points earned
   */
  getTotalPoints() {
    return this.userAchievements.reduce((total, achievement) => {
      return total + (achievement.points || 0);
    }, 0);
  }

  /**
   * Get achievements by category
   */
  getAchievementsByCategory(category) {
    return this.definitions.filter(a => a.category === category);
  }

  /**
   * Get achievement progress for specific achievement
   */
  getAchievementProgress(achievementId, currentStats) {
    const achievement = this.definitions.find(a => a.id === achievementId);
    if (!achievement || this.hasAchievement(achievementId)) {
      return null;
    }

    const req = achievement.requirements;
    let current = 0;
    let target = 0;

    switch (req.type) {
      case 'command':
        current = currentStats.commands?.[req.command] || 0;
        target = req.count;
        break;
      
      case 'total_commands':
        current = currentStats.totalCommands || 0;
        target = req.count;
        break;
      
      case 'streak':
        current = currentStats.currentStreak || 0;
        target = req.days;
        break;
      
      default:
        return null;
    }

    return {
      current,
      target,
      percentage: Math.min(100, (current / target) * 100)
    };
  }

  /**
   * Notify user of new achievements
   */
  async notifyNewAchievements(achievements) {
    if (!this.config.get('notifications.achievements', true)) {
      return;
    }

    for (const achievement of achievements) {
      console.log(`\n🏆 ${achievement.icon} ACHIEVEMENT UNLOCKED! ${achievement.icon}`);
      console.log(`   ${achievement.name}`);
      console.log(`   ${achievement.description}`);
      console.log(`   +${achievement.points} points\n`);
    }
  }

  /**
   * Get next achievable achievements
   */
  getNextAchievements(currentStats, limit = 3) {
    const unearned = this.definitions.filter(a => !this.hasAchievement(a.id));
    
    // Sort by how close user is to earning them
    const withProgress = unearned.map(achievement => {
      const progress = this.getAchievementProgress(achievement.id, currentStats);
      return {
        ...achievement,
        progress: progress || { current: 0, target: 1, percentage: 0 }
      };
    }).sort((a, b) => b.progress.percentage - a.progress.percentage);
    
    return withProgress.slice(0, limit);
  }

  /**
   * Reset all achievements
   */
  async reset() {
    this.userAchievements = [];
    await this.saveUserAchievements();
    this.logger.info('All achievements reset');
  }

  /**
   * Export achievements data
   */
  async exportAchievements() {
    return {
      definitions: this.definitions,
      userAchievements: this.userAchievements,
      totalPoints: this.getTotalPoints(),
      exportDate: new Date().toISOString()
    };
  }

  /**
   * Import achievements data
   */
  async importAchievements(data) {
    if (data.userAchievements) {
      this.userAchievements = data.userAchievements;
      await this.saveUserAchievements();
      this.logger.info('Achievements imported successfully');
    }
  }
}
