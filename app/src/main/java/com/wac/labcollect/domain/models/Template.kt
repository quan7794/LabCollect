package com.wac.labcollect.domain.models


import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "template_table")
data class Template(
    @SerializedName("editor")
    val editor: List<String> = listOf(),
    @SerializedName("endTime")
    val endTime: Int = 0,
    @SerializedName("fields")
    val fields: List<Field> = listOf(),
    @PrimaryKey
    @SerializedName("id")
    val id: String = "",
    @SerializedName("isActive")
    val isActive: Boolean = false,
    @SerializedName("isPublic")
    val isPublic: Boolean = false,
    @SerializedName("name")
    val name: String = "",
    @SerializedName("owner")
    val owner: String = "",
    @SerializedName("startTime")
    val startTime: Int = 0,
    @SerializedName("type")
    val type: String = "",
    @SerializedName("viewer")
    val viewer: List<String> = listOf()
) : Parcelable

@Parcelize
data class Field(
    @SerializedName("id")
    val id: String = "",
    @SerializedName("name")
    val name: String = "",
    @SerializedName("type")
    val type: String = ""
) : Parcelable
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