package com.cliapp.domain;

import java.util.List;

/**
 * Unit tests for Quest class using AAVE and Philly vernacular
 * Testing that jawn like it's supposed to work, ya dig?
 * 
 * This is where your project partner gonna learn how to test the Quest class proper
 */
public class QuestTest {
    
    private Quest quest;
    private List<String> learningModules;
    
    @BeforeEach
    void setUp() {
        // TODO: Set up that good good before each test, know what I'm saying?
        // Create some learning modules that explain what to learn
        // Example: "Learn Git init command to start a repository"
        //          "Understand how to add files with git add"
        //          "Master committing changes with descriptive messages"
    }
    
    @Test
    void testQuestCreation_ShouldWorkLikeItsSupposedTo() {
        // TODO: This test be checking if we can make a quest without no drama
        // Test that:
        // - Quest ain't null after creation
        // - Quest ID matches what we set
        // - Quest name is correct
        // - Quest starts as not completed (should be false)
        // - Learning modules list ain't empty
        
        fail("Your partner needs to implement this test - check the README for guidance!");
    }
    
    @Test
    void testDifficultyAsAsterisks_ShouldShowProperStars() {
        // TODO: Testing if them asterisks showing up right, ya feel me?
        // Test that:
        // - Difficulty 1 shows "*" 
        // - Difficulty 3 shows "***"
        // - Difficulty 5 shows "*****"
        // Make sure it's exactly the right number of stars, no more no less
        
        fail("Your partner needs to implement this test - check the README for guidance!");
    }
    
    @Test
    void testCompletionStatus_ShouldShowYOrN() {
        // TODO: Making sure that completion status be showing Y or N like it's supposed to
        // Test that:
        // - Incomplete quest shows "N" 
        // - Completed quest shows "Y"
        // Ain't no other letters allowed, just Y or N
        
        fail("Your partner needs to implement this test - check the README for guidance!");
    }
    
    @Test
    void testLearningModules_ShouldHoldAllThemModules() {
        // TODO: This test checking if the quest holding all them learning modules proper
        // Test that:
        // - Learning modules list ain't null
        // - List has the right number of modules
        // - Each module string explains what to learn
        // - Can add new modules to the quest
        // - Can remove modules when needed
        
        fail("Your partner needs to implement this test - check the README for guidance!");
    }
    
    @Test
    void testAddLearningModule_ShouldAddThatJawn() {
        // TODO: Test adding a new learning module to the quest
        // Test that:
        // - Module gets added to the list
        // - List size increases by 1
        // - The new module shows up when you get the list
        
        fail("Your partner needs to implement this test - check the README for guidance!");
    }
    
    @Test
    void testRemoveLearningModule_ShouldTakeItOut() {
        // TODO: Test removing a learning module from the quest
        // Test that:
        // - Module gets removed from the list
        // - Method returns true when module exists
        // - Method returns false when module don't exist
        // - List size decreases when module removed
        
        fail("Your partner needs to implement this test - check the README for guidance!");
    }
}
