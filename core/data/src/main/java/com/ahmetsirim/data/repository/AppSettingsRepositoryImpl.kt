package com.ahmetsirim.data.repository

import com.ahmetsirim.data.dto.db.entity.AppSettingsEntity
import com.ahmetsirim.data.dto.db.entity.toDomain
import com.ahmetsirim.data.local.AppSettingsDao
import com.ahmetsirim.domain.model.db.AppSettings
import com.ahmetsirim.domain.model.VoiceGenderEnum
import com.ahmetsirim.domain.model.ai.GenerativeAiModelEnum
import com.ahmetsirim.domain.repository.AppSettingsRepository
import javax.inject.Inject

class AppSettingsRepositoryImpl @Inject constructor(
    private val appSettingsDao: AppSettingsDao
) : AppSettingsRepository {

    override suspend fun getAppSettings(): AppSettings {
        val entity = appSettingsDao.getAppSettings()
            ?: AppSettingsEntity().also {
                appSettingsDao.insertOrUpdateAppSettings(it)
            }
        return entity.toDomain()
    }

    override suspend fun updateGenerativeAiModel(model: GenerativeAiModelEnum) {
        // Ensure settings exist
        getAppSettings()
        appSettingsDao.updateGenerativeAiModel(model.name)
    }

    override suspend fun updateVoiceGender(voiceGender: VoiceGenderEnum) {
        // Ensure settings exist
        getAppSettings()
        appSettingsDao.updateVoiceGender(voiceGender.name)
    }

}