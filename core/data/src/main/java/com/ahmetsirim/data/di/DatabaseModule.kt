package com.ahmetsirim.data.di

import android.content.Context
import androidx.room.Room
import com.ahmetsirim.data.local.AppSettingsDao
import com.ahmetsirim.data.local.ChatDao
import com.ahmetsirim.data.local.ChatDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext
        context: Context
    ): ChatDatabase {
        return Room.databaseBuilder(
            context,
            ChatDatabase::class.java,
            ChatDatabase.Companion.DATABASE_NAME
        ).build()
    }

    @Provides
    fun provideChatDao(database: ChatDatabase): ChatDao = database.chatDao()

    @Provides
    fun provideAppSettingsDao(database: ChatDatabase): AppSettingsDao = database.appSettingsDao()
}
