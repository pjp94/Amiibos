package com.pancholi.amiibos.time

import android.content.Context
import com.pancholi.amiibos.Logger

class TimeChecker() {

  fun getLastFetchedTime(context: Context): String? {
    return LastFetched(context).getTime()
  }

  fun getLastUpdatedTime(): String? {
    return LastUpdated().getTime()
  }

  fun shouldFetchFromServer(lastFetched: String?, lastUpdated: String?): Boolean {
    Logger.log("Last fetched time: $lastFetched; Last updated time: $lastUpdated")
    return lastFetched == null || lastUpdated == null || lastFetched < lastUpdated
  }
}