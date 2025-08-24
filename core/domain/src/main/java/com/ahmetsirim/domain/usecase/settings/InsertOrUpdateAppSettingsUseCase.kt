package com.ahmetsirim.domain.usecase.settings

import com.ahmetsirim.domain.model.db.AppSettings
import com.ahmetsirim.domain.repository.AppSettingsRepository
import javax.inject.Inject

class InsertOrUpdateAppSettingsUseCase @Inject constructor(
    private val appSettingsRepository: AppSettingsRepository
) {
    suspend operator fun invoke(appSettings: AppSettings) {
        appSettingsRepository.insertOrUpdateAppSettings(appSettings)
    }
}
