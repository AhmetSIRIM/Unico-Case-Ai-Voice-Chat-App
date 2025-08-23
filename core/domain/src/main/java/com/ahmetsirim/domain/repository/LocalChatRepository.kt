package com.ahmetsirim.domain.repository

import com.ahmetsirim.domain.model.ChatMessage
import com.ahmetsirim.domain.model.db.ChatSession

interface LocalChatRepository {
    suspend fun getAllChatHistory(): Result<List<ChatSession>>
    suspend fun getChatById(sessionId: String): Result<ChatSession?>
    suspend fun saveMessage(sessionId: String, message: ChatMessage)
    suspend fun createNewChatSession(): String
    suspend fun deleteChat(sessionId: String)
}
