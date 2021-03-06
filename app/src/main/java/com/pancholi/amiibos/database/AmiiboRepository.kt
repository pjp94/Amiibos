package com.pancholi.amiibos.database

import android.app.Application
import androidx.lifecycle.LiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AmiiboRepository(application: Application) {

  private val amiiboDao: AmiiboDao = AmiiboDatabase.getInstance(application).amiiboDao()
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

  fun getCustom(): LiveData<List<Amiibo>> {
    return amiiboDao.getCustomAmiibos()
  }
}