package com.wac.labcollect.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.wac.labcollect.domain.models.Template
import com.wac.labcollect.domain.models.Test

@Database(entities = [Test::class, Template::class], version = 4, exportSchema = true)
@TypeConverters(RoomConverter::class)
abstract class TemplateDB: RoomDatabase() {
    abstract fun templateDao() : TestDao

    companion object {
        private const val databaseName = "lab_collect_database"

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

