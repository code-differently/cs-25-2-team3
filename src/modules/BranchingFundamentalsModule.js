/**
 * Branching Fundamentals Module - Third learning module
 * 
 * Teaches Git commands: branch, checkout, merge
 * Implements user stories B4, B5, C8, C10
 */

import inquirer from 'inquirer';
import chalk from 'chalk';
import { BaseModule } from './BaseModule.js';

export class BranchingFundamentalsModule extends BaseModule {
  
  /**
   * Load tasks for this module
   */
  async loadTasks() {
    this.tasks = [
      {
        id: 'setup_branching_repo',
        title: 'Set Up Branching Practice Repository',
        description: 'Create a repository with initial content for branching practice',
        instructions: [
          'We\'ll set up a repository with some commits',
          'This gives us a base to practice branching'
        ],
        isSetup: true
      },
      
      {
        id: 'list_branches',
        title: 'List Current Branches',
        description: 'See what branches exist in your repository',
        demonstration: {
          command: 'git branch',
          purpose: 'List all local branches and show which one is currently active',
          example: 'Shows branches with * indicating the current branch'
        },
        instructions: [
          'Run git branch to see existing branches',
          'Notice the * next to the current branch',
          'You should see the main branch'
        ],
        expectedCommand: 'git branch'
      },
      
      {
        id: 'create_branch',
        title: 'Create a New Branch',
        description: 'Create a new branch for feature development',
        demonstration: {
          command: 'git branch <branch-name>',
          purpose: 'Create a new branch pointing to the current commit',
          example: 'git branch feature-branch - creates a new branch called feature-branch'
        },
        instructions: [
          'Create a new branch called "feature-update"',
          'Use: git branch feature-update',
          'This creates the branch but doesn\'t switch to it yet'
        ],
        expectedCommand: 'git branch feature-update',
        validation: {
          type: 'branch_exists',
          branchName: 'feature-update'
        }
      },
      
      {
        id: 'switch_branch',
        title: 'Switch to New Branch',
        description: 'Change to your newly created branch',
        demonstration: {
          command: 'git checkout <branch-name>',
          purpose: 'Switch to a different branch',
          example: 'git checkout feature-update - switches to the feature-update branch'
        },
        instructions: [
          'Switch to the feature-update branch',
          'Use: git checkout feature-update',
          'Notice how this changes your current branch'
        ],
        expectedCommand: 'git checkout feature-update',
        validation: {
          type: 'current_branch',
          branchName: 'feature-update'
        }
      },
      
      {
        id: 'make_branch_changes',
        title: 'Make Changes on the Branch',
        description: 'Modify files while on the new branch',
        instructions: [
          'We\'ll modify a file while on the feature-update branch',
          'This shows how branches allow parallel development'
        ]
      },
      
      {
        id: 'commit_branch_changes',
        title: 'Commit Changes on Branch',
        description: 'Save your branch-specific changes',
        instructions: [
          'Stage and commit your changes on the feature branch',
          'Use git add and git commit as before',
          'This commit will only exist on this branch'
        ],
        expectedCommand: 'git commit'
      },
      
      {
        id: 'switch_back_main',
        title: 'Switch Back to Main',
        description: 'Return to the main branch',
        instructions: [
          'Switch back to the main branch',
          'Use: git checkout main',
          'Notice how your changes are not visible here'
        ],
        expectedCommand: 'git checkout main',
        validation: {
          type: 'current_branch',
          branchName: 'main'
        }
      },
      
      {
        id: 'merge_branch',
        title: 'Merge Your Branch',
        description: 'Bring changes from feature branch into main',
        demonstration: {
          command: 'git merge <branch-name>',
          purpose: 'Merge changes from another branch into the current branch',
          example: 'git merge feature-update - merges feature-update into current branch'
        },
        instructions: [
          'Merge the feature-update branch into main',
          'Use: git merge feature-update',
          'This brings your feature changes into main'
        ],
        expectedCommand: 'git merge feature-update',
        validation: {
          type: 'merge_completed',
          branchName: 'feature-update'
        }
      }
    ];
  }

  /**
   * Execute task steps with specific handling for this module
   */
  async executeTaskSteps(task, sandbox, ui) {
    // Handle setup task
    if (task.isSetup) {
      return await this.handleSetupTask(task, sandbox, ui);
    }
    
    // Handle file modification task
    if (task.id === 'make_branch_changes') {
      return await this.handleBranchChanges(task, sandbox, ui);
    }
    
    // Handle command-based tasks
    return await this.handleCommandTask(task, sandbox, ui);
  }

  /**
   * Handle repository setup for branching
   */
  async handleSetupTask(task, sandbox, ui) {
    const startTime = Date.now();
    
    try {
      // Create initial files
      await sandbox.writeSandboxFile(sandbox.id, 'app.py', '#!/usr/bin/env python3\nprint("Hello, World!")\n');
      await sandbox.writeSandboxFile(sandbox.id, 'README.md', '# My Python App\n\nA simple Python application.\n');
      
      // Initialize and make commits
      await sandbox.executeGitCommand(sandbox, 'init');
      await sandbox.executeGitCommand(sandbox, 'config', ['user.name', 'Git Trainer']);
      await sandbox.executeGitCommand(sandbox, 'config', ['user.email', 'trainer@gitlearning.local']);
      await sandbox.executeGitCommand(sandbox, 'add', ['.']);
      await sandbox.executeGitCommand(sandbox, 'commit', ['-m', 'Initial commit: Add basic Python app']);
      
      // Make another commit
      await sandbox.writeSandboxFile(sandbox.id, 'requirements.txt', '# Dependencies will go here\n');
      await sandbox.executeGitCommand(sandbox, 'add', ['requirements.txt']);
      await sandbox.executeGitCommand(sandbox, 'commit', ['-m', 'Add requirements.txt']);
      
      console.log(chalk.green('✅ Branching practice repository set up successfully!'));
      console.log('Created files: app.py, README.md, requirements.txt');
      console.log('Made 2 commits to establish history');
      
      await ui.sleep(2000);
      
      return {
        completed: true,
        attempts: 1,
        timeSpent: Date.now() - startTime,
        commands: {},
        score: 100
      };
      
    } catch (error) {
      console.log(chalk.red('❌ Failed to set up repository: ' + error.message));
      return {
        completed: false,
        attempts: 1,
        timeSpent: Date.now() - startTime,
        commands: {},
        score: 0
      };
    }
  }

  /**
   * Handle making changes on branch
   */
  async handleBranchChanges(task, sandbox, ui) {
    const startTime = Date.now();
    
    console.log(chalk.blue('📝 Let\'s make some changes on the feature branch!'));
    console.log();
    
    try {
      // Show current branch
      const result = await sandbox.executeGitCommand(sandbox, 'branch', ['--show-current']);
      console.log(`Current branch: ${chalk.yellow(result.stdout)}`);
      console.log();
      
      const currentContent = await sandbox.readSandboxFile(sandbox.id, 'app.py');
      console.log('Current content of app.py:');
      console.log(chalk.gray(currentContent));
      console.log();
      
      const { newFeature } = await inquirer.prompt([
        {
          type: 'list',
          name: 'newFeature',
          message: 'What feature would you like to add?',
          choices: [
            { name: 'Add a greeting function', value: 'greeting' },
            { name: 'Add user input', value: 'input' },
            { name: 'Add a calculation', value: 'calc' }
          ]
        }
      ]);

      let updatedContent = currentContent;
      
      switch (newFeature) {
        case 'greeting':
          updatedContent += '\ndef greet(name):\n    return f"Hello, {name}!"\n\nprint(greet("World"))\n';
          break;
        case 'input':
          updatedContent += '\nname = input("What\'s your name? ")\nprint(f"Nice to meet you, {name}!")\n';
          break;
        case 'calc':
          updatedContent += '\ndef add_numbers(a, b):\n    return a + b\n\nresult = add_numbers(5, 3)\nprint(f"5 + 3 = {result}")\n';
          break;
      }
      
      await sandbox.writeSandboxFile(sandbox.id, 'app.py', updatedContent);
      
      console.log(chalk.green('✅ Feature added successfully!'));
      console.log('Updated app.py with new functionality');
      
      await ui.sleep(1500);
      
      return {
        completed: true,
        attempts: 1,
        timeSpent: Date.now() - startTime,
        commands: {},
        score: 100
      };
      
    } catch (error) {
      console.log(chalk.red('❌ Failed to modify file: ' + error.message));
      return {
        completed: false,
        attempts: 1,
        timeSpent: Date.now() - startTime,
        commands: {},
        score: 0
      };
    }
  }

  /**
   * Handle command-based tasks with branching validation
   */
  async handleCommandTask(task, sandbox, ui) {
    const startTime = Date.now();
    let attempts = 0;
    let completed = false;
    let commands = {};

    while (!completed && attempts < 3) {
      attempts++;
      
      try {
        const { command } = await inquirer.prompt([
          {
            type: 'input',
            name: 'command',
            message: chalk.blue('Enter Git command:'),
            validate: (input) => {
              if (!input.trim()) {
                return 'Please enter a command';
              }
              return true;
            }
          }
        ]);

        // Track command usage
        const baseCommand = command.split(' ')[0];
        commands[baseCommand] = (commands[baseCommand] || 0) + 1;

        // Validate and execute command
        const validationResult = await this.validateAndExecuteCommand(command, task, sandbox);
        
        if (validationResult.success) {
          completed = true;
          console.log(chalk.green('✅ Correct! Task completed successfully.'));
          
          if (validationResult.output) {
            console.log(chalk.gray('Output:'));
            console.log(validationResult.output);
          }
          
          // Special handling for commit task
          if (task.id === 'commit_branch_changes') {
            await this.handleStageAndCommit(sandbox);
          }
          
          await ui.sleep(2000);
        } else {
          console.log(chalk.red('❌ ' + validationResult.message));
          
          if (attempts < 3) {
            await this.provideHint(task, attempts);
            console.log(chalk.yellow('Try again...'));
            await ui.sleep(1000);
          }
        }

      } catch (error) {
        console.log(chalk.red('Error: ' + error.message));
        
        if (attempts < 3) {
          await ui.sleep(1000);
        }
      }
    }

    if (!completed) {
      console.log(chalk.red('❌ Task not completed. Moving to next task.'));
      await ui.pressEnterToContinue();
    }

    return {
      completed,
      attempts,
      timeSpent: Date.now() - startTime,
      commands,
      score: completed ? Math.max(0, 100 - (attempts - 1) * 25) : 0
    };
  }

  /**
   * Handle staging and committing for branch changes
   */
  async handleStageAndCommit(sandbox) {
    try {
      // First, let user stage the changes
      console.log(chalk.blue('Now let\'s stage and commit your changes...'));
      
      const { shouldStage } = await inquirer.prompt([
        {
          type: 'confirm',
          name: 'shouldStage',
          message: 'Stage the changes first? (git add app.py)',
          default: true
        }
      ]);

      if (shouldStage) {
        await sandbox.executeGitCommand(sandbox, 'add', ['app.py']);
        console.log(chalk.green('✅ Changes staged'));
      }

      const { commitMessage } = await inquirer.prompt([
        {
          type: 'input',
          name: 'commitMessage',
          message: 'Enter commit message:',
          default: 'Add new feature to app.py'
        }
      ]);

      await sandbox.executeGitCommand(sandbox, 'commit', ['-m', commitMessage]);
      console.log(chalk.green('✅ Changes committed on branch'));
      
    } catch (error) {
      console.log(chalk.yellow('⚠️ Automated staging/commit had issues, but that\'s okay for learning'));
    }
  }

  /**
   * Validate and execute Git command with branching validation
   */
  async validateAndExecuteCommand(userCommand, task, sandbox) {
    const trimmedCommand = userCommand.trim();
    
    if (!trimmedCommand.startsWith('git ')) {
      return {
        success: false,
        message: 'Commands should start with "git"'
      };
    }

    const parts = trimmedCommand.split(' ');
    const gitCommand = parts[1];
    const args = parts.slice(2);

    try {
      const result = await sandbox.executeGitCommand(sandbox, gitCommand, args);
      
      // Validate task-specific requirements
      if (task.validation) {
        const validationResult = await this.validateTaskRequirement(task, sandbox, gitCommand, args);
        return {
          success: validationResult.success,
          message: validationResult.message,
          output: result.stdout || result.stderr
        };
      }
      
      // Check if this matches expected command
      const expectedBase = task.expectedCommand.replace('git ', '');
      if (gitCommand === expectedBase || trimmedCommand === task.expectedCommand) {
        return {
          success: true,
          message: 'Command executed successfully',
          output: result.stdout || result.stderr
        };
      } else {
        return {
          success: false,
          message: `Expected "${task.expectedCommand}" but got "${trimmedCommand}"`
        };
      }
      
    } catch (error) {
      return {
        success: false,
        message: `Command failed: ${error.message}`
      };
    }
  }

  /**
   * Validate branching-specific task requirements
   */
  async validateTaskRequirement(task, sandbox, command, args) {
    const validation = task.validation;
    
    switch (validation.type) {
      case 'branch_exists':
        return await this.validateBranchExists(sandbox, validation.branchName);
      
      case 'current_branch':
        return await this.validateCurrentBranch(sandbox, validation.branchName);
      
      case 'merge_completed':
        return await this.validateMergeCompleted(sandbox, validation.branchName);
      
      default:
        return { success: true, message: 'Command executed successfully' };
    }
  }

  /**
   * Validate that a branch exists
   */
  async validateBranchExists(sandbox, branchName) {
    try {
      const result = await sandbox.executeGitCommand(sandbox, 'branch');
      const branches = result.stdout.split('\n').map(b => b.replace('*', '').trim());
      
      if (branches.includes(branchName)) {
        return { success: true, message: `Branch "${branchName}" created successfully` };
      }
      
      return {
        success: false,
        message: `Branch "${branchName}" was not created. Use "git branch ${branchName}"`
      };
      
    } catch (error) {
      return { success: false, message: 'Could not check branches' };
    }
  }

  /**
   * Validate current branch
   */
  async validateCurrentBranch(sandbox, expectedBranch) {
    try {
      const result = await sandbox.executeGitCommand(sandbox, 'branch', ['--show-current']);
      const currentBranch = result.stdout.trim();
      
      if (currentBranch === expectedBranch) {
        return { success: true, message: `Successfully switched to "${expectedBranch}"` };
      }
      
      return {
        success: false,
        message: `Expected to be on "${expectedBranch}" but currently on "${currentBranch}"`
      };
      
    } catch (error) {
      return { success: false, message: 'Could not determine current branch' };
    }
  }

  /**
   * Validate merge was completed
   */
  async validateMergeCompleted(sandbox, branchName) {
    try {
      // Check if merge commit exists in log
      const result = await sandbox.executeGitCommand(sandbox, 'log', ['--oneline', '-5']);
      const logOutput = result.stdout;
      
      if (logOutput.includes('Merge') || logOutput.includes(branchName)) {
        return { success: true, message: `Successfully merged "${branchName}"` };
      }
      
      return {
        success: false,
        message: `Merge not detected. Make sure you're on main and run "git merge ${branchName}"`
      };
      
    } catch (error) {
      return { success: false, message: 'Could not verify merge' };
    }
  }

  /**
   * Provide helpful hints for branching tasks
   */
  async provideHint(task, attemptNumber) {
    const hints = {
      list_branches: [
        'Hint: Use "git branch" to list all branches',
        'Hint: Type "git branch" to see existing branches',
        'Hint: The current branch will have a * next to it'
      ],
      create_branch: [
        'Hint: Use "git branch" followed by the branch name',
        'Hint: Try "git branch feature-update" to create the branch',
        'Hint: This creates the branch but doesn\'t switch to it'
      ],
      switch_branch: [
        'Hint: Use "git checkout" followed by the branch name',
        'Hint: Try "git checkout feature-update" to switch branches',
        'Hint: You can also use "git switch feature-update" in newer Git versions'
      ],
      commit_branch_changes: [
        'Hint: First stage with "git add", then commit with "git commit -m"',
        'Hint: Remember to stage your changes before committing',
        'Hint: Use descriptive commit messages for your branch changes'
      ],
      switch_back_main: [
        'Hint: Use "git checkout main" to return to main branch',
        'Hint: Type "git checkout main" to switch back',
        'Hint: Notice how your feature changes disappear from the working directory'
      ],
      merge_branch: [
        'Hint: Use "git merge" followed by the branch name',
        'Hint: Try "git merge feature-update" while on main branch',
        'Hint: Make sure you\'re on the target branch (main) before merging'
      ]
    };

    const taskHints = hints[task.id] || ['Hint: Check the instructions above for guidance'];
    const hintIndex = Math.min(attemptNumber - 1, taskHints.length - 1);
    
    console.log(chalk.yellow('💡 ' + taskHints[hintIndex]));
  }
}
