package com.example.traffic_sign_detection.di

import android.content.Context
import androidx.room.Room
import com.example.traffic_sign_detection.data.database.MetaDataDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RoomModule {

    @Singleton
    @Provides
    fun provideSignMetaDataDatabase(@ApplicationContext app : Context) : MetaDataDatabase{
        return Room.databaseBuilder(
            app, MetaDataDatabase::class.java, "SignMetaData"
        ).createFromAsset("SignMetaData.db").build()
    }
}