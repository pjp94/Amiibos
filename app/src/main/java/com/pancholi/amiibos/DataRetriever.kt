package com.pancholi.amiibos

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.pancholi.amiibos.backend.AmiiboDownloader
import com.pancholi.amiibos.database.Amiibo
import com.pancholi.amiibos.database.AmiiboRepository
import com.pancholi.amiibos.time.LastFetched
import com.pancholi.amiibos.time.TimeChecker

class DataRetriever(val context: Context) {

  private val amiiboRepository = AmiiboRepository(context)

  fun decideDataSource(amiibos: MutableLiveData<List<Amiibo>>) {
    val fetchFromServer = shouldFetchFromServer(context)
    var data = ArrayList<Amiibo>()
    var serverFetchFailed = false

    if (fetchFromServer) {
      data = loadFromServer() as ArrayList

      if (data.isNotEmpty()) {
        postServerFetch(context, data)
      } else {
        serverFetchFailed = true
      }
    }

    if (!fetchFromServer || serverFetchFailed) {
//      data = loadFromDatabase() as ArrayList
    }

    amiibos.postValue(data)
  }

  private fun shouldFetchFromServer(context: Context): Boolean {
    val timeChecker = TimeChecker()
    val lastFetched = timeChecker.getLastFetchedTime(context)
    val lastUpdated = timeChecker.getLastUpdatedTime()
    val shouldFetch = timeChecker.shouldFetchFromServer(lastFetched, lastUpdated)
    Logger.log("Should fetch from server: $shouldFetch")

    return shouldFetch
  }

  private fun loadFromServer(): List<Amiibo> {
    Logger.log("Loading Amiibos from server.")
    return AmiiboDownloader().getAmiibosFromServer()
  }

  private fun postServerFetch(context: Context, data: List<Amiibo>) {
    saveToDatabase(data)
    saveLastFetchedTimeInIso(context)
  }

  private fun saveToDatabase(data: List<Amiibo>) {
    Logger.log("Inserting ${data.size} Amiibos into database.")
    amiiboRepository.insertAll(data)
  }

  private fun saveLastFetchedTimeInIso(context: Context) {
    val lastFetched = LastFetched(context)
    lastFetched.saveCurrentTime()
  }

  private fun loadFromDatabase(): LiveData<List<Amiibo>> {
    Logger.log("Loading Amiibos from database.")
    return amiiboRepository.getAll()
  }
}