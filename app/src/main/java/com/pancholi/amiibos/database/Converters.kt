package com.pancholi.amiibos.database

import androidx.room.TypeConverter

class Converters {

  @TypeConverter
  fun fromBoolean(value: Boolean): Int {
    return if (value) 1 else 0
  }

  @TypeConverter
  fun toBoolean(value: Int): Boolean {
    return value == 1
  }

  @TypeConverter
  fun fromMapToString(map: Map<String, String?>): String {
    val builder = StringBuilder()

    for ((key, value) in map) {
      builder.append("$key:$value,")
    }

    builder.trimEnd(',')
    return builder.toString()
  }

  @TypeConverter
  fun toMapFromString(text: String): Map<String, String?> {
    val pairs = text.split(",")
    val map = HashMap<String, String?>()

    for (pair in pairs) {
      val release = pair.split(":")
      val country = release[0]
      val date = if (release.size > 1) release[1] else null
      map[country] = date
    }

    return map
  }
}