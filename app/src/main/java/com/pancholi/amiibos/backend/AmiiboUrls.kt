package com.pancholi.amiibos.backend

import java.net.URL

private const val AMIIBO_URL = "https://amiiboapi.com"
private const val ENDPOINT_GET_ALL = "/api/amiibo/"
private const val ENDPOINT_GET_LAST_UPDATED = "/api/lastupdated/"

class AmiiboUrls {

  fun getUrlForAll() = URL("$AMIIBO_URL$ENDPOINT_GET_ALL")

  fun getUrlForLastUpdate() = URL("$AMIIBO_URL$ENDPOINT_GET_LAST_UPDATED")
}