package com.pancholi.amiibos.backend

import androidx.lifecycle.LiveData
import com.pancholi.amiibos.database.Amiibo

class AmiiboDownloader {

  lateinit var amiibo: LiveData<List<Amiibo>>

  fun getAmiibosFromServer(): List<Amiibo> {
    val response = loadAmiibos()

    return if (response != null) deserializeIntoList(response) else ArrayList()
  }

  private fun loadAmiibos(): String? {
    val getAllAmiibosUrl = AmiiboUrls().getUrlForAll()
    val connection = ServerConnection(getAllAmiibosUrl)

    return connection.getResponse()
  }

  private fun deserializeIntoList(response: String): List<Amiibo> {
    val reader = ResponseReader(response)
    return reader.createAmiiboListFromJson()
  }
}