/**
 * Configuration Manager - Handles application configuration
 * 
 * Manages user preferences, application settings, and configuration persistence.
 * Implements User Story A3: Access configuration options
 */

import fs from 'fs/promises';
import path from 'path';
import os from 'os';
import inquirer from 'inquirer';

export class ConfigManager {
  constructor() {
    this.configPath = path.join(os.homedir(), '.git-trainer', 'config.json');
    this.defaultConfig = {
      version: '1.0.0',
      debug: false,
      colors: true,
      difficulty: 'beginner', // beginner, intermediate, advanced
      displayOptions: {
        animations: true,
        progressBars: true,
        detailedOutput: false,
        compactMode: false
      },
      notifications: {
        achievements: true,
        progress: true,
        hints: true
      },
      learning: {
        autoSave: true,
        confirmCommands: true,
        showExamples: true,
        pauseOnErrors: true
      },
      sandbox: {
        keepOnSuccess: false,
        keepOnError: true,
        autoCleanup: true
      }
    };
    this.config = { ...this.defaultConfig };
  }

  /**
   * Initialize configuration manager
   */
  static async initialize(options = {}) {
    const manager = new ConfigManager();
    await manager.load();
    
    // Override with command line options
    if (options.debug !== undefined) {
      manager.config.debug = options.debug;
    }
    if (options.noColors) {
      manager.config.colors = false;
    }
    if (options.config) {
      await manager.loadFromFile(options.config);
    }
    
    return manager;
  }

  /**
   * Load configuration from file
   */
  async load() {
    try {
      // Ensure config directory exists
      await fs.mkdir(path.dirname(this.configPath), { recursive: true });
      
      // Try to load existing config
      const configData = await fs.readFile(this.configPath, 'utf8');
      const loadedConfig = JSON.parse(configData);
      
      // Merge with defaults to ensure all properties exist
      this.config = { ...this.defaultConfig, ...loadedConfig };
      
      // Migrate config if version differs
      await this.migrateConfig();
      
    } catch (error) {
      if (error.code === 'ENOENT') {
        // Config file doesn't exist, create it with defaults
        await this.save();
      } else {
        console.warn('Failed to load config, using defaults:', error.message);
      }
    }
  }

  /**
   * Load configuration from specific file
   */
  async loadFromFile(filePath) {
    try {
      const configData = await fs.readFile(filePath, 'utf8');
      const loadedConfig = JSON.parse(configData);
      this.config = { ...this.config, ...loadedConfig };
    } catch (error) {
      throw new Error(`Failed to load config from ${filePath}: ${error.message}`);
    }
  }

  /**
   * Save configuration to file
   */
  async save() {
    try {
      await fs.mkdir(path.dirname(this.configPath), { recursive: true });
      await fs.writeFile(this.configPath, JSON.stringify(this.config, null, 2));
    } catch (error) {
      throw new Error(`Failed to save config: ${error.message}`);
    }
  }

  /**
   * Reset configuration to defaults
   */
  async reset() {
    this.config = { ...this.defaultConfig };
    await this.save();
  }

  /**
   * Get configuration value
   */
  get(key, defaultValue = undefined) {
    const keys = key.split('.');
    let value = this.config;
    
    for (const k of keys) {
      if (value && typeof value === 'object' && k in value) {
        value = value[k];
      } else {
        return defaultValue;
      }
    }
    
    return value;
  }

  /**
   * Set configuration value
   */
  async set(key, value) {
    const keys = key.split('.');
    let target = this.config;
    
    for (let i = 0; i < keys.length - 1; i++) {
      const k = keys[i];
      if (!(k in target) || typeof target[k] !== 'object') {
        target[k] = {};
      }
      target = target[k];
    }
    
    target[keys[keys.length - 1]] = value;
    await this.save();
  }

  /**
   * Migrate configuration for version updates
   */
  async migrateConfig() {
    if (this.config.version !== this.defaultConfig.version) {
      // Add migration logic here for future versions
      this.config.version = this.defaultConfig.version;
      await this.save();
    }
  }

  /**
   * List all configuration options
   */
  static async listConfiguration() {
    const manager = new ConfigManager();
    await manager.load();
    
    console.log('Current Configuration:');
    console.log(JSON.stringify(manager.config, null, 2));
  }

  /**
   * Interactive configuration setup
   */
  static async interactiveConfiguration() {
    const manager = new ConfigManager();
    await manager.load();
    
    const answers = await inquirer.prompt([
      {
        type: 'list',
        name: 'difficulty',
        message: 'Select your learning difficulty level:',
        choices: [
          { name: 'Beginner - New to Git', value: 'beginner' },
          { name: 'Intermediate - Some Git experience', value: 'intermediate' },
          { name: 'Advanced - Experienced with Git', value: 'advanced' }
        ],
        default: manager.config.difficulty
      },
      {
        type: 'confirm',
        name: 'animations',
        message: 'Enable animations and visual effects?',
        default: manager.config.displayOptions.animations
      },
      {
        type: 'confirm',
        name: 'progressBars',
        message: 'Show progress bars?',
        default: manager.config.displayOptions.progressBars
      },
      {
        type: 'confirm',
        name: 'achievements',
        message: 'Enable achievement notifications?',
        default: manager.config.notifications.achievements
      },
      {
        type: 'confirm',
        name: 'autoSave',
        message: 'Automatically save progress?',
        default: manager.config.learning.autoSave
      },
      {
        type: 'confirm',
        name: 'confirmCommands',
        message: 'Confirm before executing Git commands?',
        default: manager.config.learning.confirmCommands
      }
    ]);

    // Update configuration
    manager.config.difficulty = answers.difficulty;
    manager.config.displayOptions.animations = answers.animations;
    manager.config.displayOptions.progressBars = answers.progressBars;
    manager.config.notifications.achievements = answers.achievements;
    manager.config.learning.autoSave = answers.autoSave;
    manager.config.learning.confirmCommands = answers.confirmCommands;

    await manager.save();
    console.log('Configuration updated successfully!');
  }

  /**
   * Reset configuration to defaults
   */
  static async resetConfiguration() {
    const manager = new ConfigManager();
    await manager.reset();
  }

  /**
   * Get the full configuration object
   */
  getAll() {
    return { ...this.config };
  }

  /**
   * Check if debug mode is enabled
   */
  isDebugMode() {
    return this.config.debug;
  }

  /**
   * Check if colors are enabled
   */
  areColorsEnabled() {
    return this.config.colors;
  }

  /**
   * Get difficulty level
   */
  getDifficulty() {
    return this.config.difficulty;
  }

  /**
   * Check if feature is enabled
   */
  isFeatureEnabled(feature) {
    const featurePath = feature.split('.');
    return this.get(featurePath.join('.'), false);
  }
}
