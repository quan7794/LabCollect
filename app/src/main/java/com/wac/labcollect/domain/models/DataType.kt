package com.wac.labcollect.domain.models

data class DataType(var type: TYPE) {

    enum class TYPE(val textValue: String) {
        INT_TYPE("Số nguyên"),
        DOUBLE_TYPE("Số thập phân"),
        TEXT_TYPE("Văn bản"),
        BOOLEAN_TYPE("Đúng/Sai")
    }

}
