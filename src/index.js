#!/usr/bin/env node

/**
 * Git Training CLI Application - Main Entry Point
 * 
 * A command-line interface application that teaches Git and GitHub fundamentals
 * through interactive learning modules, practical command execution, and 
 * progressive skill development in a controlled environment.
 * 
 * @author CS-25-2-Team3
 * @version 1.0.0
 */

import { Command } from 'commander';
import { GitTrainingApp } from './core/GitTrainingApp.js';
import { Logger } from './utils/Logger.js';
import { ConfigManager } from './core/ConfigManager.js';

const program = new Command();
const logger = new Logger();

/**
 * Configure CLI program with commands and options
 */
function setupProgram() {
  program
    .name('git-trainer')
    .description('Interactive Git & GitHub learning CLI application')
    .version('1.0.0', '-v, --version', 'Display version information')
    .option('-d, --debug', 'Enable debug mode')
    .option('-c, --config <path>', 'Specify custom config file path')
    .option('--no-colors', 'Disable colored output')
    .option('--reset', 'Reset all progress and configurations');

  // Main command - start the interactive application
  program
    .command('start', { isDefault: true })
    .description('Start the interactive Git training application')
    .action(async (options) => {
      await startApplication(options);
    });

  // Direct module access commands
  program
    .command('module <name>')
    .description('Start a specific learning module directly')
    .option('-f, --force', 'Force restart module even if completed')
    .action(async (name, options) => {
      await startSpecificModule(name, options);
    });

  // Progress and status commands
  program
    .command('status')
    .description('Show current learning progress and statistics')
    .action(async () => {
      await showStatus();
    });

  // Badge and achievement commands
  program
    .command('badges')
    .description('Display earned badges and achievements')
    .action(async () => {
      await showBadges();
    });

  // Configuration commands
  program
    .command('config')
    .description('Manage application configuration')
    .option('--list', 'List all configuration options')
    .option('--reset', 'Reset configuration to defaults')
    .action(async (options) => {
      await manageConfig(options);
    });
}

/**
 * Start the main interactive application
 */
async function startApplication(options) {
  try {
    // Initialize configuration
    const config = await ConfigManager.initialize(options);
    
    // Set up logger based on options
    logger.setDebugMode(options.debug || config.debug);
    logger.setColors(!options.noColors && config.colors);
    
    if (options.reset) {
      await resetApplication();
      return;
    }
    
    logger.info('Starting Git Training CLI Application...');
    
    // Create and start the main application
    const app = new GitTrainingApp(config, logger);
    await app.start();
    
  } catch (error) {
    logger.error('Failed to start application:', error.message);
    if (logger.isDebugMode()) {
      console.error(error.stack);
    }
    process.exit(1);
  }
}

/**
 * Start a specific learning module directly
 */
async function startSpecificModule(moduleName, options) {
  try {
    const config = await ConfigManager.initialize();
    const app = new GitTrainingApp(config, logger);
    
    logger.info(`Starting module: ${moduleName}`);
    await app.startModule(moduleName, options);
    
  } catch (error) {
    logger.error(`Failed to start module "${moduleName}":`, error.message);
    process.exit(1);
  }
}

/**
 * Show current progress and status
 */
async function showStatus() {
  try {
    const config = await ConfigManager.initialize();
    const app = new GitTrainingApp(config, logger);
    
    await app.showStatus();
    
  } catch (error) {
    logger.error('Failed to show status:', error.message);
    process.exit(1);
  }
}

/**
 * Show badges and achievements
 */
async function showBadges() {
  try {
    const config = await ConfigManager.initialize();
    const app = new GitTrainingApp(config, logger);
    
    await app.showBadges();
    
  } catch (error) {
    logger.error('Failed to show badges:', error.message);
    process.exit(1);
  }
}

/**
 * Manage application configuration
 */
async function manageConfig(options) {
  try {
    if (options.list) {
      await ConfigManager.listConfiguration();
    } else if (options.reset) {
      await ConfigManager.resetConfiguration();
      logger.success('Configuration reset to defaults');
    } else {
      await ConfigManager.interactiveConfiguration();
    }
  } catch (error) {
    logger.error('Failed to manage configuration:', error.message);
    process.exit(1);
  }
}

/**
 * Reset application data and progress
 */
async function resetApplication() {
  try {
    const config = await ConfigManager.initialize();
    const app = new GitTrainingApp(config, logger);
    
    await app.resetAll();
    logger.success('Application reset completed');
    
  } catch (error) {
    logger.error('Failed to reset application:', error.message);
    process.exit(1);
  }
}

/**
 * Handle uncaught exceptions and unhandled rejections
 */
function setupErrorHandling() {
  process.on('uncaughtException', (error) => {
    logger.error('Uncaught Exception:', error.message);
    if (logger.isDebugMode()) {
      console.error(error.stack);
    }
    process.exit(1);
  });

  process.on('unhandledRejection', (reason, promise) => {
    logger.error('Unhandled Rejection at:', promise, 'reason:', reason);
    process.exit(1);
  });

  process.on('SIGINT', () => {
    logger.info('\nGracefully shutting down...');
    process.exit(0);
  });

  process.on('SIGTERM', () => {
    logger.info('Received SIGTERM, shutting down gracefully...');
    process.exit(0);
  });
}

/**
 * Main execution function
 */
async function main() {
  setupErrorHandling();
  setupProgram();
  
  // Parse command line arguments
  await program.parseAsync(process.argv);
}

// Execute the main function
main().catch((error) => {
  console.error('Fatal error:', error.message);
  process.exit(1);
});
