package com.ahmetsirim.data.dto.db.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.ahmetsirim.domain.model.ChatMessage
import com.android.identity.util.UUID

@Entity(
    tableName = "chat_messages",
    foreignKeys = [
        ForeignKey(
            entity = ChatSessionEntity::class,
            parentColumns = ["sessionId"],
            childColumns = ["sessionId"],
            onDelete = ForeignKey.Companion.CASCADE
        )
    ],
    indices = [Index(value = ["sessionId"])]
)
data class ChatMessageEntity(
    @PrimaryKey
    val messageId: String = UUID.Companion.randomUUID().toString(),
    val sessionId: String,
    val content: String,
    val isFromUser: Boolean,
    val timestamp: Long = System.currentTimeMillis()
)

fun ChatMessageEntity.toDomain(): ChatMessage {
    return ChatMessage(
        content = content,
        isFromUser = isFromUser
    )
}