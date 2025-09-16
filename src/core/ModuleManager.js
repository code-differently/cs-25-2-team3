/**
 * Module Manager - Handles learning module definitions and execution
 * 
 * Implements user stories:
 * - B4: View detailed information about each learning module
 * - B5: Progress through structured learning tasks
 * - C10: View command demonstrations and examples
 */

import path from 'path';
import { GitBasicsModule } from '../modules/GitBasicsModule.js';
import { WorkingWithChangesModule } from '../modules/WorkingWithChangesModule.js';
import { BranchingFundamentalsModule } from '../modules/BranchingFundamentalsModule.js';

export class ModuleManager {
  constructor(config, logger) {
    this.config = config;
    this.logger = logger;
    this.modules = new Map();
    this.moduleDefinitions = [];
  }

  /**
   * Initialize module manager
   */
  async initialize() {
    await this.loadModuleDefinitions();
    this.logger.debug('Module manager initialized');
  }

  /**
   * Load all available module definitions
   */
  async loadModuleDefinitions() {
    // Define the three core modules as specified in user stories
    this.moduleDefinitions = [
      {
        id: 'git_basics',
        name: 'Git Basics',
        description: 'Learn init, add, commit - Foundation of Git',
        difficulty: 'beginner',
        estimatedTime: '15-20 minutes',
        objectives: [
          'Initialize a Git repository',
          'Add files to staging area',
          'Create your first commit',
          'Understand Git workflow basics'
        ],
        prerequisites: [],
        commands: ['git init', 'git add', 'git commit'],
        completed: false,
        inProgress: false,
        moduleClass: GitBasicsModule
      },
      {
        id: 'working_with_changes',
        name: 'Working with Changes',
        description: 'Master status, diff, log - Track your progress',
        difficulty: 'beginner',
        estimatedTime: '20-25 minutes',
        objectives: [
          'Check repository status',
          'View differences between commits',
          'Explore commit history',
          'Understand Git change tracking'
        ],
        prerequisites: ['git_basics'],
        commands: ['git status', 'git diff', 'git log'],
        completed: false,
        inProgress: false,
        moduleClass: WorkingWithChangesModule
      },
      {
        id: 'branching_fundamentals',
        name: 'Branching Fundamentals',
        description: 'Branch, checkout, merge - Parallel development',
        difficulty: 'intermediate',
        estimatedTime: '25-30 minutes',
        objectives: [
          'Create and switch between branches',
          'Understand branch workflows',
          'Merge branches safely',
          'Resolve basic merge conflicts'
        ],
        prerequisites: ['git_basics', 'working_with_changes'],
        commands: ['git branch', 'git checkout', 'git merge'],
        completed: false,
        inProgress: false,
        moduleClass: BranchingFundamentalsModule
      }
    ];

    this.logger.debug(`Loaded ${this.moduleDefinitions.length} module definitions`);
  }

  /**
   * Get all available modules with status
   */
  async getAvailableModules() {
    // TODO: Get completion status from ProgressManager
    return this.moduleDefinitions.map(def => ({
      ...def,
      // These would be updated from ProgressManager
      completed: false,
      inProgress: false
    }));
  }

  /**
   * Get module by ID
   */
  getModuleDefinition(moduleId) {
    return this.moduleDefinitions.find(def => def.id === moduleId);
  }

  /**
   * Load and instantiate a specific module
   */
  async loadModule(moduleId) {
    // Check if module is already loaded
    if (this.modules.has(moduleId)) {
      return this.modules.get(moduleId);
    }

    const definition = this.getModuleDefinition(moduleId);
    if (!definition) {
      throw new Error(`Module not found: ${moduleId}`);
    }

    try {
      // Instantiate the module class
      const ModuleClass = definition.moduleClass;
      const moduleInstance = new ModuleClass(definition, this.config, this.logger);
      
      // Initialize the module
      await moduleInstance.initialize();
      
      // Cache the instance
      this.modules.set(moduleId, moduleInstance);
      
      this.logger.debug(`Module loaded: ${moduleId}`);
      return moduleInstance;
      
    } catch (error) {
      this.logger.error(`Failed to load module ${moduleId}:`, error.message);
      throw new Error(`Failed to load module: ${error.message}`);
    }
  }

  /**
   * Check if module prerequisites are met
   */
  arePrerequisitesMet(moduleId, completedModules = []) {
    const definition = this.getModuleDefinition(moduleId);
    if (!definition) {
      return false;
    }

    return definition.prerequisites.every(prereq => 
      completedModules.includes(prereq)
    );
  }

  /**
   * Get recommended next module based on progress
   */
  getRecommendedModule(completedModules = []) {
    // Find the first module where prerequisites are met but not completed
    for (const definition of this.moduleDefinitions) {
      if (!completedModules.includes(definition.id) && 
          this.arePrerequisitesMet(definition.id, completedModules)) {
        return definition;
      }
    }
    return null;
  }

  /**
   * Get module difficulty level
   */
  getModuleDifficulty(moduleId) {
    const definition = this.getModuleDefinition(moduleId);
    return definition ? definition.difficulty : 'unknown';
  }

  /**
   * Validate module exists and is accessible
   */
  validateModule(moduleId) {
    const definition = this.getModuleDefinition(moduleId);
    if (!definition) {
      throw new Error(`Module '${moduleId}' does not exist`);
    }

    // Additional validation could go here
    return true;
  }

  /**
   * Get module by name (fuzzy search)
   */
  findModuleByName(name) {
    const lowerName = name.toLowerCase();
    return this.moduleDefinitions.find(def => 
      def.name.toLowerCase().includes(lowerName) ||
      def.id.toLowerCase().includes(lowerName)
    );
  }

  /**
   * Get modules filtered by difficulty
   */
  getModulesByDifficulty(difficulty) {
    return this.moduleDefinitions.filter(def => def.difficulty === difficulty);
  }

  /**
   * Get module statistics
   */
  getModuleStatistics() {
    const stats = {
      total: this.moduleDefinitions.length,
      byDifficulty: {
        beginner: 0,
        intermediate: 0,
        advanced: 0
      },
      totalCommands: 0,
      averageTime: 0
    };

    this.moduleDefinitions.forEach(def => {
      stats.byDifficulty[def.difficulty]++;
      stats.totalCommands += def.commands.length;
    });

    return stats;
  }

  /**
   * Export module definitions
   */
  exportModuleDefinitions() {
    return {
      version: '1.0.0',
      exportDate: new Date().toISOString(),
      modules: this.moduleDefinitions.map(def => ({
        ...def,
        moduleClass: def.moduleClass.name // Just export class name
      }))
    };
  }

  /**
   * Clear module cache
   */
  clearCache() {
    this.modules.clear();
    this.logger.debug('Module cache cleared');
  }

  /**
   * Reload modules
   */
  async reload() {
    this.clearCache();
    await this.loadModuleDefinitions();
    this.logger.info('Modules reloaded');
  }

  /**
   * Get module execution order based on dependencies
   */
  getExecutionOrder() {
    const order = [];
    const completed = new Set();
    
    while (order.length < this.moduleDefinitions.length) {
      const available = this.moduleDefinitions.filter(def => 
        !completed.has(def.id) && 
        def.prerequisites.every(prereq => completed.has(prereq))
      );
      
      if (available.length === 0) {
        break; // Circular dependency or other issue
      }
      
      const next = available[0];
      order.push(next);
      completed.add(next.id);
    }
    
    return order;
  }
}
