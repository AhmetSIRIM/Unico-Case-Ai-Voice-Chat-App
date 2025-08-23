package com.ahmetsirim.domain.model.db

import com.ahmetsirim.domain.model.ChatMessage

data class ChatSession(
    val sessionId: String,
    val title: String,
    val createdAt: Long,
    val updatedAt: Long,
    val messages: List<ChatMessage>,
)