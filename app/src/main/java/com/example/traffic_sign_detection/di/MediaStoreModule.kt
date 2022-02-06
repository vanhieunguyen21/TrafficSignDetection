package com.example.traffic_sign_detection.di

import android.content.Context
import com.example.traffic_sign_detection.data.datasource.MediaStoreImageDatasource
import com.example.traffic_sign_detection.data.datasource.MediaStoreImageDatasourceImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object MediaStoreModule {

    @Singleton
    @Provides
    fun provideMediaStoreImageDatasource(@ApplicationContext app : Context) : MediaStoreImageDatasource{
        return MediaStoreImageDatasourceImpl(app)
    }
}