# CLI Application Implementation Guide

## Overview
This CLI Learning Application provides an interactive quest-based learning experience with glossary management and badge system.

## Quick Start

### Prerequisites
- Java 11 or higher
- No additional dependencies required (all included in Gradle)

### Running the Application

1. **Build and Run**:
   ```bash
   ./gradlew run
   ```

2. **For Windows**:
   ```bash
   gradlew.bat run
   ```

3. **Run Tests**:
   ```bash
   ./gradlew test
   ```

## Project Structure

```
src/main/java/com/cliapp/
├── CLIApplication.java          # Main application entry point
├── commands/                    # Command pattern implementations
│   ├── Command.java            # Base command interface
│   ├── QuestListCommand.java   # Quest selection and execution
│   ├── GlossaryCommand.java    # Glossary display
│   ├── BadgeCommand.java       # Badge management
│   ├── ContinueCommand.java    # Save/load functionality
│   ├── HelpCommand.java        # Help system
│   ├── ExitCommand.java        # Application exit
│   └── CommandRegistry.java    # Command management
├── services/                   # Business logic services
│   ├── QuestGameService.java   # Interactive quest engine
│   ├── GlossaryService.java    # Glossary management
│   └── BadgeService.java       # Badge system
├── domain/                     # Domain models
│   ├── Quest.java              # Quest data structure
│   ├── Question.java           # Question for interactive quests
│   ├── GlossaryEntry.java      # Glossary definitions
│   └── Badge.java              # Achievement system
├── io/                         # I/O abstractions
│   ├── Console.java            # Console interface
│   ├── SystemConsole.java      # Production console
│   └── TestConsole.java        # Testing console
├── collections/                # Data collections
├── utils/                      # Utility classes
└── exceptions/                 # Custom exceptions

src/main/resources/
├── Quest.json                  # Quest and question data
└── glossary.json              # Glossary definitions

config/
├── app-config.json            # Application configuration
├── data-config.json           # Data file paths
├── commands.json              # Command registry
└── user-config.json           # User preferences
```

## Key Features

### Interactive Quest System
- Multiple choice questions with immediate feedback
- Retry mechanism for incorrect answers
- Point-based scoring system
- Progress tracking

### Commands Available
- `quest` - Start interactive quest selection
- `glossary` - View Git command glossary
- `badge` - View earned achievements
- `continue` - Resume saved progress
- `help` - Display available commands
- `exit` - Exit application

### Architecture Highlights
- **Command Pattern**: Modular command system
- **Strategy Pattern**: Pluggable I/O for testing
- **Service Layer**: Separated business logic
- **JSON Data**: External configuration and content

## Configuration

The application uses JSON configuration files in the `config/` directory:

- **app-config.json**: Core application settings
- **data-config.json**: Data file locations
- **commands.json**: Available commands
- **user-config.json**: User preferences and progress

## Testing

### Running Tests
```bash
./gradlew test
```

### Test Coverage
```bash
./gradlew jacocoTestReport
```

### Test Categories
- **Unit Tests**: Individual component testing
- **Integration Tests**: Service interaction testing
- **E2E Tests**: Complete user story testing
- **TDD Tests**: Test-driven development examples

## Troubleshooting

### Common Issues

1. **"Main class not found"**
   - Ensure `CLIApplication.java` exists in `src/main/java/com/cliapp/`
   - Check that `mainClass` in `build.gradle` is correct

2. **"Resource files not found"**
   - Verify `Quest.json` and `glossary.json` exist in `src/main/resources/`
   - Check file paths in configuration

3. **"Console input not working"**
   - Ensure you're using `./gradlew run` (not just `java -jar`)
   - The `build.gradle` includes `standardInput = System.in` for interactive mode

4. **"Tests failing"**
   - Run `./gradlew clean test` to rebuild
   - Check that test resources are properly configured

### Build Issues
```bash
# Clean and rebuild
./gradlew clean build

# Force dependency refresh
./gradlew --refresh-dependencies clean build
```

## Development Notes

### Adding New Commands
1. Implement the `Command` interface
2. Add to `CommandRegistry`
3. Update `commands.json` configuration
4. Write corresponding tests

### Modifying Quest Content
- Edit `src/main/resources/Quest.json` for questions and content
- Edit `src/main/resources/glossary.json` for glossary entries
- Restart application to reload changes

### Interactive Features
The application supports interactive input through the Console abstraction:
- `SystemConsole` for production use
- `TestConsole` for automated testing
- All interactive features are testable

## Contributing

1. Follow existing code patterns
2. Write tests for new features
3. Update configuration files as needed
4. Document new functionality

## Contact

For questions about this implementation, refer to the test files for usage examples or check the service classes for business logic details.
