package com.ahmetsirim.domain.model.db

import com.ahmetsirim.domain.model.VoiceGenderEnum
import com.ahmetsirim.domain.model.ai.GenerativeAiModelEnum

data class AppSettings(
    val generativeAiModel: GenerativeAiModelEnum = GenerativeAiModelEnum.GEMINI,
    val voiceGender: VoiceGenderEnum = VoiceGenderEnum.FEMALE,
    val updatedAt: Long = System.currentTimeMillis()
)
