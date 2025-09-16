/**
 * Sandbox Manager - Creates and manages isolated Git environments
 * 
 * Implements user story:
 * - C9: Work within isolated sandbox repositories for safe practice
 */

import fs from 'fs/promises';
import path from 'path';
import os from 'os';
import { execSync, spawn } from 'child_process';
import { promisify } from 'util';

export class SandboxManager {
  constructor(config, logger) {
    this.config = config;
    this.logger = logger;
    this.sandboxRoot = path.join(os.tmpdir(), 'git-trainer-sandboxes');
    this.activeSandboxes = new Map();
  }

  /**
   * Initialize sandbox manager
   */
  async initialize() {
    await this.ensureSandboxDirectory();
    await this.cleanupOldSandboxes();
    this.logger.debug('Sandbox manager initialized');
  }

  /**
   * Ensure sandbox root directory exists
   */
  async ensureSandboxDirectory() {
    try {
      await fs.mkdir(this.sandboxRoot, { recursive: true });
    } catch (error) {
      this.logger.error('Failed to create sandbox directory:', error.message);
      throw error;
    }
  }

  /**
   * Create a new sandbox environment for a module
   */
  async createSandbox(moduleName, options = {}) {
    const sandboxId = this.generateSandboxId(moduleName);
    const sandboxPath = path.join(this.sandboxRoot, sandboxId);

    try {
      // Create sandbox directory
      await fs.mkdir(sandboxPath, { recursive: true });

      // Initialize the sandbox
      const sandbox = {
        id: sandboxId,
        moduleName,
        path: sandboxPath,
        created: new Date().toISOString(),
        gitInitialized: false,
        files: [],
        commits: [],
        branches: [],
        options: { ...options }
      };

      // Set up initial files if specified
      if (options.initialFiles) {
        await this.createInitialFiles(sandbox, options.initialFiles);
      }

      // Initialize Git repository if needed
      if (options.initGit !== false) {
        await this.initializeGitRepo(sandbox);
      }

      // Track the sandbox
      this.activeSandboxes.set(sandboxId, sandbox);

      this.logger.debug(`Sandbox created: ${sandboxId} at ${sandboxPath}`);
      return sandbox;

    } catch (error) {
      this.logger.error(`Failed to create sandbox for ${moduleName}:`, error.message);
      throw error;
    }
  }

  /**
   * Generate unique sandbox ID
   */
  generateSandboxId(moduleName) {
    const timestamp = Date.now();
    const random = Math.random().toString(36).substring(2, 8);
    const cleanModuleName = moduleName.replace(/[^a-z0-9]/gi, '_').toLowerCase();
    return `${cleanModuleName}_${timestamp}_${random}`;
  }

  /**
   * Initialize Git repository in sandbox
   */
  async initializeGitRepo(sandbox) {
    try {
      await this.executeGitCommand(sandbox, 'init');
      
      // Configure Git user for the sandbox
      await this.executeGitCommand(sandbox, 'config', ['user.name', 'Git Trainer']);
      await this.executeGitCommand(sandbox, 'config', ['user.email', 'trainer@gitlearning.local']);
      
      sandbox.gitInitialized = true;
      this.logger.debug(`Git repository initialized in sandbox: ${sandbox.id}`);
      
    } catch (error) {
      this.logger.error(`Failed to initialize Git in sandbox ${sandbox.id}:`, error.message);
      throw error;
    }
  }

  /**
   * Create initial files in sandbox
   */
  async createInitialFiles(sandbox, fileDefinitions) {
    for (const file of fileDefinitions) {
      const filePath = path.join(sandbox.path, file.name);
      
      // Ensure directory exists
      await fs.mkdir(path.dirname(filePath), { recursive: true });
      
      // Create file with content
      await fs.writeFile(filePath, file.content || '');
      
      sandbox.files.push({
        name: file.name,
        path: filePath,
        created: new Date().toISOString()
      });
    }
    
    this.logger.debug(`Created ${fileDefinitions.length} initial files in sandbox: ${sandbox.id}`);
  }

  /**
   * Execute Git command in sandbox
   */
  async executeGitCommand(sandbox, command, args = [], options = {}) {
    const fullArgs = [command, ...args];
    
    try {
      const result = await this.runCommand('git', fullArgs, {
        cwd: sandbox.path,
        ...options
      });

      // Track command execution
      this.trackCommand(sandbox, command, args, true);
      
      return result;
      
    } catch (error) {
      this.trackCommand(sandbox, command, args, false, error.message);
      throw error;
    }
  }

  /**
   * Run command with promise wrapper
   */
  async runCommand(command, args, options = {}) {
    return new Promise((resolve, reject) => {
      const child = spawn(command, args, {
        stdio: ['pipe', 'pipe', 'pipe'],
        ...options
      });

      let stdout = '';
      let stderr = '';

      child.stdout.on('data', (data) => {
        stdout += data.toString();
      });

      child.stderr.on('data', (data) => {
        stderr += data.toString();
      });

      child.on('close', (code) => {
        if (code === 0) {
          resolve({
            stdout: stdout.trim(),
            stderr: stderr.trim(),
            exitCode: code
          });
        } else {
          reject(new Error(`Command failed with code ${code}: ${stderr || stdout}`));
        }
      });

      child.on('error', (error) => {
        reject(error);
      });
    });
  }

  /**
   * Track command execution for learning analytics
   */
  trackCommand(sandbox, command, args, success, error = null) {
    const commandRecord = {
      command: `git ${command} ${args.join(' ')}`.trim(),
      timestamp: new Date().toISOString(),
      success,
      error
    };

    if (!sandbox.commandHistory) {
      sandbox.commandHistory = [];
    }
    
    sandbox.commandHistory.push(commandRecord);
  }

  /**
   * Get sandbox by ID
   */
  getSandbox(sandboxId) {
    return this.activeSandboxes.get(sandboxId);
  }

  /**
   * List files in sandbox
   */
  async listSandboxFiles(sandboxId) {
    const sandbox = this.getSandbox(sandboxId);
    if (!sandbox) {
      throw new Error(`Sandbox not found: ${sandboxId}`);
    }

    try {
      const entries = await fs.readdir(sandbox.path, { withFileTypes: true });
      return entries
        .filter(entry => !entry.name.startsWith('.git'))
        .map(entry => ({
          name: entry.name,
          type: entry.isDirectory() ? 'directory' : 'file',
          path: path.join(sandbox.path, entry.name)
        }));
    } catch (error) {
      this.logger.error(`Failed to list files in sandbox ${sandboxId}:`, error.message);
      throw error;
    }
  }

  /**
   * Read file content from sandbox
   */
  async readSandboxFile(sandboxId, fileName) {
    const sandbox = this.getSandbox(sandboxId);
    if (!sandbox) {
      throw new Error(`Sandbox not found: ${sandboxId}`);
    }

    const filePath = path.join(sandbox.path, fileName);
    
    try {
      const content = await fs.readFile(filePath, 'utf8');
      return content;
    } catch (error) {
      if (error.code === 'ENOENT') {
        throw new Error(`File not found: ${fileName}`);
      }
      throw error;
    }
  }

  /**
   * Write file to sandbox
   */
  async writeSandboxFile(sandboxId, fileName, content) {
    const sandbox = this.getSandbox(sandboxId);
    if (!sandbox) {
      throw new Error(`Sandbox not found: ${sandboxId}`);
    }

    const filePath = path.join(sandbox.path, fileName);
    
    try {
      // Ensure directory exists
      await fs.mkdir(path.dirname(filePath), { recursive: true });
      
      // Write file
      await fs.writeFile(filePath, content, 'utf8');
      
      // Track file creation
      if (!sandbox.files.some(f => f.name === fileName)) {
        sandbox.files.push({
          name: fileName,
          path: filePath,
          created: new Date().toISOString()
        });
      }
      
    } catch (error) {
      this.logger.error(`Failed to write file ${fileName} in sandbox ${sandboxId}:`, error.message);
      throw error;
    }
  }

  /**
   * Get Git status in sandbox
   */
  async getSandboxGitStatus(sandboxId) {
    const sandbox = this.getSandbox(sandboxId);
    if (!sandbox || !sandbox.gitInitialized) {
      throw new Error(`Git not initialized in sandbox: ${sandboxId}`);
    }

    try {
      const result = await this.executeGitCommand(sandbox, 'status', ['--porcelain']);
      return this.parseGitStatus(result.stdout);
    } catch (error) {
      this.logger.error(`Failed to get Git status in sandbox ${sandboxId}:`, error.message);
      throw error;
    }
  }

  /**
   * Parse Git status output
   */
  parseGitStatus(output) {
    const lines = output.split('\n').filter(line => line.trim());
    const status = {
      staged: [],
      unstaged: [],
      untracked: []
    };

    lines.forEach(line => {
      const statusCode = line.substring(0, 2);
      const fileName = line.substring(3);

      if (statusCode[0] !== ' ' && statusCode[0] !== '?') {
        status.staged.push(fileName);
      }
      if (statusCode[1] !== ' ' && statusCode[1] !== '?') {
        status.unstaged.push(fileName);
      }
      if (statusCode === '??') {
        status.untracked.push(fileName);
      }
    });

    return status;
  }

  /**
   * Get commit history in sandbox
   */
  async getSandboxCommitHistory(sandboxId, limit = 10) {
    const sandbox = this.getSandbox(sandboxId);
    if (!sandbox || !sandbox.gitInitialized) {
      throw new Error(`Git not initialized in sandbox: ${sandboxId}`);
    }

    try {
      const result = await this.executeGitCommand(sandbox, 'log', [
        '--oneline',
        `--max-count=${limit}`,
        '--pretty=format:%H|%s|%an|%ad',
        '--date=iso'
      ]);

      if (!result.stdout) {
        return [];
      }

      return result.stdout.split('\n').map(line => {
        const [hash, subject, author, date] = line.split('|');
        return { hash, subject, author, date };
      });
    } catch (error) {
      // No commits yet is not an error
      if (error.message.includes('does not have any commits yet')) {
        return [];
      }
      throw error;
    }
  }

  /**
   * Cleanup specific sandbox
   */
  async cleanupSandbox(sandboxId) {
    const sandbox = this.getSandbox(sandboxId);
    if (!sandbox) {
      return; // Already cleaned up
    }

    try {
      // Check if we should keep sandbox based on configuration
      const shouldKeep = this.shouldKeepSandbox(sandbox);
      if (shouldKeep) {
        this.logger.debug(`Keeping sandbox ${sandboxId} based on configuration`);
        return;
      }

      // Remove sandbox directory
      await fs.rm(sandbox.path, { recursive: true, force: true });
      
      // Remove from active sandboxes
      this.activeSandboxes.delete(sandboxId);
      
      this.logger.debug(`Sandbox cleaned up: ${sandboxId}`);
      
    } catch (error) {
      this.logger.warn(`Failed to cleanup sandbox ${sandboxId}:`, error.message);
    }
  }

  /**
   * Determine if sandbox should be kept
   */
  shouldKeepSandbox(sandbox) {
    const config = this.config.get('sandbox', {});
    
    // Keep on error if configured
    if (config.keepOnError && sandbox.hasErrors) {
      return true;
    }
    
    // Keep on success if configured
    if (config.keepOnSuccess && sandbox.completed) {
      return true;
    }
    
    return false;
  }

  /**
   * Cleanup old sandboxes
   */
  async cleanupOldSandboxes() {
    try {
      const entries = await fs.readdir(this.sandboxRoot, { withFileTypes: true });
      const maxAge = 24 * 60 * 60 * 1000; // 24 hours
      const now = Date.now();

      for (const entry of entries) {
        if (entry.isDirectory()) {
          const sandboxPath = path.join(this.sandboxRoot, entry.name);
          const stats = await fs.stat(sandboxPath);
          
          if (now - stats.mtime.getTime() > maxAge) {
            await fs.rm(sandboxPath, { recursive: true, force: true });
            this.logger.debug(`Cleaned up old sandbox: ${entry.name}`);
          }
        }
      }
    } catch (error) {
      this.logger.warn('Failed to cleanup old sandboxes:', error.message);
    }
  }

  /**
   * Cleanup all sandboxes
   */
  async cleanupAll() {
    const sandboxIds = Array.from(this.activeSandboxes.keys());
    
    await Promise.all(
      sandboxIds.map(id => this.cleanupSandbox(id))
    );
    
    this.logger.info('All sandboxes cleaned up');
  }

  /**
   * Cleanup resources
   */
  async cleanup() {
    if (this.config.get('sandbox.autoCleanup', true)) {
      await this.cleanupAll();
    }
  }

  /**
   * Get sandbox statistics
   */
  getSandboxStatistics(sandboxId) {
    const sandbox = this.getSandbox(sandboxId);
    if (!sandbox) {
      return null;
    }

    const commandHistory = sandbox.commandHistory || [];
    const successfulCommands = commandHistory.filter(cmd => cmd.success).length;
    const totalCommands = commandHistory.length;

    return {
      id: sandboxId,
      moduleName: sandbox.moduleName,
      created: sandbox.created,
      filesCreated: sandbox.files.length,
      commandsExecuted: totalCommands,
      successfulCommands,
      successRate: totalCommands > 0 ? (successfulCommands / totalCommands) * 100 : 0,
      gitInitialized: sandbox.gitInitialized
    };
  }
}
