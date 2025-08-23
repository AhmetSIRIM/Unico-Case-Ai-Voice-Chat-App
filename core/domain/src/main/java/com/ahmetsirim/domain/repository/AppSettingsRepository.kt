package com.ahmetsirim.domain.repository

import com.ahmetsirim.domain.model.db.AppSettings
import com.ahmetsirim.domain.model.VoiceGenderEnum
import com.ahmetsirim.domain.model.ai.GenerativeAiModelEnum

interface AppSettingsRepository {
    suspend fun getAppSettings(): AppSettings
    suspend fun updateGenerativeAiModel(model: GenerativeAiModelEnum)
    suspend fun updateVoiceGender(voiceGender: VoiceGenderEnum)
}