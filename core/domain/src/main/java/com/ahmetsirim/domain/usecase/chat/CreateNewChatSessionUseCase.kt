package com.ahmetsirim.domain.usecase.chat

import com.ahmetsirim.domain.repository.LocalChatRepository
import javax.inject.Inject

class CreateNewChatSessionUseCase @Inject constructor(
    private val localChatRepository: LocalChatRepository
) {
    suspend operator fun invoke(): String {
        return localChatRepository.createNewChatSession()
    }
}
