# Git Training CLI - Enterprise Edition## Features

- **Animated Steve Harvey** - Full character animation with expressions
- **3 Learning Modules** - Progressive Git skill building
- **Achievement System** - Unlock badges and track progress
- **Session Persistence** - Automatic progress saving
- **Interactive Experience** - Family Feud themed learning
- **Enterprise Architecture** - Professional code organizationrehensive, enterprise-grade Git learning application featuring interactive modules, achievements, and Family Feud themed animations with Steve Harvey.

## Quick Start

```bash
# Build and run the enterprise application
./scripts/build.sh

# Or run directly
cd apps/git-training-cli/
javac GitTrainingCLIEnterprise.java
java GitTrainingCLIEnterprise
```

## Project Structure

This project follows Google-level enterprise architecture patterns:

```
├── apps/                          # Deployable applications
│   └── git-training-cli/          # Main CLI application
├── java-src/                     # Modular enterprise source code
│   ├── achievements/              # Achievement system
│   ├── cli/                      # Command-line interface
│   ├── core/                     # Core application logic
│   ├── modules/                  # Learning modules
│   ├── persistence/              # Data persistence
│   ├── ui/                       # User interface & animations
│   └── utils/                    # Utility classes
├── docs/                         # Documentation
├── scripts/                      # Build and deployment scripts
├── build-artifacts/              # Compiled binaries
└── archive/                      # Legacy code and history
    ├── legacy-monolith/          # Original single-file app
    └── legacy-modules/           # Original modular components
```

## ✨ Features

- � **Animated Steve Harvey** - Full character animation with expressions
- 📚 **3 Learning Modules** - Progressive Git skill building
- 🏆 **Achievement System** - Unlock badges and track progress
- 💾 **Session Persistence** - Automatic progress saving
- 🎮 **Interactive Experience** - Family Feud themed learning
- 🏗️ **Enterprise Architecture** - Professional code organization

## Documentation

- **[Architecture Guide](docs/ARCHITECTURE.md)** - Detailed project structure and patterns
- **[Quick Start Guide](docs/QUICKSTART.md)** - Get up and running fast
- **[Legacy Documentation](archive/)** - Historical code evolution

## � Development

### Building Applications
```bash
# Interactive build system
./scripts/build.sh

# Options:
# 1. Enterprise Application (Single File)
# 2. Modular Architecture (Full Enterprise) 
# 3. Both Applications
# 4. Clean Build Artifacts
```

### Running Different Versions
```bash
# Enterprise Single-File (Recommended)
cd apps/git-training-cli/
java GitTrainingCLIEnterprise

# Modular Architecture
cd build-artifacts/
java CommandLineInterface

# Legacy Monolith (Historical)
cd archive/legacy-monolith/
java GitTrainingCLI
```

## Learning Path

1. **Git Basics** (100 points) - Fundamental commands
2. **Branching & Merging** (200 points) - Advanced workflows  
3. **Collaboration** (300 points) - Team development

## Achievement System

- **First Steps** - Start your Git journey
- **Module Completions** - Finish learning modules
- **Point Collector** - Earn 500+ points
- **Git Master** - Achieve perfection

## Entertainment

Experience the full Steve Harvey animation sequence featuring:
- Progressive eye movement and blinking
- Character expressions and gestures
- Family Feud themed quotes and interactions
- Motivational messages throughout learning

## Architecture Highlights

### Enterprise Patterns Implemented
- **Repository Pattern** - Data access abstraction
- **Service Layer** - Business logic separation
- **Factory Pattern** - Component creation
- **Strategy Pattern** - Flexible algorithms

### Code Quality Standards
- Comprehensive JavaDoc documentation
- Enterprise-grade error handling
- Modular design for testability
- Performance optimized
- Security best practices

## Evolution

This project demonstrates the evolution from:
- **Monolithic** → **Modular** → **Enterprise** → **Production**

Each phase is preserved in the `archive/` directory for educational purposes.

## Contributing

Follow the established enterprise patterns and maintain the Google-level code quality standards demonstrated throughout the project.

---

*"Survey says... Git mastered!"* - Steve Harvey
npm start

# Or run directly
node src/index.js
```

## Learning Modules

### 1. Git Basics (Beginner)
- Initialize repositories with `git init`
- Stage files with `git add`
- Create commits with `git commit`
- Check status with `git status`

### 2. Working with Changes (Beginner)
- View differences with `git diff`
- Track file changes with `git status`
- Explore history with `git log`

### 3. Branching Fundamentals (Intermediate)
- Create branches with `git branch`
- Switch branches with `git checkout`
- Merge branches with `git merge`

## Commands

```bash
# Start interactive application (default)
git-trainer

# Start specific module
git-trainer module git_basics

# View progress
git-trainer status

# View achievements
git-trainer badges

# Manage configuration
git-trainer config

# Reset all progress
git-trainer --reset
```

## Configuration

The application stores configuration and progress in `~/.git-trainer/`:
- `config.json` - User preferences and settings
- `progress.json` - Learning progress and statistics
- `achievements.json` - Earned badges and achievements

## Development

```bash
# Run with auto-reload during development
npm run dev

# Run tests
npm test

# Lint code
npm run lint

# Build project
npm run build
```

## Architecture

Built with modern Node.js and following senior engineering practices:

- **Modular Design**: Separate managers for different concerns
- **Plugin Architecture**: Easy to add new learning modules
- **Robust Error Handling**: Graceful failure and recovery
- **Progress Persistence**: JSON-based local storage
- **Sandbox Isolation**: Safe Git practice environments

## User Stories Implementation

✅ **A1**: Launch application and view available learning modules  
✅ **A2**: Resume previous session and continue from where you left off  
✅ **A3**: Access configuration options to customize learning experience  
✅ **B4**: View detailed information about each learning module  
✅ **B5**: Progress through structured learning tasks with completion tracking  
✅ **B6**: Access contextual help and hints when encountering difficulties  
✅ **B7**: Earn experience points, achieve skill levels, and unlock achievement badges  
✅ **C8**: Application validates Git command execution in real-time  
✅ **C9**: Work within isolated sandbox repositories for each learning module  
✅ **C10**: View command demonstrations and examples before attempting execution  
✅ **D11**: Learning progress automatically saved locally to maintain continuity  

## Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Add tests if needed
5. Submit a pull request

