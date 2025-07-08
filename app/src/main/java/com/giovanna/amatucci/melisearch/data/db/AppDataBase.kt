package com.giovanna.amatucci.melisearch.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.giovanna.amatucci.melisearch.BuildConfig
import com.giovanna.amatucci.melisearch.data.model.SearchEntity
import com.giovanna.amatucci.melisearch.data.model.TokenEntity
import net.sqlcipher.database.SupportFactory

@Database(entities = [TokenEntity::class, SearchEntity::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun TokenDao(): TokenDao
    abstract fun SearchDao(): SearchDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val passphrase = BuildConfig.DB_PASSPHRASE.toByteArray()
                val factory = SupportFactory(passphrase)
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "token_database"
                )
                    .openHelperFactory(factory)
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}