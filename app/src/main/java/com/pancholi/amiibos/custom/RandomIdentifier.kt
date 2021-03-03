package com.pancholi.amiibos.custom

import java.util.concurrent.ThreadLocalRandom

class RandomIdentifier {

  fun getRandomNumberString(): String {
    val number = ThreadLocalRandom.current().nextInt(100000000)
    return String.format("%08d", number)
  }
}