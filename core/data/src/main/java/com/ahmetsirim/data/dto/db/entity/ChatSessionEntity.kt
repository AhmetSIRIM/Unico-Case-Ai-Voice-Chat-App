package com.ahmetsirim.data.dto.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.ahmetsirim.data.dto.db.relation.ChatSessionWithMessages
import com.ahmetsirim.domain.model.db.ChatSession

@Entity(tableName = "chat_sessions")
data class ChatSessionEntity(
    @PrimaryKey
    val sessionId: String,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis(),
    val title: String = "New Chat"
)

fun ChatSessionEntity.toDomain(): ChatSession {
    return ChatSession(
        sessionId = sessionId,
        title = title,
        createdAt = createdAt,
        updatedAt = updatedAt,
        messages = emptyList()
    )
}

fun ChatSessionWithMessages.toDomain(): ChatSession {
    return ChatSession(
        sessionId = session.sessionId,
        title = session.title,
        createdAt = session.createdAt,
        updatedAt = session.updatedAt,
        messages = messages.map { it.toDomain() }
    )
}