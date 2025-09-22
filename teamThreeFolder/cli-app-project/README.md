# CLI Learning Application

## Overview
This CLI application provides an interactive, quest-based learning experience for mastering Git commands. Users can select quests, answer multiple-choice questions, view a glossary of Git commands, and earn achievement badges as they progress.

## Features
- **Interactive Quests:** Choose from a set of learning quests, each with multiple modules and questions.
- **Immediate Feedback:** Get instant feedback and retry opportunities for incorrect answers.
- **Glossary:** Access a comprehensive glossary of Git commands and definitions.
- **Badges:** Earn achievement badges by completing quests and reaching milestones.
- **Progress Tracking:** Track your progress and resume where you left off.

## How to Run

### Prerequisites
- Java 11 or higher
- Gradle (use the included Gradle Wrapper for consistency)

### Running the Application
1. Open a terminal in the project directory.
2. Run the following command:
   ```bash
   ./gradlew run --console=plain --quiet
   ```
   If you are on Windows, use:
   ```bash
   gradlew.bat run --console=plain --quiet
   ```

### Running Tests
To run all tests and check coverage:
```bash
./gradlew test
./gradlew jacocoTestReport
```

## Commands Available
- `quest` - Start interactive quest selection
- `glossary` - View Git command glossary
- `badge` - View earned achievements
- `continue` - Resume saved progress
- `help` - Display available commands
- `exit` - Exit application

## Project Structure
- `src/main/java/com/cliapp/` - Main source code
- `src/main/resources/` - Quest and glossary data
- `config/` - Application configuration files
- `build.gradle` - Build configuration

## Troubleshooting
- If you see errors about missing files, ensure `Quest.json` and `glossary.json` exist in `src/main/resources/`.
- For build issues, try:
  ```bash
  ./gradlew clean build
  ```
- For interactive features, always use the Gradle run command above.

## Contributing
- Follow the command and service patterns in the codebase.
- Write tests for new features.
- Update configuration files as needed.

## Contact
For questions or help, refer to the test files and service classes for examples and business logic details.
