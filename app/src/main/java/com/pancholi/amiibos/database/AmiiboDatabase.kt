package com.pancholi.amiibos.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [Amiibo::class], version = 3)
@TypeConverters(Converters::class)
abstract class AmiiboDatabase : RoomDatabase() {

  abstract fun amiiboDao(): AmiiboDao

  companion object {

    private const val NAME = "AmiiboDatabase"

    @Volatile
    private var instance: AmiiboDatabase? = null

    fun getInstance(context: Context): AmiiboDatabase {
      return instance ?: synchronized(this) {
        instance ?: createDatabase(context).also {
          instance = it
        }
      }
    }

    private fun createDatabase(context: Context): AmiiboDatabase {
      return Room.databaseBuilder(context, AmiiboDatabase::class.java, NAME)
        .fallbackToDestructiveMigration()
        .build()
    }
  }
}