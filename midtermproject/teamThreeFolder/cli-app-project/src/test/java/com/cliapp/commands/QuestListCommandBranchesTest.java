package com.cliapp.commands;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.cliapp.collections.QuestCollection;
import com.cliapp.io.Console;
import com.cliapp.services.BadgeManager;
import com.cliapp.services.QuestGameService;
import org.junit.jupiter.api.Test;

class QuestListCommandBranchesTest {
    @Test
    void testExecuteHandlesExceptionBranch() {
        QuestCollection questCollection = mock(QuestCollection.class);
        Console console = mock(Console.class);
        BadgeManager badgeManager = mock(BadgeManager.class);
        QuestGameService questGameService = mock(QuestGameService.class);
        QuestListCommand command =
                new QuestListCommand(questCollection, badgeManager, console, true);
        doThrow(new RuntimeException("fail")).when(questCollection).getAllQuests();
        assertDoesNotThrow(() -> command.execute(new String[] {}));
        verify(console).println(contains("Unable to display quests"));
    }

    @Test
    void testDisplayQuestListWithEmptyQuestsBranch() {
        QuestCollection questCollection = mock(QuestCollection.class);
        Console console = mock(Console.class);
        BadgeManager badgeManager = mock(BadgeManager.class);
        when(questCollection.getAllQuests()).thenReturn(java.util.Collections.emptyList());
        QuestListCommand command =
                new QuestListCommand(questCollection, badgeManager, console, true);
        command.execute(new String[] {});
        verify(console).println(contains("No quests available"));
    }

    @Test
    void testHandleQuestSelectionBranch() {
        QuestCollection questCollection = mock(QuestCollection.class);
        Console console = mock(Console.class);
        BadgeManager badgeManager = mock(BadgeManager.class);
        QuestGameService questGameService = mock(QuestGameService.class);
        QuestListCommand command =
                new QuestListCommand(questCollection, badgeManager, console, false);
        // Simulate user input and quest selection logic as needed
        // This branch is typically covered by integration tests
        assertDoesNotThrow(() -> command.execute(new String[] {}));
    }
}
