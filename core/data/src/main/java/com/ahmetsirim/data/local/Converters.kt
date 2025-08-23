package com.ahmetsirim.data.local

import androidx.room.TypeConverter
import com.ahmetsirim.domain.model.VoiceGenderEnum
import com.ahmetsirim.domain.model.ai.GenerativeAiModelEnum
import java.util.Date

class Converters {
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? = value?.let { Date(it) }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? = date?.time

    @TypeConverter
    fun fromGenerativeAiModelEnum(value: GenerativeAiModelEnum): String = value.name

    @TypeConverter
    fun toGenerativeAiModelEnum(value: String): GenerativeAiModelEnum = GenerativeAiModelEnum.valueOf(value)

    @TypeConverter
    fun fromVoiceGenderEnum(value: VoiceGenderEnum): String = value.name

    @TypeConverter
    fun toVoiceGenderEnum(value: String): VoiceGenderEnum = VoiceGenderEnum.valueOf(value)
}