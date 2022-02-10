package com.example.traffic_sign_detection.di

import com.example.traffic_sign_detection.domain.data.datasource.MediaStoreImageDatasource
import com.example.traffic_sign_detection.domain.data.datasource.SignMetadataDatabase
import com.example.traffic_sign_detection.domain.data.repository.MediaStoreImageRepository
import com.example.traffic_sign_detection.domain.data.repository.MediaStoreImageRepositoryImpl
import com.example.traffic_sign_detection.domain.data.repository.SignMetadataRepository
import com.example.traffic_sign_detection.domain.data.repository.SignMetadataRepositoryImpl
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

    @Singleton
    @Provides
    fun provideMediaStoreImageRepository(datasource: MediaStoreImageDatasource): MediaStoreImageRepository {
        return MediaStoreImageRepositoryImpl(datasource)
    }
}