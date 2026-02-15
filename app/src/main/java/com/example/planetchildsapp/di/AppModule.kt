package com.example.planetchildsapp.di

import android.content.Context
import com.example.planetchildsapp.service.SecretStorage
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideTokenStorage(@ApplicationContext context: Context): SecretStorage {
        return SecretStorage(context)
    }
}
