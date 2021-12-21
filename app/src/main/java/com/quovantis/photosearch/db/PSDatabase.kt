package com.quovantis.photosearch.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.quovantis.photosearch.db.dao.RecentSearchDao
import com.quovantis.photosearch.db.entities.RecentSearchData

@Database(version = 1, exportSchema = false, entities = [RecentSearchData::class])
abstract class PSDatabase : RoomDatabase() {

    abstract fun getRecentSearchDao(): RecentSearchDao

    companion object {

        @Volatile
        private var instance: PSDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: createDatabase(context).also { instance = it }
        }

        private fun createDatabase(context: Context): PSDatabase {
            return Room.databaseBuilder(context, PSDatabase::class.java, "PhotoSearch.db").build()
        }

    }
}