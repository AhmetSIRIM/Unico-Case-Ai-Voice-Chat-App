package com.ahmetsirim.domain.usecase.settings

import com.ahmetsirim.domain.model.db.AppSettings
import com.ahmetsirim.domain.repository.AppSettingsRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

class GetAppSettingsUseCase @Inject constructor(
    private val appSettingsRepository: AppSettingsRepository
) {
    operator fun invoke(): Flow<Result<AppSettings?>> {
        return appSettingsRepository.getAppSettings()
    }
}
