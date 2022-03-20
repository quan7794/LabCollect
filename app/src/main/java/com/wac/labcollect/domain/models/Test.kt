package com.wac.labcollect.domain.models


import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize @Entity(tableName = "test_table")
data class Test(@PrimaryKey(autoGenerate = true) @SerializedName("id") val id: Int = 0,
    @SerializedName("editor") var editor: List<String> = listOf(),
    @SerializedName("template") var template: Template = Template(),
    @SerializedName("records") var records: List<String> = listOf(),
    @SerializedName("isActive") var isActive: Boolean = true,
    @SerializedName("isPublic") var isPublic: Boolean = true,
    @SerializedName("uniqueName") val uniqueName: String = "",
    @SerializedName("spreadId") var spreadId: String = "",
    @SerializedName("docsId") var docsId: String = "",
    @SerializedName("title") var title: String = "",
    @SerializedName("owner") var owner: String = "",
    @SerializedName("startTime") var startTime: String = "",
    @SerializedName("endTime") var endTime: String = "",
    @SerializedName("type") var type: String = "",
    @SerializedName("viewer") var viewer: List<String> = listOf(),
    @SerializedName("lastEditTimestamp") var lastEditTimestamp: String = "",
    @SerializedName("createdTimestamp") var createTimestamp: String = ""
) : Parcelable

@Parcelize @Entity(tableName = "template_table")
data class Template(@PrimaryKey(autoGenerate = true) @SerializedName("id") val id: Int = 0,
    @SerializedName("uniqueName") val uniqueName: String = "",
    @SerializedName("spreadId") var spreadId: String = "",
    @SerializedName("title") var title: String = "",
    @SerializedName("isPublic") var isPublic: Boolean = true,
    @SerializedName("isActive") var isActive: Boolean = true,
    @SerializedName("owner") val owner: String = "",
    @SerializedName("fields") var fields: List<Field> = listOf(),
    @SerializedName("viewers") var viewers: List<String> = listOf(),
    @SerializedName("lastEditTimestamp") var lastEditTimestamp: String = "",
    @SerializedName("createdTimestamp") var createTimestamp: String = ""
) : Parcelable

@Parcelize
data class Field(@SerializedName("key") var key: String = "",
                 @SerializedName("type") var type: TYPE = TYPE.UNKNOWN) : Parcelable
//
//{
//    "name": "Brynna",
//    "id": "2547031607283367",
//    "type": "Christian",
//    "owner": "Brynna@gmail.com",
//    "fields": [
//    {
//        "id": "74",
//        "name": "Gaynor",
//        "type": "Double"
//    },
//    {
//        "id": "41",
//        "name": "Dudley",
//        "type": "Double"
//    },
//    {
//        "id": "41",
//        "name": "Strephon",
//        "type": "Double"
//    }
//    ],
//    "isPublic": true,
//    "editor": [
//    "Demitria@gmail.com",
//    "Vale@gmail.com",
//    "Adalbert@gmail.com",
//    "Allys@gmail.com"
//    ],
//    "viewer": [
//    "Connelly@gmail.com",
//    "Magdalen@gmail.com",
//    "Monaco@gmail.com",
//    "Even@gmail.com"
//    ],
//    "startTime": 1651561,
//    "endTime": 16515615,
//    "isActive": true
//}
//
//https://extendsclass.com/json-generator.html
//
//{
//    "name": firstname(),
//    "id": random(),
//    "type": firstname(),
//    "owner": function () {
//    return this.name + '@gmail.com';
//},
//    "fields": repeat(3, {
//    "id": random(1,100),
//    "name": lastname(),
//    "type": 'Double'
//}),
//    "isPublic": true,
//    "editor": repeat(4,lastname()+'@gmail.com'),
//    "viewer": repeat(4,lastname()+'@gmail.com'),
//    "startTime": 16515615,
//    "endTime": 16515615,
//    "isActive": true
//}