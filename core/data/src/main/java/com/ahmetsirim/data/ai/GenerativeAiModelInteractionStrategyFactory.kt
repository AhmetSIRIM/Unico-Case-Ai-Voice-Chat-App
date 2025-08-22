package com.ahmetsirim.data.ai

import com.ahmetsirim.domain.model.ai.GenerativeAiModelEnum
import com.ahmetsirim.data.ai.strategy.GenerativeAiModelInteractionStrategy
import com.ahmetsirim.data.ai.strategy.gemini.GeminiInteractionStrategy
import com.ahmetsirim.data.exception.UnsupportedGenerativeAiException
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Factory class responsible for creating appropriate [GenerativeAiModelInteractionStrategy]
 * instances based on the requested AI model provider.
 *
 * This factory implements the Factory Pattern to abstract strategy creation and
 * provides a central place for managing different AI provider implementations.
 *
 * Currently supports:
 * - Gemini (Google AI)
 *
 * Future implementations:
 * - OpenAI GPT models
 * - Anthropic Claude models
 *
 * @param geminiInteractionStrategy Injected Gemini strategy implementation
 */
@Singleton
class GenerativeAiModelInteractionStrategyFactory @Inject constructor(
    private val geminiInteractionStrategy: GeminiInteractionStrategy,
//    private val openAIGPTInteractionStrategy: OpenAIGPTInteractionStrategy, TODO: It will be integrated later
//    private val anthropicClaudeStrategy: AnthropicClaudeStrategy,           TODO: It will be integrated later
) {
    fun createGenerativeAiModelInteractionStrategy(generativeAiModelEnum: GenerativeAiModelEnum): GenerativeAiModelInteractionStrategy {
        return when (generativeAiModelEnum) {
            GenerativeAiModelEnum.GEMINI -> geminiInteractionStrategy
            GenerativeAiModelEnum.OPENAI -> throw UnsupportedGenerativeAiException("OpenAI GPT")
            GenerativeAiModelEnum.CLAUDE -> throw UnsupportedGenerativeAiException("Anthropic Claude")
        }
    }
}