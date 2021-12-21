package com.quovantis.photosearch.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = RecentSearchTableData.TABLE_NAME)
data class RecentSearchData(

    @PrimaryKey
    @ColumnInfo(name = RecentSearchTableData.COL_VALUE)
    var value: String,

    @ColumnInfo(name = RecentSearchTableData.COL_CREATED_AT)
    var createdAt: Long = System.currentTimeMillis(),
)