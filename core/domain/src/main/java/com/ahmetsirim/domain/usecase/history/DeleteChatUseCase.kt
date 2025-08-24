package com.ahmetsirim.domain.usecase.history

import com.ahmetsirim.domain.repository.LocalChatRepository
import javax.inject.Inject

class DeleteChatUseCase @Inject constructor(
    private val localChatRepository: LocalChatRepository
) {
    suspend operator fun invoke(sessionId: String) {
        localChatRepository.deleteChat(sessionId)
    }
}
