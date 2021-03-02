package com.pancholi.amiibos

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pancholi.amiibos.database.Amiibo
import com.pancholi.amiibos.database.AmiiboRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AmiiboViewModel : ViewModel() {

  private var amiibos = MutableLiveData<List<Amiibo>>()
  private lateinit var gridAdapter: AmiiboGridAdapter
  private lateinit var amiiboRepository: AmiiboRepository

  private var isLoadingData = false

  fun setGridProperties(
    context: Context,
    amiiboGrid: RecyclerView,
    gridItemClicked: GridItemClicked
  ) {
    gridAdapter = AmiiboGridAdapter(context, gridItemClicked)
    amiiboGrid.layoutManager =
      GridLayoutManager(context, context.resources.getInteger(R.integer.grid_column_count))
    amiiboGrid.setHasFixedSize(true)
    amiiboGrid.adapter = gridAdapter
  }

  fun updateGridAndNotify(amiibos: List<Amiibo>) {
    gridAdapter.setAmiibos(amiibos)
    gridAdapter.notifyDataSetChanged()
  }

  fun getAmiibos(context: Context): LiveData<List<Amiibo>> {
    if (amiibos.value.isNullOrEmpty()) {
      loadAmiibos(context)
    }

    return amiibos
  }

  private fun loadAmiibos(context: Context) {
    if (!isLoadingData) {
      viewModelScope.launch(Dispatchers.IO) {
        DataRetriever(context).decideDataSource(amiibos)
        isLoadingData = false
      }

      isLoadingData = true
    }
  }

  fun updateAmiibo(position: Int) {
    gridAdapter.notifyItemChanged(position)
  }
}