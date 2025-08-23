package com.ahmetsirim.settings

import com.ahmetsirim.domain.model.VoiceGenderEnum
import com.ahmetsirim.domain.model.ai.GenerativeAiModelEnum
import com.ahmetsirim.domain.model.common.ErrorState
import com.ahmetsirim.domain.model.db.AppSettings

internal class SettingsContract {

    data class UiState(
        val appSettings: AppSettings? = null,
        val isLoading: Boolean = true,
        val errorState: ErrorState? = null,
    )

    sealed interface UiEvent {
        data class UpdateGenerativeAiModel(val model: GenerativeAiModelEnum) : UiEvent
        data class UpdateVoiceGender(val voiceGender: VoiceGenderEnum) : UiEvent
        data object LoadSettings : UiEvent
        data object ErrorNotified : UiEvent
    }
}