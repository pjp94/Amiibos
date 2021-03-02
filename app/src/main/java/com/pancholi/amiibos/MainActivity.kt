package com.pancholi.amiibos

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.pancholi.amiibos.database.Amiibo
import com.pancholi.amiibos.detail.DetailActivity
import com.pancholi.amiibos.detail.EXTRA_AMIIBO_DETAILS
import com.pancholi.amiibos.detail.EXTRA_GRID_POSITION

class MainActivity : AppCompatActivity() {

  private lateinit var amiiboViewModel: AmiiboViewModel
  private lateinit var activityLauncher: ActivityResultLauncher<Intent>

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)
    setViewModel()
    registerActivityForResult()
    setGrid()
    getAmiibosAndDisplay()
  }

  private fun setViewModel() {
    amiiboViewModel = ViewModelProvider(this).get(AmiiboViewModel::class.java)
  }

  private fun registerActivityForResult() {
    activityLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
      val position = it.data?.getIntExtra(EXTRA_GRID_POSITION, -1)

      if (position != null && position > -1) {
        Logger.log("Updating position $position in the grid.")
        amiiboViewModel.updateAmiibo(position)
      }
    }
  }

  private fun setGrid() {
    val amiiboGrid = findViewById<RecyclerView>(R.id.amiiboGrid)
    amiiboViewModel.setGridProperties(this, amiiboGrid, setOnGridItemClicked())
  }

  private fun setOnGridItemClicked(): GridItemClicked {
    return object : GridItemClicked {
      override fun onGridItemClicked(position: Int) {
        val amiibo = amiiboViewModel.getAmiibos(this@MainActivity).value?.get(position)
        Logger.log("Starting detail activity for ${amiibo?.name}.")
        val intent = Intent(this@MainActivity, DetailActivity::class.java)
          .putExtra(EXTRA_GRID_POSITION, position)
          .putExtra(EXTRA_AMIIBO_DETAILS, amiibo)
        activityLauncher.launch(intent)
      }
    }
  }

  private fun getAmiibosAndDisplay() {
    amiiboViewModel.getAmiibos(this).observe(this, {
      runOnUiThread {
        setPostFetchViews(it)
      }
    })
  }

  private fun setPostFetchViews(amiibos: List<Amiibo>) {
    if (amiibos.isNotEmpty()) {
      updateGrid(amiibos)
      showAddFab()
    } else {
      showNoAmiibosLoadedMessage()
    }
  }

  private fun updateGrid(amiibos: List<Amiibo>) {
    Logger.log("Loading Amiibos into grid.")
    amiiboViewModel.updateGridAndNotify(amiibos)
  }

  private fun showAddFab() {
    findViewById<FloatingActionButton>(R.id.addAmiiboFab).visibility = View.VISIBLE
  }

  private fun showNoAmiibosLoadedMessage() {
    findViewById<LinearLayout>(R.id.no_amiibos_message).visibility = View.VISIBLE
  }
}