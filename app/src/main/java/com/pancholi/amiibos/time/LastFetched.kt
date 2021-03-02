package com.pancholi.amiibos.time

import android.content.Context
import com.pancholi.amiibos.Logger

private const val SHARED_PREFERENCES_NAME = "com.pancholi.amiibos.SHARED_PREFERENCES"
private const val LAST_FETCHED = "sharedPreferencesLastFetchedTime"

class LastFetched(private val context: Context) {

  fun getTime(): String? {
    return context.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE)
      .getString(LAST_FETCHED, null)
  }

  fun saveCurrentTime() {
    val currentTime = IsoTimestamp().getCurrentTime()
    Logger.log("Saving $currentTime as last fetched time to SharedPreferences.")

    context
      .getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE)
      .edit()
      .putString(LAST_FETCHED, currentTime)
      .apply()
  }
}