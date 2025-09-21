package com.cliapp.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class QuestTest {

    private Quest quest;
    private List<String> learningModules;

    @BeforeEach
    void setUp() {
        learningModules =
                Arrays.asList(
                        "Learn Git init command to start a repository",
                        "Understand how to add files with git add",
                        "Master committing changes with descriptive messages");
        quest =
                new Quest(
                        "git-1",
                        "Git Basics",
                        "Learn fundamental Git commands",
                        learningModules,
                        3);
    }

    @Test
    void testQuestCreation_ShouldWorkLikeItsSupposedTo() {
        assertNotNull(quest, "Quest ain't null after creation");
        assertEquals("git-1", quest.getId(), "Quest ID matches what we set");
        assertEquals("Git Basics", quest.getName(), "Quest name is correct");
        assertEquals(
                "Learn fundamental Git commands",
                quest.getDescription(),
                "Quest description is correct");
        assertFalse(quest.isCompleted(), "Quest starts as not completed (should be false)");
        assertFalse(quest.getLearningModules().isEmpty(), "Learning modules list ain't empty");
    }

    @Test
    void testDifficultyAsAsterisks_ShouldShowProperStars() {
        Quest easyQuest = new Quest("easy", "Easy Quest", "Simple quest", learningModules, 1);
        assertEquals("*", easyQuest.getDifficultyAsAsterisks(), "Difficulty 1 shows '*'");

        Quest mediumQuest = new Quest("medium", "Medium Quest", "Medium quest", learningModules, 3);
        assertEquals("***", mediumQuest.getDifficultyAsAsterisks(), "Difficulty 3 shows '***'");

        Quest hardQuest = new Quest("hard", "Hard Quest", "Hard quest", learningModules, 5);
        assertEquals("*****", hardQuest.getDifficultyAsAsterisks(), "Difficulty 5 shows '*****'");

        Quest invalidQuest =
                new Quest("invalid", "Invalid Quest", "Invalid quest", learningModules, 7);
        assertEquals(
                "*", invalidQuest.getDifficultyAsAsterisks(), "Invalid difficulty defaults to '*'");
    }

    @Test
    void testCompletionStatus_ShouldShowYOrN() {
        assertEquals("N", quest.getCompletionStatus(), "Incomplete quest shows 'N'");

        quest.setCompleted(true);
        assertEquals("Y", quest.getCompletionStatus(), "Completed quest shows 'Y'");
    }

    @Test
    void testLearningModules_ShouldHoldAllThemModules() {
        List<String> modules = quest.getLearningModules();
        assertNotNull(modules, "Learning modules list ain't null");
        assertEquals(3, modules.size(), "List has the right number of modules");
        assertTrue(
                modules.contains("Learn Git init command to start a repository"),
                "Module string explains what to learn");
        assertTrue(
                modules.contains("Understand how to add files with git add"),
                "Module string explains what to learn");
        assertTrue(
                modules.contains("Master committing changes with descriptive messages"),
                "Module string explains what to learn");
    }

    @Test
    void testAddLearningModule_ShouldAddThatJawn() {
        int initialSize = quest.getLearningModules().size();
        String newModule = "Learn Git branching and merging";

        quest.addLearningModule(newModule);

        List<String> updatedModules = quest.getLearningModules();
        assertEquals(initialSize + 1, updatedModules.size(), "List size increases by 1");
        assertTrue(
                updatedModules.contains(newModule),
                "The new module shows up when you get the list");
    }

    @Test
    void testRemoveLearningModule_ShouldTakeItOut() {
        String moduleToRemove = "Learn Git init command to start a repository";
        String nonExistentModule = "This module doesn't exist";

        int initialSize = quest.getLearningModules().size();

        boolean removeResult = quest.removeLearningModule(moduleToRemove);
        assertTrue(removeResult, "Method returns true when module exists");
        assertEquals(
                initialSize - 1,
                quest.getLearningModules().size(),
                "List size decreases when module removed");
        assertFalse(
                quest.getLearningModules().contains(moduleToRemove),
                "Module gets removed from the list");

        boolean removeNonExistent = quest.removeLearningModule(nonExistentModule);
        assertFalse(removeNonExistent, "Method returns false when module don't exist");
    }

    @Test
    void testSettersAndGetters() {
        quest.setId("new-id");
        assertEquals("new-id", quest.getId(), "ID setter works");

        quest.setName("New Name");
        assertEquals("New Name", quest.getName(), "Name setter works");

        quest.setDescription("New Description");
        assertEquals("New Description", quest.getDescription(), "Description setter works");

        quest.setDifficultyLevel(5);
        assertEquals(5, quest.getDifficultyLevel(), "Difficulty level setter works");
    }

    @Test
    void testEmptyConstructor() {
        Quest emptyQuest = new Quest();
        assertNull(emptyQuest.getId(), "ID is null for empty constructor");
        assertNull(emptyQuest.getName(), "Name is null for empty constructor");
        assertFalse(emptyQuest.isCompleted(), "Quest is not completed by default");
        assertTrue(emptyQuest.getLearningModules().isEmpty(), "Learning modules list is empty");
    }

    @Test
    void testAddNullOrEmptyModule() {
        int initialSize = quest.getLearningModules().size();

        quest.addLearningModule(null);
        assertEquals(initialSize, quest.getLearningModules().size(), "Null module not added");

        quest.addLearningModule("");
        assertEquals(initialSize, quest.getLearningModules().size(), "Empty string not added");

        quest.addLearningModule("   ");
        assertEquals(
                initialSize, quest.getLearningModules().size(), "Whitespace-only string not added");
    }
}
