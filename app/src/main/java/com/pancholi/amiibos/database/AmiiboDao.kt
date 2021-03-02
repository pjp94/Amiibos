package com.pancholi.amiibos.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface AmiiboDao {

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  fun insert(amiibo: Amiibo)

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  fun insertAll(amiibos: List<Amiibo>)

  @Update
  fun update(amiibo: Amiibo)

  @Delete
  fun delete(amiibo: Amiibo)

  @Query("SELECT * FROM amiibos")
  fun getAllAmiibos(): LiveData<List<Amiibo>>

  @Query("SELECT * FROM amiibos WHERE purchased = 1")
  fun getPurchasedAmiibos(): LiveData<List<Amiibo>>

  @Query("SELECT * FROM amiibos WHERE custom = 1")
  fun getCustomAmiibos(): LiveData<List<Amiibo>>
}