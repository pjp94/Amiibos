package com.pancholi.amiibos.database

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(
  tableName = "amiibos",
  primaryKeys = ["head", "tail"]
)
data class Amiibo(
  @ColumnInfo(name = "amiibo_series")
  val amiiboSeries: String,
  val character: String,
  @ColumnInfo(name = "game_series")
  val gameSeries: String,
  val head: String,
  val image: String,
  val name: String,
  val releases: Map<String, String?>,
  val tail: String,
  val type: String,
  var purchased: Boolean
) : Parcelable {

  class Builder {

    lateinit var amiiboSeries: String
    lateinit var character: String
    lateinit var gameSeries: String
    lateinit var head: String
    lateinit var image: String
    lateinit var name: String
    lateinit var releases: Map<String, String?>
    lateinit var tail: String
    lateinit var type: String

    private var purchased = false

    fun build(): Amiibo {
      return Amiibo(
        amiiboSeries,
        character,
        gameSeries,
        head,
        image,
        name,
        releases,
        tail,
        type,
        purchased
      )
    }
  }
}