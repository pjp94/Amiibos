package com.pancholi.amiibos

import android.app.Application
import com.pancholi.amiibos.backend.AmiiboDownloader
import com.pancholi.amiibos.database.Amiibo
import com.pancholi.amiibos.database.AmiiboRepository
import com.pancholi.amiibos.time.LastFetched
import com.pancholi.amiibos.time.TimeChecker

class DataRetriever(private val application: Application) {

  fun getFromServerIfNecessary() {
    if (shouldFetchFromServer()) {
      fetchFromServer()
    }
  }

  private fun shouldFetchFromServer(): Boolean {
    val timeChecker = TimeChecker()
    val lastFetched = timeChecker.getLastFetchedTime(application.applicationContext)
    val lastUpdated = timeChecker.getLastUpdatedTime()
    val shouldFetch = timeChecker.shouldFetchFromServer(lastFetched, lastUpdated)
    Logger.log("Should fetch from server: $shouldFetch")

    return shouldFetch
  }

  private fun fetchFromServer() {
    Logger.log("Fetching Amiibos from server.")
    val amiibos = AmiiboDownloader().getAmiibosFromServer()
    postServerFetch(amiibos)
  }

  private fun postServerFetch(data: List<Amiibo>) {
    if (data.isNotEmpty()) {
      saveToDatabase(data)
      saveLastFetchedTimeInIso()
    }
  }

  private fun saveToDatabase(data: List<Amiibo>) {
    Logger.log("Inserting ${data.size} Amiibos into database.")
    val amiiboRepository = AmiiboRepository(application)
    amiiboRepository.insertAll(data)
  }

  private fun saveLastFetchedTimeInIso() {
    val lastFetched = LastFetched(application.applicationContext)
    lastFetched.saveCurrentTime()
  }
}