package com.giovanna.amatucci.melisearch.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.giovanna.amatucci.melisearch.data.model.SearchEntity

@Dao
interface SearchDao {
    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    suspend fun insertSearch(search: SearchEntity)
    @Query("SELECT * FROM searchEntity")
    suspend fun getSearchHistory(): SearchEntity?
}