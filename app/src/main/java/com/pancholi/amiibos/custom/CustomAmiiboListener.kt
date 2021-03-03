package com.pancholi.amiibos.custom

import com.pancholi.amiibos.database.Amiibo

interface CustomAmiiboListener {

  fun pickImageOrRequestPermission()
  fun saveAmiiboToDatabase(amiibo: Amiibo)
}