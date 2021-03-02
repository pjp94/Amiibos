package com.pancholi.amiibos

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.pancholi.amiibos.database.Amiibo
import com.pancholi.amiibos.detail.*

class MainActivity : AppCompatActivity() {

  private lateinit var amiiboViewModel: AmiiboViewModel
  private lateinit var activityLauncher: ActivityResultLauncher<Intent>
  private lateinit var gridAdapter: AmiiboGridAdapter

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
      when (it.resultCode) {
        RESULT_REMOVED -> handleDetailResultRemoved(it)
      }
    }
  }

  private fun setGrid() {
    val amiiboGrid = findViewById<RecyclerView>(R.id.amiiboGrid)
    gridAdapter = AmiiboGridAdapter(this, setOnGridItemClicked())
    amiiboGrid.layoutManager =
      GridLayoutManager(this, resources.getInteger(R.integer.grid_column_count))
    amiiboGrid.setHasFixedSize(true)
    amiiboGrid.adapter = gridAdapter
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
    } else {
      showNoAmiibosLoadedMessage()
    }
  }

  private fun updateGrid(amiibos: List<Amiibo>) {
    Logger.log("Loading Amiibos into grid.")
    gridAdapter.setAmiibosAndNotify(amiibos)
  }

  private fun showNoAmiibosLoadedMessage() {
    findViewById<LinearLayout>(R.id.no_amiibos_message).visibility = View.VISIBLE
  }

  private fun handleDetailResultRemoved(activityResult: ActivityResult) {
    val name = activityResult.data?.getStringExtra(EXTRA_AMIIBO_NAME)

    Snackbar.make(
      findViewById(R.id.storeCoordinator),
      "Removed $name from your store.",
      Snackbar.LENGTH_SHORT
    )
      .setBackgroundTint(getColor(R.color.red))
      .setTextColor(getColor(R.color.white))
      .show()
  }
}