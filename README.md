# DSA-AI-Assistant

An AI-powered Data Structures and Algorithms (DSA) learning assistant agent. Built with Spring Boot and Spring AI, it acts as a virtual study partner to help you understand and practice DSA concepts.

## Tech Stack

*   **Java 21**
*   **Spring Boot 3.4.x**
*   **Spring AI 1.0.0-M5 (OpenAI)**
*   **Spring Data JPA**
*   **PostgreSQL / H2 Database**
*   **Lombok**
*   **Maven**



## Getting Started

1.  **Set OpenAI API Key:**
    You need to configure your OpenAI API key. You can do this by setting an environment variable:
    ```bash
    export SPRING_AI_OPENAI_API_KEY=your_api_key_here
    ```
    Or by adding it to your `src/main/resources/application.properties` (or `application.yml`).

2.  **Build the project:**
    ```bash
    mvn clean package
    ```

3.  **Run the application:**
    ```bash
    mvn spring-boot:run
    ```

## Testing API Endpoints

The project includes HTTP request files in the root directory for easy testing:
*   `test-chat.http`
*   `test-progress.http`

You can use these files with the REST Client extension in VS Code or the built-in HTTP client in IntelliJ IDEA to send test requests to your running application.
