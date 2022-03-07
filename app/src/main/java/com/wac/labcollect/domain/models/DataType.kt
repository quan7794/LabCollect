package com.wac.labcollect.domain.models

data class DataType(var type: TYPE) {

    enum class TYPE(val textValue: String) {
        INT_TYPE("Số nguyên"),
        DOUBLE_TYPE("Số thập phân"),
        TEXT_TYPE("Văn bản"),
        BOOLEAN_TYPE("Đúng/Sai"),
        UNKNOWN_TYPE("N/A")
    }

    companion object {
        fun getDataTypes() = arrayListOf(
            TYPE.INT_TYPE.textValue,
            TYPE.DOUBLE_TYPE.textValue,
            TYPE.TEXT_TYPE.textValue,
            TYPE.BOOLEAN_TYPE.textValue
        )

        fun getTypeFromTextVal(textValue: String): TYPE {
            return when (textValue) {
                TYPE.INT_TYPE.textValue -> TYPE.INT_TYPE
                TYPE.DOUBLE_TYPE.textValue -> TYPE.DOUBLE_TYPE
                TYPE.TEXT_TYPE.textValue -> TYPE.TEXT_TYPE
                TYPE.BOOLEAN_TYPE.textValue -> TYPE.BOOLEAN_TYPE
                else -> { TYPE.UNKNOWN_TYPE }
            }
        }
    }
}


