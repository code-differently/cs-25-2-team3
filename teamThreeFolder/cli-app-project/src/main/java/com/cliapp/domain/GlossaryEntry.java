package com.cliapp.domain;

/**
 * Glossary Entry domain object for Git commands and definitions SOLID: Single Responsibility -
 * represents a single glossary entry
 */
public class GlossaryEntry {

    private String command;
    private String definition;
    private String example;
    private String category;

    public GlossaryEntry() {
        // Default constructor
    }

    public GlossaryEntry(String command, String definition, String example, String category) {
        this.command = command;
        this.definition = definition;
        this.example = example;
        this.category = category;
    }

    // Getters and Setters
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

    public String getExample() {
        return example;
    }

    public void setExample(String example) {
        this.example = example;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    /** Format the glossary entry for display */
    public String formatForDisplay() {
        StringBuilder sb = new StringBuilder();
        sb.append("Command: ").append(command).append("\n");
        sb.append("Definition: ").append(definition).append("\n");
        if (example != null && !example.isEmpty()) {
            sb.append("Example: ").append(example).append("\n");
        }
        return sb.toString();
    }

    @Override
    public String toString() {
        return "GlossaryEntry{"
                + "command='"
                + command
                + '\''
                + ", definition='"
                + definition
                + '\''
                + ", example='"
                + example
                + '\''
                + ", category='"
                + category
                + '\''
                + '}';
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        GlossaryEntry other = (GlossaryEntry) obj;
        // Only compare command for equality, not definition/example/category
        return command != null && command.equals(other.command);
    }

    @Override
    public int hashCode() {
        return command != null ? command.hashCode() : 0;
    }
}
