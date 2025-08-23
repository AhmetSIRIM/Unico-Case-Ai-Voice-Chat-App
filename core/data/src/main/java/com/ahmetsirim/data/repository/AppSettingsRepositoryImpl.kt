package com.ahmetsirim.data.repository

import com.ahmetsirim.data.dto.db.entity.toDomain
import com.ahmetsirim.data.dto.db.entity.toEntity
import com.ahmetsirim.data.local.AppSettingsDao
import com.ahmetsirim.domain.model.db.AppSettings
import com.ahmetsirim.domain.repository.AppSettingsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class AppSettingsRepositoryImpl @Inject constructor(
    private val appSettingsDao: AppSettingsDao,
) : AppSettingsRepository {

    override fun getAppSettings(): Flow<Result<AppSettings?>> = appSettingsDao.getAppSettings().map { runCatching { it?.toDomain() } }

    override suspend fun insertOrUpdateAppSettings(model: AppSettings) = appSettingsDao.insertOrUpdateAppSettings(model.toEntity())

}