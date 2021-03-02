package com.pancholi.amiibos.detail

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.commit
import androidx.lifecycle.ViewModelProvider
import com.pancholi.amiibos.R
import com.pancholi.amiibos.database.Amiibo

const val EXTRA_GRID_POSITION = "com.pancholi.amiibo.EXTRA_GRID_POSITION"
const val EXTRA_AMIIBO_PURCHASED = "com.pancholi.amiibo.EXTRA_AMIIBO_PURCHASED"
const val EXTRA_AMIIBO_DETAILS = "com.pancholi.amiibo.EXTRA_AMIIBO_DETAILS"

class DetailActivity : AppCompatActivity() {

  private lateinit var detailViewModel: DetailViewModel

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_detail)
    setViewModelAndFragments()
  }

  override fun onBackPressed() {
    val position = intent.getIntExtra(EXTRA_GRID_POSITION, -1)
    val result = Intent()
      .putExtra(EXTRA_GRID_POSITION, position)
    setResult(RESULT_OK, result)
    super.onBackPressed()
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
    val imageBundle = detailViewModel.getImageBundle()
    val infoBundle = detailViewModel.getInfoBundle()

    supportFragmentManager.commit {
      setReorderingAllowed(true)
      add(R.id.detailImageFragment, DetailImageFragment::class.java, imageBundle)
      add(R.id.detailInfoFragment, DetailInfoFragment::class.java, infoBundle)
    }
  }
}