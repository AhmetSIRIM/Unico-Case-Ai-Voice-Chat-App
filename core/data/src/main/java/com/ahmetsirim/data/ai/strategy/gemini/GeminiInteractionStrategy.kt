package com.ahmetsirim.data.ai.strategy.gemini

import com.ahmetsirim.common.prompt.PromptsUsedThroughoutTheApplication
import com.ahmetsirim.data.BuildConfig
import com.ahmetsirim.data.ai.strategy.GenerativeAiModelInteractionStrategy
import com.ahmetsirim.domain.model.ChatMessage
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.BlockThreshold
import com.google.ai.client.generativeai.type.Content
import com.google.ai.client.generativeai.type.GenerationConfig
import com.google.ai.client.generativeai.type.HarmCategory
import com.google.ai.client.generativeai.type.SafetySetting
import com.google.ai.client.generativeai.type.content
import javax.inject.Inject

/**
 * Google Gemini implementation of the [GenerativeAiModelInteractionStrategy].
 *
 * This strategy configures and uses Google's Gemini AI model with specific parameters:
 * - Temperature: 0.9 (creative responses)
 * - TopK: 40 (token sampling)
 * - TopP: 0.95 (nucleus sampling)
 * - Max tokens: 8192
 * - Safety settings: Disabled for all categories
 * - System instruction: Friendly assistant behavior
 *
 * The model is lazily initialized on first use to optimize startup performance.
 */
class GeminiInteractionStrategy @Inject constructor() : GenerativeAiModelInteractionStrategy {

    /**
     * Lazily initialized Gemini model with predefined configuration.
     * The model is configured for creative, conversational responses with
     * disabled safety filters for maximum flexibility.
     */
    override val generativeModel: GenerativeModel by lazy {
        GenerativeModel(
            modelName = BuildConfig.GEMINI_GENERATIVE_AI_MODEL_NAME,
            apiKey = BuildConfig.GEMINI_GENERATIVE_AI_API_KEY,
            generationConfig = GenerationConfig.Companion.builder()
                .apply {
                    temperature = 0.9f
                    topK = 40
                    topP = 0.95f
                    maxOutputTokens = 8192
                }
                .build(),
            safetySettings = listOf(
                SafetySetting(
                    harmCategory = HarmCategory.HARASSMENT,
                    threshold = BlockThreshold.NONE
                ),
                SafetySetting(
                    harmCategory = HarmCategory.HATE_SPEECH,
                    threshold = BlockThreshold.NONE
                ),
                SafetySetting(
                    harmCategory = HarmCategory.SEXUALLY_EXPLICIT,
                    threshold = BlockThreshold.NONE
                ),
                SafetySetting(
                    harmCategory = HarmCategory.DANGEROUS_CONTENT,
                    threshold = BlockThreshold.NONE
                )
            ),
            systemInstruction = content {
                text(
                    PromptsUsedThroughoutTheApplication.FRIENDLY_ASSISTANT_BEHAVIOR_PROMPT
                )
            }
        )
    }

    /**
     * Generates content using Gemini model without chat history context.
     * @throws IllegalStateException if Gemini fails to generate content
     */
    override suspend fun generateContent(message: String): String {
        return generativeModel.generateContent(prompt = message).text ?: error(
            "GenAi couldn't generate"
        )
    }

    /**
     * Generates content using Gemini model with chat history context.
     *
     * This method:
     * 1. Sorts it by timestamp
     * 3. Converts [ChatMessage] list to Gemini's [Content] format
     * 3. Uses the history to generate contextually aware responses
     *
     * @param message The current input message
     * @param chatHistory Previous conversation messages
     * @return Generated response text
     * @throws Exception if Gemini fails to generate content
     */
    override suspend fun generateContentWithHistory(
        message: String,
        chatHistory: List<ChatMessage>
    ): String {
        val sortedChatHistory = chatHistory.sortedBy { it.timestamp }
        val mappedChatHistory = sortedChatHistory.map {
            content(
                role = if (it.isFromUser) "user" else "model"
            ) { text(it.content) }
        }

        return generativeModel
            .startChat(history = mappedChatHistory)
            .sendMessage(message)
            .text ?: error("GenAi couldn't generate")
    }
}
