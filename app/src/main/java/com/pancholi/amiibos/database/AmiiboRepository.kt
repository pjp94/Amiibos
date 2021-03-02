package com.pancholi.amiibos.database

import android.content.Context
import androidx.lifecycle.LiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AmiiboRepository(context: Context) {

  private val amiiboDao: AmiiboDao = AmiiboDatabase.getInstance(context).amiiboDao()
  private val amiibos = amiiboDao.getAllAmiibos()

  fun insert(amiibo: Amiibo) {
    CoroutineScope(Dispatchers.IO).launch {
      amiiboDao.insert(amiibo)
    }
  }

  fun insertAll(amiibos: List<Amiibo>) {
    CoroutineScope(Dispatchers.IO).launch {
      amiiboDao.insertAll(amiibos)
    }
  }

  fun update(amiibo: Amiibo) {
    CoroutineScope(Dispatchers.IO).launch {
      amiiboDao.update(amiibo)
    }
  }

  fun delete(amiibo: Amiibo) {
    CoroutineScope(Dispatchers.IO).launch {
      amiiboDao.delete(amiibo)
    }
  }

  fun getAll(): LiveData<List<Amiibo>> {
    return amiibos
  }

  fun getPurchased(): LiveData<List<Amiibo>> {
    return amiiboDao.getPurchasedAmiibos()
  }
}