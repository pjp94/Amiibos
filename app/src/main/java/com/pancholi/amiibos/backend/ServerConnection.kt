package com.pancholi.amiibos.backend

import android.net.TrafficStats
import com.pancholi.amiibos.Logger
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class ServerConnection(private val url: URL) {

  fun getResponse(): String? {
    try {
      (url.openConnection() as HttpURLConnection).run {
        TrafficStats.setThreadStatsTag(Thread.currentThread().id.toInt())
        connectTimeout = 10000
        readTimeout = 10000

        return InputStreamReader(inputStream).buffered().use(BufferedReader::readText)
      }
    } catch (exception: IOException) {
      Logger.log("Exception making network request: ${exception.message}")
      return null
    }
  }
}