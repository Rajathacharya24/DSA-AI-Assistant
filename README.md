# AI DSA Study Assistant Agent

An intelligent, AI-powered study assistant designed to act as a personalized Data Structures and Algorithms (DSA) tutor. It provides progressive hints, code reviews, problem recommendations, and tracks user progress to help developers prepare for technical interviews.

## Problem

Preparing for DSA interviews can be an overwhelming and frustrating experience. Traditional coding platforms provide static solutions that don't explain the underlying thought process. When learners get stuck, they often have to look up the complete answer, which hinders actual learning and problem-solving skill development. Furthermore, users lack personalized feedback on their code quality and a clear roadmap of what to study next based on their weaknesses.

## Solution

This project introduces an AI-driven DSA tutor that acts as a virtual pair-programming partner. Instead of just giving the answer, the AI agent utilizes a tool-calling architecture to retrieve problem context, generate progressive conceptual hints, evaluate submitted code, and recommend the next best problem based on the user's historical performance. It mimics a real human tutor by guiding the user towards the solution step-by-step.

## Features

- **AI DSA Tutor:** A conversational agent trained to act as a strict but helpful programming tutor.
- **Tool Calling:** The AI can dynamically invoke backend Java functions to fetch data (e.g., user stats, problem details) before answering.
- **Problem Generation/Retrieval:** Access to a curated list of DSA problems.
- **Progressive Hints:** The AI provides small nudges instead of full solutions when requested.
- **AI Code Review:** Automated evaluation of user code for correctness, time complexity, and clean code practices.
- **Progress Tracking:** Monitors successful submissions, failure rates, and topic proficiency.
- **Personalized Recommendations:** Suggests subsequent problems based on tracked weaknesses.
- **JWT Authentication:** Secure, stateless user sessions.
- **PostgreSQL:** Reliable relational data storage for users, problems, and progress.
- **Web Dashboard:** A clean, intuitive frontend interface for learning.

## Architecture

The system follows a modern multi-tier architecture, augmented by an AI agent layer:

```text
                         USER
                           │
                           ▼
                  ┌─────────────────┐
                  │   Web Frontend  │
                  │ HTML/CSS/JS     │
                  └────────┬────────┘
                           │
                           ▼
                  ┌─────────────────┐
                  │ Spring Boot API │
                  └────────┬────────┘
                           │
                           ▼
                  ┌─────────────────┐
                  │   AI AGENT      │
                  │                 │
                  │ Intent          │
                  │ Context         │
                  │ Tool Selection  │
                  └────────┬────────┘
                           │
          ┌────────────────┼─────────────────┐
          │                │                 │
          ▼                ▼                 ▼
   Problem Tool       Hint Tool       Progress Tool
          │                │                 │
          └────────────────┼─────────────────┘
                           │
                           ▼
                  ┌─────────────────┐
                  │ Service Layer   │
                  └────────┬────────┘
                           │
                           ▼
                  ┌─────────────────┐
                  │ JPA/Hibernate   │
                  └────────┬────────┘
                           │
                           ▼
                  ┌─────────────────┐
                  │   PostgreSQL    │
                  └─────────────────┘

                           +

                  ┌─────────────────┐
                  │      LLM        │
                  │  Spring AI      │
                  └─────────────────┘
```

## Tech Stack

- **Java 21**
- **Spring Boot**
- **Spring AI**
- **PostgreSQL**
- **JPA/Hibernate**
- **HTML/CSS/JavaScript**
- **JWT (JSON Web Tokens)**
- **Docker**

## API Documentation

- `POST /api/auth/register` - Register a new user account.
- `POST /api/auth/login` - Authenticate a user and receive a JWT.
- `GET /api/problems` - Retrieve a paginated list of available DSA problems.
- `GET /api/problems/{id}` - Get detailed information about a specific problem.
- `POST /api/chat` - Interact with the AI DSA Tutor (chat/hint interface).
- `POST /api/submissions` - Submit code for evaluation and review.
- `GET /api/progress` - Fetch the authenticated user's progress and statistics.

## Setup

### Prerequisites
- Java 21 or higher
- Maven
- PostgreSQL running locally or via Docker
- An API Key for your chosen LLM (e.g., Google Gemini or OpenAI)

### Local Installation

1. **Clone the repository:**
   ```bash
   git clone https://github.com/yourusername/ai-dsa-assistant.git
   cd ai-dsa-assistant
   ```

2. **Configure the database:**
   Ensure PostgreSQL is running and create a database named `dsadb`.

3. **Configure Environment Variables:**
   Create a `.env` file in the root directory (or configure via your IDE) based on the `.env.example` file below.

4. **Build and Run:**
   ```bash
   ./mvnw clean install
   ./mvnw spring-boot:run
   ```

## Environment Variables

Create a `.env` file (do not commit this to version control):

```properties
# .env.example
DB_URL=jdbc:postgresql://localhost:5432/dsadb
DB_USERNAME=postgres
DB_PASSWORD=your_db_password
JWT_SECRET=your_super_secret_jwt_key_here
GEMINI_API_KEY=your_gemini_api_key_here
```

## Running the Project (via Docker)

You can quickly start the required PostgreSQL database using Docker:

```bash
# Start PostgreSQL container
docker run --name dsa-postgres -e POSTGRES_PASSWORD=postgres -e POSTGRES_DB=dsadb -p 5432:5432 -d postgres

# Start the Spring Boot Application
./mvnw spring-boot:run
```

## Screenshots

*(Add screenshots of your application here)*

![Dashboard](screenshots/dashboard.png)
*User Dashboard and Progress Overview*

![Chat Interface](screenshots/chat.png)
*Interactive AI Chat for Progressive Hints*

![Code Review](screenshots/code-review.png)
*AI Code Evaluation and Feedback*

## Future Improvements

- **Multiple AI agents:** Specialized agents (e.g., one specifically for hints, one strictly for deep code review).
- **RAG using DSA documentation:** Integrating Retrieval-Augmented Generation with official documentation or textbook material for more accurate explanations.
- **Code execution sandbox:** A secure environment (like Docker or Firecracker) to physically run and test user code against hidden test cases.
- **More programming languages:** Support for Python, C++, and JavaScript code submissions.
- **Advanced analytics:** Granular charts and heatmaps showing learning velocity.
- **Cloud deployment:** Containerizing the full stack and deploying to AWS or GCP using Kubernetes.
