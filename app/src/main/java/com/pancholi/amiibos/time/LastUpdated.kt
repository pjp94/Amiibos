package com.pancholi.amiibos.time

import com.pancholi.amiibos.backend.AmiiboUrls
import com.pancholi.amiibos.backend.ServerConnection
import org.json.JSONObject

class LastUpdated {

  fun getTime(): String? {
    val response = getResponse()

    return if (response != null) deserializeResponse(response) else null
  }

  private fun getResponse(): String? {
    val lastUpdateUrl = AmiiboUrls().getUrlForLastUpdate()
    val connection = ServerConnection(lastUpdateUrl)

    return connection.getResponse()
  }

  private fun deserializeResponse(response: String): String {
    return JSONObject(response).getString("lastUpdated")
  }
}