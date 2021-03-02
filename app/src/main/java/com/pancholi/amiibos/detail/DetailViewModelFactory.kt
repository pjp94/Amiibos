package com.pancholi.amiibos.detail

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.pancholi.amiibos.database.Amiibo

@Suppress("UNCHECKED_CAST")
class DetailViewModelFactory(private val application: Application, private val amiibo: Amiibo) :
  ViewModelProvider.Factory {

  override fun <T : ViewModel?> create(modelClass: Class<T>): T {
    return DetailViewModel(application, amiibo) as T
  }
}