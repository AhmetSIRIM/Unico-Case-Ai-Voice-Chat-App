package com.ahmetsirim.data.repository

import com.ahmetsirim.data.dto.db.entity.ChatMessageEntity
import com.ahmetsirim.data.dto.db.entity.ChatSessionEntity
import com.ahmetsirim.data.dto.db.entity.toDomain
import com.ahmetsirim.data.local.ChatDao
import com.ahmetsirim.domain.model.ChatMessage
import com.ahmetsirim.domain.model.db.ChatSession
import com.ahmetsirim.domain.repository.LocalChatRepository
import com.android.identity.util.UUID
import javax.inject.Inject

class LocalChatRepositoryImpl @Inject constructor(
    private val chatDao: ChatDao
) : LocalChatRepository {

    override suspend fun getAllChatHistory(): Result<List<ChatSession>> {
        return runCatching { chatDao.getAllChatHistory().map { it.toDomain() } }
    }

    override suspend fun getChatById(sessionId: String): Result<ChatSession?> {
        return runCatching { chatDao.getChatById(sessionId)?.toDomain() }
    }

    override suspend fun saveMessage(sessionId: String, message: ChatMessage) {
        // Ensure session exists
        if (chatDao.getChatById(sessionId) == null) {
            chatDao.insertChatSession(
                ChatSessionEntity(
                    sessionId = sessionId
                )
            )
        }

        // Insert message
        chatDao.insertMessage(
            ChatMessageEntity(
                sessionId = sessionId,
                content = message.content,
                isFromUser = message.isFromUser
            )
        )

        // Update session timestamp
        chatDao.updateSessionTimestamp(sessionId)
    }

    override suspend fun createNewChatSession(): String {
        val sessionId = UUID.Companion.randomUUID().toString()
        chatDao.insertChatSession(
            ChatSessionEntity(
                sessionId = sessionId
            )
        )
        return sessionId
    }

    override suspend fun deleteChat(sessionId: String) = chatDao.deleteChat(sessionId)
}
