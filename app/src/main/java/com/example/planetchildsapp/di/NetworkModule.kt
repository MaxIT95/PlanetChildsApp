package com.example.planetchildsapp.di

import com.example.planetchildsapp.client.apis.ChildApi
import com.example.planetchildsapp.client.apis.CoachChildrenApi
import com.example.planetchildsapp.client.apis.CoachServiceApi
import com.example.planetchildsapp.client.apis.DictServiceApi
import com.example.planetchildsapp.client.apis.DictServicePlaceApi
import com.example.planetchildsapp.client.apis.UserApi
import com.example.planetchildsapp.configuration.RetrofitClient
import com.example.planetchildsapp.service.SecretStorage
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideRetrofitClient(tokenStorage: SecretStorage): RetrofitClient {
        return RetrofitClient(tokenStorage)
    }

    @Provides
    @Singleton
    fun provideRetrofit(retrofitClient: RetrofitClient): Retrofit {
        return retrofitClient.retrofit
    }

    @Provides
    @Singleton
    fun provideUserApi(retrofit: Retrofit): UserApi {
        return retrofit.create(UserApi::class.java)
    }

    @Provides
    @Singleton
    fun provideChildApi(retrofit: Retrofit): ChildApi {
        return retrofit.create(ChildApi::class.java)
    }

    @Provides
    @Singleton
    fun provideCoachChildrenApi(retrofit: Retrofit): CoachChildrenApi {
        return retrofit.create(CoachChildrenApi::class.java)
    }

    @Provides
    @Singleton
    fun provideCoachServiceApi(retrofit: Retrofit): CoachServiceApi {
        return retrofit.create(CoachServiceApi::class.java)
    }

    @Provides
    @Singleton
    fun provideDictServiceApi(retrofit: Retrofit): DictServiceApi {
        return retrofit.create(DictServiceApi::class.java)
    }

    @Provides
    @Singleton
    fun provideDictServicePlaceApi(retrofit: Retrofit): DictServicePlaceApi {
        return retrofit.create(DictServicePlaceApi::class.java)
    }
}
