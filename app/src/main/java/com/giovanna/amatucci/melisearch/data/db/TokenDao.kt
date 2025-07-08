package com.giovanna.amatucci.melisearch.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.giovanna.amatucci.melisearch.data.model.TokenEntity

@Dao
interface TokenDao {
    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    suspend fun saveToken(token: TokenEntity)
    @Query("SELECT * FROM token")
    suspend fun getToken(): TokenEntity?
    @Query("DELETE FROM token")
    suspend fun deleteToken()
}