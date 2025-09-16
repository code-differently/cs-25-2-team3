/**
 * Git Basics Module - First learning module
 * 
 * Teaches fundamental Git commands: init, add, commit
 * Implements user stories B4, B5, C8, C10
 */

import inquirer from 'inquirer';
import chalk from 'chalk';
import { BaseModule } from './BaseModule.js';

export class GitBasicsModule extends BaseModule {
  
  /**
   * Load tasks for this module
   */
  async loadTasks() {
    this.tasks = [
      {
        id: 'git_init',
        title: 'Initialize Git Repository',
        description: 'Learn to create a new Git repository with git init',
        demonstration: {
          command: 'git init',
          purpose: 'Initialize a new Git repository in the current directory',
          example: 'Creates a .git folder to track your project'
        },
        instructions: [
          'Run the git init command',
          'Observe the output message',
          'Verify that a .git directory was created'
        ],
        expectedCommand: 'git init',
        validation: {
          type: 'git_repo_exists',
          message: 'Git repository should be initialized'
        }
      },
      
      {
        id: 'create_file',
        title: 'Create Your First File',
        description: 'Create a simple text file to start tracking',
        instructions: [
          'Create a file called "hello.txt"',
          'Add some text content to the file',
          'Save the file'
        ],
        validation: {
          type: 'file_exists',
          fileName: 'hello.txt',
          message: 'File hello.txt should exist'
        }
      },
      
      {
        id: 'git_status',
        title: 'Check Repository Status',
        description: 'Learn to view the current state of your repository',
        demonstration: {
          command: 'git status',
          purpose: 'Show the current state of the repository and working directory',
          example: 'Lists untracked, modified, and staged files'
        },
        instructions: [
          'Run git status to see untracked files',
          'Notice that hello.txt appears as untracked',
          'Read the helpful Git messages'
        ],
        expectedCommand: 'git status',
        validation: {
          type: 'command_executed',
          command: 'git status'
        }
      },
      
      {
        id: 'git_add',
        title: 'Stage Your Changes',
        description: 'Add files to the staging area before committing',
        demonstration: {
          command: 'git add <filename>',
          purpose: 'Add files to the staging area to prepare for commit',
          example: 'git add hello.txt - stages hello.txt for commit'
        },
        instructions: [
          'Run git add hello.txt to stage the file',
          'Run git status again to see the change',
          'Notice the file is now in "Changes to be committed"'
        ],
        expectedCommand: 'git add hello.txt',
        validation: {
          type: 'file_staged',
          fileName: 'hello.txt',
          message: 'File hello.txt should be staged for commit'
        }
      },
      
      {
        id: 'git_commit',
        title: 'Make Your First Commit',
        description: 'Save your changes permanently to Git history',
        demonstration: {
          command: 'git commit -m "message"',
          purpose: 'Save staged changes to the repository with a descriptive message',
          example: 'git commit -m "Add hello.txt file" - commits with a message'
        },
        instructions: [
          'Run git commit -m "Add hello.txt file"',
          'Use a descriptive commit message',
          'Verify the commit was successful'
        ],
        expectedCommand: 'git commit',
        validation: {
          type: 'commit_exists',
          message: 'At least one commit should exist'
        }
      },
      
      {
        id: 'git_log',
        title: 'View Commit History',
        description: 'Learn to view your repository\'s commit history',
        demonstration: {
          command: 'git log',
          purpose: 'Display the commit history of the repository',
          example: 'Shows commit hashes, authors, dates, and messages'
        },
        instructions: [
          'Run git log to see your commit history',
          'Notice your commit message and details',
          'Try git log --oneline for a compact view'
        ],
        expectedCommand: 'git log',
        validation: {
          type: 'command_executed',
          command: 'git log'
        }
      }
    ];
  }

  /**
   * Execute task steps with Git-specific validation
   */
  async executeTaskSteps(task, sandbox, ui) {
    const startTime = Date.now();
    let attempts = 0;
    let completed = false;
    let commands = {};

    // Special handling for file creation task
    if (task.id === 'create_file') {
      return await this.handleFileCreation(task, sandbox, ui);
    }

    while (!completed && attempts < 3) {
      attempts++;
      
      try {
        // Get user input for command
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
          
          await ui.sleep(1500);
        } else {
          console.log(chalk.red('❌ ' + validationResult.message));
          
          if (attempts < 3) {
            // Provide hints
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
   * Handle file creation task
   */
  async handleFileCreation(task, sandbox, ui) {
    const startTime = Date.now();
    
    console.log(chalk.blue('📝 Let\'s create a file in your repository!'));
    console.log();
    
    const { content } = await inquirer.prompt([
      {
        type: 'input',
        name: 'content',
        message: 'Enter some text for hello.txt:',
        default: 'Hello, Git World!'
      }
    ]);

    try {
      await sandbox.writeSandboxFile(sandbox.id, 'hello.txt', content);
      console.log(chalk.green('✅ File hello.txt created successfully!'));
      
      // Show file content
      console.log(chalk.gray('File content:'));
      console.log(content);
      
      await ui.sleep(1500);
      
      return {
        completed: true,
        attempts: 1,
        timeSpent: Date.now() - startTime,
        commands: {},
        score: 100
      };
      
    } catch (error) {
      console.log(chalk.red('❌ Failed to create file: ' + error.message));
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
   * Validate and execute Git command
   */
  async validateAndExecuteCommand(userCommand, task, sandbox) {
    const trimmedCommand = userCommand.trim();
    
    // Check if command starts with git
    if (!trimmedCommand.startsWith('git ')) {
      return {
        success: false,
        message: 'Commands should start with "git"'
      };
    }

    // Parse the command
    const parts = trimmedCommand.split(' ');
    const gitCommand = parts[1];
    const args = parts.slice(2);

    try {
      // Execute the command
      const result = await sandbox.executeGitCommand(sandbox, gitCommand, args);
      
      // Validate against task requirements
      const validationResult = await this.validateTaskRequirement(task, sandbox, gitCommand, args);
      
      return {
        success: validationResult.success,
        message: validationResult.message,
        output: result.stdout || result.stderr
      };
      
    } catch (error) {
      return {
        success: false,
        message: `Command failed: ${error.message}`
      };
    }
  }

  /**
   * Validate task-specific requirements
   */
  async validateTaskRequirement(task, sandbox, command, args) {
    const validation = task.validation;
    
    switch (validation.type) {
      case 'git_repo_exists':
        return await this.validateGitRepoExists(sandbox);
      
      case 'command_executed':
        return this.validateCommandExecuted(task.expectedCommand, command, args);
      
      case 'file_staged':
        return await this.validateFileStaged(sandbox, validation.fileName);
      
      case 'commit_exists':
        return await this.validateCommitExists(sandbox);
      
      default:
        return { success: true, message: 'Command executed successfully' };
    }
  }

  /**
   * Validate Git repository exists
   */
  async validateGitRepoExists(sandbox) {
    try {
      await sandbox.executeGitCommand(sandbox, 'status');
      return { success: true, message: 'Git repository initialized successfully' };
    } catch (error) {
      return { success: false, message: 'Git repository not found. Make sure to run git init.' };
    }
  }

  /**
   * Validate command was executed correctly
   */
  validateCommandExecuted(expectedCommand, actualCommand, args) {
    const expected = expectedCommand.replace('git ', '');
    
    if (actualCommand === expected) {
      return { success: true, message: 'Command executed successfully' };
    }
    
    return { 
      success: false, 
      message: `Expected "${expectedCommand}", but got "git ${actualCommand} ${args.join(' ')}".trim()` 
    };
  }

  /**
   * Validate file is staged
   */
  async validateFileStaged(sandbox, fileName) {
    try {
      const status = await sandbox.getSandboxGitStatus(sandbox.id);
      
      if (status.staged.includes(fileName)) {
        return { success: true, message: `File ${fileName} is staged for commit` };
      }
      
      return { 
        success: false, 
        message: `File ${fileName} is not staged. Use "git add ${fileName}" to stage it.` 
      };
      
    } catch (error) {
      return { success: false, message: 'Could not check file status' };
    }
  }

  /**
   * Validate at least one commit exists
   */
  async validateCommitExists(sandbox) {
    try {
      const commits = await sandbox.getSandboxCommitHistory(sandbox.id, 1);
      
      if (commits.length > 0) {
        return { success: true, message: 'Commit created successfully' };
      }
      
      return { 
        success: false, 
        message: 'No commits found. Make sure to stage files and commit them.' 
      };
      
    } catch (error) {
      return { success: false, message: 'Could not check commit history' };
    }
  }

  /**
   * Provide helpful hints based on attempt number
   */
  async provideHint(task, attemptNumber) {
    const hints = {
      git_init: [
        'Hint: The command to initialize a Git repository is "git init"',
        'Hint: Make sure to type exactly "git init" (without quotes)',
        'Hint: Remember, Git commands always start with "git"'
      ],
      git_status: [
        'Hint: Use "git status" to see the current state',
        'Hint: Type "git status" to check what files are tracked',
        'Hint: The status command shows you untracked and modified files'
      ],
      git_add: [
        'Hint: Use "git add" followed by the filename',
        'Hint: Try "git add hello.txt" to stage the file',
        'Hint: The add command prepares files for commit'
      ],
      git_commit: [
        'Hint: Use "git commit -m" followed by a message in quotes',
        'Hint: Try \'git commit -m "Add hello.txt file"\'',
        'Hint: Commit messages should describe what you did'
      ],
      git_log: [
        'Hint: Use "git log" to see commit history',
        'Hint: Type "git log" to view your commits',
        'Hint: You can also try "git log --oneline" for a shorter view'
      ]
    };

    const taskHints = hints[task.id] || ['Hint: Check the instructions above for guidance'];
    const hintIndex = Math.min(attemptNumber - 1, taskHints.length - 1);
    
    console.log(chalk.yellow('💡 ' + taskHints[hintIndex]));
  }
}
