package com.pancholi.amiibos

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.pancholi.amiibos.custom.CustomAmiibo
import com.pancholi.amiibos.database.Amiibo
import com.pancholi.amiibos.database.AmiiboRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class StoreViewModel(amiiboApplication: Application) :
  AndroidViewModel(amiiboApplication) {

  private val amiiboRepository = AmiiboRepository(amiiboApplication)
  private val dataRetriever = DataRetriever(amiiboApplication)
  private var amiibos = amiiboRepository.getAll()
  private var isLoadingData = false
  private var customAmiibo: CustomAmiibo? = null

  fun getAmiibos(): LiveData<List<Amiibo>> {
    return amiibos
  }

  fun getNewestAmiibos(): LiveData<List<Amiibo>> {
    if (!isLoadingData) {
      viewModelScope.launch(Dispatchers.IO) {
        dataRetriever.getFromServerIfNecessary()
        isLoadingData = false
      }
    }

    isLoadingData = true

    return amiibos
  }

  fun addCustomAmiibo(amiibo: Amiibo) {
    amiiboRepository.insert(amiibo)
  }

  fun setAllAmiibos() {
    amiibos = amiiboRepository.getAll()
  }

  fun setPurchasedAmiibos() {
    amiibos = amiiboRepository.getPurchased()
  }

  fun setCustomAmiibos() {
    amiibos = amiiboRepository.getCustom()
  }

  fun setCustomAmiibo(customAmiibo: CustomAmiibo?) {
    this.customAmiibo = customAmiibo
  }

  fun getCustomAmiibo(): CustomAmiibo? {
    return customAmiibo
  }
}