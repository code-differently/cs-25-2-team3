package com.cliapp.commands;

import static org.junit.jupiter.api.Assertions.*;

import com.cliapp.domain.Quest;
import com.cliapp.models.UserSession;
import com.cliapp.services.QuestService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Continue Command Tests")
class ContinueCommandTest {

    @Mock
    private QuestService mockQuestService;

    @Mock
    private UserSession mockUserSession;

    @Mock
    private Quest mockQuest;

    private ContinueCommand continueCommand;

    @BeforeEach
    void setUp() {
        continueCommand = new ContinueCommand(mockQuestService, mockUserSession);
    }

    @Test
    @DisplayName("Given_ValidServices_When_CreateCommand_Then_CommandCreated")
    void testCommandCreation() {
        assertNotNull(continueCommand);
        assertEquals("continue", continueCommand.getName());
        assertEquals("continue", continueCommand.getUsage());
        assertEquals("Continue your current quest or resume progress", continueCommand.getDescription());
    }

    @Test
    @DisplayName("Given_QuestInProgress_When_Execute_Then_ShowsQuestInfo")
    void testExecuteWithQuestInProgress() {
        // Arrange
        String questId = "quest-123";
        when(mockUserSession.getCurrentQuestId()).thenReturn(questId);
        when(mockQuest.getName()).thenReturn("Git Basics");
        when(mockQuest.getCompletionStatus()).thenReturn("In Progress");
        when(mockQuestService.getQuestById(questId)).thenReturn(mockQuest);
        
        // Act
        continueCommand.execute(new String[]{});
        
        // Assert
        verify(mockUserSession).getCurrentQuestId();
        verify(mockQuestService).getQuestById(questId);
        verify(mockQuest).getName();
        verify(mockQuest).getCompletionStatus();
    }

    @Test
    @DisplayName("Given_NoQuestInProgress_When_Execute_Then_ShowsNoQuestMessage")
    void testExecuteWithoutQuestInProgress() {
        // Arrange
        when(mockUserSession.getCurrentQuestId()).thenReturn(null);
        
        // Act
        continueCommand.execute(new String[]{});
        
        // Assert
        verify(mockUserSession).getCurrentQuestId();
        verify(mockQuestService, never()).getQuestById(anyString());
    }

    @Test
    @DisplayName("Given_EmptyQuestId_When_Execute_Then_ShowsNoQuestMessage")
    void testExecuteWithEmptyQuestId() {
        // Arrange
        when(mockUserSession.getCurrentQuestId()).thenReturn("");
        
        // Act
        continueCommand.execute(new String[]{});
        
        // Assert
        verify(mockUserSession).getCurrentQuestId();
        verify(mockQuestService, never()).getQuestById(anyString());
    }

    @Test
    @DisplayName("Given_QuestNotFound_When_Execute_Then_ClearsCurrentQuestId")
    void testExecuteWithQuestNotFound() {
        // Arrange
        String questId = "nonexistent-quest";
        when(mockUserSession.getCurrentQuestId()).thenReturn(questId);
        when(mockQuestService.getQuestById(questId)).thenReturn(null);
        
        // Act
        continueCommand.execute(new String[]{});
        
        // Assert
        verify(mockUserSession).getCurrentQuestId();
        verify(mockQuestService).getQuestById(questId);
        verify(mockUserSession).setCurrentQuestId(null);
    }

    @Test
    @DisplayName("Given_ServiceThrowsException_When_Execute_Then_HandlesGracefully")
    void testExecuteWithServiceException() {
        // Arrange
        when(mockUserSession.getCurrentQuestId()).thenThrow(new RuntimeException("Session error"));
        
        // Act & Assert
        assertDoesNotThrow(() -> continueCommand.execute(new String[]{}));
        
        verify(mockUserSession).getCurrentQuestId();
    }

    @Test
    @DisplayName("Given_QuestServiceThrowsException_When_Execute_Then_HandlesGracefully")
    void testExecuteWithQuestServiceException() {
        // Arrange
        String questId = "quest-123";
        when(mockUserSession.getCurrentQuestId()).thenReturn(questId);
        when(mockQuestService.getQuestById(questId)).thenThrow(new RuntimeException("Service error"));
        
        // Act & Assert
        assertDoesNotThrow(() -> continueCommand.execute(new String[]{}));
        
        verify(mockUserSession).getCurrentQuestId();
        verify(mockQuestService).getQuestById(questId);
    }

    @Test
    @DisplayName("Given_ValidateArgs_When_AnyArgs_Then_ReturnsTrue")
    void testValidateArgs() {
        assertTrue(continueCommand.validateArgs(new String[]{}));
        assertTrue(continueCommand.validateArgs(new String[]{"extra", "args"}));
        assertTrue(continueCommand.validateArgs(null));
    }

    @Test
    @DisplayName("Given_Command_When_GetCommandDetails_Then_ReturnsCorrectInfo")
    void testCommandDetails() {
        assertEquals("continue", continueCommand.getName());
        assertEquals("continue", continueCommand.getUsage());
        assertNotNull(continueCommand.getDescription());
        assertFalse(continueCommand.getDescription().trim().isEmpty());
    }

    @Test
    @DisplayName("Given_NullQuestService_When_CreateCommand_Then_AcceptsNull")
    void testCommandCreationWithNullQuestService() {
        assertDoesNotThrow(() -> {
            ContinueCommand command = new ContinueCommand(null, mockUserSession);
            assertNotNull(command);
        });
    }

    @Test
    @DisplayName("Given_NullUserSession_When_CreateCommand_Then_AcceptsNull")
    void testCommandCreationWithNullUserSession() {
        assertDoesNotThrow(() -> {
            ContinueCommand command = new ContinueCommand(mockQuestService, null);
            assertNotNull(command);
        });
    }

    @Test
    @DisplayName("Given_MultipleExecutions_When_Execute_Then_EachExecutionIndependent")
    void testMultipleExecutions() {
        // First execution - no quest
        when(mockUserSession.getCurrentQuestId()).thenReturn(null);
        continueCommand.execute(new String[]{});
        
        // Second execution - has quest
        String questId = "quest-456";
        when(mockUserSession.getCurrentQuestId()).thenReturn(questId);
        when(mockQuestService.getQuestById(questId)).thenReturn(mockQuest);
        when(mockQuest.getName()).thenReturn("Advanced Git");
        when(mockQuest.getCompletionStatus()).thenReturn("50% Complete");
        
        continueCommand.execute(new String[]{});
        
        // Verify both executions
        verify(mockUserSession, times(2)).getCurrentQuestId();
        verify(mockQuestService, times(1)).getQuestById(questId);
    }

    @Test
    @DisplayName("Given_Command_When_ToString_Then_ReturnsNonNull")
    void testCommandToString() {
        String result = continueCommand.toString();
        assertNotNull(result);
        assertFalse(result.trim().isEmpty());
    }

    // Integration-style tests with realistic scenarios
    @Test
    @DisplayName("Given_UserRestartsApp_When_ExecuteContinue_Then_WorksCorrectly")
    void testRealisticUserScenario() {
        // Simulate user scenario: app restart, no current quest
        when(mockUserSession.getCurrentQuestId()).thenReturn(null);
        
        continueCommand.execute(new String[]{});
        
        verify(mockUserSession).getCurrentQuestId();
        verify(mockQuestService, never()).getQuestById(anyString());
    }

    @Test
    @DisplayName("Given_UserHasOngoingQuest_When_ExecuteContinue_Then_ShowsProgress")
    void testRealisticContinueScenario() {
        // Simulate user scenario: continuing an ongoing quest
        String questId = "git-fundamentals";
        when(mockUserSession.getCurrentQuestId()).thenReturn(questId);
        when(mockQuestService.getQuestById(questId)).thenReturn(mockQuest);
        when(mockQuest.getName()).thenReturn("Git Fundamentals");
        when(mockQuest.getCompletionStatus()).thenReturn("3/5 modules completed");
        
        continueCommand.execute(new String[]{});
        
        verify(mockQuestService).getQuestById(questId);
        verify(mockQuest).getName();
        verify(mockQuest).getCompletionStatus();
    }

    @Test
    @DisplayName("Given_QuestWithDifferentStatuses_When_Execute_Then_DisplaysStatus")
    void testQuestStatusDisplay() {
        String questId = "test-quest";
        when(mockUserSession.getCurrentQuestId()).thenReturn(questId);
        when(mockQuestService.getQuestById(questId)).thenReturn(mockQuest);
        when(mockQuest.getName()).thenReturn("Test Quest");
        
        // Test different completion statuses
        String[] statuses = {"Not Started", "In Progress", "Completed", "50% Complete"};
        
        for (String status : statuses) {
            when(mockQuest.getCompletionStatus()).thenReturn(status);
            continueCommand.execute(new String[]{});
            verify(mockQuest, atLeastOnce()).getCompletionStatus();
        }
    }

    @Test
    @DisplayName("Given_CommandInterface_When_CheckImplementation_Then_ImplementsCommand")
    void testCommandInterfaceImplementation() {
        assertTrue(continueCommand instanceof Command);
        
        // Test all Command interface methods
        assertDoesNotThrow(() -> continueCommand.execute(new String[]{}));
        assertDoesNotThrow(() -> continueCommand.getName());
        assertDoesNotThrow(() -> continueCommand.getDescription());
        assertDoesNotThrow(() -> continueCommand.getUsage());
        assertDoesNotThrow(() -> continueCommand.validateArgs(new String[]{}));
    }
}
