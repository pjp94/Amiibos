package com.pancholi.amiibos.detail

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.pancholi.amiibos.database.Amiibo
import com.pancholi.amiibos.database.AmiiboRepository

class DetailViewModel(amiiboApplication: Application, private val amiibo: Amiibo) :
  AndroidViewModel(amiiboApplication) {

  private val amiiboRepository = AmiiboRepository(amiiboApplication)

  fun getAmiibo(): Amiibo {
    return amiibo
  }

  fun setPurchasedState(purchased: Boolean) {
    amiibo.purchased = purchased
    amiiboRepository.update(amiibo)
  }

  fun removeAmiibo() {
    amiiboRepository.delete(amiibo)
  }
}