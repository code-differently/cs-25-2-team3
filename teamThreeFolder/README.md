# cs-25-2-team3
Main repo for Code Society 25.2 Cohort Team 3

_________________________________________________________________________________________________________

# Project OOP


### Changelog
- 7/21 @anthonydmays Initial draft

## Introduction

For this project, you and your teammates are tasked with modeling a solution to a real-world problem using object-oriented and SOLID design principles, from ideation to implementation.

## Prerequisites

Before starting work on your project, you will need to submit three user stories as feature requests in your assigned GitHub repo. These will need to be approved by the instructor before you can begin coding. Your final project submission must enable the functionality described by your user stories.

## Project Requirements

* All work must be submitted in your team's assigned GitHub repository.
* The assignment can be completed in TypeScript or in Java.
* Must include at least 5 types of objects with meaningful relationships to each other.
* One of your objects must be a custom data structure that provides for adding, removing, and updating items in a collection.
* Implement at least two custom exceptions.
* Write unit tests achieving 90% code coverage (using JaCoCo for Java or Jest for Typescript).
* Must include an integration test for each user story that demonstrates how your code implements the desired feature.
* Your solution must illustrate each of the SOLID principles.
* Each team member must contribute *at least one* submitted pull request containing working code and tests.
* Include a README for your repo describing the problem you're solving, the solution, and how you would improve your solution.

# Presentation Requirements

* Your presentation should be no more than 10 minutes with a maximum of 10 slides.
* Each member of the team must speak during the presentation.
* Your presentation must address the following questions:
    * What problem were you attempting to solve?
    * How does your design address the solution?
    * How did you address each of the SOLID principles?
    * How would you improve on your solution?

## Extra Credit

Design a CLI that allows users to interact with your application. Check out the code in [lesson_10](/lesson_10/libraries/src/cli/) for an example in TypeScript, or [this file](/lib/java/codedifferently-instructional/instructional-lib/src/main/java/com/codedifferently/instructional/quiz/QuizProctor.java) for an example in Java.

## Timeline

* Submit three user stories (Monday, 9/15, 5PM ET)
* Receive approval for your user stories (Tuesday, 9/16, 1PM ET)
* Finish code commits (Friday, 9/19, 1PM ET)
* Give presentation (Monday, 9/22, 1PM ET)

## Grading

Your grade for this project will amount to 25% of your final grade in the course.

* 50% of your project grade will be composed of a team score. Your final solution and presentation will be assessed on how well it meets the described functional and technical requirements. Work submitted after the assigned deadline will result in a deduction of points.
    * Completing the extra credit will enable up to an additional 50% increase to the team score component.
* The remaining 50% of your grade will be composed of an individual score. The individual score will be computed based on survey feedback from your teammates and the instructors/TAs regarding your technical ability, communication skills, and teamwork contributions.
_________________________________________________________________________________________________________

## User Story 1
**"As a user, I want to launch the application and view a list of available learning modules with their respective difficulty levels and completion status"**

### CLI Flow for Quest Feature
When user launches the CLI application, they see main menu options:
1. **quest** - View all available learning quests
2. **continue** - Resume current quest
3. **badges** - View earned achievements  
4. **quit** - Exit application
5. **glossary** - View Git terms and definitions

When user selects **quest** (option 1), they see a long list that displays:
- Quest names and descriptions
- Learning modules (strings explaining what each module teaches)
- Difficulty levels represented by asterisks: `*` (easy), `***` (medium), `*****` (hard)
- Completion status: `Y` (completed) or `N` (not completed)

### Unit Testing Guide for Quest Class (AAVE & Philly Vernacular Style)

Yo, listen up partner! 👂 This section gonna teach you how to write them unit tests for the Quest class using that real Philly talk, no cap. We keeping it 💯 while making sure our code work right.

#### Getting Started - The Real Talk 🗣️

Before you start writing tests, you gotta understand what we testing:
- **Quest class** - This jawn holds all the learning modules with their difficulty and completion status
- **Learning modules** - These are strings that explain what the user gonna learn (like "Learn Git init command to create a new repository")
- **Difficulty levels** - Represented by asterisks: `*`, `***`, `*****`
- **Completion status** - Simple Y/N to show if the quest done or not

#### Test Structure - How We Do It In Philly 🏙️

Each test should follow this pattern (keeping it real):

```java
@Test
void testSomething_ShouldDoWhatItsSupposedTo() {
    // 1. ARRANGE - Set up that good good (your test data)
    // 2. ACT - Do the thing you testing 
    // 3. ASSERT - Make sure it worked like it should, no cap
}
```

#### Essential Tests You Gotta Write 📝

**1. Quest Creation Test**
```
Yo, this test making sure we can create a quest without no problems:
- Quest object ain't null after we make it
- All the properties set correctly (ID, name, description) 
- Learning modules list got the right modules in it
- Quest starts as incomplete (isCompleted should be false)
- Difficulty level matches what we set
```

**2. Difficulty Display Test** 
```
This one checking if them asterisks showing up right:
- When difficulty = 1, should return "*" (one star, that's it)
- When difficulty = 3, should return "***" (three stars, no more no less)  
- When difficulty = 5, should return "*****" (all five stars, real talk)
- Don't accept no other values, keep it consistent
```

**3. Completion Status Test**
```
Making sure that Y/N status working proper:
- When quest not completed, should return "N"
- When quest completed, should return "Y" 
- Ain't no other letters allowed, just Y or N, period
```

**4. Learning Modules Test**
```
This test checking if quest holding all them learning modules:
- List ain't null or empty when it shouldn't be
- Can add new modules to the quest
- Can remove modules when needed
- Each module string actually explains what to learn
- List size changes correctly when adding/removing
```

#### Test Comments - Keep It Real 💬

Use this style in your test comments:
```java
// This test be checking if we can make a quest without no drama
// Making sure that completion status showing Y or N like it's supposed to
// Testing if them asterisks showing up right, ya feel me?
// Should find that quest, it's right there in the collection
// Shouldn't find no fake quest, that don't exist
// Quest better be in the list after we add it, straight up
```

#### Common Test Mistakes - Don't Do This! ❌

1. **Don't test without setup** - Always set up your test data first
2. **Don't use weak assertions** - Be specific about what you expecting
3. **Don't forget edge cases** - Test empty lists, null values, invalid inputs
4. **Don't write tests that always pass** - Make sure they can actually fail
5. **Don't forget to test the error conditions** - What happens when something goes wrong?

#### Sample Test Implementation Guide 🛠️

For each test method, follow these steps:

**Step 1: Set Up Your Data (BeforeEach)**
```java
@BeforeEach 
void setUp() {
    // Create some learning modules that make sense
    learningModules = Arrays.asList(
        "Learn Git init command to start a new repository",
        "Understand git add to stage files for commit", 
        "Master git commit with meaningful messages"
    );
    
    // Create a quest with that data
    quest = new Quest("git-basics", "Git Fundamentals", 
                     "Learn the essential Git commands", 
                     learningModules, 1);
}
```

**Step 2: Write Your Test Logic**
```java
@Test
void testQuestCreation_ShouldWorkLikeItsSupposedTo() {
    // Make sure quest got created right
    assertNotNull(quest, "Quest shouldn't be null, that ain't it");
    assertEquals("git-basics", quest.getId(), "ID should match what we set");
    assertEquals("Git Fundamentals", quest.getName(), "Name gotta be right");
    assertFalse(quest.isCompleted(), "New quest shouldn't be done yet");
    assertEquals(3, quest.getLearningModules().size(), "Should have 3 modules");
}
```

**Step 3: Test Edge Cases**
```java
@Test 
void testEmptyLearningModules_ShouldHandleIt() {
    Quest emptyQuest = new Quest("empty", "Empty Quest", "No modules", null, 1);
    assertNotNull(emptyQuest.getLearningModules(), "Should handle null modules gracefully");
    assertTrue(emptyQuest.getLearningModules().isEmpty(), "Empty list better be empty");
}
```

#### Running Your Tests - Make Sure They Work! 🏃‍♂️

1. Run individual tests to make sure they pass
2. Run all Quest tests together  
3. Check test coverage - aim for at least 90%
4. Make sure tests fail when they supposed to (change the code and see if test catches it)

#### Integration with CLI - The Big Picture 🖼️

Remember, these Quest objects gonna be used in the CLI when user types "quest":
- QuestService will manage multiple quests
- QuestCollection will store and organize them
- QuestListCommand will display them to the user
- Your tests making sure each piece work right individually

#### Final Advice - Keep It 💯

- Write tests that actually test something important
- Use clear, descriptive test names (even with the slang, make it clear)
- Don't skip testing just cause it seem obvious
- Test the happy path AND the error cases
- Keep your tests simple - one thing per test
- Make sure your partner can understand your tests later

That's how we do it in Philly - thorough, real, and making sure everything work like it's supposed to! 🔥

#### Classes Created for User Story 1 📋

Your project partner gonna work with these classes:

**Core Classes:**
- `Quest.java` - Domain object that holds learning modules, difficulty, and completion status
- `QuestCollection.java` - Custom data structure for managing multiple quests
- `QuestListCommand.java` - CLI command to display the quest list
- `QuestService.java` - Service layer for quest business logic

**Test Classes:**
- `QuestTest.java` - Unit tests for the Quest class (needs implementation)
- `QuestListUserStoryTest.java` - Integration test for the complete user story

**What Your Partner Needs to Do:**
1. Implement the TODO methods in `Quest.java` (especially `getDifficultyAsAsterisks()` and `getCompletionStatus()`)
2. Complete the unit tests in `QuestTest.java` following the AAVE guidance above
3. Make sure all tests pass and demonstrate the functionality working
4. Test that when user types "quest" in CLI, they see the long list with modules, difficulty, and completion status

Remember: Keep it real, test everything properly, and make sure that jawn works like it's supposed to! 💪

---