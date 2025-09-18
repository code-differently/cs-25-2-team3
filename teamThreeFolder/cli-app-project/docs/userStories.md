# Git Training CLI Application - User Stories and Sprint Planning

## Product Vision
A command-line interface application that teaches Git and GitHub fundamentals through interactive learning modules, practical command execution, and progressive skill development in a controlled environment.

## Definition of Done (Adjusted for One-Week Timeline)
- Feature implementation completed and basic functionality verified
- Core functionality manually tested and validated
- Basic documentation updated (CLI help, README with usage examples)
- Code committed to main branch
- Application can be launched and demonstrates key learning features

## Scrum Sprint Planning and Delivery Strategy
**Project Timeline**: September 15-22, 2025 (Due: Monday, September 22)
**Sprint Duration**: 2 days each | **Team Capacity**: Adjusted for academic project timeline

## User Stories
- **User Story 1 (a)**: As a user, I want to launch the application and view a list of available options 
- **User Story 2 (b)**: As a user, I want to select quest and view a list of available learning modules with their title, respective difficulty levels, and completion status and complete scenarios from learning module with required commands inputted by the user
- **User Story 3 (c)**: As a user, I want to view glossary with a bunch of commands before attempting to execute quests if I am lower in my level of understanding and memorization of

## Acceptance Criteria
(a) When the application is launched, a list of all available options is displayed (quest, continue, quit, badges, glossary)
(a) Options are displayed in a structured list format that is easy to navigate
(a) Can type number assosciated with option and go to that screen


(b) Each learning module shows:
    - Title
    - Difficulty level (e.g., *Beginner*, Intermediate, Advanced)
    - Completion status (e.g., Complete/Incomplete)
(b) Options are displayed in a structured list format that is easy to navigate
(b) If you select the learning module, you are shown a brief scenario to respond to
(b) If your answer is correct this adds to your badges  (points recieved) and returns you to the main menu with your point update from (0 --> current points/badges)
(b) If your answer is wrong this gives you a quick review extending from glossary before reprompting the same question

 

(c) Each command is in the glossary the module has at least a definition and one example.
(c) Glossary should be set up in the same funtionality as quest list but with word definition and the example should be accessible if needed.
(c) If person sends wrong input in scenario for quest it should pull from this glossary and show them that commands specified example and definition

   
## Daily Scrum Framework
**Daily Standup Structure** (15 minutes max):
1. **What did I accomplish yesterday?**
2. **What will I work on today?**
3. **Are there any impediments or blockers?**

## Success Metrics (MVP Definition)
- **Functional**: User can complete 3 Git learning modules end-to-end
- **Educational**: Command demonstrations are clear and helpful
- **Technical**: Application starts in <3 seconds, handles errors gracefully
- **User Experience**: Intuitive navigation, clear progress indicators
- **Delivery**: Working demo ready by Monday morning with documentation
