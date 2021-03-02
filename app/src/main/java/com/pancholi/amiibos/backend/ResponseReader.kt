package com.pancholi.amiibos.backend

import android.util.JsonReader
import com.pancholi.amiibos.Logger
import com.pancholi.amiibos.database.Amiibo
import org.json.JSONException
import java.io.StringReader
import java.util.*
import kotlin.collections.ArrayList

class ResponseReader(private val json: String) {

  fun createAmiiboListFromJson(): List<Amiibo> {
    JsonReader(StringReader(json)).use { reader ->
      return parseArray(reader)
    }
  }

  private fun parseArray(reader: JsonReader): List<Amiibo> {
    val amiibos = ArrayList<Amiibo>()
    reader.beginObject()
    reader.nextName()
    reader.beginArray()

    while (reader.hasNext()) {
      amiibos.add(createAmiibo(reader))
    }

    Logger.log("Returning list of ${amiibos.size} Amiibos retrieved from server.")
    return amiibos
  }

  private fun createAmiibo(reader: JsonReader): Amiibo {
    val builder = Amiibo.Builder()
    reader.beginObject()

    while (reader.hasNext()) {
      when (reader.nextName()) {
        "amiiboSeries" -> builder.amiiboSeries = reader.nextString()
        "character" -> builder.character = reader.nextString()
        "gameSeries" -> builder.gameSeries = reader.nextString()
        "head" -> builder.head = reader.nextString()
        "image" -> builder.image = reader.nextString()
        "name" -> builder.name = reader.nextString()
        "release" -> builder.releases = createReleaseMap(reader)
        "tail" -> builder.tail = reader.nextString()
        "type" -> builder.type = reader.nextString()
        else -> throw JSONException("Invalid name found in response JSON.")
      }
    }

    reader.endObject()

    return builder.build()
  }

  private fun createReleaseMap(reader: JsonReader): Map<String, String?> {
    val release = HashMap<String, String?>()
    reader.beginObject()

    while (reader.hasNext()) {
      val country = reader.nextName()
      val date = try {
        reader.nextString()
      } catch (error: IllegalStateException) {
        reader.nextNull()
        null
      }

      release[country] = date
    }

    reader.endObject()

    return release
  }
}