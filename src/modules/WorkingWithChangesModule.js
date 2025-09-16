/**
 * Working with Changes Module - Second learning module
 * 
 * Teaches Git commands: status, diff, log
 * Implements user stories B4, B5, C8, C10
 */

import inquirer from 'inquirer';
import chalk from 'chalk';
import { BaseModule } from './BaseModule.js';

export class WorkingWithChangesModule extends BaseModule {
  
  /**
   * Load tasks for this module
   */
  async loadTasks() {
    this.tasks = [
      {
        id: 'setup_repo',
        title: 'Set Up Practice Repository',
        description: 'Create a repository with some initial content',
        instructions: [
          'We\'ll set up a practice repository for you',
          'This will have some files to work with'
        ],
        isSetup: true
      },
      
      {
        id: 'git_status_basic',
        title: 'Check Repository Status',
        description: 'Learn to view the current state of your repository',
        demonstration: {
          command: 'git status',
          purpose: 'Show the status of files in your working directory and staging area',
          example: 'Shows modified, staged, and untracked files'
        },
        instructions: [
          'Run git status to see the current state',
          'Notice which files have been modified',
          'Read the helpful Git messages'
        ],
        expectedCommand: 'git status'
      },
      
      {
        id: 'modify_file',
        title: 'Modify an Existing File',
        description: 'Make changes to see how Git tracks them',
        instructions: [
          'Edit the content of hello.txt',
          'Add a new line or change existing text',
          'Save the file'
        ]
      },
      
      {
        id: 'git_diff',
        title: 'View File Differences',
        description: 'See exactly what changed in your files',
        demonstration: {
          command: 'git diff',
          purpose: 'Show the differences between working directory and staging area',
          example: 'Displays line-by-line changes with + and - markers'
        },
        instructions: [
          'Run git diff to see your changes',
          'Notice the + and - signs showing additions and deletions',
          'Try git diff --staged to see staged changes'
        ],
        expectedCommand: 'git diff'
      },
      
      {
        id: 'stage_changes',
        title: 'Stage Your Changes',
        description: 'Add modified files to the staging area',
        instructions: [
          'Use git add to stage your modified file',
          'Run git status again to see the change',
          'Notice how the file moved to "staged" section'
        ],
        expectedCommand: 'git add'
      },
      
      {
        id: 'commit_changes',
        title: 'Commit Your Changes',
        description: 'Save your staged changes to Git history',
        instructions: [
          'Commit your staged changes with a descriptive message',
          'Use git commit -m "Your message here"',
          'Make the message descriptive of what you changed'
        ],
        expectedCommand: 'git commit'
      },
      
      {
        id: 'git_log_explore',
        title: 'Explore Commit History',
        description: 'Learn different ways to view your commit history',
        demonstration: {
          command: 'git log',
          purpose: 'Display the commit history with full details',
          example: 'Shows commit hashes, authors, dates, and full messages'
        },
        instructions: [
          'Run git log to see full commit history',
          'Try git log --oneline for a compact view',
          'Try git log --graph for a visual representation'
        ],
        expectedCommand: 'git log'
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
    if (task.id === 'modify_file') {
      return await this.handleFileModification(task, sandbox, ui);
    }
    
    // Handle command-based tasks
    return await this.handleCommandTask(task, sandbox, ui);
  }

  /**
   * Handle repository setup
   */
  async handleSetupTask(task, sandbox, ui) {
    const startTime = Date.now();
    
    try {
      // Create initial files
      await sandbox.writeSandboxFile(sandbox.id, 'hello.txt', 'Hello, Git World!\nThis is my first Git repository.');
      await sandbox.writeSandboxFile(sandbox.id, 'README.md', '# My Git Practice\n\nThis is a practice repository for learning Git.');
      
      // Initialize and make initial commit
      await sandbox.executeGitCommand(sandbox, 'init');
      await sandbox.executeGitCommand(sandbox, 'config', ['user.name', 'Git Trainer']);
      await sandbox.executeGitCommand(sandbox, 'config', ['user.email', 'trainer@gitlearning.local']);
      await sandbox.executeGitCommand(sandbox, 'add', ['.']);
      await sandbox.executeGitCommand(sandbox, 'commit', ['-m', 'Initial commit']);
      
      console.log(chalk.green('✅ Practice repository set up successfully!'));
      console.log('Created files: hello.txt, README.md');
      console.log('Made initial commit with both files');
      
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
   * Handle file modification task
   */
  async handleFileModification(task, sandbox, ui) {
    const startTime = Date.now();
    
    console.log(chalk.blue('📝 Let\'s modify a file to see how Git tracks changes!'));
    console.log();
    
    const currentContent = await sandbox.readSandboxFile(sandbox.id, 'hello.txt');
    console.log('Current content of hello.txt:');
    console.log(chalk.gray(currentContent));
    console.log();
    
    const { newContent } = await inquirer.prompt([
      {
        type: 'input',
        name: 'newContent',
        message: 'Add a new line to hello.txt:',
        default: 'I\'m learning Git commands!'
      }
    ]);

    try {
      const updatedContent = currentContent + '\n' + newContent;
      await sandbox.writeSandboxFile(sandbox.id, 'hello.txt', updatedContent);
      
      console.log(chalk.green('✅ File modified successfully!'));
      console.log('New content:');
      console.log(chalk.gray(updatedContent));
      
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
   * Handle command-based tasks
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

    // Show task result
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
   * Validate and execute Git command
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
      
      // Check if this matches expected command
      const expectedBase = task.expectedCommand.replace('git ', '');
      if (gitCommand === expectedBase || gitCommand === task.expectedCommand) {
        return {
          success: true,
          message: 'Command executed successfully',
          output: result.stdout || result.stderr
        };
      } else {
        return {
          success: false,
          message: `Expected "${task.expectedCommand}" but got "git ${gitCommand}"`
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
   * Provide helpful hints based on attempt number
   */
  async provideHint(task, attemptNumber) {
    const hints = {
      git_status_basic: [
        'Hint: Use "git status" to see the current state',
        'Hint: Type "git status" to check file changes',
        'Hint: The status command shows modified, staged, and untracked files'
      ],
      git_diff: [
        'Hint: Use "git diff" to see changes in files',
        'Hint: Type "git diff" to view line-by-line differences',
        'Hint: Git diff shows what changed with + and - markers'
      ],
      stage_changes: [
        'Hint: Use "git add" followed by filename or "." for all',
        'Hint: Try "git add hello.txt" to stage the modified file',
        'Hint: The add command prepares files for commit'
      ],
      commit_changes: [
        'Hint: Use "git commit -m" followed by a message in quotes',
        'Hint: Try \'git commit -m "Update hello.txt"\'',
        'Hint: Commit messages should describe what you changed'
      ],
      git_log_explore: [
        'Hint: Use "git log" to see commit history',
        'Hint: Type "git log" to view your commits',
        'Hint: Try "git log --oneline" or "git log --graph" for different views'
      ]
    };

    const taskHints = hints[task.id] || ['Hint: Check the instructions above for guidance'];
    const hintIndex = Math.min(attemptNumber - 1, taskHints.length - 1);
    
    console.log(chalk.yellow('💡 ' + taskHints[hintIndex]));
  }
}
