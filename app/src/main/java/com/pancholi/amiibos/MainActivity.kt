package com.pancholi.amiibos

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.LinearLayout
import android.widget.Spinner
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.ContentLoadingProgressBar
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.pancholi.amiibos.database.Amiibo
import com.pancholi.amiibos.detail.*

class MainActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {

  private lateinit var storeViewModel: StoreViewModel
  private lateinit var activityLauncher: ActivityResultLauncher<Intent>
  private lateinit var gridAdapter: AmiiboGridAdapter

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)
    setViewModel()
    registerActivityForResult()
    setGrid()
    getAmiibosAndDisplay()
    setFilterSpinner()
  }

  private fun setViewModel() {
    storeViewModel = ViewModelProvider(this).get(StoreViewModel::class.java)
  }

  private fun registerActivityForResult() {
    activityLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
      when (it.resultCode) {
        RESULT_REMOVED -> handleDetailResultRemoved(it)
      }
    }
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
        val amiibo = storeViewModel.getAmiibos().value?.get(position)
        Logger.log("Starting detail activity for ${amiibo?.name}.")
        val intent = Intent(this@MainActivity, DetailActivity::class.java)
          .putExtra(EXTRA_GRID_POSITION, position)
          .putExtra(EXTRA_AMIIBO_DETAILS, amiibo)
        activityLauncher.launch(intent)
      }
    }
  }

  private fun getAmiibosAndDisplay() {
    storeViewModel.getNewestAmiibos().observe(this, {
      runOnUiThread {
        setPostFetchViews(it)
      }
    })
  }

  private fun setPostFetchViews(amiibos: List<Amiibo>) {
    findViewById<ContentLoadingProgressBar>(R.id.storeLoading).hide()
    updateGrid(amiibos)
    setNoAmiibosMessageVisibility(amiibos.isEmpty())
  }

  private fun updateGrid(amiibos: List<Amiibo>) {
    Logger.log("Loading Amiibos into grid.")
    gridAdapter.setAmiibosAndNotify(amiibos)
  }

  private fun setNoAmiibosMessageVisibility(isEmpty: Boolean) {
    findViewById<LinearLayout>(R.id.no_amiibos_message).visibility =
      if (isEmpty) View.VISIBLE else View.INVISIBLE
  }

  private fun setFilterSpinner() {
    val filter = findViewById<Spinner>(R.id.gridFilter)
    ArrayAdapter.createFromResource(
      this,
      R.array.filters,
      android.R.layout.simple_spinner_item
    )
      .also {
        it.setDropDownViewResource(android.R.layout.simple_selectable_list_item)
        filter.adapter = it
      }
    filter.onItemSelectedListener = this
  }

  override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
    when (position) {
      0 -> filterToAll()
      1 -> filterToPurchased()
      2 -> filterToCustom()
    }
  }

  private fun filterToAll() {
    storeViewModel.setAllAmiibos()
    observeFilteredAmiibos()
  }

  private fun filterToPurchased() {
    storeViewModel.setPurchasedAmiibos()
    observeFilteredAmiibos()
  }

  private fun filterToCustom() {
    storeViewModel.setCustomAmiibos()
    observeFilteredAmiibos()
  }

  private fun observeFilteredAmiibos() {
    storeViewModel.getAmiibos().observe(this, {
      setPostFetchViews(it)
    })
  }

  override fun onNothingSelected(parent: AdapterView<*>?) {

  }
}