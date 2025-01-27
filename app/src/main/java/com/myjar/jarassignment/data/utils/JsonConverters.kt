package com.myjar.jarassignment.data.utils

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.myjar.jarassignment.data.model.ComputerItem

object JsonConverters {
    fun convertComputerItemToJson(computerItems: List<ComputerItem>): String {
        val gson = Gson()
        return gson.toJson(computerItems)
    }

    fun convertComputerItemFromJson(json: String): List<ComputerItem>? {
        return try {
            val gson = Gson()
            val token = object : TypeToken<List<ComputerItem>>() {}.type
            gson.fromJson(json, token)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }

    }
}