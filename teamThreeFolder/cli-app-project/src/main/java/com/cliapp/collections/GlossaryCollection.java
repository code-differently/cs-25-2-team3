package com.cliapp.collections;

import com.cliapp.domain.GlossaryEntry;
import java.util.*;

/**
 * Custom data structure for managing glossary entries SOLID: Single Responsibility - manages only
 * glossary entries
 */
public class GlossaryCollection implements Iterable<GlossaryEntry> {

    private final List<GlossaryEntry> entries;
    private final Map<String, GlossaryEntry> commandIndex;

    public GlossaryCollection() {
        this.entries = new ArrayList<>();
        this.commandIndex = new HashMap<>();
    }

    /** Add a glossary entry to the collection */
    public boolean add(GlossaryEntry entry) {
        if (entry != null && entry.getCommand() != null) {
            entries.add(entry);
            commandIndex.put(entry.getCommand().toLowerCase(), entry);
            return true;
        }
        return false;
    }

    /** Remove a glossary entry from the collection */
    public boolean remove(GlossaryEntry entry) {
        if (entry != null) {
            boolean removed = entries.remove(entry);
            if (removed && entry.getCommand() != null) {
                commandIndex.remove(entry.getCommand().toLowerCase());
            }
            return removed;
        }
        return false;
    }

    /** Update a glossary entry in the collection */
    public boolean update(String command, GlossaryEntry newEntry) {
        GlossaryEntry existing = getByCommand(command);
        if (existing != null && newEntry != null) {
            int index = entries.indexOf(existing);
            entries.set(index, newEntry);
            commandIndex.put(command.toLowerCase(), newEntry);
            return true;
        }
        return false;
    }

    /** Get glossary entry by command */
    public GlossaryEntry getByCommand(String command) {
        if (command != null) {
            return commandIndex.get(command.toLowerCase());
        }
        return null;
    }

    /** Get all glossary entries */
    public List<GlossaryEntry> getAllEntries() {
        return new ArrayList<>(entries);
    }

    /** Get entries by category */
    public List<GlossaryEntry> getEntriesByCategory(String category) {
        List<GlossaryEntry> result = new ArrayList<>();
        for (GlossaryEntry entry : entries) {
            if (category != null && category.equals(entry.getCategory())) {
                result.add(entry);
            }
        }
        return result;
    }

    /** Search entries by keyword */
    public List<GlossaryEntry> searchEntries(String keyword) {
        List<GlossaryEntry> result = new ArrayList<>();
        if (keyword != null) {
            String lowerKeyword = keyword.toLowerCase();
            for (GlossaryEntry entry : entries) {
                if (entry.getCommand().toLowerCase().contains(lowerKeyword)
                        || entry.getDefinition().toLowerCase().contains(lowerKeyword)) {
                    result.add(entry);
                }
            }
        }
        return result;
    }

    /** Get the size of the collection */
    public int size() {
        return entries.size();
    }

    /** Check if collection is empty */
    public boolean isEmpty() {
        return entries.isEmpty();
    }

    @Override
    public Iterator<GlossaryEntry> iterator() {
        return entries.iterator();
    }
}
