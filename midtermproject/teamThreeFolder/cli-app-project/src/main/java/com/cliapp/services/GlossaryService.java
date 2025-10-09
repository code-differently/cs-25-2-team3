package com.cliapp.services;

import com.cliapp.collections.GlossaryCollection;
import com.cliapp.domain.GlossaryEntry;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Service class for managing glossary operations SOLID: Single Responsibility - handles glossary
 * business logic and JSON reading
 */
public class GlossaryService {

    private final GlossaryCollection glossaryCollection;
    private final ObjectMapper objectMapper;

    public GlossaryService() {
        this.glossaryCollection = new GlossaryCollection();
        this.objectMapper = new ObjectMapper();
        loadGlossaryFromJson();
    }

    /** Get the glossary collection */
    public GlossaryCollection getGlossaryCollection() {
        return glossaryCollection;
    }

    /** Load glossary entries from JSON file */
    private void loadGlossaryFromJson() {
        try {
            InputStream inputStream = getClass().getResourceAsStream("/glossary.json");
            if (inputStream == null) {
                System.err.println("Could not find glossary.json file. Creating default entries.");
                createDefaultEntries();
                return;
            }

            JsonNode root = objectMapper.readTree(inputStream);
            JsonNode entriesNode = root.get("glossaryEntries");

            if (entriesNode != null && entriesNode.isArray()) {
                for (JsonNode entryNode : entriesNode) {
                    String command = entryNode.get("command").asText();
                    String definition = entryNode.get("definition").asText();
                    String example = entryNode.get("example").asText();
                    String category = entryNode.get("category").asText();

                    GlossaryEntry entry = new GlossaryEntry(command, definition, example, category);
                    glossaryCollection.add(entry);
                }
            }
        } catch (Exception e) {
            System.err.println("Error loading glossary from JSON: " + e.getMessage());
            createDefaultEntries();
        }
    }

    /** Create default entries if JSON loading fails */
    private void createDefaultEntries() {
        addEntry(
                "git init",
                "Initialize a new Git repository",
                "git init my-project",
                "Repository Setup");
        addEntry("git add", "Add files to staging area", "git add .", "Staging Changes");
        addEntry(
                "git commit",
                "Create a new commit",
                "git commit -m \"message\"",
                "Committing Changes");
    }

    /** Add a new glossary entry */
    public void addEntry(String command, String definition, String example, String category) {
        GlossaryEntry entry = new GlossaryEntry(command, definition, example, category);
        glossaryCollection.add(entry);
    }

    /** Get all glossary entries */
    public List<GlossaryEntry> getAllEntries() {
        return glossaryCollection.getAllEntries();
    }

    /** Get glossary entry by command */
    public GlossaryEntry getEntryByCommand(String command) {
        return glossaryCollection.getByCommand(command);
    }

    /** Get entries by category */
    public List<GlossaryEntry> getEntriesByCategory(String category) {
        return glossaryCollection.getEntriesByCategory(category);
    }

    /** Search entries by keyword */
    public List<GlossaryEntry> searchEntries(String keyword) {
        return glossaryCollection.searchEntries(keyword);
    }

    /** Get all unique categories */
    public Set<String> getAllCategories() {
        return glossaryCollection.getAllEntries().stream()
                .map(GlossaryEntry::getCategory)
                .collect(Collectors.toSet());
    }

    /** Get the total number of entries */
    public int getEntryCount() {
        return glossaryCollection.size();
    }
}
