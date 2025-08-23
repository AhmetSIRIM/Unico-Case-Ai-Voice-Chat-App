package com.ahmetsirim.domain.model.ai

/**
 * Enum representing different generative AI model providers that can be used
 * throughout the application for content generation.
 *
 * Currently supported providers:
 * - [GEMINI]: Google's Gemini AI model
 *
 * Planned future providers:
 * - [OPENAI]: OpenAI's GPT models
 * - [CLAUDE]: Anthropic's Claude models
 */
enum class GenerativeAiModelEnum {
    GEMINI,
    OPENAI,
    CLAUDE
}
