package com.pancholi.amiibos

import com.pancholi.amiibos.time.IsoTimestamp
import org.junit.Assert
import org.junit.Test
import java.util.*

class TimeTests {

  @Test
  fun testTimeSavedInIso() {
    val calendar = Calendar.getInstance()
    calendar.run {
      set(Calendar.MONTH, 2)
      set(Calendar.DAY_OF_MONTH, 3)
      set(Calendar.YEAR, 2021)
      set(Calendar.HOUR_OF_DAY, 8)
      set(Calendar.MINUTE, 0)
      set(Calendar.SECOND, 0)
    }

    val isoTimestamp = IsoTimestamp()
    val timeInIso = isoTimestamp.getCustomTime(calendar.timeInMillis)

    Assert.assertEquals("2021-03-03T13:00:00", timeInIso)
  }

  @Test
  fun testFetchedTimeNewerThanUpdatedTime() {
    val lastUpdated = "2021-03-03T12:20:10:123435"
    val lastFetched = "2021-03-03T12:20:10"

    Assert.assertTrue(lastFetched > lastUpdated)
  }

  @Test
  fun testFetchedTimeOlderThanUpdatedTime() {
    val lastFetched = "2021-03-03T13:59:00"
    val lastUpdated = "2021-03-03T14:00:00:123253"

    Assert.assertTrue(lastFetched < lastUpdated)
  }

  @Test
  fun testSameTimes_UpdatedOlder() {
    val lastFetched = "2021-03-03T14:00:00"
    val lastUpdated = "2021-03-03T14:00:00:123253"

    Assert.assertTrue(lastUpdated > lastFetched)
  }
}