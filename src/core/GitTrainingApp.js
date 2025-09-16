/**
 * Git Training CLI Application - Main Application Class
 * 
 * This class manages the overall application flow, user interface,
 * and coordinates between different modules and components.
 * 
 * Implements user stories:
 * - A1: Launch application and view learning modules
 * - A2: Resume previous session 
 * - A3: Access configuration options
 * - B7: Achievement and progression systems
 */

import inquirer from 'inquirer';
import chalk from 'chalk';
import figlet from 'figlet';
import gradient from 'gradient-string';
import boxen from 'boxen';
import * as emoji from 'node-emoji';
import { UserInterface } from '../ui/UserInterface.js';
import { ProgressManager } from './ProgressManager.js';
import { ModuleManager } from './ModuleManager.js';
import { AchievementSystem } from './AchievementSystem.js';
import { SandboxManager } from './SandboxManager.js';

export class GitTrainingApp {
  constructor(config, logger) {
    this.config = config;
    this.logger = logger;
    this.ui = new UserInterface(config, logger);
    this.progressManager = new ProgressManager(config, logger);
    this.moduleManager = new ModuleManager(config, logger);
    this.achievementSystem = new AchievementSystem(config, logger);
    this.sandboxManager = new SandboxManager(config, logger);
    this.isRunning = false;
  }

  /**
   * Start the main application
   */
  async start() {
    try {
      this.isRunning = true;
      
      // Initialize all managers
      await this.initialize();
      
      // Show welcome screen
      await this.showWelcomeScreen();
      
      // Start main application loop
      await this.mainLoop();
      
    } catch (error) {
      this.logger.error('Application error:', error.message);
      throw error;
    } finally {
      await this.cleanup();
    }
  }

  /**
   * Initialize all application components
   */
  async initialize() {
    this.logger.debug('Initializing application components...');
    
    await Promise.all([
      this.progressManager.initialize(),
      this.moduleManager.initialize(),
      this.achievementSystem.initialize(),
      this.sandboxManager.initialize()
    ]);
    
    this.logger.debug('Application components initialized successfully');
  }

  /**
   * Show animated welcome screen with Family Feud theme
   */
  async showWelcomeScreen() {
    await this.ui.clearScreen();
    
    // Show animated Steve Harvey face
    await this.ui.showLoadingAnimation();
    
    // Display welcome banner
    const title = figlet.textSync('GIT TRAINER', {
      font: 'Big',
      horizontalLayout: 'default',
      verticalLayout: 'default'
    });
    
    const gradientTitle = gradient(['#ff6b6b', '#4ecdc4', '#45b7d1'])(title);
    
    console.log(gradientTitle);
    console.log();
    
    const welcomeBox = boxen(
      chalk.bold.white('Welcome to the Interactive Git Learning Experience!') + '\n\n' +
      chalk.yellow('Master Git commands through hands-on practice') + '\n' +
      chalk.cyan('in safe sandbox environments.') + '\n\n' +
      chalk.green('Survey says... Let\'s get started! ') + emoji.get('rocket'),
      {
        title: 'Family Let\'s "GIT_IT_TOGETHER"',
        titleAlignment: 'center',
        padding: 1,
        margin: 1,
        borderStyle: 'double',
        borderColor: 'blue'
      }
    );
    
    console.log(welcomeBox);
    
    // Brief pause for dramatic effect
    await this.ui.sleep(1500);
  }

  /**
   * Main application loop
   */
  async mainLoop() {
    while (this.isRunning) {
      try {
        const choice = await this.showMainMenu();
        await this.handleMainMenuChoice(choice);
      } catch (error) {
        if (error.isTtyError) {
          this.logger.error('Interactive prompt not supported in this environment');
          break;
        }
        this.logger.error('Menu error:', error.message);
        await this.ui.pressEnterToContinue();
      }
    }
  }

  /**
   * Show main menu and get user choice
   */
  async showMainMenu() {
    await this.ui.clearScreen();
    
    // Show current progress summary
    const progress = await this.progressManager.getProgressSummary();
    this.ui.showProgressSummary(progress);
    
    const choices = [
      {
        name: chalk.green('🎮 PLAY') + '     - Start new Git learning modules',
        value: 'play',
        short: 'Play'
      },
      {
        name: chalk.yellow('🏆 BADGES') + '   - View your achievements and progress',
        value: 'badges',
        short: 'Badges'
      },
      {
        name: chalk.cyan('📚 CONTINUE') + ' - Resume your previous session',
        value: 'continue',
        short: 'Continue'
      },
      {
        name: chalk.blue('⚙️  CONFIG') + '  - Customize your learning experience',
        value: 'config',
        short: 'Config'
      },
      {
        name: chalk.red('🚪 QUIT') + '     - Exit the application',
        value: 'quit',
        short: 'Quit'
      }
    ];

    const { choice } = await inquirer.prompt([
      {
        type: 'list',
        name: 'choice',
        message: chalk.bold('Choose an option:'),
        choices,
        pageSize: 10
      }
    ]);

    return choice;
  }

  /**
   * Handle main menu choice
   */
  async handleMainMenuChoice(choice) {
    switch (choice) {
      case 'play':
        await this.handlePlayOption();
        break;
      case 'badges':
        await this.handleBadgesOption();
        break;
      case 'continue':
        await this.handleContinueOption();
        break;
      case 'config':
        await this.handleConfigOption();
        break;
      case 'quit':
        await this.handleQuitOption();
        break;
      default:
        this.logger.warn('Unknown menu choice:', choice);
    }
  }

  /**
   * Handle PLAY option - Start new learning modules
   * Implements User Story A1: View available learning modules
   */
  async handlePlayOption() {
    const modules = await this.moduleManager.getAvailableModules();
    const moduleChoice = await this.ui.showModuleSelection(modules);
    
    if (moduleChoice && moduleChoice !== 'back') {
      await this.startModule(moduleChoice);
    }
  }

  /**
   * Handle BADGES option - View achievements
   * Implements User Story B7: Achievement and progression systems
   */
  async handleBadgesOption() {
    const achievements = await this.achievementSystem.getAllAchievements();
    const progress = await this.progressManager.getDetailedProgress();
    
    await this.ui.showAchievements(achievements, progress);
    await this.ui.pressEnterToContinue();
  }

  /**
   * Handle CONTINUE option - Resume previous session
   * Implements User Story A2: Resume previous session
   */
  async handleContinueOption() {
    const lastSession = await this.progressManager.getLastSession();
    
    if (lastSession) {
      const shouldContinue = await this.ui.confirmContinueSession(lastSession);
      if (shouldContinue) {
        await this.resumeSession(lastSession);
      }
    } else {
      await this.ui.showNoSessionFound();
      await this.ui.pressEnterToContinue();
    }
  }

  /**
   * Handle CONFIG option - Customize learning experience
   * Implements User Story A3: Access configuration options
   */
  async handleConfigOption() {
    const configChoice = await this.ui.showConfigurationMenu();
    await this.handleConfigurationChoice(configChoice);
  }

  /**
   * Handle QUIT option - Exit application
   */
  async handleQuitOption() {
    const shouldQuit = await this.ui.confirmQuit();
    if (shouldQuit) {
      this.isRunning = false;
      await this.showGoodbyeScreen();
    }
  }

  /**
   * Start a specific learning module
   */
  async startModule(moduleName, options = {}) {
    try {
      this.logger.info(`Starting module: ${moduleName}`);
      
      // Create sandbox environment
      const sandbox = await this.sandboxManager.createSandbox(moduleName);
      
      // Load module
      const module = await this.moduleManager.loadModule(moduleName);
      
      // Start module execution
      const result = await module.execute(sandbox, this.ui, options);
      
      // Update progress
      await this.progressManager.updateModuleProgress(moduleName, result);
      
      // Check for new achievements
      await this.achievementSystem.checkAchievements(result);
      
      // Cleanup sandbox if needed
      if (!options.keepSandbox) {
        await this.sandboxManager.cleanupSandbox(sandbox.id);
      }
      
    } catch (error) {
      this.logger.error(`Failed to execute module "${moduleName}":`, error.message);
      await this.ui.showError(`Module execution failed: ${error.message}`);
      await this.ui.pressEnterToContinue();
    }
  }

  /**
   * Resume a previous session
   */
  async resumeSession(session) {
    try {
      this.logger.info('Resuming previous session...');
      
      // Restore session state
      await this.progressManager.restoreSession(session);
      
      // Continue from where user left off
      const moduleToResume = session.currentModule;
      const taskToResume = session.currentTask;
      
      if (moduleToResume) {
        await this.startModule(moduleToResume, { 
          resumeFromTask: taskToResume,
          sessionId: session.id 
        });
      }
      
    } catch (error) {
      this.logger.error('Failed to resume session:', error.message);
      await this.ui.showError(`Session resume failed: ${error.message}`);
      await this.ui.pressEnterToContinue();
    }
  }

  /**
   * Handle configuration menu choices
   */
  async handleConfigurationChoice(choice) {
    switch (choice) {
      case 'difficulty':
        await this.ui.configureDifficulty(this.config);
        break;
      case 'display':
        await this.ui.configureDisplay(this.config);
        break;
      case 'notifications':
        await this.ui.configureNotifications(this.config);
        break;
      case 'reset':
        await this.resetConfiguration();
        break;
      case 'back':
        break;
      default:
        this.logger.warn('Unknown configuration choice:', choice);
    }
  }

  /**
   * Reset application configuration
   */
  async resetConfiguration() {
    const shouldReset = await this.ui.confirmReset();
    if (shouldReset) {
      await this.config.reset();
      await this.ui.showMessage('Configuration reset to defaults', 'success');
      await this.ui.pressEnterToContinue();
    }
  }

  /**
   * Show current status
   */
  async showStatus() {
    const progress = await this.progressManager.getDetailedProgress();
    const achievements = await this.achievementSystem.getUserAchievements();
    
    await this.ui.showDetailedStatus(progress, achievements);
  }

  /**
   * Show badges and achievements
   */
  async showBadges() {
    const achievements = await this.achievementSystem.getAllAchievements();
    const userAchievements = await this.achievementSystem.getUserAchievements();
    
    await this.ui.showBadgeGallery(achievements, userAchievements);
  }

  /**
   * Reset all application data
   */
  async resetAll() {
    const shouldReset = await this.ui.confirmFullReset();
    if (shouldReset) {
      await Promise.all([
        this.progressManager.reset(),
        this.achievementSystem.reset(),
        this.sandboxManager.cleanupAll(),
        this.config.reset()
      ]);
      
      await this.ui.showMessage('Application completely reset', 'success');
    }
  }

  /**
   * Show goodbye screen
   */
  async showGoodbyeScreen() {
    await this.ui.clearScreen();
    
    const goodbyeBox = boxen(
      chalk.bold.green('Thank you for using') + '\n\n' +
      chalk.bold.red('FAMILY LET\'S "GIT_IT_TOGETHER"') + '\n\n' +
      chalk.yellow('Keep learning and happy coding!') + '\n\n' +
      chalk.cyan('Survey says... Git mastered! ') + emoji.get('trophy'),
      {
        title: 'Goodbye!',
        titleAlignment: 'center',
        padding: 2,
        margin: 1,
        borderStyle: 'double',
        borderColor: 'green'
      }
    );
    
    console.log(goodbyeBox);
  }

  /**
   * Cleanup resources before exit
   */
  async cleanup() {
    this.logger.debug('Cleaning up application resources...');
    
    try {
      await Promise.all([
        this.sandboxManager.cleanup(),
        this.progressManager.saveCurrentSession()
      ]);
    } catch (error) {
      this.logger.warn('Cleanup warning:', error.message);
    }
  }

  /**
   * Get application running status
   */
  isApplicationRunning() {
    return this.isRunning;
  }

  /**
   * Stop the application
   */
  stop() {
    this.isRunning = false;
  }
}
