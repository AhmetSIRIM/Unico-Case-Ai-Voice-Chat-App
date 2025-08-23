package com.ahmetsirim.domain.repository

import com.ahmetsirim.domain.model.db.AppSettings
import kotlinx.coroutines.flow.Flow

interface AppSettingsRepository {
    fun getAppSettings(): Flow<Result<AppSettings?>>
    suspend fun insertOrUpdateAppSettings(model: AppSettings)

}