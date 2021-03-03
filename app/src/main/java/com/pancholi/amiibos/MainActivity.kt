package com.pancholi.amiibos

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.widget.ContentLoadingProgressBar
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.pancholi.amiibos.custom.CustomAmiiboDialog
import com.pancholi.amiibos.custom.CustomAmiiboListener
import com.pancholi.amiibos.database.Amiibo
import com.pancholi.amiibos.detail.*

private const val IS_DIALOG_SHOWING = "com.pancholi.amiibos.IS_DIALOG_SHOWING"

class MainActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener, CustomAmiiboListener {

  private val activityLauncher = registerActivityForResult()
  private val permissionLauncher = registerForPermissionResult()
  private val contentLauncher = registerForContentResult()

  private lateinit var storeViewModel: StoreViewModel
  private lateinit var gridAdapter: AmiiboGridAdapter
  private lateinit var customAmiiboDialog: CustomAmiiboDialog

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)
    setViewModel()
    setGrid()
    getAmiibosAndDisplay()
    setFilterSpinner()
    setAddCustomButton()
  }

  override fun onSaveInstanceState(outState: Bundle) {
    super.onSaveInstanceState(outState)
    checkForDialog(outState)
  }

  private fun checkForDialog(outState: Bundle) {
    if (this::customAmiiboDialog.isInitialized && customAmiiboDialog.isShowing) {
      outState.putBoolean(IS_DIALOG_SHOWING, true)
      storeViewModel.setCustomAmiibo(customAmiiboDialog.saveFields())
      customAmiiboDialog.dismiss()
    }
  }

  override fun onRestoreInstanceState(savedInstanceState: Bundle) {
    super.onRestoreInstanceState(savedInstanceState)
    restoreDialog(savedInstanceState)
  }

  private fun restoreDialog(savedInstanceState: Bundle) {
    val isDialogShowing = savedInstanceState.getBoolean(IS_DIALOG_SHOWING, false)

    if (isDialogShowing) {
      showCustomAmiiboDialog()
      customAmiiboDialog.setFields(storeViewModel.getCustomAmiibo())
    }
  }

  private fun setViewModel() {
    storeViewModel = ViewModelProvider(this).get(StoreViewModel::class.java)
  }

  private fun registerActivityForResult(): ActivityResultLauncher<Intent> {
    return registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
      if (it.resultCode == RESULT_REMOVED) {
        val name = it.data?.getStringExtra(EXTRA_AMIIBO_NAME)
        handleDetailResultRemoved(name)
      }
    }
  }

  private fun registerForPermissionResult(): ActivityResultLauncher<String> {
    return registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
      if (isGranted) {
        openImagePicker()
      }
    }
  }

  private fun registerForContentResult(): ActivityResultLauncher<String> {
    return registerForActivityResult(ActivityResultContracts.GetContent()) {
      customAmiiboDialog.show()

      if (it != null) {
        customAmiiboDialog.setSelectedImage(it)
      }
    }
  }

  private fun handleDetailResultRemoved(name: String?) {
    SnackbarDisplayer.showSnackbar(
      findViewById(R.id.storeCoordinator),
      getString(R.string.remove_amiibo_snackbar, name),
      getColor(R.color.red),
      getColor(R.color.white)
    )
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
    if (amiibos.isNotEmpty()) {
      findViewById<ContentLoadingProgressBar>(R.id.storeLoading).hide()
    }

    updateGrid(amiibos)
  }

  private fun updateGrid(amiibos: List<Amiibo>) {
    Logger.log("Loading Amiibos into grid.")
    gridAdapter.setAmiibosAndNotify(amiibos)
  }


  private fun setFilterSpinner() {
    val filter = findViewById<Spinner>(R.id.gridFilter)
    ArrayAdapter.createFromResource(
      this,
      R.array.filters,
      R.layout.spinner_item
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
    Logger.log("Filtering to all.")
    storeViewModel.setAllAmiibos()
    observeFilteredAmiibos()
  }

  private fun filterToPurchased() {
    Logger.log("Filtering to purchased.")
    storeViewModel.setPurchasedAmiibos()
    observeFilteredAmiibos()
  }

  private fun filterToCustom() {
    Logger.log("Filtering to custom.")
    storeViewModel.setCustomAmiibos()
    observeFilteredAmiibos()
  }

  private fun observeFilteredAmiibos() {
    storeViewModel.getAmiibos().observe(this, {
      setPostFetchViews(it)
    })
  }

  override fun onNothingSelected(parent: AdapterView<*>?) {
    filterToAll()
  }

  private fun setAddCustomButton() {
    findViewById<FloatingActionButton>(R.id.addAmiiboFab).setOnClickListener {
      showCustomAmiiboDialog()
    }
  }

  private fun showCustomAmiiboDialog() {
    customAmiiboDialog = CustomAmiiboDialog(this, R.style.CustomDialog, this)
    customAmiiboDialog.show()
  }

  override fun pickImageOrRequestPermission() {
    if (ContextCompat.checkSelfPermission(
        this,
        Manifest.permission.READ_EXTERNAL_STORAGE
      ) == PackageManager.PERMISSION_GRANTED
    ) {
      openImagePicker()
    } else {
      permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
    }
  }

  private fun openImagePicker() {
    contentLauncher.launch("image/*")
  }

  override fun saveAmiiboToDatabase(amiibo: Amiibo) {
    storeViewModel.addCustomAmiibo(amiibo)
    showCustomAmiiboSnackbar(amiibo.name)
  }

  private fun showCustomAmiiboSnackbar(name: String?) {
    SnackbarDisplayer.showSnackbar(
      findViewById(R.id.storeCoordinator),
      getString(R.string.added_message, name),
      getColor(R.color.lightBlue),
      getColor(R.color.white)
    )
  }
}