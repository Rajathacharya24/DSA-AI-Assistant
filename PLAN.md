# DSA AI Assistant — Development Plan

## 1. Goal

Build a smart learning platform where a student does not just solve random problems, but follows a structured learning flow:

1. Study a plan first
2. Practice on focused problems
3. Validate logic before submission
4. Let AI review the solution quality
5. Improve the approach
6. Track progress on a dashboard
7. Move toward the final goal step by step

This version will focus on the student learning journey, not only answer generation.

---

## 2. Core Idea

The application should behave like a real mentor.

Instead of immediately giving the answer, it should:

- suggest what the user should study
- generate a small learning plan
- recommend next problems based on difficulty and topic
- allow the student to solve gradually
- validate logic, best-case and worst-case scenarios, and common edge cases
- rate the quality of the approach
- give AI feedback on how to improve
- show progress toward a final goal

---

## 3. Proposed Learning Workflow

### Phase 1: Study Plan

Before solving, the system should create a plan.

Example:

- Goal: Arrays and Strings fundamentals
- Weekly target: 3 easy, 2 medium, 1 hard problem
- Learning sequence:
  - concept refresh
  - pattern recognition
  - example walk-through
  - targeted practice
  - review mistakes

The app should show:

- current topic
- target difficulty
- problems assigned
- expected time commitment
- progress percentage

### Phase 2: Practice

Once the student starts, the app should support:

- reading the problem clearly
- understanding input/output
- identifying constraints
- selecting an approach
- solving with reasoning before final code

The system should encourage thinking before coding.

### Phase 3: Validation Before Submission

Before final submission, the app should ask the student to verify:

- Best-case scenario
- Worst-case scenario
- Empty input / edge input
- Time complexity
- Space complexity
- Correctness of the logic
- Whether the chosen data structure is appropriate

This checkpoint is critical. It teaches students to judge their own solution before asking the AI for feedback.

### Phase 4: AI Mentor Review

After submission, AI should respond with:


### Phase 5: Improvement Loop
- simplify the solution
- resubmit or re-evaluate
### Phase 5: Improvement Loop
### Phase 5: Improvement Loop

The student should then:

- revise the logic
- handle missing edge cases
- improve time complexity
- simplify the solution
- resubmit or re-evaluate

This creates a proper improvement cycle.

### Phase 6: Dashboard and Goal Tracking

The dashboard should answer these questions:

- Where am I now?
- What is my target?
- Which topic am I weak in?
- What should I do next?
- How much progress have I made?

The dashboard should show:

- completed problems
- current streak
- solved by topic
- difficulty distribution
- weak areas
- recommended next task
- learning score

---

## 4. Functional Modules to Build

### 4.1 Study Plan Module

Responsibilities:

- create weekly or daily plan
- assign topic-specific tasks
- generate beginner-friendly goals
- allow user to track milestones

Possible entities:

- StudyPlan
- StudyGoal
- LearningPath
- DailyTask

### 4.2 Practice Module

Responsibilities:

- load a selected DSA problem
- show statement, example input, and constraints
- support code preparation
- allow user to submit solution

Possible entities:

- SessionAttempt
- PracticeRound
- SubmittedSolution

### 4.3 Validation Module

Responsibilities:

- check edge cases
- show best-case/worst-case reasoning
- evaluate complexity assumptions
- detect likely logic gaps

Possible outputs:

- Edge Cases Covered
- Complexity Review
- Runtime Reasoning
- Potential pitfalls

### 4.4 AI Evaluation Module

Responsibilities:

- judge solution quality
- rank approach clarity
- explain bug risks
- suggest improvement steps
- rate logic quality out of 10

Possible output format:

- Logic Score: 8/10
- Correctness: Good
- Issue: Missing null/empty handling
- Improvement: Use a hash map for faster lookup

### 4.5 Progress Dashboard Module

Responsibilities:

- show learning progress
- show goal completion
- show topic strengths and weaknesses
- show recommended next action

Metrics:

- solved count
- attempted count
- streak
- hints used
- easy/medium/hard solved
- overall learning score

---

## 5. Proposed Backend Design

### Core APIs

- `POST /api/plans` — create learning plan
- `GET /api/plans/{userId}` — fetch user plan
- `POST /api/practice/start` — start a practice session
- `POST /api/problems/{id}/submit` — submit code
- `POST /api/validation/review` — validate logic before submission
- `POST /api/ai/review` — AI approach review
- `GET /api/users/{id}/dashboard` — fetch dashboard progress

### Main services

- `StudyPlanService`
- `PracticeService`
- `ValidationService`
- `AiReviewService`
- `DashboardService`

---

## 6. Proposed Frontend Flow

### Login / Dashboard

- user logs in
- dashboard shows goal status and next action

### Study Plan View

- user sees weekly goals
- plan is generated from selected topic and difficulty

### Problem Practice View

- user opens a selected problem
- reads statement, examples, constraints
- writes solution
- clicks "Validate Logic"

### Review View

- app shows:
  - best case
  - worst case
  - edge case checks
  - complexity review
  - AI feedback

### Goal Dashboard View

- show current progress
- show next recommended action
- show strong and weak areas

---

## 7. Recommended Product Priorities

### Priority 1: Basic learning flow

- plan generation
- practice session
- submission review
- progress tracking

### Priority 2: Quality validation

- best/worst case analysis
- complexity review
- edge-case evaluation

### Priority 3: Smart AI mentor

- logic rating
- improvement suggestions
- personalized guidance

### Priority 4: Dashboard and motivation

- clear goal progress
- weak area detection
- next-step recommendations

---

## 8. Suggested MVP Scope

For the first version, keep the scope small but effective:

- user login
- topic-based study plan
- problem list
- solution submission
- AI review for logic quality
- best/worst-case checklist
- dashboard with status and next actions

This is enough to validate the learning model and product direction.

---

## 9. Success Criteria

The product is successful when:

- the user understands what to study next
- the user can follow a structured learning path
- the AI gives useful feedback beyond just final answers
- the student can see progress clearly
- the system helps improve reasoning, not just code output

---

## 10. Final Direction

This project should evolve into a structured DSA coaching platform where the student:

- studies a plan first
- practices with purpose
- validates logic before submitting
- improves through AI guidance
- tracks their growth on a dashboard

This approach makes the product more realistic, educational, and valuable than a simple code assistant.
