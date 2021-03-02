package com.pancholi.amiibos.time

import java.text.SimpleDateFormat
import java.util.*

class IsoTimestamp {

  fun getCurrentTime(): String {
    val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mmmm", Locale.US)
    dateFormat.timeZone = TimeZone.getTimeZone("UTC")

    return dateFormat.format(Date())
  }
}