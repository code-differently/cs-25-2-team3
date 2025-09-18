package org.example;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit test to verify that menu navigation routes to the correct screens.
 */
public class AppLaunchMenuNavigationTest {

    @Test
    public void testNavigationByNumber_ShouldRouteCorrectly() {
        App app = new App();

        // Test navigation to Quest screen
        String questResult = app.navigateFromMenu(1);
        assertEquals("Quest Screen", questResult, "Typing 1 should take you to Quest Screen");

        // Test navigation to Continue screen
        String continueResult = app.navigateFromMenu(2);
        assertEquals("Continue Screen", continueResult, "Typing 2 should take you to Continue Screen");

        // Test navigation to Quit screen
        String quitResult = app.navigateFromMenu(3);
        assertEquals("Quit Screen", quitResult, "Typing 3 should take you to Quit Screen");

        // Test navigation to Badges screen
        String badgesResult = app.navigateFromMenu(4);
        assertEquals("Badges Screen", badgesResult, "Typing 4 should take you to Badges Screen");

        // Test navigation to Glossary screen
        String glossaryResult = app.navigateFromMenu(5);
        assertEquals("Glossary Screen", glossaryResult, "Typing 5 should take you to Glossary Screen");
    }
}
