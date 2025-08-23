package com.ahmetsirim.data.dto.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.ahmetsirim.domain.model.VoiceGenderEnum
import com.ahmetsirim.domain.model.ai.GenerativeAiModelEnum
import com.ahmetsirim.domain.model.db.AppSettings

@Entity(tableName = "app_settings")
data class AppSettingsEntity(
    @PrimaryKey
    val id: String = "app_settings", // Single row table
    val generativeAiModel: String = GenerativeAiModelEnum.GEMINI.name,
    val voiceGender: String = VoiceGenderEnum.FEMALE.name,
    val updatedAt: Long = System.currentTimeMillis()
)

fun AppSettingsEntity.toDomain(): AppSettings {
    return AppSettings(
        generativeAiModel = GenerativeAiModelEnum.valueOf(generativeAiModel),
        voiceGender = VoiceGenderEnum.valueOf(voiceGender),
        updatedAt = updatedAt
    )
}

fun AppSettings.toEntity(): AppSettingsEntity {
    return AppSettingsEntity(
        generativeAiModel = generativeAiModel.name,
        voiceGender = voiceGender.name,
        updatedAt = updatedAt
    )
}
