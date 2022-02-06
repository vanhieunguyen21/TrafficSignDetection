package com.example.traffic_sign_detection.di

import com.example.traffic_sign_detection.data.datasource.SignMetadataDatabase
import com.example.traffic_sign_detection.data.repository.SignMetadataRepository
import com.example.traffic_sign_detection.data.repository.SignMetadataRepositoryImpl
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
    fun provideMetaDataRepository(signMetadataDatabase: SignMetadataDatabase): SignMetadataRepository {
        return SignMetadataRepositoryImpl(signMetadataDatabase)
    }
}