package com.ahmetsirim.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ahmetsirim.domain.model.common.ErrorState
import com.ahmetsirim.domain.model.db.AppSettings
import com.ahmetsirim.domain.usecase.settings.GetAppSettingsUseCase
import com.ahmetsirim.domain.usecase.settings.InsertOrUpdateAppSettingsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
internal class SettingsViewModel @Inject constructor(
    private val getAppSettingsUseCase: GetAppSettingsUseCase,
    private val insertOrUpdateAppSettingsUseCase: InsertOrUpdateAppSettingsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(SettingsContract.UiState())
    val uiState: StateFlow<SettingsContract.UiState> = _uiState
        .onStart { loadSettings() }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = SettingsContract.UiState()
        )

    fun onEvent(event: SettingsContract.UiEvent) {
        when (event) {
            SettingsContract.UiEvent.ErrorNotified -> _uiState.update { it.copy(errorState = null) }
            is SettingsContract.UiEvent.UpdateAppSettings -> viewModelScope.launch { insertOrUpdateAppSettingsUseCase(appSettings = event.newAppSettings) }
        }
    }

    private fun loadSettings() {
        viewModelScope.launch {
            getAppSettingsUseCase()
                .collect { appSettingsResult ->
                    appSettingsResult
                        .onSuccess { nullableAppSettings ->

                            nullableAppSettings ?: run {
                                insertOrUpdateAppSettingsUseCase.invoke(appSettings = AppSettings())
                                return@onSuccess
                            }

                            _uiState.update { it.copy(appSettings = nullableAppSettings) }
                        }
                        .onFailure {
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
}
