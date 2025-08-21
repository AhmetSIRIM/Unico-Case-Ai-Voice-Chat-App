package com.ahmetsirim.domain.repository

import com.ahmetsirim.domain.model.ChatMessage
import com.ahmetsirim.domain.model.common.Response
import kotlinx.coroutines.flow.Flow

interface GenerativeAiModelRepository {

    suspend fun getMessage(message: String): Flow<Response<String>>
    suspend fun getMessageWithContext(message: String, chatHistory: List<ChatMessage>): Flow<Response<String>>

}