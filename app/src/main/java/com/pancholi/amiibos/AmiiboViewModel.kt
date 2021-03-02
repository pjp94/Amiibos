package com.pancholi.amiibos

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.pancholi.amiibos.database.Amiibo
import com.pancholi.amiibos.database.AmiiboRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AmiiboViewModel(application: Application) : AndroidViewModel(application) {

  private val amiiboRepository: AmiiboRepository = AmiiboRepository(application)
  private val amiibos = amiiboRepository.getAll()
  private var isLoadingData = false

  fun getAmiibos(context: Context): LiveData<List<Amiibo>> {
//    if (amiibos.value.isNullOrEmpty()) {
//      loadAmiibos(context)
//    }

    return amiibos
  }

  private fun loadAmiibos(context: Context) {
    if (!isLoadingData) {
      viewModelScope.launch(Dispatchers.IO) {
//        DataRetriever(context).decideDataSource(amiibos)
        isLoadingData = false
      }

      isLoadingData = true
    }
  }
}