package com.ahmetsirim.data.repository

import com.ahmetsirim.common.di.coroutine.dispatcher.DispatcherQualifier
import com.ahmetsirim.common.di.coroutine.dispatcher.DispatcherTypeEnum
import com.ahmetsirim.data.ai.GenerativeAiModelInteractionStrategyFactory
import com.ahmetsirim.data.utility.handleNetworkFlowRequest
import com.ahmetsirim.domain.model.ChatMessage
import com.ahmetsirim.domain.model.ai.GenerativeAiModelEnum
import com.ahmetsirim.domain.model.common.Response
import com.ahmetsirim.domain.repository.GenerativeAiModelRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GenerativeAiModelRepositoryImpl @Inject constructor(
    @param:DispatcherQualifier(DispatcherTypeEnum.IO)
    private val ioDispatcher: CoroutineDispatcher,
    generativeAiModelInteractionStrategyFactory: GenerativeAiModelInteractionStrategyFactory,
) : GenerativeAiModelRepository {

    /**
     * TODO: Replace hardcoded Gemini strategy with dynamic model switching once PreferencesDataStore integration and user settings UI are implemented
     */
    private val geminiInteractionStrategy = generativeAiModelInteractionStrategyFactory
        .createGenerativeAiModelInteractionStrategy(
            generativeAiModelEnum = GenerativeAiModelEnum.GEMINI
        )

    override suspend fun generateContent(message: String): Flow<Response<String>> {
        return handleNetworkFlowRequest(ioDispatcher) {
            Response.Success(
                result = geminiInteractionStrategy.generateContent(message = message)
            )
        }
    }

    override suspend fun generateContentWithContext(
        message: String,
        chatHistory: List<ChatMessage>,
    ): Flow<Response<String>> {
        return handleNetworkFlowRequest(ioDispatcher) {
            val response = geminiInteractionStrategy
                .generateContentWithHistory(
                    message = message,
                    chatHistory = chatHistory
                )

            Response.Success(result = response)
        }
    }

}