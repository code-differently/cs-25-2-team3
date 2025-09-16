/**
 * Logger Utility - Centralized logging with different levels
 * 
 * Provides consistent logging across the application with configurable
 * output formatting and debug capabilities.
 */

import chalk from 'chalk';

export class Logger {
  constructor(options = {}) {
    this.debugMode = options.debug || false;
    this.useColors = options.colors !== false;
    this.logLevel = options.logLevel || 'info';
    this.timestamps = options.timestamps || false;
  }

  /**
   * Set debug mode
   */
  setDebugMode(enabled) {
    this.debugMode = enabled;
  }

  /**
   * Set color output
   */
  setColors(enabled) {
    this.useColors = enabled;
  }

  /**
   * Check if debug mode is enabled
   */
  isDebugMode() {
    return this.debugMode;
  }

  /**
   * Format message with timestamp if enabled
   */
  formatMessage(level, message, ...args) {
    const timestamp = this.timestamps ? `[${new Date().toISOString()}] ` : '';
    const levelTag = `[${level.toUpperCase()}]`;
    
    if (this.useColors) {
      const coloredLevel = this.getColoredLevel(level);
      return `${chalk.gray(timestamp)}${coloredLevel} ${message}`;
    }
    
    return `${timestamp}${levelTag} ${message}`;
  }

  /**
   * Get colored level indicator
   */
  getColoredLevel(level) {
    const colors = {
      debug: chalk.gray,
      info: chalk.blue,
      success: chalk.green,
      warn: chalk.yellow,
      error: chalk.red,
      critical: chalk.bgRed.white
    };
    
    const colorFn = colors[level] || chalk.white;
    return colorFn(`[${level.toUpperCase()}]`);
  }

  /**
   * Check if level should be logged
   */
  shouldLog(level) {
    const levels = {
      debug: 0,
      info: 1,
      success: 1,
      warn: 2,
      error: 3,
      critical: 4
    };
    
    const currentLevel = levels[this.logLevel] || 1;
    const messageLevel = levels[level] || 1;
    
    return messageLevel >= currentLevel;
  }

  /**
   * Debug level logging
   */
  debug(message, ...args) {
    if (this.debugMode && this.shouldLog('debug')) {
      console.log(this.formatMessage('debug', message), ...args);
    }
  }

  /**
   * Info level logging
   */
  info(message, ...args) {
    if (this.shouldLog('info')) {
      console.log(this.formatMessage('info', message), ...args);
    }
  }

  /**
   * Success level logging
   */
  success(message, ...args) {
    if (this.shouldLog('success')) {
      console.log(this.formatMessage('success', message), ...args);
    }
  }

  /**
   * Warning level logging
   */
  warn(message, ...args) {
    if (this.shouldLog('warn')) {
      console.warn(this.formatMessage('warn', message), ...args);
    }
  }

  /**
   * Error level logging
   */
  error(message, ...args) {
    if (this.shouldLog('error')) {
      console.error(this.formatMessage('error', message), ...args);
    }
  }

  /**
   * Critical level logging
   */
  critical(message, ...args) {
    if (this.shouldLog('critical')) {
      console.error(this.formatMessage('critical', message), ...args);
    }
  }

  /**
   * Create a child logger with specific context
   */
  child(context) {
    return {
      debug: (message, ...args) => this.debug(`[${context}] ${message}`, ...args),
      info: (message, ...args) => this.info(`[${context}] ${message}`, ...args),
      success: (message, ...args) => this.success(`[${context}] ${message}`, ...args),
      warn: (message, ...args) => this.warn(`[${context}] ${message}`, ...args),
      error: (message, ...args) => this.error(`[${context}] ${message}`, ...args),
      critical: (message, ...args) => this.critical(`[${context}] ${message}`, ...args)
    };
  }

  /**
   * Log with custom level
   */
  log(level, message, ...args) {
    switch (level) {
      case 'debug':
        this.debug(message, ...args);
        break;
      case 'info':
        this.info(message, ...args);
        break;
      case 'success':
        this.success(message, ...args);
        break;
      case 'warn':
        this.warn(message, ...args);
        break;
      case 'error':
        this.error(message, ...args);
        break;
      case 'critical':
        this.critical(message, ...args);
        break;
      default:
        this.info(message, ...args);
    }
  }

  /**
   * Create a progress logger for long-running operations
   */
  progress(total, message = 'Processing') {
    let current = 0;
    
    return {
      update: (increment = 1, customMessage) => {
        current += increment;
        const percent = Math.round((current / total) * 100);
        const msg = customMessage || message;
        this.info(`${msg}... ${percent}% (${current}/${total})`);
      },
      complete: (customMessage) => {
        const msg = customMessage || `${message} completed`;
        this.success(`${msg} ✓`);
      }
    };
  }

  /**
   * Create a timer for measuring operation duration
   */
  timer(label) {
    const start = Date.now();
    
    return {
      end: () => {
        const duration = Date.now() - start;
        this.debug(`${label} completed in ${duration}ms`);
        return duration;
      }
    };
  }

  /**
   * Set log level
   */
  setLogLevel(level) {
    this.logLevel = level;
  }

  /**
   * Enable/disable timestamps
   */
  setTimestamps(enabled) {
    this.timestamps = enabled;
  }
}
