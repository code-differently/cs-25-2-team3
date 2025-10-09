# 🗳️ Voting Application — User Stories & Acceptance Criteria

This document outlines the **user stories** and corresponding **acceptance criteria** for the Voting Application project.  
The goal of this application is to allow users to create accounts, make polls, vote, and view poll results — with administrative tools for managing polls.

---

## 📘 User Story 1 — Voter Account Creation & Login

**As a voter**, I want to be able to create/login to my account **so that my votes are registered in my name.**

### ✅ Acceptance Criteria
- [ ] User can register with a valid email, username, and password.  
- [ ] System validates that email and username are unique.  
- [ ] User can log in using their registered credentials.  
- [ ] On successful login, the system displays a confirmation and redirects to the dashboard/home page.  
- [ ] Invalid login credentials show an appropriate error message.  
- [ ] Logged-in users’ votes are tied to their account ID.  

---

## 🗂️ User Story 2 — Poll Creation

**As a user**, I want to be able to create a poll based upon a random topic **so that other users can vote on the questions I created.**

### ✅ Acceptance Criteria
- [ ] Logged-in users can access a “Create Poll” page or form.  
- [ ] Polls require at least one question and two answer options.  
- [ ] Users can generate or select a random topic for their poll.  
- [ ] System saves the poll with a unique ID and associates it with the creator’s account.  
- [ ] Other users can view and vote on active polls.  
- [ ] Confirmation message appears after successful poll creation.  

---

## 📊 User Story 3 — View Poll Results

**As a user**, I want to be able to overview the results of a poll **so that I can see the general consensus and opinion of the app's users.**

### ✅ Acceptance Criteria
- [ ] Users can view poll results after voting or from a “Results” page.  
- [ ] Poll results display total votes and percentage breakdown per option.  
- [ ] Results update dynamically when new votes are submitted.  
- [ ] Only existing polls can be viewed (invalid poll IDs show an error).  
- [ ] Results page clearly identifies the poll’s question and creator.  

---

## ⚙️ User Story 4 — Admin Management

**As an admin**, I want to be able to manage polls that users made **so that I can add, update, or remove them.**

### ✅ Acceptance Criteria
- [ ] Admin users can view a list of all polls with creator details.  
- [ ] Admins can edit poll questions, options, or status (active/inactive).  
- [ ] Admins can delete polls permanently or archive them.  
- [ ] Admin-only actions are restricted by role-based authentication.  
- [ ] Changes made by admins are reflected immediately in the system.  
- [ ] System logs admin actions for audit purposes.  

---

## 🧩 Summary

This document ensures all major features are testable and verifiable before implementation.  
Each user story can be mapped directly to development tasks, routes, and UI components.

---
