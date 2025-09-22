package com.cliapp.services;

import com.cliapp.domain.Question;
import com.cliapp.io.Console;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.InputStream;
import java.util.*;

/** Service for handling quest game functionality */
public class QuestGameService {

    private final List<Question> questions;
    private final ObjectMapper objectMapper;
    private final Console console;

    public QuestGameService() {
        this(new com.cliapp.io.SystemConsole());
        loadQuestionsFromJson();
    }

    public QuestGameService(Console console) {
        this.questions = new ArrayList<>();
        this.objectMapper = new ObjectMapper();
        this.console = console;
        loadQuestionsFromJson();
    }

    private void loadQuestionsFromJson() {
        try {
            InputStream inputStream = getClass().getResourceAsStream("/Quest.json");

            if (inputStream == null) {
                console.println("Could not find Quest.json file.");
                return;
            }

            JsonNode root = objectMapper.readTree(inputStream);
            JsonNode questionsNode = root.get("questions");

            if (questionsNode != null && questionsNode.isArray()) {
                for (JsonNode questionNode : questionsNode) {
                    Question question = parseQuestion(questionNode);
                    if (question != null) {
                        questions.add(question);
                    }
                }
            }
        } catch (Exception e) {
            console.println("Error loading questions from JSON: " + e.getMessage());
        }
    }

    private Question parseQuestion(JsonNode questionNode) {
        try {
            String level = questionNode.get("level").asText();
            String scenario = questionNode.get("scenario").asText();
            String correct = questionNode.get("correct").asText();

            List<Question.Option> options = new ArrayList<>();
            JsonNode optionsNode = questionNode.get("options");
            if (optionsNode != null && optionsNode.isArray()) {
                for (JsonNode optionNode : optionsNode) {
                    String id = optionNode.get("id").asText();
                    String command = optionNode.get("command").asText();
                    options.add(new Question.Option(id, command));
                }
            }

            Question.Feedback feedback = null;
            JsonNode feedbackNode = questionNode.get("feedback");
            if (feedbackNode != null) {
                String correctMsg = feedbackNode.get("correct").asText();

                Question.IncorrectFeedback incorrectFeedback = null;
                JsonNode incorrectNode = feedbackNode.get("incorrect");
                if (incorrectNode != null) {
                    String command =
                            incorrectNode.has("command")
                                    ? incorrectNode.get("command").asText()
                                    : null;
                    String definition = incorrectNode.get("definition").asText();
                    String analogy =
                            incorrectNode.has("analogy")
                                    ? incorrectNode.get("analogy").asText()
                                    : null;
                    String example =
                            incorrectNode.has("example")
                                    ? incorrectNode.get("example").asText()
                                    : analogy;
                    boolean retry = incorrectNode.get("retry").asBoolean();
                    incorrectFeedback =
                            new Question.IncorrectFeedback(
                                    command, definition, analogy, example, retry);
                }

                feedback = new Question.Feedback(correctMsg, incorrectFeedback);
            }

            return new Question(level, scenario, options, correct, feedback);
        } catch (Exception e) {
            System.err.println("Error parsing question: " + e.getMessage());
            return null;
        }
    }

    public void playQuest() {
        playQuest("beginner");
    }

    public void playQuest(String level) {
        Question question = getQuestionByLevel(level);
        if (question == null) {
            console.println("No question found for level: " + level);
            return;
        }

        console.println("🎮 Starting " + level + " level quest!");
        console.println("");

        boolean correct = askQuestion(question);

        if (correct) {
            double points = getPointsForLevel(level);
            console.println("🌟 Quest completed! You earned " + points + " points!");
        } else {
            console.println("💪 Keep practicing! Try again when you're ready.");
        }
    }

    private boolean askQuestion(Question question) {
        console.println("🎯 " + question.getScenario());
        console.println("");

        // Display options
        for (Question.Option option : question.getOptions()) {
            console.println(option.getId() + ") " + option.getCommand());
        }

        while (true) {
            console.print("\nYour answer: ");
            String userAnswer = console.readLine().trim().toLowerCase();

            if (userAnswer.equals(question.getCorrect())) {
                console.println("✅ " + question.getFeedback().getCorrect());
                return true;
            } else {
                console.println("❌ Incorrect!");
                Question.IncorrectFeedback incorrect = question.getFeedback().getIncorrect();
                if (incorrect != null) {
                    if (incorrect.getCommand() != null) {
                        console.println("💡 Correct command: " + incorrect.getCommand());
                    }
                    console.println("� " + incorrect.getDefinition());
                    if (incorrect.getExample() != null) {
                        console.println("🔍 " + incorrect.getExample());
                    }

                    if (incorrect.isRetry()) {
                        console.println("\n🔄 Try again!");
                        continue;
                    }
                }
                return false;
            }
        }
    }

    private double getPointsForLevel(String level) {
        if (level == null) {
            return 5;
        }
        switch (level.toLowerCase()) {
            case "beginner":
                return 5;
            case "intermediate":
                return 7.5;
            case "advanced":
                return 10;
            default:
                return 5;
        }
    }

    public double calculatePointsForLevel(String level) {
        return getPointsForLevel(level);
    }

    public boolean hasQuestions() {
        return !questions.isEmpty();
    }

    public String getQuestTitle() {
        return "🎮 === Git Quest Started ===";
    }

    public String getQuestInstructions() {
        return "Answer the questions to test your Git knowledge!";
    }

    private void showQuestResults(int correct, int total, int points) {
        clearScreen();
        System.out.println("🏆 === Quest Complete ===");
        System.out.println("─".repeat(40));
        System.out.printf("✅ Correct Answers: %d/%d\n", correct, total);
        System.out.printf("⭐ Points Earned: %d\n", points);

        double percentage = (double) correct / total * 100;
        if (percentage >= 80) {
            System.out.println("🎉 Excellent work! You're a Git master!");
        } else if (percentage >= 60) {
            System.out.println("👍 Good job! Keep practicing!");
        } else {
            System.out.println("📚 Keep studying! Practice makes perfect!");
        }

        System.out.println("\nReturning to main menu...");
        System.out.println("─".repeat(40));
    }

    private void clearScreen() {
        // Simple clear screen for better UX
        System.out.print("\033[2J\033[H");
    }

    public int getQuestionCount() {
        return questions.size();
    }

    private Question getQuestionByLevel(String level) {
        // Find questions matching the level
        List<Question> levelQuestions =
                questions.stream().filter(q -> q.getLevel().equalsIgnoreCase(level)).toList();

        if (levelQuestions.isEmpty()) {
            return null;
        }

        // Return a random question from the level
        Random random = new Random();
        return levelQuestions.get(random.nextInt(levelQuestions.size()));
    }
}
