package com.cliapp.commands;

import com.cliapp.domain.GlossaryEntry;
import com.cliapp.services.GlossaryService;
import java.util.List;

/**
 * Command to display the glossary of Git commands SOLID: Single Responsibility - handles only
 * glossary display
 */
public class GlossaryCommand implements Command {

    private final GlossaryService glossaryService;

    public GlossaryCommand(GlossaryService glossaryService) {
        this.glossaryService = glossaryService;
    }

    @Override
    public void execute(String[] args) {
        try {
            System.out.println("\n=== Git Command Glossary ===");
            List<GlossaryEntry> entries = glossaryService.getAllEntries();

            // Handle null entries
            if (entries == null || entries.isEmpty()) {
                System.out.println("No commands available in the glossary.");
                return;
            }

            System.out.println("Available Git Commands:");
            for (int i = 0; i < entries.size(); i++) {
                GlossaryEntry entry = entries.get(i);
                if (entry != null && entry.getCommand() != null && entry.getDefinition() != null) {
                    System.out.printf(
                            "%d. %s - %s\n", i + 1, entry.getCommand(), entry.getDefinition());
                }
            }

            System.out.printf("\nTotal commands: %d\n", entries.size());
        } catch (Exception e) {
            System.err.println("Error loading glossary: " + e.getMessage());
            System.out.println("Unable to display glossary at this time.");
        }
    }

    @Override
    public String getDescription() {
        return "Display Git command glossary and definitions";
    }

    @Override
    public String getName() {
        return "glossary";
    }

    @Override
    public String getUsage() {
        return "glossary";
    }

    @Override
    public boolean validateArgs(String[] args) {
        return true; // Glossary command doesn't require arguments
    }
}
