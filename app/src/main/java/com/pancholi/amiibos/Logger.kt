package com.pancholi.amiibos

import android.util.Log

class Logger private constructor() {

  companion object {
    private const val TAG = "AmiibosTag"

    fun log(message: String) {
      Log.d(TAG, message)
    }
  }
}