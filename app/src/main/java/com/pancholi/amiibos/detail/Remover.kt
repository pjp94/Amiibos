package com.pancholi.amiibos.detail

import android.content.Context
import com.pancholi.amiibos.database.Amiibo
import com.pancholi.amiibos.database.AmiiboRepository

class Remover(private val context: Context) {

  fun remove(amiibo: Amiibo) {
    val amiiboRepository = AmiiboRepository(context)
    amiiboRepository.delete(amiibo)
  }
}