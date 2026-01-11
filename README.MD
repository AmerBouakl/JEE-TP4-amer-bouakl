# Amer Bot (Spring AI + MCP + Telegram Client)

This project creates a Telegram bot powered by Spring AI and integrates with a Model Context Protocol (MCP) server.

## Prerequisites

- JDK 21
- Maven (or use the provided `mvnw` wrapper)
- A Telegram Bot Token (from @BotFather)
- OpenAI API Key

## Configuration

Before running the application, you must set the following environment variables:

- `OPENAI_API_KEY`: Your OpenAI API Key.
- `TELEGRAM_API_KEY`: Your Telegram Bot Token.

Alternatively, you can modify `src/main/resources/application.properties` directly, but **do not commit your keys**.

## Usage

This project consists of two parts: the MCP Server and the Bot Client.

### 1. Run the MCP Server

The MCP server provides tools/resources to the AI agent.

**PowerShell:**
```powershell
./run_mcp.ps1
```
*Or manually:*
```bash
cd mcp-server
./mvnw spring-boot:run
```

The MCP server typically runs on port `8989`.

### 2. Run the Telegram Bot

Once the MCP server is running, start the main bot application.

**PowerShell:**
```powershell
./run_bot.ps1
```
*Or manually:*
```bash
./mvnw spring-boot:run
```

The bot application runs on port `8087` by default.

## Architecture

- **Spring AI**: For LLM integration.
- **MCP Client**: Consumes tools from the MCP server.
- **Telegram Bots**: Interface for user interaction.
