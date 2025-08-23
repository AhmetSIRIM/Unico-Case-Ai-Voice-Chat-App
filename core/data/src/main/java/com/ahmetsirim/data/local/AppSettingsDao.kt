package com.ahmetsirim.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ahmetsirim.data.dto.db.entity.AppSettingsEntity

@Dao
interface AppSettingsDao {

    @Query("SELECT * FROM app_settings WHERE id = 'app_settings' LIMIT 1")
    suspend fun getAppSettings(): AppSettingsEntity?

    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    suspend fun insertOrUpdateAppSettings(settings: AppSettingsEntity)

    @Query("UPDATE app_settings SET generativeAiModel = :model, updatedAt = :timestamp WHERE id = 'app_settings'")
    suspend fun updateGenerativeAiModel(model: String, timestamp: Long = System.currentTimeMillis())

    @Query("UPDATE app_settings SET voiceGender = :voiceGender, updatedAt = :timestamp WHERE id = 'app_settings'")
    suspend fun updateVoiceGender(voiceGender: String, timestamp: Long = System.currentTimeMillis())

}