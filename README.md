# Spring AI in Practice – Model Evaluation, RAG and MCP

Examples and exercises for the one-day workshop *Spring AI in der Praxis* by
[Thorben Janssen](https://thorben-janssen.com).

The workshop covers:

- Spring AI basics: `ChatClient`, prompting, structured output, advisors and session memory
- Model evaluation: testing non-deterministic LLM responses and evaluating them at runtime
- Retrieval-Augmented Generation (RAG)
- Tool calling and the Model Context Protocol (MCP)

The slides are available as PDF in [`slides/`](slides/).

## Requirements

- Java 21
- Maven
- An OpenAI API key (provided during the workshop)
- [Ollama](https://ollama.com) with the following models:
  ```
  ollama pull qwen3:8b
  ollama pull bespoke-minicheck
  ```
- Docker, only for the Testcontainers examples in `lectures/04_Test_Testcontainers*`

### OpenAI API key

The examples read the OpenAI API key from the environment variable `MyVerySecretChatGptKey`:

```
# Linux / macOS
export MyVerySecretChatGptKey=<your key>

# Windows (PowerShell)
$env:MyVerySecretChatGptKey="<your key>"
```

Alternatively, you can set the variable in the run configuration of your IDE.

## Versions

| Library     | Version   |
|-------------|-----------|
| Spring AI   | 2.1.0-RC1 |
| Spring Boot | 4.2.0-RC1 |
| Vaadin      | 25.0.0    |

## Repository structure

```
lectures/    examples shown during the workshop
exercises/   exercises with their solutions
slides/      slides as PDF
```

Both `lectures/` and `exercises/` are multi-module Maven projects. You can open the folder in your IDE
or build all modules at once:

```
cd lectures
mvn verify
```

Most examples are Spring Boot applications with a small Vaadin chat UI. Start them via their
`main` method in your IDE or with `mvn spring-boot:run` in the module folder.

### Lectures

| Module                                     | Topic                                              |
|--------------------------------------------|----------------------------------------------------|
| `01_ErsteAnwendung`                        | Your first `ChatClient` application                |
| `02_Prompting_StructuredOutput`            | Prompting and structured output                    |
| `03_Advisors`                              | Advisors, including guardrail advisors             |
| `03_Session_Jdbc`                          | Session memory stored in a database (H2)           |
| `04_Test`                                  | Testing and evaluating LLM responses               |
| `04_Test_TestcontainersOllama`             | Tests with a local model in an Ollama container    |
| `04_Test_TestcontainersDockerModelRunner`  | Tests with a local model via Docker Model Runner   |
| `04_X_RuntimeEvaluation`                   | Evaluating responses at runtime                    |
| `05_RAG`                                   | Retrieval-Augmented Generation                     |
| `06_ToolCalling`                           | Tool calling                                       |
| `07_MCPServer`                             | MCP server (port 7070)                             |
| `07_MCPClient`                             | MCP client using the MCP server                    |

### Exercises

Each exercise has an `_Exercise` module with the starting point and a `_Solution` module with a
possible solution:

| Exercise                         | Topic                            |
|----------------------------------|----------------------------------|
| `02_Prompting_StructuredOutput`  | Prompting and structured output  |
| `04_Test`                        | Testing and evaluating responses |
| `05_RAG`                         | Retrieval-Augmented Generation   |
| `07_MCPServer`                   | Implementing an MCP server       |
| `07_MCPClient`                   | Using an MCP server as a client  |

The MCP client examples connect to the MCP server at `http://localhost:7070`. Start the
MCP server first.
