package com.example.traffic_sign_detection.di

import com.example.traffic_sign_detection.data.database.MetaDataDatabase
import com.example.traffic_sign_detection.data.repository.MetaDataRepository
import com.example.traffic_sign_detection.data.repository.MetaDataRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Singleton
    @Provides
    fun provideMetaDataRepository(metaDataDatabase: MetaDataDatabase): MetaDataRepository {
        return MetaDataRepositoryImpl(metaDataDatabase)
    }
}