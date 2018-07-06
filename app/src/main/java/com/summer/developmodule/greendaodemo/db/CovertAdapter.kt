package com.summer.developmodule.greendaodemo.db

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.greenrobot.greendao.converter.PropertyConverter

class CovertAdapter<T> : PropertyConverter<List<T>, String> {
    override fun convertToDatabaseValue(entityProperty: List<T>?): String {
        return Gson().toJson(entityProperty)
    }

    override fun convertToEntityProperty(databaseValue: String?): List<T> {
        val type = object : TypeToken<List<T>>() {
        }.type
        return Gson().fromJson<List<T>>(databaseValue, type)
    }
}