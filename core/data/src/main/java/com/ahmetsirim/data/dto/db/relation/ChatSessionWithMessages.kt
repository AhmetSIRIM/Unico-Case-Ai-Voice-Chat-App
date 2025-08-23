package com.ahmetsirim.data.dto.db.relation

import androidx.room.Embedded
import androidx.room.Relation
import com.ahmetsirim.data.dto.db.entity.ChatMessageEntity
import com.ahmetsirim.data.dto.db.entity.ChatSessionEntity

data class ChatSessionWithMessages(
    @Embedded
    val session: ChatSessionEntity,
    @Relation(
        parentColumn = "sessionId",
        entityColumn = "sessionId"
    )
    val messages: List<ChatMessageEntity>
)