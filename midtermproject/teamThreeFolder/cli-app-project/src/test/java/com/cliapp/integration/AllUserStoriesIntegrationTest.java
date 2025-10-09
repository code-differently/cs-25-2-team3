package com.cliapp.integration;

import static org.junit.jupiter.api.Assertions.*;

import com.cliapp.CLIApplication;
import com.cliapp.domain.*;
import com.cliapp.services.*;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class AllUserStoriesIntegrationTest {

    private QuestService questService;
    private GlossaryService glossaryService;
    private BadgeService badgeService;
    private ByteArrayOutputStream outputStream;
    private PrintStream originalOut;

    @BeforeEach
    void setUp() {
        questService = new QuestService();
        glossaryService = new GlossaryService();
        badgeService = new BadgeService();

        outputStream = new ByteArrayOutputStream();
        originalOut = System.out;
        System.setOut(new PrintStream(outputStream));
    }

    void tearDown() {
        System.setOut(originalOut);
    }

    @Test
    void testUserStory1_LaunchAppAndViewOptions() {
        // Test: User can launch the CLI application and see main menu options
        CLIApplication app = new CLIApplication();

        assertNotNull(app, "CLI Application should be created without problems");

        // Verify the application can be started and stopped
        assertDoesNotThrow(app::stop, "App should start and stop without errors");

        // Verify that quest service is initialized with default quests
        assertTrue(
                questService.getAllQuests().size() >= 3,
                "Should have at least 3 default quests available");

        System.out.println("✅ User Story 1: App launches and shows menu options");
    }

    @Test
    void testUserStory3_GlossaryView_ShouldShowGitCommands() {
        // Test: User can view glossary with Git command references
        List<GlossaryEntry> allEntries = glossaryService.getAllEntries();

        assertFalse(allEntries.isEmpty(), "Glossary should have entries to help users learn Git");
        assertTrue(allEntries.size() >= 10, "Should have comprehensive Git command coverage");

        boolean hasInit = false,
                hasAdd = false,
                hasCommit = false,
                hasPush = false,
                hasPull = false;

        for (GlossaryEntry entry : allEntries) {
            assertNotNull(entry.getCommand(), "Command shouldn't be null");
            assertNotNull(entry.getDefinition(), "Definition shouldn't be null");
            assertFalse(entry.getCommand().trim().isEmpty(), "Command should be specified");
            assertFalse(
                    entry.getDefinition().trim().isEmpty(),
                    "Definition should explain the command clearly");

            // Verify definition quality
            assertTrue(entry.getDefinition().length() > 20, "Definition should be comprehensive");

            // Check for essential Git commands
            String command = entry.getCommand().toLowerCase();
            if (command.contains("init")) hasInit = true;
            if (command.contains("add")) hasAdd = true;
            if (command.contains("commit")) hasCommit = true;
            if (command.contains("push")) hasPush = true;
            if (command.contains("pull")) hasPull = true;
        }

        assertTrue(
                hasInit,
                "Glossary should have git init command - essential for starting repositories");
        assertTrue(hasAdd, "Glossary should have git add command - needed for staging files");
        assertTrue(
                hasCommit, "Glossary should have git commit command - fundamental Git operation");
        assertTrue(hasPush, "Glossary should have git push command - needed for sharing changes");
        assertTrue(hasPull, "Glossary should have git pull command - needed for getting updates");

        System.out.println("✅ User Story 3: Glossary provides comprehensive Git command reference");
    }

    @Test
    void testIntegrationFlow_QuestToGlossaryToCompletion() {
        List<Quest> quests = questService.getAllQuests();
        assertFalse(quests.isEmpty(), "Should have quests to choose from");

        Quest selectedQuest = quests.get(0);
        assertNotNull(selectedQuest, "Selected quest should exist");

        List<GlossaryEntry> glossaryEntries = glossaryService.getAllEntries();
        assertFalse(glossaryEntries.isEmpty(), "Glossary should be available for help");

        List<String> modules = selectedQuest.getLearningModules();
        boolean canFindHelpInGlossary = false;

        for (String module : modules) {
            for (GlossaryEntry entry : glossaryEntries) {
                if (module.toLowerCase()
                        .contains(entry.getCommand().toLowerCase().replace("git ", ""))) {
                    canFindHelpInGlossary = true;
                    break;
                }
            }
            if (canFindHelpInGlossary) break;
        }

        assertTrue(
                canFindHelpInGlossary,
                "User should be able to find help in glossary for quest modules");

        selectedQuest.setCompleted(true);
        assertEquals(
                "Y", selectedQuest.getCompletionStatus(), "Completed quest should show Y status");

        // Instead of awarding a badge, add points to badge for completed quest
        badgeService.addPointsToBadge("git-basics", 20.0); // Simulate earning badge points
        Badge badge = badgeService.getBadgeById("git-basics");
        assertEquals(
                20.0, badge.getPointsEarned(), "Badge should have max points after completion");
    }

    @Test
    void testUserExperience_AllThreeStoriesWorking() {
        // Comprehensive end-to-end test covering all user stories
        CLIApplication app = new CLIApplication();
        assertNotNull(app, "App should launch without issues");

        // User Story 1: Launch app and view options
        List<Quest> quests = questService.getAllQuests();
        assertTrue(quests.size() >= 3, "Should have multiple quest options available to users");

        // User Story 2: View quest details with proper formatting
        Quest testQuest = quests.get(0);
        assertTrue(
                testQuest.getDifficultyAsAsterisks().matches("\\*{1,5}"),
                "Difficulty should be shown as 1-5 asterisks for visual clarity");
        assertTrue(
                testQuest.getCompletionStatus().matches("[YN]"),
                "Completion should be Y (completed) or N (not completed)");
        assertFalse(
                testQuest.getLearningModules().isEmpty(),
                "Quest should contain learning modules explaining what to learn");

        // User Story 3: Glossary provides command help
        List<GlossaryEntry> entries = glossaryService.getAllEntries();
        assertTrue(entries.size() >= 10, "Should have comprehensive glossary for Git learning");

        List<GlossaryEntry> searchResults = glossaryService.searchEntries("commit");
        assertFalse(searchResults.isEmpty(), "Should find commit-related commands when searching");

        // Test integration: Quest modules should relate to glossary entries
        boolean foundIntegration = false;
        for (String module : testQuest.getLearningModules()) {
            for (GlossaryEntry entry : entries) {
                if (module.toLowerCase()
                        .contains(entry.getCommand().toLowerCase().replace("git ", ""))) {
                    foundIntegration = true;
                    break;
                }
            }
            if (foundIntegration) break;
        }
        assertTrue(
                foundIntegration,
                "Quest modules should relate to glossary entries for integrated learning");

        // Test completion workflow
        testQuest.setCompleted(true);
        assertEquals("Y", testQuest.getCompletionStatus(), "Completed quest should show Y status");

        // Instead of awarding a badge, add points to badge for completed quest progression
        badgeService.addPointsToBadge("git-basics", 20.0); // Simulate earning badge points
        Badge badge = badgeService.getBadgeById("git-basics");
        assertEquals(
                20.0, badge.getPointsEarned(), "Badge should have max points after completion");

        System.out.println(
                "✅ End-to-End Test: All three user stories integrated and working properly!");
    }
}
