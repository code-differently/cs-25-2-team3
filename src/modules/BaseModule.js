/**
 * Base Module Class - Abstract base for all learning modules
 * 
 * Provides common functionality and structure for learning modules
 */

export class BaseModule {
  constructor(definition, config, logger) {
    this.definition = definition;
    this.config = config;
    this.logger = logger;
    this.tasks = [];
    this.currentTask = 0;
    this.startTime = null;
    this.endTime = null;
  }

  /**
   * Initialize the module
   */
  async initialize() {
    await this.loadTasks();
    this.logger.debug(`Module ${this.definition.name} initialized with ${this.tasks.length} tasks`);
  }

  /**
   * Load module tasks - to be implemented by subclasses
   */
  async loadTasks() {
    throw new Error('loadTasks must be implemented by subclass');
  }

  /**
   * Execute the module
   */
  async execute(sandbox, ui, options = {}) {
    try {
      this.startTime = Date.now();
      
      // Show module introduction
      await this.showIntroduction(ui);
      
      // Resume from specific task if requested
      if (options.resumeFromTask) {
        this.currentTask = Math.max(0, options.resumeFromTask);
      }
      
      // Execute tasks
      const result = await this.executeTasks(sandbox, ui, options);
      
      this.endTime = Date.now();
      
      // Show completion summary
      if (result.completed) {
        await this.showCompletion(ui, result);
      }
      
      return {
        ...result,
        timeSpent: this.endTime - this.startTime,
        moduleName: this.definition.name,
        moduleId: this.definition.id
      };
      
    } catch (error) {
      this.logger.error(`Module execution failed: ${error.message}`);
      throw error;
    }
  }

  /**
   * Show module introduction
   */
  async showIntroduction(ui) {
    await ui.clearScreen();
    
    console.log(`🎯 Starting Module: ${this.definition.name}`);
    console.log(`📝 ${this.definition.description}`);
    console.log();
    console.log('📋 Learning Objectives:');
    this.definition.objectives.forEach((objective, index) => {
      console.log(`   ${index + 1}. ${objective}`);
    });
    console.log();
    console.log('⚡ Commands you\'ll learn:');
    this.definition.commands.forEach(cmd => {
      console.log(`   • ${cmd}`);
    });
    console.log();
    console.log(`⏱️  Estimated time: ${this.definition.estimatedTime}`);
    console.log();
    
    await ui.pressEnterToContinue('Press Enter to begin...');
  }

  /**
   * Execute all tasks in the module
   */
  async executeTasks(sandbox, ui, options) {
    let completedTasks = 0;
    let totalScore = 0;
    const taskResults = {};
    const commands = {};
    
    for (let i = this.currentTask; i < this.tasks.length; i++) {
      const task = this.tasks[i];
      this.currentTask = i;
      
      try {
        const taskResult = await this.executeTask(task, sandbox, ui, i + 1);
        
        if (taskResult.completed) {
          completedTasks++;
          totalScore += taskResult.score || 0;
        }
        
        taskResults[task.id] = taskResult;
        
        // Track commands
        if (taskResult.commands) {
          Object.keys(taskResult.commands).forEach(cmd => {
            commands[cmd] = (commands[cmd] || 0) + taskResult.commands[cmd];
          });
        }
        
        // Break if task failed and not continuing on error
        if (!taskResult.completed && !options.continueOnError) {
          break;
        }
        
      } catch (error) {
        this.logger.error(`Task ${task.id} failed:`, error.message);
        
        if (!options.continueOnError) {
          break;
        }
      }
    }
    
    const completed = completedTasks === this.tasks.length;
    const accuracy = this.tasks.length > 0 ? (completedTasks / this.tasks.length) * 100 : 0;
    
    return {
      completed,
      score: totalScore,
      accuracy,
      completedTasks,
      totalTasks: this.tasks.length,
      tasks: taskResults,
      commands,
      pointsEarned: this.calculatePoints(completedTasks, totalScore, accuracy)
    };
  }

  /**
   * Execute a single task
   */
  async executeTask(task, sandbox, ui, taskNumber) {
    await ui.clearScreen();
    
    // Show task header
    console.log(`📋 Task ${taskNumber}/${this.tasks.length}: ${task.title}`);
    console.log(`📝 ${task.description}`);
    console.log();
    
    // Show demonstration if available
    if (task.demonstration) {
      await this.showDemonstration(task, ui);
    }
    
    // Show instructions
    if (task.instructions) {
      console.log('📖 Instructions:');
      task.instructions.forEach((instruction, index) => {
        console.log(`   ${index + 1}. ${instruction}`);
      });
      console.log();
    }
    
    // Execute task steps
    return await this.executeTaskSteps(task, sandbox, ui);
  }

  /**
   * Show command demonstration
   */
  async showDemonstration(task, ui) {
    console.log('👀 Demonstration:');
    console.log(`   Command: ${task.demonstration.command}`);
    console.log(`   Purpose: ${task.demonstration.purpose}`);
    
    if (task.demonstration.example) {
      console.log(`   Example: ${task.demonstration.example}`);
    }
    
    console.log();
  }

  /**
   * Execute task steps - to be implemented by subclasses
   */
  async executeTaskSteps(task, sandbox, ui) {
    throw new Error('executeTaskSteps must be implemented by subclass');
  }

  /**
   * Show module completion
   */
  async showCompletion(ui, result) {
    await ui.clearScreen();
    
    console.log('🎉 MODULE COMPLETED! 🎉');
    console.log();
    console.log(`✅ ${this.definition.name} finished successfully!`);
    console.log();
    console.log('📊 Results:');
    console.log(`   • Tasks completed: ${result.completedTasks}/${result.totalTasks}`);
    console.log(`   • Accuracy: ${result.accuracy.toFixed(1)}%`);
    console.log(`   • Points earned: ${result.pointsEarned}`);
    console.log(`   • Time spent: ${Math.round((this.endTime - this.startTime) / 1000)}s`);
    console.log();
    
    if (result.accuracy === 100) {
      console.log('🏆 Perfect score! You\'re a natural at Git!');
    } else if (result.accuracy >= 80) {
      console.log('🌟 Great job! You\'ve mastered the basics!');
    } else {
      console.log('👍 Good effort! Practice makes perfect!');
    }
    
    console.log();
    await ui.pressEnterToContinue();
  }

  /**
   * Calculate points earned based on performance
   */
  calculatePoints(completedTasks, totalScore, accuracy) {
    const basePoints = completedTasks * 10;
    const accuracyBonus = accuracy >= 100 ? 50 : accuracy >= 80 ? 25 : 0;
    const speedBonus = this.calculateSpeedBonus();
    
    return basePoints + accuracyBonus + speedBonus;
  }

  /**
   * Calculate speed bonus points
   */
  calculateSpeedBonus() {
    if (!this.startTime || !this.endTime) return 0;
    
    const timeSpent = this.endTime - this.startTime;
    const estimatedTime = this.parseEstimatedTime(this.definition.estimatedTime);
    
    if (timeSpent < estimatedTime * 0.5) {
      return 30; // Very fast
    } else if (timeSpent < estimatedTime * 0.75) {
      return 15; // Fast
    }
    
    return 0;
  }

  /**
   * Parse estimated time string to milliseconds
   */
  parseEstimatedTime(timeString) {
    // Simple parser for "15-20 minutes" format
    const match = timeString.match(/(\d+)[-–]?(\d+)?\s*minutes?/i);
    if (match) {
      const avgMinutes = match[2] ? (parseInt(match[1]) + parseInt(match[2])) / 2 : parseInt(match[1]);
      return avgMinutes * 60 * 1000;
    }
    
    return 15 * 60 * 1000; // Default 15 minutes
  }

  /**
   * Get module progress
   */
  getProgress() {
    return {
      currentTask: this.currentTask,
      totalTasks: this.tasks.length,
      percentage: this.tasks.length > 0 ? (this.currentTask / this.tasks.length) * 100 : 0
    };
  }
}
