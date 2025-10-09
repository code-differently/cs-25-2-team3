package com.cliapp.domain;

import java.util.List;

/** Question domain object for quest scenarios */
public class Question {
    private String level;
    private String scenario;
    private List<Option> options;
    private String correct;
    private Feedback feedback;

    public Question() {}

    public Question(
            String level,
            String scenario,
            List<Option> options,
            String correct,
            Feedback feedback) {
        this.level = level;
        this.scenario = scenario;
        this.options = options;
        this.correct = correct;
        this.feedback = feedback;
    }

    // Getters and setters
    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getScenario() {
        return scenario;
    }

    public void setScenario(String scenario) {
        this.scenario = scenario;
    }

    public List<Option> getOptions() {
        return options;
    }

    public void setOptions(List<Option> options) {
        this.options = options;
    }

    public String getCorrect() {
        return correct;
    }

    public void setCorrect(String correct) {
        this.correct = correct;
    }

    public Feedback getFeedback() {
        return feedback;
    }

    public void setFeedback(Feedback feedback) {
        this.feedback = feedback;
    }

    public static class Option {
        private String id;
        private String command;

        public Option() {}

        public Option(String id, String command) {
            this.id = id;
            this.command = command;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getCommand() {
            return command;
        }

        public void setCommand(String command) {
            this.command = command;
        }

        // Adapter methods for legacy/test expectations
        public void setKey(String key) {
            this.id = key;
        }

        public String getKey() {
            return this.id;
        }

        public void setValue(String value) {
            this.command = value;
        }

        public String getValue() {
            return this.command;
        }
    }

    public static class Feedback {
        private String correct;
        private IncorrectFeedback incorrect;

        public Feedback() {}

        public Feedback(String correct, IncorrectFeedback incorrect) {
            this.correct = correct;
            this.incorrect = incorrect;
        }

        public String getCorrect() {
            return correct;
        }

        public void setCorrect(String correct) {
            this.correct = correct;
        }

        public IncorrectFeedback getIncorrect() {
            return incorrect;
        }

        public void setIncorrect(IncorrectFeedback incorrect) {
            this.incorrect = incorrect;
        }

        // Adapter methods expected by tests
        public void setCorrectFeedback(String msg) {
            this.correct = msg;
        }

        public String getCorrectFeedback() {
            return this.correct;
        }

        public void setIncorrectFeedback(String msg) {
            if (this.incorrect == null) {
                this.incorrect = new IncorrectFeedback(null, msg, null, null, false);
            } else {
                this.incorrect.setDefinition(msg);
            }
        }

        public String getIncorrectFeedback() {
            return this.incorrect != null ? this.incorrect.getDefinition() : null;
        }
    }

    public static class IncorrectFeedback {
        private String command;
        private String definition;
        private String analogy;
        private String example;
        private boolean retry;

        public IncorrectFeedback() {}

        public IncorrectFeedback(
                String command, String definition, String analogy, String example, boolean retry) {
            this.command = command;
            this.definition = definition;
            this.analogy = analogy;
            this.example = example;
            this.retry = retry;
        }

        public String getCommand() {
            return command;
        }

        public void setCommand(String command) {
            this.command = command;
        }

        public String getDefinition() {
            return definition;
        }

        public void setDefinition(String definition) {
            this.definition = definition;
        }

        public String getAnalogy() {
            return analogy;
        }

        public void setAnalogy(String analogy) {
            this.analogy = analogy;
        }

        public String getExample() {
            return example;
        }

        public void setExample(String example) {
            this.example = example;
        }

        public boolean isRetry() {
            return retry;
        }

        public void setRetry(boolean retry) {
            this.retry = retry;
        }
    }
}
