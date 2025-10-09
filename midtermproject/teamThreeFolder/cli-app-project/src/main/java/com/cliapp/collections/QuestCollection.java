package com.cliapp.collections;

import com.cliapp.domain.Quest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Custom data structure for managing quests Requirement: Custom data structure with add, remove,
 * update operations SOLID: Single Responsibility - manages only quests
 */
public class QuestCollection implements Iterable<Quest> {

    private List<Quest> quests;
    private Map<String, Quest> questIndex;

    public QuestCollection() {
        this.quests = new ArrayList<>();
        this.questIndex = new HashMap<>();
    }

    /** Add a quest to the collection */
    public boolean add(Quest quest) {
        if (quest == null || quest.getId() == null) {
            return false;
        }

        // Check if quest already exists
        if (questIndex.containsKey(quest.getId())) {
            return false;
        }

        quests.add(quest);
        questIndex.put(quest.getId(), quest);
        return true;
    }

    /** Remove a quest from the collection */
    public boolean remove(Quest quest) {
        if (quest == null || quest.getId() == null) {
            return false;
        }

        boolean removedFromList = quests.remove(quest);
        Quest removedFromIndex = questIndex.remove(quest.getId());

        return removedFromList && removedFromIndex != null;
    }

    /** Update a quest in the collection */
    public boolean update(String id, Quest quest) {
        if (id == null || quest == null) {
            return false;
        }

        Quest existingQuest = questIndex.get(id);
        if (existingQuest == null) {
            return false;
        }

        // Remove old quest
        quests.remove(existingQuest);
        questIndex.remove(id);

        // Add updated quest
        quest.setId(id); // Ensure ID matches
        quests.add(quest);
        questIndex.put(id, quest);

        return true;
    }

    /** Get quest by ID */
    public Quest getById(String id) {
        return questIndex.get(id);
    }

    /** Get all quests */
    public List<Quest> getAllQuests() {
        return new ArrayList<>(quests);
    }

    /** Get quests by completion status */
    public List<Quest> getQuestsByCompletionStatus(boolean completed) {
        List<Quest> filteredQuests = new ArrayList<>();
        for (Quest quest : quests) {
            if (quest.isCompleted() == completed) {
                filteredQuests.add(quest);
            }
        }
        return filteredQuests;
    }

    /** Get quests by difficulty level */
    public List<Quest> getQuestsByDifficulty(int difficultyLevel) {
        List<Quest> filteredQuests = new ArrayList<>();
        for (Quest quest : quests) {
            if (quest.getDifficultyLevel() == difficultyLevel) {
                filteredQuests.add(quest);
            }
        }
        return filteredQuests;
    }

    /** Get the size of the collection */
    public int size() {
        return quests.size();
    }

    /** Check if collection is empty */
    public boolean isEmpty() {
        return quests.isEmpty();
    }

    @Override
    public Iterator<Quest> iterator() {
        return quests.iterator();
    }
}
