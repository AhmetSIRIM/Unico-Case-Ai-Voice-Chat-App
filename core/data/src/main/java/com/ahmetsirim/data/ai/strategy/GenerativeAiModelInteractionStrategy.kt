package com.ahmetsirim.data.ai.strategy

import com.ahmetsirim.domain.model.ChatMessage
import com.google.ai.client.generativeai.GenerativeModel

/**
 * Strategy interface for interacting with different generative AI models.
 * This interface abstracts the specifics of each AI provider and provides
 * a common contract for content generation operations.
 *
 * Implementations should handle:
 * - Model initialization and configuration
 * - Content generation with or without chat history
 */
interface GenerativeAiModelInteractionStrategy {
    val generativeModel: GenerativeModel

    /**
     * Generates content based on a single message without any chat history context.
     */
    suspend fun generateContent(message: String): String

    /**
     * Generates content based on a message with the context of previous chat history.
     * This method maintains conversation context and provides more coherent responses
     * in multi-turn conversations.
     */
    suspend fun generateContentWithHistory(message: String, chatHistory: List<ChatMessage>): String
}
