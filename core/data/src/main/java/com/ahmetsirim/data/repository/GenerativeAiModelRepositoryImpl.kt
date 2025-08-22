package com.ahmetsirim.data.repository

import com.ahmetsirim.common.di.coroutine.dispatcher.DispatcherQualifier
import com.ahmetsirim.common.di.coroutine.dispatcher.DispatcherTypeEnum
import com.ahmetsirim.common.prompt.PromptsUsedThroughoutTheApplication
import com.ahmetsirim.data.BuildConfig
import com.ahmetsirim.data.utility.handleNetworkFlowRequest
import com.ahmetsirim.domain.model.ChatMessage
import com.ahmetsirim.domain.model.common.Response
import com.ahmetsirim.domain.repository.GenerativeAiModelRepository
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.BlockThreshold
import com.google.ai.client.generativeai.type.Content
import com.google.ai.client.generativeai.type.GenerationConfig
import com.google.ai.client.generativeai.type.HarmCategory
import com.google.ai.client.generativeai.type.SafetySetting
import com.google.ai.client.generativeai.type.content
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GenerativeAiModelRepositoryImpl @Inject constructor(
    @param:DispatcherQualifier(DispatcherTypeEnum.IO)
    private val ioDispatcher: CoroutineDispatcher,
) : GenerativeAiModelRepository {

    private val generativeModel: GenerativeModel by lazy {
        GenerativeModel(
            modelName = BuildConfig.GEMINI_GENERATIVE_AI_MODEL_NAME,
            apiKey = BuildConfig.GEMINI_GENERATIVE_AI_API_KEY,
            generationConfig = GenerationConfig.builder()
                .apply {
                    temperature = 0.9f
                    topK = 40
                    topP = 0.95f
                    maxOutputTokens = 8192
                }
                .build(),
            safetySettings = listOf(
                SafetySetting(harmCategory = HarmCategory.HARASSMENT, threshold = BlockThreshold.NONE),
                SafetySetting(harmCategory = HarmCategory.HATE_SPEECH, threshold = BlockThreshold.NONE),
                SafetySetting(harmCategory = HarmCategory.SEXUALLY_EXPLICIT, threshold = BlockThreshold.NONE),
                SafetySetting(harmCategory = HarmCategory.DANGEROUS_CONTENT, threshold = BlockThreshold.NONE)
            ),
            systemInstruction = content { text(PromptsUsedThroughoutTheApplication.FRIENDLY_ASSISTANT_BEHAVIOR_PROMPT) }
        )
    }

    override suspend fun getMessage(message: String): Flow<Response<String>> {
        return handleNetworkFlowRequest(ioDispatcher) {
            Response.Success(
                result = generativeModel.generateContent(prompt = message).text ?: error("GenAi couldn't generate")
            )
        }
    }

    override suspend fun getMessageWithContext(
        message: String,
        chatHistory: List<ChatMessage>,
    ): Flow<Response<String>> {
        return handleNetworkFlowRequest(ioDispatcher) {

            val totalChatHistory = run {
                val modelChatHistory: List<Content> = chatHistory
                    .filter { !it.isFromUser }
                    .map { content("model") { text(it.content) } }

                val userChatHistory: List<Content> = chatHistory
                    .filter { it.isFromUser }
                    .map { content("user") { text(it.content) } }

                modelChatHistory + userChatHistory
            }

            val response = generativeModel
                .startChat(history = totalChatHistory)
                .sendMessage(message)
                .text ?: error("GenAi couldn't generate")

            Response.Success(result = response)
        }
    }

}