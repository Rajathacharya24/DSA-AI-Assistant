# AI DSA Study Assistant

A Java Spring Boot application that acts like a personalized DSA tutor. It combines a problem library, user progress tracking, JWT-based authentication, and an AI assistant that helps learners with hints, concept explanations, and code review.

## 1. Project Purpose

This project helps students and interview candidates practice Data Structures and Algorithms in a guided way instead of just reading static solutions.

The system aims to:

- give structured DSA practice problems
- let users learn through progressive hints instead of instant answers
- analyze submitted Java code and provide beginner-friendly feedback
- track performance and learning streaks
- recommend the next topic/problem based on user progress
- provide a web dashboard and chat-style learning experience

## 2. Core Idea

The product behaves like a virtual coding mentor:

- a user logs in
- chooses a problem or asks for help
- the AI understands the context
- it may call backend tools such as get problem, get hint, get progress, or get recommendation
- the backend fetches data from the database and returns a contextual answer
- the system builds a learning loop around concept understanding, practice, and feedback

## 3. System Architecture

The project follows a layered architecture with a frontend, backend API, AI service, and database.

```text
User / Browser
      |
      v
Static Web App (HTML + CSS + JS)
      |
      v
Spring Boot REST API
      |
      +--> Authentication + Security
      |
      +--> Controllers
      |
      +--> Services
      |
      +--> AI Agent Layer
                |
                +--> Problem Tool
                +--> Hint Tool
                +--> Progress Tool
                +--> Recommendation Tool
      |
      +--> JPA / Repository Layer
      |
      v
PostgreSQL Database
      |
      v
LLM via Spring AI
```

## 4. Tech Stack

- Java 21
- Spring Boot 3
- Spring Security
- Spring Data JPA
- PostgreSQL
- Spring AI with OpenAI-style chat integration
- JWT for authentication
- HTML, CSS, JavaScript for the frontend
- Docker for local container-based setup

## 5. Project Structure

```text
DSA-AI-Assistant/
├── docker-compose.yml
├── Dockerfile
├── pom.xml
├── README.md
├── src/
│   ├── main/
│   │   ├── java/com/dsa/assistant/
│   │   │   ├── config/
│   │   │   ├── controller/
│   │   │   ├── dto/
│   │   │   ├── exception/
│   │   │   ├── model/
│   │   │   ├── repository/
│   │   │   ├── security/
│   │   │   └── service/
│   │   └── resources/
│   │       ├── application.properties
│   │       └── static/
│   │           ├── app.js
│   │           ├── index.html
│   │           └── style.css
│   └── test/
│       └── java/com/dsa/assistant/
└── target/
```

## 6. Main Components

### Frontend

The frontend is a lightweight static single-page app served from the Spring Boot resources folder.

It handles:

- login and registration
- dashboard rendering
- chat messages with the AI tutor
- progress view
- problem navigation

The browser logic is primarily in `src/main/resources/static/app.js`.

### Backend API

The backend exposes REST endpoints under `/api` and `/api/auth`.

Main controller responsibilities:

- `AuthController`: register and login accounts
- `AiChatController`: accept chat messages from the user
- `ProblemController`: list, fetch, create, and submit problems
- `ProgressController`: return user stats and recommendations
- `HealthController`: basic health endpoint

### Security Layer

Security is enforced using Spring Security and JWT tokens.

Flow:

1. User registers or logs in.
2. Backend validates credentials.
3. A JWT is generated and returned.
4. Later requests include the bearer token.
5. The filter checks the token before access is allowed.

Sensitive routes are protected, and only the authenticated user can access their own progress.

### AI Agent Layer

The AI assistant is wired through Spring AI and configured with a system prompt and callable tools.

The agent is designed to:

- answer concept questions in simple language
- guide users step by step instead of giving full solutions up front
- ask for context when needed
- use backend tools to fetch a problem, hint, user progress, or recommendation

The tool configuration is in `AgentTools.java`.

## 7. How the Core Workflow Works

### A. User Registration and Login

1. The user fills in registration details on the frontend.
2. `AuthController` receives the request.
3. `AuthService` validates the email and saves the user.
4. The password is encoded with BCrypt.
5. A JWT is created and returned to the client.
6. The browser stores the token and user ID in local storage.

During login:

- credentials are verified using `AuthenticationManager`
- the system loads the user identity and generates a fresh JWT

### B. Problem Access

The problem flow is handled by:

- `ProblemController`
- `ProblemService`
- `ProblemRepository`
- `TopicRepository`

The service can:

- list all problems
- fetch by ID
- filter by topic or difficulty
- create a new problem
- find a matching problem for the AI tutor

### C. AI Chat Interaction

When a user sends a message:

1. the frontend posts to `/api/chat`
2. `AiChatController` receives the request
3. `AgentService` sends the message to the configured chat client
4. the model uses its system prompt and available tools
5. it calls a tool such as:
   - `problemTool`
   - `hintTool`
   - `progressTool`
   - `recommendationTool`
6. the tool returns data from the service layer
7. the final answer is sent back to the browser

This is the heart of the learning experience: the AI acts more like a tutor than an answer generator.

### D. Hints and Guided Learning

The hint system is deliberately progressive.

The backend provides a problem context and then asks the AI to generate the right style of hint based on level:

- Hint 1: conceptual direction
- Hint 2: more specific approach
- Hint 3: near-solution guidance

The service also increments the hint count in the user progress record, so learning analytics can reflect struggle and usage.

### E. Code Review and Submission

The code review flow works like this:

1. user submits Java code for a problem
2. front-end calls the problem submit endpoint
3. controller passes request to `CodeReviewService`
4. the service combines:
   - problem details
   - the user code
   - the problem title and description
5. the AI evaluates the code for:
   - correctness
   - bugs
   - complexity
   - code quality
6. a structured review result is returned to the client

### F. Progress Tracking and Recommendations

The app tracks progress using `Progress`, `Attempt`, and `User` entities.

`ProgressService` computes metrics such as:

- total attempted problems
- solved problems
- hints used
- streak
- easy/medium/hard counts

`RecommendationService` identifies the weakest topic and recommends the next problem to solve. It chooses a topic with fewer solved problems and suggests a suitable difficulty level.

## 8. Data Model Overview

Core entities:

- `User`: account identity, authentication, and profile details
- `Topic`: DSA category such as Arrays, Strings, Hashing
- `Problem`: challenge metadata, description, samples, solution, and difficulty
- `Progress`: tracks a user’s state for a given problem
- `Attempt`: logs individual submission attempts and outcomes

This design allows the app to track both user behavior and learning history alongside the problem catalog.

## 9. Request Flow Example

Example flow for a user asking “Give me a hint for this Array problem”:

1. Browser sends chat request to `/api/chat` with the message.
2. Backend controller receives the request.
3. Agent service sends the query to the AI model.
4. AI decides a hint is needed.
5. `hintTool` is called.
6. `ProblemService.getHint(...)` loads the challenge and user activity.
7. `ProgressService` updates hint usage.
8. The AI returns a guided hint in natural language.
9. Frontend shows the hint in the chat window.

## 10. Setup and Run

### Prerequisites

- Java 21+
- Maven
- Docker (optional but recommended)
- PostgreSQL or Dockerized PostgreSQL
- LLM API key configured for Spring AI

### Option 1: Run with Docker

```bash
docker-compose up --build
```

This starts:

- the application container
- the PostgreSQL database container

### Option 2: Run Locally

```bash
mvn clean install
mvn spring-boot:run
```

### Environment Variables

The app uses environment variables such as:

- `SPRING_DATASOURCE_URL`
- `SPRING_DATASOURCE_USERNAME`
- `SPRING_DATASOURCE_PASSWORD`
- `SPRING_AI_OPENAI_API_KEY`
- `APP_JWT_SECRET`

The default config is defined in `src/main/resources/application.properties`.

## 11. Key API Endpoints

```text
POST /api/auth/register
POST /api/auth/login
GET /api/problems
GET /api/problems/{id}
GET /api/problems/topic/{topic}
GET /api/problems/difficulty/{difficulty}
POST /api/problems
POST /api/problems/{problemId}/submit
POST /api/chat
GET /api/users/{userId}/progress
GET /api/users/{userId}/recommendations
```

## 12. Main Design Strengths

- clean separation between API, business logic, and persistence
- AI tool-calling model instead of hardcoded response logic
- personalized learning recommendations
- beginner-friendly educational tone
- progress monitoring for interview preparation

## 13. Planned Improvement Areas

- add a secure code execution sandbox for hidden test cases
- add more DSA topics and problem categories
- improve recommendation logic with deeper analytics
- support more programming languages and multiple tutors
- add a richer dashboard with charts and insights

## 14. Summary

This project is a full-stack learning assistant built to make DSA preparation more personalized and interactive. It combines a modern Java backend, a lightweight frontend, secure user authentication, and an AI tutor that guides students toward understanding rather than simply supplying answers.

The overall design is centered around one principle: make learning adaptive, measurable, and supportive.
