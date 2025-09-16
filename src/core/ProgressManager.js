/**
 * Progress Manager - Handles user progress tracking and session management
 * 
 * Implements user stories:
 * - A2: Resume previous session and continue from where left off
 * - D11: Learning progress automatically saved locally
 */

import fs from 'fs/promises';
import path from 'path';
import os from 'os';

export class ProgressManager {
  constructor(config, logger) {
    this.config = config;
    this.logger = logger;
    this.progressPath = path.join(os.homedir(), '.git-trainer', 'progress.json');
    this.sessionsPath = path.join(os.homedir(), '.git-trainer', 'sessions.json');
    this.progress = {
      version: '1.0.0',
      user: {
        level: 1,
        totalPoints: 0,
        currentStreak: 0,
        lastActivity: null
      },
      modules: {},
      sessions: [],
      achievements: [],
      statistics: {
        totalCommands: 0,
        successfulCommands: 0,
        timeSpent: 0,
        modulesStarted: 0,
        modulesCompleted: 0
      }
    };
  }

  /**
   * Initialize progress manager
   */
  async initialize() {
    await this.ensureDirectories();
    await this.loadProgress();
    this.logger.debug('Progress manager initialized');
  }

  /**
   * Ensure required directories exist
   */
  async ensureDirectories() {
    const dataDir = path.dirname(this.progressPath);
    await fs.mkdir(dataDir, { recursive: true });
  }

  /**
   * Load progress from file
   */
  async loadProgress() {
    try {
      const data = await fs.readFile(this.progressPath, 'utf8');
      const loadedProgress = JSON.parse(data);
      
      // Merge with defaults to ensure all properties exist
      this.progress = { ...this.progress, ...loadedProgress };
      
      this.logger.debug('Progress loaded successfully');
    } catch (error) {
      if (error.code === 'ENOENT') {
        // No progress file exists yet, start fresh
        await this.saveProgress();
        this.logger.debug('Created new progress file');
      } else {
        this.logger.error('Failed to load progress:', error.message);
        throw error;
      }
    }
  }

  /**
   * Save progress to file
   */
  async saveProgress() {
    try {
      this.progress.user.lastActivity = new Date().toISOString();
      await fs.writeFile(this.progressPath, JSON.stringify(this.progress, null, 2));
      this.logger.debug('Progress saved successfully');
    } catch (error) {
      this.logger.error('Failed to save progress:', error.message);
      throw error;
    }
  }

  /**
   * Get progress summary for UI display
   */
  async getProgressSummary() {
    const totalModules = Object.keys(this.progress.modules).length;
    const completedModules = Object.values(this.progress.modules)
      .filter(module => module.completed).length;

    return {
      level: this.progress.user.level,
      points: this.progress.user.totalPoints,
      total: Math.max(totalModules, 3), // At least show 3 modules
      completed: completedModules,
      lastActivity: this.progress.user.lastActivity ? 
        new Date(this.progress.user.lastActivity).toLocaleDateString() : null
    };
  }

  /**
   * Get detailed progress information
   */
  async getDetailedProgress() {
    return {
      user: { ...this.progress.user },
      modules: { ...this.progress.modules },
      statistics: { ...this.progress.statistics },
      totalModules: Object.keys(this.progress.modules).length,
      modulesCompleted: Object.values(this.progress.modules)
        .filter(module => module.completed).length,
      totalPoints: this.progress.user.totalPoints,
      currentStreak: this.progress.user.currentStreak
    };
  }

  /**
   * Update module progress
   */
  async updateModuleProgress(moduleName, result) {
    const moduleId = this.getModuleId(moduleName);
    
    if (!this.progress.modules[moduleId]) {
      this.progress.modules[moduleId] = {
        id: moduleId,
        name: moduleName,
        started: new Date().toISOString(),
        completed: false,
        attempts: 0,
        bestScore: 0,
        totalTime: 0,
        tasks: {}
      };
      this.progress.statistics.modulesStarted++;
    }

    const module = this.progress.modules[moduleId];
    module.attempts++;
    module.lastAttempt = new Date().toISOString();

    if (result.completed) {
      module.completed = true;
      module.completedDate = new Date().toISOString();
      module.bestScore = Math.max(module.bestScore, result.score || 0);
      
      if (!module.firstCompletion) {
        module.firstCompletion = new Date().toISOString();
        this.progress.statistics.modulesCompleted++;
      }
    }

    // Update task progress
    if (result.tasks) {
      Object.assign(module.tasks, result.tasks);
    }

    // Update statistics
    if (result.commands) {
      this.progress.statistics.totalCommands += result.commands.total || 0;
      this.progress.statistics.successfulCommands += result.commands.successful || 0;
    }

    if (result.timeSpent) {
      module.totalTime += result.timeSpent;
      this.progress.statistics.timeSpent += result.timeSpent;
    }

    // Update user points and level
    if (result.pointsEarned) {
      this.progress.user.totalPoints += result.pointsEarned;
      await this.updateUserLevel();
    }

    await this.saveProgress();
    this.logger.info(`Progress updated for module: ${moduleName}`);
  }

  /**
   * Update user level based on points
   */
  async updateUserLevel() {
    const pointsForNextLevel = this.progress.user.level * 100;
    if (this.progress.user.totalPoints >= pointsForNextLevel) {
      this.progress.user.level++;
      this.logger.info(`Level up! Now level ${this.progress.user.level}`);
      return true;
    }
    return false;
  }

  /**
   * Save current session for later resumption
   */
  async saveCurrentSession(sessionData = null) {
    if (!sessionData) {
      // Auto-save current state
      sessionData = {
        id: Date.now().toString(),
        timestamp: new Date().toISOString(),
        currentModule: null,
        currentTask: null,
        state: 'idle'
      };
    }

    this.progress.sessions.unshift(sessionData);
    
    // Keep only last 10 sessions
    this.progress.sessions = this.progress.sessions.slice(0, 10);
    
    await this.saveProgress();
  }

  /**
   * Get last session for resumption
   */
  async getLastSession() {
    const sessions = this.progress.sessions || [];
    return sessions.length > 0 ? sessions[0] : null;
  }

  /**
   * Restore session state
   */
  async restoreSession(session) {
    this.logger.info(`Restoring session from ${session.timestamp}`);
    // Implementation would restore specific session state
    // This is a placeholder for session restoration logic
    return session;
  }

  /**
   * Get module ID from name
   */
  getModuleId(moduleName) {
    return moduleName.toLowerCase().replace(/[^a-z0-9]/g, '_');
  }

  /**
   * Check if module is completed
   */
  isModuleCompleted(moduleName) {
    const moduleId = this.getModuleId(moduleName);
    return this.progress.modules[moduleId]?.completed || false;
  }

  /**
   * Get module progress
   */
  getModuleProgress(moduleName) {
    const moduleId = this.getModuleId(moduleName);
    return this.progress.modules[moduleId] || null;
  }

  /**
   * Update streak counter
   */
  async updateStreak() {
    const today = new Date().toDateString();
    const lastActivity = this.progress.user.lastActivity ? 
      new Date(this.progress.user.lastActivity).toDateString() : null;

    if (lastActivity !== today) {
      const yesterday = new Date();
      yesterday.setDate(yesterday.getDate() - 1);
      
      if (lastActivity === yesterday.toDateString()) {
        // Consecutive day, increment streak
        this.progress.user.currentStreak++;
      } else if (lastActivity !== today) {
        // Missed days, reset streak
        this.progress.user.currentStreak = 1;
      }
      
      await this.saveProgress();
    }
  }

  /**
   * Add achievement to progress
   */
  async addAchievement(achievementId, achievementData) {
    if (!this.progress.achievements.find(a => a.id === achievementId)) {
      this.progress.achievements.push({
        id: achievementId,
        earnedDate: new Date().toISOString(),
        ...achievementData
      });
      
      await this.saveProgress();
      this.logger.success(`Achievement earned: ${achievementData.name}`);
    }
  }

  /**
   * Get user achievements
   */
  getUserAchievements() {
    return [...this.progress.achievements];
  }

  /**
   * Export progress data
   */
  async exportProgress() {
    return {
      exportDate: new Date().toISOString(),
      progress: { ...this.progress }
    };
  }

  /**
   * Import progress data
   */
  async importProgress(importData) {
    if (importData.progress) {
      this.progress = { ...this.progress, ...importData.progress };
      await this.saveProgress();
      this.logger.info('Progress imported successfully');
    }
  }

  /**
   * Reset all progress
   */
  async reset() {
    this.progress = {
      version: '1.0.0',
      user: {
        level: 1,
        totalPoints: 0,
        currentStreak: 0,
        lastActivity: null
      },
      modules: {},
      sessions: [],
      achievements: [],
      statistics: {
        totalCommands: 0,
        successfulCommands: 0,
        timeSpent: 0,
        modulesStarted: 0,
        modulesCompleted: 0
      }
    };
    
    await this.saveProgress();
    this.logger.info('Progress reset to default state');
  }

  /**
   * Get statistics summary
   */
  getStatistics() {
    return {
      ...this.progress.statistics,
      successRate: this.progress.statistics.totalCommands > 0 ? 
        (this.progress.statistics.successfulCommands / this.progress.statistics.totalCommands * 100).toFixed(1) : 0,
      averageTimePerModule: this.progress.statistics.modulesCompleted > 0 ?
        (this.progress.statistics.timeSpent / this.progress.statistics.modulesCompleted / 1000).toFixed(1) : 0
    };
  }
}
