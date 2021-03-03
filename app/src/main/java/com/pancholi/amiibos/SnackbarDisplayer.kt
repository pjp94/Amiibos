package com.pancholi.amiibos

import android.view.View
import androidx.annotation.ColorInt
import com.google.android.material.snackbar.Snackbar

class SnackbarDisplayer {

  companion object {

    fun showSnackbar(
      coordinator: View,
      content: String,
      @ColorInt backgroundColor: Int,
      @ColorInt textColor: Int
    ) {
      Snackbar.make(
        coordinator,
        content,
        Snackbar.LENGTH_SHORT
      )
        .setBackgroundTint(backgroundColor)
        .setTextColor(textColor)
        .show()
    }
  }
}