package com.wac.labcollect.domain.models

enum class TYPE(val textValue: String) {
    INT("Số nguyên"),
    DOUBLE("Số thập phân"),
    TEXT("Văn bản"),
    BOOLEAN("Đúng/Sai"),
    UNKNOWN("N/A")
}

data class DataType(var type: TYPE) {

    companion object {
        fun getDataTypes() = arrayListOf(
            TYPE.INT.textValue,
            TYPE.DOUBLE.textValue,
            TYPE.TEXT.textValue,
            TYPE.BOOLEAN.textValue
        )

        fun getTypeFromTextVal(textValue: String): TYPE {
            return when (textValue) {
                TYPE.INT.textValue -> TYPE.INT
                TYPE.DOUBLE.textValue -> TYPE.DOUBLE
                TYPE.TEXT.textValue -> TYPE.TEXT
                TYPE.BOOLEAN.textValue -> TYPE.BOOLEAN
                else -> { TYPE.UNKNOWN }
            }
        }
    }
}


