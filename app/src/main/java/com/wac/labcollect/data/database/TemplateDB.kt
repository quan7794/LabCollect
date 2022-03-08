package com.wac.labcollect.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.wac.labcollect.domain.models.Template

@Database(entities = [Template::class], version = 1, exportSchema = true)
abstract class TemplateDB: RoomDatabase() {
    abstract fun templateDao() : TemplateDao

    companion object {
        private const val databaseName = "fav_dish_database"

        @Volatile
        private var INSTANCE: TemplateDB? = null

        fun getDatabase(context: Context): TemplateDB {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext, TemplateDB::class.java, databaseName
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                instance
            }
        }
    }
}

