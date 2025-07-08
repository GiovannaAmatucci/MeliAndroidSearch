package com.giovanna.amatucci.melisearch.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "token")
data class TokenEntity(
    @PrimaryKey(autoGenerate = true) val userId: Long = 0,
    val accessToken: String,
    val refreshToken: String,
    val tokenType: String,
    val expiresIn: Long,
    val scope: String
)