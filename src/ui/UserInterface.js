/**
 * User Interface Manager - Handles all user interactions and display
 * 
 * Provides a consistent interface for user interactions, animations,
 * and visual feedback throughout the application.
 */

import inquirer from 'inquirer';
import chalk from 'chalk';
import ora from 'ora';
import boxen from 'boxen';
import * as emoji from 'node-emoji';
import cliProgress from 'cli-progress';

export class UserInterface {
  constructor(config, logger) {
    this.config = config;
    this.logger = logger;
    this.progressBar = null;
  }

  /**
   * Clear the console screen
   */
  async clearScreen() {
    if (this.config.get('displayOptions.animations', true)) {
      process.stdout.write('\x1B[2J\x1B[0f');
    }
  }

  /**
   * Sleep for specified milliseconds
   */
  async sleep(ms) {
    return new Promise(resolve => setTimeout(resolve, ms));
  }

  /**
   * Show loading animation with Steve Harvey theme
   */
  async showLoadingAnimation() {
    if (!this.config.get('displayOptions.animations', true)) {
      return;
    }

    const spinner = ora({
      text: 'Loading Family Feud Git Edition...',
      spinner: 'dots',
      color: 'yellow'
    }).start();

    await this.sleep(1000);

    // Show Steve Harvey face animation frames
    const frames = [
      '                    ╭─────────────────────╮\n' +
      '                   ╱                       ╲\n' +
      '                  ╱   ◦             ◦   ╲\n' +
      '                 │                         │\n' +
      '                 │          ────          │\n' +
      '                 │                         │\n' +
      '                 │    ───────────────    │\n' +
      '                  ╲                       ╱\n' +
      '                   ╲_____________________╱',

      '                    ╭─────────────────────╮\n' +
      '                   ╱                       ╲\n' +
      '                  ╱   ●             ●   ╲\n' +
      '                 │                         │\n' +
      '                 │          ╲___╱          │\n' +
      '                 │                         │\n' +
      '                 │    ╲_______________╱    │\n' +
      '                  ╲                       ╱\n' +
      '                   ╲_____________________╱'
    ];

    for (const frame of frames) {
      spinner.stop();
      await this.clearScreen();
      console.log(chalk.yellow(frame));
      await this.sleep(300);
      spinner.start();
    }

    spinner.succeed('Steve Harvey has entered the building!');
    await this.sleep(500);
  }

  /**
   * Show progress summary
   */
  showProgressSummary(progress) {
    if (!progress) return;

    const box = boxen(
      `Modules Completed: ${chalk.green(progress.completed)}/${chalk.blue(progress.total)}\n` +
      `Current Level: ${chalk.yellow(progress.level)}\n` +
      `Total Points: ${chalk.cyan(progress.points)}\n` +
      `Last Activity: ${chalk.gray(progress.lastActivity || 'Never')}`,
      {
        title: 'Your Progress',
        padding: 1,
        borderColor: 'cyan',
        borderStyle: 'round'
      }
    );

    console.log(box);
    console.log();
  }

  /**
   * Show module selection menu
   */
  async showModuleSelection(modules) {
    const choices = modules.map(module => ({
      name: this.formatModuleChoice(module),
      value: module.id,
      short: module.name
    }));

    choices.push({
      name: chalk.gray('← Back to Main Menu'),
      value: 'back',
      short: 'Back'
    });

    const { choice } = await inquirer.prompt([
      {
        type: 'list',
        name: 'choice',
        message: 'Select a learning module:',
        choices,
        pageSize: 10
      }
    ]);

    return choice;
  }

  /**
   * Format module choice for display
   */
  formatModuleChoice(module) {
    const statusIcon = module.completed ? '✅' : 
                      module.inProgress ? '🔄' : '🆕';
    const difficultyColor = {
      beginner: chalk.green,
      intermediate: chalk.yellow,
      advanced: chalk.red
    }[module.difficulty] || chalk.white;

    return `${statusIcon} ${chalk.bold(module.name)} - ${difficultyColor(module.difficulty)} - ${module.description}`;
  }

  /**
   * Show achievements and badges
   */
  async showAchievements(achievements, progress) {
    await this.clearScreen();

    console.log(chalk.bold.yellow('🏆 YOUR ACHIEVEMENTS & BADGES 🏆'));
    console.log();

    if (achievements.length === 0) {
      console.log(chalk.gray('No achievements earned yet. Start playing to earn your first badges!'));
    } else {
      achievements.forEach(achievement => {
        const icon = achievement.earned ? '🏆' : '🔒';
        const color = achievement.earned ? chalk.green : chalk.gray;
        console.log(`${icon} ${color(achievement.name)} - ${achievement.description}`);
        if (achievement.earned && achievement.earnedDate) {
          console.log(`   ${chalk.gray('Earned on:')} ${chalk.cyan(achievement.earnedDate)}`);
        }
        console.log();
      });
    }

    if (progress) {
      console.log(chalk.bold('📊 PROGRESS OVERVIEW'));
      console.log(`Total Points: ${chalk.cyan(progress.totalPoints)}`);
      console.log(`Modules Completed: ${chalk.green(progress.modulesCompleted)}/${progress.totalModules}`);
      console.log(`Current Streak: ${chalk.yellow(progress.currentStreak)} days`);
    }
  }

  /**
   * Confirm session continuation
   */
  async confirmContinueSession(session) {
    const { shouldContinue } = await inquirer.prompt([
      {
        type: 'confirm',
        name: 'shouldContinue',
        message: `Continue from ${session.moduleName} (${session.taskName})?`,
        default: true
      }
    ]);

    return shouldContinue;
  }

  /**
   * Show no session found message
   */
  async showNoSessionFound() {
    const box = boxen(
      chalk.yellow('No previous session found.') + '\n\n' +
      chalk.cyan('Start with "PLAY" to begin your Git learning journey!'),
      {
        title: 'Session Status',
        padding: 1,
        borderColor: 'yellow',
        borderStyle: 'round'
      }
    );

    console.log(box);
  }

  /**
   * Show configuration menu
   */
  async showConfigurationMenu() {
    const choices = [
      {
        name: '🎯 Difficulty Level - Adjust learning complexity',
        value: 'difficulty',
        short: 'Difficulty'
      },
      {
        name: '🎨 Display Options - Customize visual appearance',
        value: 'display',
        short: 'Display'
      },
      {
        name: '🔔 Notifications - Manage alerts and feedback',
        value: 'notifications',
        short: 'Notifications'
      },
      {
        name: '🔄 Reset Settings - Restore default configuration',
        value: 'reset',
        short: 'Reset'
      },
      {
        name: chalk.gray('← Back to Main Menu'),
        value: 'back',
        short: 'Back'
      }
    ];

    const { choice } = await inquirer.prompt([
      {
        type: 'list',
        name: 'choice',
        message: 'Configuration Options:',
        choices
      }
    ]);

    return choice;
  }

  /**
   * Configure difficulty settings
   */
  async configureDifficulty(config) {
    const { difficulty } = await inquirer.prompt([
      {
        type: 'list',
        name: 'difficulty',
        message: 'Select your preferred difficulty level:',
        choices: [
          { name: '🟢 Beginner - New to Git, detailed explanations', value: 'beginner' },
          { name: '🟡 Intermediate - Some Git experience, moderate pace', value: 'intermediate' },
          { name: '🔴 Advanced - Experienced with Git, fast pace', value: 'advanced' }
        ],
        default: config.get('difficulty')
      }
    ]);

    await config.set('difficulty', difficulty);
    await this.showMessage('Difficulty level updated!', 'success');
  }

  /**
   * Configure display settings
   */
  async configureDisplay(config) {
    const answers = await inquirer.prompt([
      {
        type: 'confirm',
        name: 'animations',
        message: 'Enable animations and visual effects?',
        default: config.get('displayOptions.animations')
      },
      {
        type: 'confirm',
        name: 'progressBars',
        message: 'Show progress bars during operations?',
        default: config.get('displayOptions.progressBars')
      },
      {
        type: 'confirm',
        name: 'compactMode',
        message: 'Use compact display mode?',
        default: config.get('displayOptions.compactMode')
      }
    ]);

    await config.set('displayOptions.animations', answers.animations);
    await config.set('displayOptions.progressBars', answers.progressBars);
    await config.set('displayOptions.compactMode', answers.compactMode);
    
    await this.showMessage('Display settings updated!', 'success');
  }

  /**
   * Configure notification settings
   */
  async configureNotifications(config) {
    const answers = await inquirer.prompt([
      {
        type: 'confirm',
        name: 'achievements',
        message: 'Show achievement notifications?',
        default: config.get('notifications.achievements')
      },
      {
        type: 'confirm',
        name: 'progress',
        message: 'Show progress notifications?',
        default: config.get('notifications.progress')
      },
      {
        type: 'confirm',
        name: 'hints',
        message: 'Show helpful hints during learning?',
        default: config.get('notifications.hints')
      }
    ]);

    await config.set('notifications.achievements', answers.achievements);
    await config.set('notifications.progress', answers.progress);
    await config.set('notifications.hints', answers.hints);
    
    await this.showMessage('Notification settings updated!', 'success');
  }

  /**
   * Confirm quit action
   */
  async confirmQuit() {
    const { shouldQuit } = await inquirer.prompt([
      {
        type: 'confirm',
        name: 'shouldQuit',
        message: 'Are you sure you want to quit?',
        default: false
      }
    ]);

    return shouldQuit;
  }

  /**
   * Confirm reset action
   */
  async confirmReset() {
    const { shouldReset } = await inquirer.prompt([
      {
        type: 'confirm',
        name: 'shouldReset',
        message: 'This will reset all configuration to defaults. Continue?',
        default: false
      }
    ]);

    return shouldReset;
  }

  /**
   * Confirm full application reset
   */
  async confirmFullReset() {
    const { shouldReset } = await inquirer.prompt([
      {
        type: 'confirm',
        name: 'shouldReset',
        message: chalk.red('This will delete ALL progress, achievements, and settings. Are you sure?'),
        default: false
      }
    ]);

    if (shouldReset) {
      const { confirmation } = await inquirer.prompt([
        {
          type: 'input',
          name: 'confirmation',
          message: 'Type "RESET" to confirm:',
          validate: input => input === 'RESET' || 'Please type "RESET" to confirm'
        }
      ]);

      return confirmation === 'RESET';
    }

    return false;
  }

  /**
   * Show generic message with type-based styling
   */
  async showMessage(message, type = 'info') {
    const colors = {
      info: chalk.blue,
      success: chalk.green,
      warning: chalk.yellow,
      error: chalk.red
    };

    const icons = {
      info: 'ℹ️',
      success: '✅',
      warning: '⚠️',
      error: '❌'
    };

    const color = colors[type] || chalk.white;
    const icon = icons[type] || '•';

    console.log(`${icon} ${color(message)}`);
  }

  /**
   * Show error message
   */
  async showError(message) {
    await this.showMessage(message, 'error');
  }

  /**
   * Press enter to continue prompt
   */
  async pressEnterToContinue(message = 'Press Enter to continue...') {
    await inquirer.prompt([
      {
        type: 'input',
        name: 'continue',
        message: chalk.gray(message),
        default: ''
      }
    ]);
  }

  /**
   * Show detailed status information
   */
  async showDetailedStatus(progress, achievements) {
    await this.clearScreen();
    
    console.log(chalk.bold.blue('📊 DETAILED STATUS REPORT 📊'));
    console.log();
    
    // Progress information
    if (progress) {
      this.showProgressSummary(progress);
    }
    
    // Recent achievements
    const recentAchievements = achievements.filter(a => a.earned).slice(-3);
    if (recentAchievements.length > 0) {
      console.log(chalk.bold('🏆 Recent Achievements:'));
      recentAchievements.forEach(achievement => {
        console.log(`  ${chalk.green('✓')} ${achievement.name}`);
      });
      console.log();
    }
  }

  /**
   * Show badge gallery
   */
  async showBadgeGallery(allAchievements, userAchievements) {
    await this.clearScreen();
    
    console.log(chalk.bold.yellow('🏆 BADGE GALLERY 🏆'));
    console.log();
    
    const earnedIds = new Set(userAchievements.map(a => a.id));
    
    allAchievements.forEach(achievement => {
      const earned = earnedIds.has(achievement.id);
      const icon = earned ? '🏆' : '🔒';
      const color = earned ? chalk.green : chalk.gray;
      const status = earned ? chalk.green('EARNED') : chalk.gray('LOCKED');
      
      console.log(`${icon} ${color.bold(achievement.name)} [${status}]`);
      console.log(`   ${color(achievement.description)}`);
      
      if (earned) {
        const userAchievement = userAchievements.find(a => a.id === achievement.id);
        if (userAchievement && userAchievement.earnedDate) {
          console.log(`   ${chalk.cyan('Earned:')} ${userAchievement.earnedDate}`);
        }
      }
      console.log();
    });
  }
}
