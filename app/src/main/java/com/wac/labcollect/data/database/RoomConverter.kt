package com.wac.labcollect.data.database

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.wac.labcollect.domain.models.Field
import com.wac.labcollect.domain.models.Template
import java.util.*

object RoomConverter {

    @TypeConverter @JvmStatic
    fun fromString(value: String): List<String> {
        val listType = object : TypeToken<List<String>>() {}.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter @JvmStatic
    fun fromList(list: List<String>): String {
        val gson = Gson()
        return gson.toJson(list)
    }

    @TypeConverter
    @JvmStatic
    fun stringToFields(data: String?): MutableList<Field> {
        val gson = Gson()
        if (data == null) {
            return Collections.emptyList()
        }
        val listType = object : TypeToken<MutableList<Field>>() {}.type
        return gson.fromJson(data, listType)
    }

    @TypeConverter
    @JvmStatic
    fun fieldsToString(myObjects: MutableList<Field>): String {
        val gson = Gson()
        return gson.toJson(myObjects)
    }

    @TypeConverter
    @JvmStatic
    fun stringToTemplate(data: String?): Template {
        val gson = Gson()
        if (data == null) {
            return Template()
        }
        val type = object : TypeToken<Template>() {}.type
        return gson.fromJson(data, type)
    }

    @TypeConverter
    @JvmStatic
    fun templateToString(myObject: Template): String {
        val gson = Gson()
        return gson.toJson(myObject)
    }

    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return if (value == null) null else Date(value)
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }
}
