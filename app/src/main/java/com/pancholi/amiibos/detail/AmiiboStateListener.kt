package com.pancholi.amiibos.detail

interface AmiiboStateListener {

  fun onPurchaseChanged(purchased: Boolean)
  fun onRemoved()
}