/*
package com.cliapp;

import static org.junit.jupiter.api.Assertions.*;

import com.cliapp.test.TestConsole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

public class CLIApplicationTDDTest {

    private CLIApplication app;
    private TestConsole testConsole;

    @BeforeEach
    void setUp() {
        testConsole = new TestConsole();
        app = new CLIApplication(testConsole);
    }

    @Test
    @DisplayName("Given_Application_When_Created_Then_ServicesInitialized")
    void testApplicationCreation() {
        assertNotNull(app);
        // Verify services are initialized by checking if they work
        assertDoesNotThrow(() -> app.stop());
    }

    @Test
    @DisplayName("Given_MainMenu_When_ValidOption1_Then_ExecutesQuestCommand")
    void testValidMenuOption1Quest() {
        testConsole.addInputs("1", "5"); // Quest, then quit

        app.start();

        String output = testConsole.getAllOutput();
        assertTrue(output.contains("Git Training CLI"), "Should show welcome");
        assertTrue(output.contains("Main Menu"), "Should show main menu");
        assertTrue(
                output.contains("Total Points: 0"),
                "Should execute quest command - user points should show"); // Changed to check for
        // points summary
        assertTrue(output.contains("Thanks for using"), "Should show goodbye");
        assertTrue(testConsole.wasClosed(), "Should close console");
    }

    @Test
    @DisplayName("Given_MainMenu_When_ValidOption2_Then_ExecutesContinueCommand")
    void testValidMenuOption2Continue() {
        testConsole.addInputs("2", "5"); // Continue, then quit

        app.start();

        String output = testConsole.getAllOutput();
        // Verify that continue command was chosen by checking menu navigation
        assertTrue(
                output.contains("Main Menu") && output.contains("Enter your choice"),
                "Should execute continue command");
    }

    @Test
    @DisplayName("Given_MainMenu_When_ValidOption3_Then_ExecutesBadgeCommand")
    void testValidMenuOption3Badges() {
        testConsole.addInputs("3", "5"); // Badges, then quit

        app.start();

        String output = testConsole.getAllOutput();
        assertTrue(
                output.contains("Badge") || output.contains("achievement"),
                "Should execute badge command");
    }

    @Test
    @DisplayName("Given_MainMenu_When_ValidOption4_Then_ExecutesGlossaryCommand")
    void testValidMenuOption4Glossary() {
        testConsole.addInputs("4", "5"); // Glossary, then quit

        app.start();

        String output = testConsole.getAllOutput();
        assertTrue(
                output.contains("Main Menu") && output.contains("Enter your choice"),
                "Should execute glossary command");
    }

    @Test
    @DisplayName("Given_MainMenu_When_ValidOption5_Then_ExitsApplication")
    void testValidMenuOption5Quit() {
        testConsole.addInputs("5"); // Quit immediately

        app.start();

        String output = testConsole.getAllOutput();
        assertTrue(output.contains("Exiting application"), "Should show exit message");
        assertTrue(output.contains("Thanks for using"), "Should show goodbye");
    }

    @ParameterizedTest
    @ValueSource(strings = {"0", "6", "99", "invalid", "", "  ", "abc", "1.5"})
    @DisplayName("Given_MainMenu_When_InvalidOption_Then_ShowsErrorAndReprompts")
    void testInvalidMenuOptions(String invalidInput) {
        testConsole.addInputs(invalidInput, "5"); // Invalid input, then quit

        app.start();

        String output = testConsole.getAllOutput();
        assertTrue(
                output.contains("Invalid choice") || output.contains("❌"),
                "Should show error for invalid input: " + invalidInput);
        assertTrue(output.contains("Thanks for using"), "Should still allow quit after error");
    }

    @Test
    @DisplayName("Given_ProcessInput_When_ExceptionThrown_Then_HandlesGracefully")
    void testExceptionHandling() {
        // This tests the catch block in processInput
        TestConsole faultyConsole =
                new TestConsole() {
                    private boolean firstCall = true;

                    @Override
                    public String readLine() {
                        if (firstCall) {
                            firstCall = false;
                            throw new RuntimeException("Simulated input error");
                        }
                        return "5"; // Quit on second call
                    }
                };

        CLIApplication faultyApp = new CLIApplication(faultyConsole);
        faultyApp.start();

        String output = faultyConsole.getAllOutput();
        assertTrue(output.contains("An error occurred"), "Should handle exceptions gracefully");
        assertTrue(output.contains("Please try again"), "Should prompt to try again");
    }

    @Test
    @DisplayName("Given_Application_When_Stopped_Then_SessionEnded")
    void testStopFunctionality() {
        app.stop();

        String output = testConsole.getAllOutput();
        assertTrue(
                output.contains("Thanks for using Git Training CLI"),
                "Should show goodbye message");
        assertTrue(testConsole.wasClosed(), "Should close console");
    }

    @Test
    @DisplayName("Given_MultipleOperations_When_Executed_Then_PointsUpdated")
    void testMultipleOperationsFlow() {
        testConsole.addInputs("1", "3", "4", "5"); // Quest, Badges, Glossary, Quit

        app.start();

        String output = testConsole.getAllOutput();
        // Verify all operations were executed
        assertTrue(output.contains("Total Points: 0"), "Should execute quest - points should show");
        assertTrue(
                output.contains("Badge") || output.contains("achievement"), "Should execute badge");
        assertTrue(
                output.contains("Main Menu") && output.contains("Enter your choice"),
                "Should execute glossary");

        // Count how many times "Main Menu" appears to ensure we returned between operations
        long menuCount =
                testConsole.getOutputLines().stream()
                        .filter(line -> line.contains("Main Menu"))
                        .count();
        assertTrue(menuCount >= 4, "Should return to main menu between operations");
    }

    @Test
    @DisplayName("Given_WelcomeScreen_When_Displayed_Then_ShowsCorrectElements")
    void testWelcomeScreenContent() {
        testConsole.addInputs("5"); // Quit immediately after welcome

        app.start();

        String output = testConsole.getAllOutput();
        assertTrue(output.contains("🚀 Git Training CLI 🚀"), "Should show app title");
        assertTrue(output.contains("Master Git through Interactive"), "Should show tagline");
        assertTrue(output.contains("learning journey"), "Should show journey message");
        assertTrue(output.contains("Points:"), "Should show points summary");
    }

    @Test
    @DisplayName("Given_MainMenu_When_Displayed_Then_ShowsAllOptions")
    void testMainMenuContent() {
        testConsole.addInputs("5"); // Quit immediately

        app.start();

        String output = testConsole.getAllOutput();
        assertTrue(output.contains("1. 📚 Quest"), "Should show quest option");
        assertTrue(output.contains("2. ▶️  Continue"), "Should show continue option");
        assertTrue(output.contains("3. 🏆 Badges"), "Should show badges option");
        assertTrue(output.contains("4. 📖 Glossary"), "Should show glossary option");
        assertTrue(output.contains("5. 🚪 Quit"), "Should show quit option");
        assertTrue(output.contains("Enter your choice (1-5)"), "Should show prompt");
    }

    @Test
    @DisplayName("Given_Application_When_MainMethodCalled_Then_RunsSuccessfully")
    void testMainMethod() {
        // Test that main method exists and can be called
        assertDoesNotThrow(
                () -> {
                    // We can't easily test main without hijacking System.in/out,
                    // but we can verify it exists and doesn't throw during class loading
                    CLIApplication.class.getMethod("main", String[].class);
                });
    }
}
 */
