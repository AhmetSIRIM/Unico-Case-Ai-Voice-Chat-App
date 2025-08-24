package com.ahmetsirim.domain.usecase.chat

import com.ahmetsirim.domain.model.ChatMessage
import com.ahmetsirim.domain.model.common.Response
import com.ahmetsirim.domain.repository.GenerativeAiModelRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

class GenerateContentWithContextUseCase @Inject constructor(
    private val generativeAiModelRepository: GenerativeAiModelRepository
) {
    suspend operator fun invoke(message: String, chatHistory: List<ChatMessage>): Flow<Response<String>> {
        return generativeAiModelRepository.generateContentWithContext(message, chatHistory)
    }
}
