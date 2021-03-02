package com.pancholi.amiibos.detail

import android.os.Bundle
import androidx.core.os.bundleOf
import androidx.lifecycle.ViewModel
import com.pancholi.amiibos.database.Amiibo

class DetailViewModel(val amiibo: Amiibo) : ViewModel() {

  fun getImageBundle(): Bundle {
    return bundleOf(IMAGE_URL to amiibo.image)
  }

  fun getInfoBundle(): Bundle {
    return bundleOf(SELECTED_AMIIBO to amiibo)
  }
}