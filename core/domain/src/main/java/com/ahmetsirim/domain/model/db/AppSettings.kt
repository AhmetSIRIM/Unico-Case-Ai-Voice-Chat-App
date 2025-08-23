package com.ahmetsirim.domain.model.db

import com.ahmetsirim.domain.model.VoiceGenderEnum
import com.ahmetsirim.domain.model.ai.GenerativeAiModelEnum

data class AppSettings(
    val generativeAiModel: GenerativeAiModelEnum,
    val voiceGender: VoiceGenderEnum,
    val updatedAt: Long
)