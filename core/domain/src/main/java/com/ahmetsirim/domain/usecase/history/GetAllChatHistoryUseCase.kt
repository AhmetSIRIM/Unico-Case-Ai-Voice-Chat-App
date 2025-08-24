package com.ahmetsirim.domain.usecase.history

import com.ahmetsirim.domain.model.db.ChatSession
import com.ahmetsirim.domain.repository.LocalChatRepository
import javax.inject.Inject

class GetAllChatHistoryUseCase @Inject constructor(
    private val localChatRepository: LocalChatRepository
) {
    suspend operator fun invoke(): Result<List<ChatSession>> {
        return localChatRepository.getAllChatHistory()
    }
}
