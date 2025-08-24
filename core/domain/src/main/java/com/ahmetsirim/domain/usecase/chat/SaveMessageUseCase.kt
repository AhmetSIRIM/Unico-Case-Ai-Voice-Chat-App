package com.ahmetsirim.domain.usecase.chat

import com.ahmetsirim.domain.model.ChatMessage
import com.ahmetsirim.domain.repository.LocalChatRepository
import javax.inject.Inject

class SaveMessageUseCase @Inject constructor(
    private val localChatRepository: LocalChatRepository
) {
    suspend operator fun invoke(sessionId: String, message: ChatMessage) {
        localChatRepository.saveMessage(sessionId, message)
    }
}
