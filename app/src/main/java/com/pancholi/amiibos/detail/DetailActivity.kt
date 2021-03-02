package com.pancholi.amiibos.detail

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.commit
import androidx.lifecycle.ViewModelProvider
import com.pancholi.amiibos.R
import com.pancholi.amiibos.database.Amiibo

const val EXTRA_GRID_POSITION = "com.pancholi.amiibo.EXTRA_GRID_POSITION"
const val EXTRA_AMIIBO_DETAILS = "com.pancholi.amiibo.EXTRA_AMIIBO_DETAILS"
const val EXTRA_AMIIBO_REMOVED = "com.pancholi.amiibo.EXTRA_AMIIBO_REMOVED"
const val EXTRA_AMIIBO_NAME = "com.pancholi.amiibo.EXTRA_AMIIBO_NAME"
const val RESULT_REMOVED = 100

class DetailActivity : AppCompatActivity() {

  private lateinit var detailViewModel: DetailViewModel

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_detail)
    setViewModelAndFragments()
  }

  private fun setViewModelAndFragments() {
    val amiibo = intent.extras?.getParcelable(EXTRA_AMIIBO_DETAILS) as Amiibo?

    if (amiibo != null) {
      setViewModel(amiibo)
      setFragments()
    }
  }

  private fun setViewModel(amiibo: Amiibo) {
    detailViewModel = ViewModelProvider(this, DetailViewModelFactory(amiibo))
      .get(DetailViewModel::class.java)
  }

  private fun setFragments() {
    val imageFragment = DetailImageFragment(detailViewModel.amiibo.image)
    val infoFragment = DetailInfoFragment(detailViewModel.amiibo, getAmiiboRemoval())

    supportFragmentManager.commit {
      setReorderingAllowed(true)
      add(R.id.detailImageFragment, imageFragment, null)
      add(R.id.detailInfoFragment, infoFragment, null)
    }
  }

  private fun getAmiiboRemoval(): AmiiboRemoval {
    return object : AmiiboRemoval {
      override fun onRemoved() {
        handleAmiiboRemoved()
      }
    }
  }

  private fun handleAmiiboRemoved() {
    val position = intent.getIntExtra(EXTRA_GRID_POSITION, -1)
    val result = Intent()
      .putExtra(EXTRA_AMIIBO_REMOVED, position)
      .putExtra(EXTRA_AMIIBO_NAME, detailViewModel.amiibo.name)
    setResult(RESULT_REMOVED, result)
    finish()
  }
}