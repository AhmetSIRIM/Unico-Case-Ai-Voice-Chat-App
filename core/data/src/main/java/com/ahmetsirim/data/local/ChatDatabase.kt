package com.ahmetsirim.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.ahmetsirim.data.dto.db.entity.AppSettingsEntity
import com.ahmetsirim.data.dto.db.entity.ChatMessageEntity
import com.ahmetsirim.data.dto.db.entity.ChatSessionEntity

@Database(
    entities = [
        ChatSessionEntity::class,
        ChatMessageEntity::class,
        AppSettingsEntity::class
    ],
    version = 1,
    exportSchema = true
)
@TypeConverters(Converters::class)
abstract class ChatDatabase : RoomDatabase() {
    abstract fun chatDao(): ChatDao
    abstract fun appSettingsDao(): AppSettingsDao

    companion object {
        const val DATABASE_NAME = "chat_database"
    }
}
