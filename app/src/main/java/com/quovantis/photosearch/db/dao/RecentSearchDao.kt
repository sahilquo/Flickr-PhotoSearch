package com.quovantis.photosearch.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.quovantis.photosearch.db.entities.RecentSearchData
import com.quovantis.photosearch.db.entities.RecentSearchTableData
import kotlinx.coroutines.flow.Flow

@Dao
interface RecentSearchDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSearch(searchItem: RecentSearchData)

    @Query("SELECT * FROM ${RecentSearchTableData.TABLE_NAME} ORDER BY ${RecentSearchTableData.COL_CREATED_AT} DESC LIMIT ${RecentSearchTableData.MAX_ROWS}")
    fun getRecentSearches(): Flow<List<RecentSearchData>>

    @Query("DELETE FROM ${RecentSearchTableData.TABLE_NAME} where ${RecentSearchTableData.COL_VALUE} NOT IN (SELECT ${RecentSearchTableData.COL_VALUE} from ${RecentSearchTableData.TABLE_NAME} ORDER BY ${RecentSearchTableData.COL_CREATED_AT} DESC LIMIT ${RecentSearchTableData.MAX_ROWS})")
    suspend fun removeOldSearches()

}