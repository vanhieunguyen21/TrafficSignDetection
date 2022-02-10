package com.example.traffic_sign_detection.di

import android.content.Context
import com.example.traffic_sign_detection.domain.application.BaseApplication
import com.example.traffic_sign_detection.domain.core.TFLiteClassifier
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Singleton
    @Provides
    fun provideApplication(@ApplicationContext app: Context): BaseApplication {
        return app as BaseApplication
    }

    @Singleton
    @Provides
    fun provideClassifier(@ApplicationContext app: Context): TFLiteClassifier {
        return TFLiteClassifier(app)
    }
}