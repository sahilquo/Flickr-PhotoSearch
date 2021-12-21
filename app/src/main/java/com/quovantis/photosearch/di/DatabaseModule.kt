package com.quovantis.photosearch.di

import android.content.Context
import com.quovantis.photosearch.db.PSDatabase
import com.quovantis.photosearch.db.dao.RecentSearchDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class DatabaseModule {

    @Singleton
    @Provides
    fun providesDatabase(@ApplicationContext context: Context): PSDatabase {
        return PSDatabase.invoke(context)
    }

    @Singleton
    @Provides
    fun providesRecentSearchDao(database: PSDatabase): RecentSearchDao {
        return database.getRecentSearchDao()
    }
}