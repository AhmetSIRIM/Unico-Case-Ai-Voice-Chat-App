package com.ahmetsirim.settings

import com.ahmetsirim.domain.model.common.ErrorState
import com.ahmetsirim.domain.model.db.AppSettings

internal class SettingsContract {

    data class UiState(
        val appSettings: AppSettings? = null,
        val isLoading: Boolean = true,
        val errorState: ErrorState? = null
    )

    sealed interface UiEvent {
        data class UpdateAppSettings(val newAppSettings: AppSettings) : UiEvent
        data object ErrorNotified : UiEvent
    }
}
