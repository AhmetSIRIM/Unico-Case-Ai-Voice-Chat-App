package com.ahmetsirim.domain.usecase.chat

import com.ahmetsirim.domain.model.ChatMessage
import com.ahmetsirim.domain.repository.LocalChatRepository
import javax.inject.Inject

class GetMessagesBySessionIdUseCase @Inject constructor(
    private val localChatRepository: LocalChatRepository
) {
    suspend operator fun invoke(sessionId: String?): List<ChatMessage> {
        sessionId ?: return emptyList()

        return localChatRepository
            .getChatById(sessionId)
            .getOrNull()
            ?.messages
            ?: emptyList()
    }
}
