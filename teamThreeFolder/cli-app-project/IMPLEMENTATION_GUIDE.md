# Implementation Guide for User Story 1 - Quest Feature

## Overview
This guide is for your project partner to implement the Quest feature that shows a long list of learning modules with difficulty levels (asterisks) and completion status (Y/N) when the user selects "quest" from the CLI menu.

## Classes Created (Skeleton Only - Need Implementation)

### 1. Quest.java (Domain Object)
**Location:** `src/main/java/com/cliapp/domain/Quest.java`

**Key methods to implement:**
- `getDifficultyAsAsterisks()` - Return "*", "***", or "*****" based on difficulty level
- `getCompletionStatus()` - Return "Y" if completed, "N" if not
- `addLearningModule(String module)` - Add a learning module to the quest
- `removeLearningModule(String module)` - Remove a learning module from the quest

### 2. QuestCollection.java (Custom Data Structure)
**Location:** `src/main/java/com/cliapp/collections/QuestCollection.java`

**Key methods to implement:**
- `add(Quest quest)` - Add quest to both list and index map
- `remove(Quest quest)` - Remove quest from both collections
- `update(String id, Quest quest)` - Update existing quest
- `getById(String id)` - Get quest by ID from index map
- `getAllQuests()` - Return copy of all quests
- `getQuestsByDifficulty(int level)` - Filter quests by difficulty
- `getQuestsByCompletionStatus(boolean completed)` - Filter by completion
- `size()` and `isEmpty()` - Collection utility methods
- `iterator()` - Make collection iterable

### 3. QuestListCommand.java (CLI Command)
**Location:** `src/main/java/com/cliapp/commands/QuestListCommand.java`

**Key methods to implement:**
- `execute(String[] args)` - Main command execution
- `displayQuestList()` - Format and display all quests
- `formatQuestDisplay(Quest quest)` - Format individual quest display

### 4. QuestService.java (Business Logic)
**Location:** `src/main/java/com/cliapp/services/QuestService.java`

**Key methods to implement:**
- `initializeDefaultQuests()` - Create sample quests with learning modules
- `getAllQuests()` - Get all quests from collection
- `getQuestById(String id)` - Get specific quest
- `markQuestAsCompleted(String questId)` - Mark quest as done
- Various filtering methods

## Test Implementation

### Unit Tests (QuestTest.java)
**Location:** `src/test/java/com/cliapp/domain/QuestTest.java`

Follow the README guidance with AAVE/Philly vernacular for:
1. Quest creation tests
2. Difficulty asterisk display tests  
3. Completion status Y/N tests
4. Learning modules management tests
5. Edge case tests (null values, empty lists)

### Integration Test (QuestListUserStoryTest.java)
**Location:** `src/test/java/com/cliapp/integration/QuestListUserStoryTest.java`

Test the complete user story flow - when user selects "quest", they see the formatted list.

## Expected CLI Output Format

When user types "quest", they should see something like:

```
Available Learning Quests:
========================

1. Web Development Intro [*] - Y
   - Learn HTML basics and structure
   - Understand CSS styling and layouts  
   - Master JavaScript fundamentals

2. Java Programming [***] - N
   - Understand Java syntax and variables
   - Learn object-oriented programming concepts
   - Master collections and data structures
   - Handle exceptions properly

3. Full Stack Mastery [*****] - N  
   - Build React frontend applications
   - Create Node.js backend services
   - Design and implement databases
   - Develop REST APIs
   - Deploy applications to production
```

## Implementation Priority

1. **Start with Quest.java** - Implement the TODO methods first
2. **Write unit tests** - Follow the README guidance with AAVE style
3. **Implement QuestCollection** - Custom data structure with add/remove/update
4. **Create QuestService** - Initialize default quests for testing
5. **Implement QuestListCommand** - Display logic for CLI
6. **Test integration** - Make sure everything works together

## Key Requirements

- Learning modules are strings explaining what to learn
- Difficulty shown as asterisks: * (easy), *** (medium), ***** (hard)  
- Completion status as Y (completed) or N (not completed)
- Quest list should be a "long list" showing all the information
- Must work when user selects "quest" option from CLI menu

## Testing Notes

- Follow the AAVE/Philly vernacular style in test comments as shown in README
- Test edge cases (empty collections, null values)
- Ensure 90% code coverage as required by project
- Write integration test that demonstrates complete user story

Good luck! Keep it real and make sure that jawn works properly! 💪
