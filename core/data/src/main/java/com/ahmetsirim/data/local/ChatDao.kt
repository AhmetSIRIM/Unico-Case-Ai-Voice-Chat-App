package com.ahmetsirim.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.ahmetsirim.data.dto.db.entity.ChatMessageEntity
import com.ahmetsirim.data.dto.db.entity.ChatSessionEntity
import com.ahmetsirim.data.dto.db.relation.ChatSessionWithMessages
import java.text.SimpleDateFormat
import java.util.Locale

@Dao
interface ChatDao {

    @Transaction
    @Query("SELECT * FROM chat_sessions ORDER BY updatedAt DESC")
    suspend fun getAllChatHistory(): List<ChatSessionWithMessages>

    @Transaction
    @Query("SELECT * FROM chat_sessions WHERE sessionId = :sessionId")
    suspend fun getChatById(sessionId: String): ChatSessionWithMessages?

    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    suspend fun insertChatSession(session: ChatSessionEntity)

    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    suspend fun insertMessage(message: ChatMessageEntity)

    @Query("UPDATE chat_sessions SET updatedAt = :timestamp, title = :title WHERE sessionId = :sessionId")
    suspend fun updateSessionTimestampAndTitle(
        sessionId: String,
        timestamp: Long = System.currentTimeMillis(),
        title: String = SimpleDateFormat( // TODO: Time operations will be moved to the TimeHelper.kt
            "MMM dd, yyyy HH:mm",
            Locale.getDefault()
        ).format(timestamp),
    )

    @Query("DELETE FROM chat_sessions WHERE sessionId = :sessionId")
    suspend fun deleteChat(sessionId: String)

    @Query("DELETE FROM chat_messages WHERE sessionId = :sessionId")
    suspend fun deleteMessagesForSession(sessionId: String)
}
