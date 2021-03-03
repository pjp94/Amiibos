package com.pancholi.amiibos.time

import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

private const val ISO_PATTERN = "yyyy-MM-dd'T'HH:mm:ss"

class IsoTimestamp {

  private fun getDateFormat(): DateFormat {
    val dateFormat = SimpleDateFormat(ISO_PATTERN, Locale.US)
    dateFormat.timeZone = TimeZone.getTimeZone("UTC")

    return dateFormat
  }

  fun getCurrentTime(): String {
    return getDateFormat().format(Date())
  }

  fun getCustomTime(time: Long): String {
    return getDateFormat().format(time)
  }
}