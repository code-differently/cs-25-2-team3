package com.cliapp.collections;

import static org.junit.jupiter.api.Assertions.*;

import com.cliapp.domain.Quest;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class QuestCollectionTest {

    private QuestCollection questCollection;
    private Quest quest1;
    private Quest quest2;

    @BeforeEach
    void setUp() {
        questCollection = new QuestCollection();
        quest1 =
                new Quest(
                        "quest1",
                        "First Quest",
                        "Learn basics",
                        Arrays.asList("Module 1", "Module 2"),
                        1);
        quest2 =
                new Quest(
                        "quest2",
                        "Second Quest",
                        "Advanced topics",
                        Arrays.asList("Module 3", "Module 4"),
                        5);
    }

    @Test
    void testQuestCollectionCreation() {
        assertNotNull(questCollection, "Quest collection should be created");
        assertEquals(0, questCollection.size(), "New collection should be empty");
        assertTrue(questCollection.isEmpty(), "New collection should be empty");
    }

    @Test
    void testAddQuest() {
        assertTrue(questCollection.add(quest1), "Should successfully add quest");
        assertEquals(1, questCollection.size(), "Collection should have one quest");
        assertFalse(questCollection.isEmpty(), "Collection should not be empty");

        assertTrue(questCollection.add(quest2), "Should successfully add second quest");
        assertEquals(2, questCollection.size(), "Collection should have two quests");
    }

    @Test
    void testAddNullQuest() {
        assertFalse(questCollection.add(null), "Should not add null quest");
        assertEquals(0, questCollection.size(), "Collection should remain empty");
    }

    @Test
    void testAddQuestWithNullId() {
        Quest questWithNullId = new Quest(null, "Name", "Description", Arrays.asList("Module"), 3);
        assertFalse(questCollection.add(questWithNullId), "Should not add quest with null ID");
        assertEquals(0, questCollection.size(), "Collection should remain empty");
    }

    @Test
    void testAddDuplicateQuest() {
        assertTrue(questCollection.add(quest1), "Should successfully add quest first time");
        assertFalse(questCollection.add(quest1), "Should not add duplicate quest");
        assertEquals(1, questCollection.size(), "Collection should still have only one quest");
    }

    @Test
    void testRemoveQuest() {
        questCollection.add(quest1);
        questCollection.add(quest2);

        assertTrue(questCollection.remove(quest1), "Should successfully remove quest");
        assertEquals(1, questCollection.size(), "Collection should have one less quest");
        assertNull(questCollection.getById(quest1.getId()), "Should not find removed quest by ID");
        assertNotNull(questCollection.getById(quest2.getId()), "Should still find other quest");
    }

    @Test
    void testRemoveNonExistentQuest() {
        questCollection.add(quest1);

        assertFalse(questCollection.remove(quest2), "Should not remove non-existent quest");
        assertEquals(1, questCollection.size(), "Collection size should not change");
    }

    @Test
    void testRemoveNullQuest() {
        questCollection.add(quest1);

        assertFalse(questCollection.remove(null), "Should not remove null quest");
        assertEquals(1, questCollection.size(), "Collection size should not change");
    }

    @Test
    void testGetById() {
        questCollection.add(quest1);
        questCollection.add(quest2);

        Quest found = questCollection.getById("quest1");
        assertNotNull(found, "Should find quest by ID");
        assertEquals(quest1.getId(), found.getId(), "Should return correct quest");

        Quest notFound = questCollection.getById("nonexistent");
        assertNull(notFound, "Should return null for non-existent ID");
    }

    @Test
    void testGetAllQuests() {
        questCollection.add(quest1);
        questCollection.add(quest2);

        List<Quest> allQuests = questCollection.getAllQuests();
        assertEquals(2, allQuests.size(), "Should return all quests");
        assertTrue(
                allQuests.stream().anyMatch(q -> q.getId().equals(quest1.getId())),
                "Should contain first quest");
        assertTrue(
                allQuests.stream().anyMatch(q -> q.getId().equals(quest2.getId())),
                "Should contain second quest");
    }

    @Test
    void testIterator() {
        questCollection.add(quest1);
        questCollection.add(quest2);

        Iterator<Quest> iterator = questCollection.iterator();
        assertNotNull(iterator, "Should return iterator");

        int count = 0;
        while (iterator.hasNext()) {
            Quest quest = iterator.next();
            assertNotNull(quest, "Iterator should return valid quests");
            count++;
        }
        assertEquals(2, count, "Iterator should iterate over all quests");
    }

    @Test
    void testGetQuestsByDifficulty() {
        Quest beginnerQuest =
                new Quest("b1", "Beginner Quest", "Easy", Arrays.asList("Module 1"), 1);
        Quest advancedQuest =
                new Quest("a1", "Advanced Quest", "Hard", Arrays.asList("Module 2"), 5);

        questCollection.add(beginnerQuest);
        questCollection.add(advancedQuest);

        List<Quest> beginnerQuests = questCollection.getQuestsByDifficulty(1);
        assertEquals(1, beginnerQuests.size(), "Should find one beginner quest");
        assertEquals(
                1, beginnerQuests.get(0).getDifficultyLevel(), "Should return correct difficulty");

        List<Quest> advancedQuests = questCollection.getQuestsByDifficulty(5);
        assertEquals(1, advancedQuests.size(), "Should find one advanced quest");

        List<Quest> expertQuests = questCollection.getQuestsByDifficulty(3);
        assertEquals(0, expertQuests.size(), "Should find no expert quests");
    }

    @Test
    void testGetQuestsByCompletionStatus() {
        questCollection.add(quest1);
        questCollection.add(quest2);

        // Initially no quests are completed
        List<Quest> completedQuests = questCollection.getQuestsByCompletionStatus(true);
        assertEquals(0, completedQuests.size(), "Should have no completed quests initially");

        List<Quest> incompleteQuests = questCollection.getQuestsByCompletionStatus(false);
        assertEquals(2, incompleteQuests.size(), "Should have two incomplete quests");

        // Mark one quest as completed
        quest1.setCompleted(true);
        completedQuests = questCollection.getQuestsByCompletionStatus(true);
        assertEquals(1, completedQuests.size(), "Should have one completed quest");
        assertEquals(
                quest1.getId(),
                completedQuests.get(0).getId(),
                "Should return the completed quest");
    }

    @Test
    void testUpdateQuest() {
        questCollection.add(quest1);

        Quest updatedQuest =
                new Quest(
                        "quest1",
                        "Updated Quest",
                        "Updated description",
                        Arrays.asList("New Module"),
                        3);
        assertTrue(
                questCollection.update("quest1", updatedQuest), "Should successfully update quest");

        Quest retrieved = questCollection.getById("quest1");
        assertEquals("Updated Quest", retrieved.getName(), "Should have updated name");
        assertEquals(
                "Updated description",
                retrieved.getDescription(),
                "Should have updated description");
        assertEquals(3, retrieved.getDifficultyLevel(), "Should have updated difficulty");
    }

    @Test
    void testUpdateNonExistentQuest() {
        Quest newQuest =
                new Quest("quest1", "New Quest", "Description", Arrays.asList("Module"), 1);
        assertFalse(
                questCollection.update("nonexistent", newQuest),
                "Should not update non-existent quest");
    }

    @Test
    void testUpdateWithNullValues() {
        questCollection.add(quest1);

        assertFalse(questCollection.update(null, quest2), "Should not update with null ID");
        assertFalse(questCollection.update("quest1", null), "Should not update with null quest");
    }
}
