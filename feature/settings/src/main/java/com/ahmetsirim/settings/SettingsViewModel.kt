package com.ahmetsirim.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ahmetsirim.domain.model.VoiceGenderEnum
import com.ahmetsirim.domain.model.ai.GenerativeAiModelEnum
import com.ahmetsirim.domain.model.common.ErrorState
import com.ahmetsirim.domain.repository.AppSettingsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class SettingsViewModel @Inject constructor(
    private val appSettingsRepository: AppSettingsRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(SettingsContract.UiState())
    val uiState: StateFlow<SettingsContract.UiState> = _uiState
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = SettingsContract.UiState()
        )

    init {
        loadSettings()
    }

    fun onEvent(event: SettingsContract.UiEvent) {
        when (event) {
            SettingsContract.UiEvent.LoadSettings -> loadSettings()
            is SettingsContract.UiEvent.UpdateGenerativeAiModel -> updateGenerativeAiModel(event.model)
            is SettingsContract.UiEvent.UpdateVoiceGender -> updateVoiceGender(event.voiceGender)
            SettingsContract.UiEvent.ErrorNotified -> _uiState.update { it.copy(errorState = null) }
        }
    }

    private fun loadSettings() {
        viewModelScope.launch {
            try {
                _uiState.update { it.copy(isLoading = true) }
                val settings = appSettingsRepository.getAppSettings()
                _uiState.update {
                    it.copy(
                        appSettings = settings,
                        isLoading = false,
                        errorState = null
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorState = ErrorState(
                            exceptionMessageResId = com.ahmetsirim.designsystem.R.string.try_again,
                            exceptionSolutionSuggestionResId = com.ahmetsirim.designsystem.R.string.empty
                        )
                    )
                }
            }
        }
    }

    private fun updateGenerativeAiModel(model: GenerativeAiModelEnum) {
        viewModelScope.launch {
            try {
                appSettingsRepository.updateGenerativeAiModel(model)
                loadSettings() // Refresh settings
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        errorState = ErrorState(
                            exceptionMessageResId = com.ahmetsirim.designsystem.R.string.try_again,
                            exceptionSolutionSuggestionResId = com.ahmetsirim.designsystem.R.string.empty
                        )
                    )
                }
            }
        }
    }

    private fun updateVoiceGender(voiceGender: VoiceGenderEnum) {
        viewModelScope.launch {
            try {
                appSettingsRepository.updateVoiceGender(voiceGender)
                loadSettings() // Refresh settings
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        errorState = ErrorState(
                            exceptionMessageResId = com.ahmetsirim.designsystem.R.string.try_again,
                            exceptionSolutionSuggestionResId = com.ahmetsirim.designsystem.R.string.empty
                        )
                    )
                }
            }
        }
    }
}