package com.cliapp.integration;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.List;

/**
 * Integration test for User Story 1 using AAVE and Philly vernacular
 * "As a user, I want to launch the app and see quest long list that holds learning modules 
 * (list of strings) with their respective difficulty levels (represented by asteriks, *, ***, *****) 
 * and completion status (complete Y/N)"
 * 
 * This is the integration test that shows the whole user story working together
 */
public class QuestListUserStoryTest {
    
    private QuestService questService;
    private QuestCollection questCollection;
    private QuestListCommand questListCommand;
    private ByteArrayOutputStream outputStream;
    private PrintStream originalOut;
    
    // TODO: Add @BeforeEach annotation when JUnit is available
    void setUp() {
        // Setting up everything we need for this integration test, ya dig?
        questCollection = new QuestCollection();
        questService = new QuestService(questCollection);
        questListCommand = new QuestListCommand(questCollection);
        
        // Capture output so we can test what gets displayed
        outputStream = new ByteArrayOutputStream();
        originalOut = System.out;
        System.setOut(new PrintStream(outputStream));
        
        // Add some test quests like we would have in the real app
        setupTestQuests();
    }
    
    // TODO: Add @AfterEach annotation when JUnit is available
    void tearDown() {
        // Put everything back how it was
        System.setOut(originalOut);
    }
    
    private void setupTestQuests() {
        // Create some quests with different difficulties and completion status
        List<String> webModules = Arrays.asList(
            "Learn HTML basics and structure", 
            "Understand CSS styling and layouts", 
            "Master JavaScript fundamentals"
        );
        Quest webQuest = new Quest("web-1", "Web Development Intro", "Learn the web fundamentals", webModules, 1);
        webQuest.setCompleted(true);
        
        List<String> javaModules = Arrays.asList(
            "Understand Java syntax and variables", 
            "Learn object-oriented programming concepts", 
            "Master collections and data structures", 
            "Handle exceptions properly"
        );
        Quest javaQuest = new Quest("java-1", "Java Programming", "Master Java programming", javaModules, 3);
        javaQuest.setCompleted(false);
        
        List<String> fullStackModules = Arrays.asList(
            "Build React frontend applications", 
            "Create Node.js backend services", 
            "Design and implement databases", 
            "Develop REST APIs", 
            "Deploy applications to production"
        );
        Quest fullStackQuest = new Quest("full-1", "Full Stack Mastery", "Become a full stack developer", fullStackModules, 5);
        fullStackQuest.setCompleted(false);
        
        questCollection.add(webQuest);
        questCollection.add(javaQuest);
        questCollection.add(fullStackQuest);
    }
    
    // TODO: Add @Test annotation when JUnit is available
    void testUserStory1_DisplayQuestListWithAllRequiredInfo() {
        // This the main test for User Story 1 - making sure when user launches the app,
        // they can see the quest list with learning modules, difficulty, and completion status
        
        // Execute the quest list command like a user would
        questListCommand.execute(new String[]{});
        
        String output = outputStream.toString();
        
        // TODO: Replace these with proper assertions when JUnit is available
        // Check that all quests are displayed
        if (!output.contains("Web Development Intro")) {
            System.err.println("ERROR: Should show the web development quest name, that's basic");
        }
        if (!output.contains("Java Programming")) {
            System.err.println("ERROR: Should display the Java quest, that's important");
        }
        if (!output.contains("Full Stack Mastery")) {
            System.err.println("ERROR: Full stack quest better be showing up too");
        }
        
        // Check that learning modules are displayed for each quest
        if (!output.contains("Learn HTML basics and structure")) {
            System.err.println("ERROR: Should show HTML learning module, that's fundamental");
        }
        if (!output.contains("Understand CSS styling and layouts")) {
            System.err.println("ERROR: CSS module better be listed too");
        }
        if (!output.contains("Master JavaScript fundamentals")) {
            System.err.println("ERROR: JavaScript module gotta be there");
        }
        if (!output.contains("Understand Java syntax and variables")) {
            System.err.println("ERROR: Java syntax should be displayed");
        }
        if (!output.contains("Learn object-oriented programming concepts")) {
            System.err.println("ERROR: OOP should be in the list, that's crucial");
        }
        if (!output.contains("Build React frontend applications")) {
            System.err.println("ERROR: React should show up in the full stack modules");
        }
        if (!output.contains("Design and implement databases")) {
            System.err.println("ERROR: Database Design better be listed");
        }
        
        // Check that difficulty levels are shown as asterisks
        if (!output.contains("*") || output.contains("**")) {
            System.err.println("ERROR: Easy quest should show one asterisk only");
        }
        if (!output.contains("***")) {
            System.err.println("ERROR: Medium quest should show three asterisks, that's the rule");
        }
        if (!output.contains("*****")) {
            System.err.println("ERROR: Hard quest gotta show five asterisks, all of them");
        }
        
        // Check that completion status is shown as Y/N
        if (!output.contains("Y")) {
            System.err.println("ERROR: Should show 'Y' for completed quests, straight up");
        }
        if (!output.contains("N")) {
            System.err.println("ERROR: Should show 'N' for incomplete quests, that's facts");
        }
        
        System.out.println("Integration test completed - check error messages above");
    }
    
    // TODO: Add @Test annotation when JUnit is available
    void testQuestListDisplaysInCorrectFormat() {
        // Making sure the quest list displays in a format that makes sense for users
        questListCommand.execute(new String[]{});
        String output = outputStream.toString();
        
        // The output should be organized and readable
        if (output.isEmpty()) {
            System.err.println("ERROR: Output shouldn't be empty when we got quests to show");
        }
        
        // Should have some kind of structure or headers
        if (output.length() <= 100) {
            System.err.println("ERROR: Output should have substantial content, not just a few words");
        }
        
        // Each quest should have its required info displayed together
        String[] lines = output.split("\n");
        if (lines.length < 3) {
            System.err.println("ERROR: Should have at least one line per quest, that's minimum");
        }
        
        System.out.println("Format test completed - check error messages above");
    }
    
    // TODO: Add @Test annotation when JUnit is available
    void testEmptyQuestCollection_ShouldHandleGracefully() {
        // Testing what happens when ain't no quests to show
        QuestCollection emptyCollection = new QuestCollection();
        QuestListCommand emptyCommand = new QuestListCommand(emptyCollection);
        
        emptyCommand.execute(new String[]{});
        String output = outputStream.toString();
        
        // Should handle empty collection without crashing
        if (output == null) {
            System.err.println("ERROR: Output shouldn't be null even with no quests");
        }
        // Could show a message like "No quests available" or something helpful
        
        System.out.println("Empty collection test completed - check error messages above");
    }
}
