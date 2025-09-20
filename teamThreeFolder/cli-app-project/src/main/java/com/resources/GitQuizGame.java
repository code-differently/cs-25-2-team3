package com.resources;

import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class GitQuizGame {

    static class Option {
        String id;
        String command;
        Option(String id, String command) {
            this.id = id;
            this.command = command;
        }
    }

    static class Feedback {
        String correct;
        Incorrect incorrect;
        Feedback(String correct, Incorrect incorrect) {
            this.correct = correct;
            this.incorrect = incorrect;
        }
    }

    static class Incorrect {
        String definition;
        String analogy;
        boolean retry;
        Incorrect(String definition, String analogy, boolean retry) {
            this.definition = definition;
            this.analogy = analogy;
            this.retry = retry;
        }
    }

    static class Question {
        String level;
        String scenario;
        List<Option> options;
        String correct;
        Feedback feedback;
        Question(String level, String scenario, List<Option> options, String correct, Feedback feedback) {
            this.level = level;
            this.scenario = scenario;
            this.options = options;
            this.correct = correct;
            this.feedback = feedback;
        }
    }

    public static void main(String[] args) {
        List<Question> questions = Arrays.asList(
            new Question(
                "Beginner",
                "It’s 12:58 PM on a Monday. You’ve just finished editing your homework files for Mr. Mays’ class. You want to make sure your staged changes are officially saved in your project history so that they can be included in your upcoming pull request. What is the appropriate git command?",
                Arrays.asList(
                    new Option("a", "git push"),
                    new Option("b", "git commit"),
                    new Option("c", "git request")
                ),
                "b",
                new Feedback(
                    "Great, you Git it!",
                    new Incorrect(
                        "Saves staged changes to the local repository with a message describing what was changed.",
                        "Like sealing your homework folder and writing a note in your personal log about what’s inside.",
                        true
                    )
                )
            ),
            new Question(
                "Beginner",
                "While reviewing your homework, you notice that several files have been modified locally, but none of them are marked to be saved in the repository yet. What is the best way to get them ready for inclusion in the next commit?",
                Arrays.asList(
                    new Option("a", "git add"),
                    new Option("b", "git commit"),
                    new Option("c", "git push")
                ),
                "a",
                new Feedback(
                    "Lesss Git it!",
                    new Incorrect(
                        "Stages changes in your working directory so they will be included in the next commit.",
                        "Like putting your completed homework papers into a folder so they’re ready to submit.",
                        true
                    )
                )
            ),
            new Question(
                "Beginner",
                "You’re about to start Lesson 02 homework, but the instructor has warned everyone not to make changes directly on the main branch. You need to create a new branch and switch directly into it. Which command do you use?",
                Arrays.asList(
                    new Option("a", "git checkout -b"),
                    new Option("b", "git create"),
                    new Option("c", "git merge")
                ),
                "a",
                new Feedback(
                    "Git on with ya bad self!",
                    new Incorrect(
                        "Creates a new branch and switches to it immediately.",
                        "Like taking a fresh folder labeled 'Lesson 02 Homework' so you can start your work without disturbing the main class folder.",
                        true
                    )
                )
            ),
            new Question(
                "Intermediate",
                "You just recorded your latest changes in your local project history. You want your GitHub fork to reflect this so you can continue working on other branches. What command should you use?",
                Arrays.asList(
                    new Option("a", "git files"),
                    new Option("b", "git commit"),
                    new Option("c", "git push")
                ),
                "c",
                new Feedback(
                    "You're pushing greatness! Git on outta here!",
                    new Incorrect(
                        "Sends your local commits to a remote repository (e.g., GitHub).",
                        "Like taking your sealed homework folder and dropping it into Mr. Mays’ main filing cabinet so he can see your updates.",
                        true
                    )
                )
            ),
            new Question(
                "Intermediate",
                "You open your local copy of the repo and realize the main repository has received updates since your last check. Which command ensures your local copy stays in sync without losing your work?",
                Arrays.asList(
                    new Option("a", "git push"),
                    new Option("b", "git pull"),
                    new Option("c", "git merge")
                ),
                "b",
                new Feedback(
                    "Nicely pulled together! Git it done!",
                    new Incorrect(
                        "Fetches and integrates changes from a remote repository into your current branch.",
                        "Like checking Mr. Mays’ main folder and adding any new homework papers to your own copy so you stay up to date.",
                        true
                    )
                )
            ),
            new Question(
                "Advanced",
                "You’ve finished editing files on one branch and committed your work. Now you want to fix a bug in a different branch. Which command allows you to switch branches safely?",
                Arrays.asList(
                    new Option("a", "git checkout <other-branch>"),
                    new Option("b", "git add <other-branch>"),
                    new Option("c", "git push <other-branch>")
                ),
                "a",
                new Feedback(
                    "Exactly! You really git’n the hang of this!",
                    new Incorrect(
                        "Switches your working directory to another branch so you can see or continue work there.",
                        "Like closing one notebook after finishing some notes and opening another notebook to review a different subject.",
                        true
                    )
                )
            ),
            new Question(
                "Advanced",
                "You accidentally deleted your local copy of your forked repo. Luckily, it still exists on GitHub. Which command allows you to get a fresh copy and continue working?",
                Arrays.asList(
                    new Option("a", "git fetch"),
                    new Option("b", "git pull"),
                    new Option("c", "git clone <repo-url>")
                ),
                "c",
                new Feedback(
                    "Git in the zone and crush it!",
                    new Incorrect(
                        "Creates a local copy of a remote repository.",
                        "Like getting a fresh copy of the class folder from Mr. Mays if your local one got lost, so you can keep working independently.",
                        true
                    )
                )
            ),
            new Question(
                "Advanced",
                "Mr. Mays wants to make sure a student's commits are applied on top of the latest main branch updates before merging. Which command keeps the project history clean?",
                Arrays.asList(
                    new Option("a", "git applied"),
                    new Option("b", "git rebase main"),
                    new Option("c", "git pull")
                ),
                "b",
                new Feedback(
                    "Git up, git learning, git awesome!",
                    new Incorrect(
                        "Moves your commits on top of the latest changes from another branch to keep the history clean.",
                        "Like making sure your homework is added on top of the latest class notes before combining it into the main folder, so nothing gets mixed up or duplicated.",
                        true
                    )
                )
            ),
            new Question(
                "Intermediate",
                "You want to review the history of commits in your repository to see what changes have been made. Which git command will show you a list of commits?",
                Arrays.asList(
                    new Option("a", "git clone"),
                    new Option("b", "git history"),
                    new Option("c", "git log")
                ),
                "c",
                new Feedback(
                    "Looking back never felt so good! Lesss git it!",
                    new Incorrect(
                        "Displays a list of commits in the repository, showing the history of changes.",
                        "Like flipping through your completed homework folders to see what you submitted and when.",
                        true
                    )
                )
            )
        );

        Scanner scanner = new Scanner(System.in);
        System.out.println("Welcome to the Git Quiz Game!");
        int score = 0;

        for (Question q : questions) {
            boolean answered = false;
            while (!answered) {
                System.out.println("\n[" + q.level + "] " + q.scenario);
                for (Option opt : q.options) {
                    System.out.println(opt.id + ") " + opt.command);
                }
                System.out.print("Your answer: ");
                String input = scanner.nextLine().trim().toLowerCase();

                if (input.equals(q.correct)) {
                    System.out.println(q.feedback.correct);
                    score++;
                    answered = true;
                } else {
                    System.out.println("Incorrect.");
                    System.out.println("Definition: " + q.feedback.incorrect.definition);
                    System.out.println("Analogy: " + q.feedback.incorrect.analogy);
                    if (q.feedback.incorrect.retry) {
                        System.out.println("Try again!");
                    } else {
                        answered = true;
                    }
                }
            }
        }
        System.out.println("\nQuiz complete! Your score: " + score + "/" + questions.size());
        scanner.close();
    }
}