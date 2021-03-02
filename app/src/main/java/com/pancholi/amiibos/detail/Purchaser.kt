package com.pancholi.amiibos.detail

import android.content.Context
import com.pancholi.amiibos.Logger
import com.pancholi.amiibos.database.Amiibo
import com.pancholi.amiibos.database.AmiiboRepository

class Purchaser(private val context: Context) {

  fun purchase(amiibo: Amiibo) {
    Logger.log("Purchasing ${amiibo.name}.")
    val amiiboRepository = AmiiboRepository(context)
    amiibo.purchased = true
    amiiboRepository.update(amiibo)
  }

  fun undoPurchase(amiibo: Amiibo) {
    Logger.log("Undoing purchase of ${amiibo.name}.")
    val amiiboRepository = AmiiboRepository(context)
    amiibo.purchased = false
    amiiboRepository.update(amiibo)
  }
}