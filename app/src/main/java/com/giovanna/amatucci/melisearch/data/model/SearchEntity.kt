package com.giovanna.amatucci.melisearch.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "searchEntity")
data class SearchEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val queries: List<String>?
)