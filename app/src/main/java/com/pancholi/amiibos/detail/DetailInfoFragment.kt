package com.pancholi.amiibos.detail

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import com.pancholi.amiibos.R
import com.pancholi.amiibos.database.Amiibo

const val SELECTED_AMIIBO = "com.pancholi.amiibos.SELECTED_AMIIBO"

class DetailInfoFragment : Fragment(R.layout.fragment_detail_info) {

  private lateinit var root: View
  private lateinit var purchaseButton: Button
  private var purchased: Boolean? = null
  private var amiibo: Amiibo? = null

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    getArgs()
  }

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    root = inflater.inflate(R.layout.fragment_detail_info, container, false)
    setInfo()
    setPurchaseButton()
    setRemoveButton()

    return root
  }

  private fun getArgs() {
    arguments?.let {
      amiibo = it.get(SELECTED_AMIIBO) as Amiibo
    }
  }

  private fun setInfo() {
    setTextViews()
    setReleases()
  }

  private fun setTextViews() {
    val resources = activity?.resources

    root.findViewById<TextView>(R.id.detailName).text = amiibo?.name
    root.findViewById<TextView>(R.id.detailCharacter).text =
      resources?.getString(R.string.detail_character, amiibo?.character)
    root.findViewById<TextView>(R.id.detailAmiiboSeries).text =
      resources?.getString(R.string.detail_amiibo_series, amiibo?.amiiboSeries)
    root.findViewById<TextView>(R.id.detailGameSeries).text =
      resources?.getString(R.string.detail_game_series, amiibo?.gameSeries)
    root.findViewById<TextView>(R.id.detailType).text =
      resources?.getString(R.string.detail_type, amiibo?.type)
  }

  private fun setReleases() {
    root.findViewById<TextView>(R.id.auDate).text = getReleaseDateString("au")
    root.findViewById<TextView>(R.id.euDate).text = getReleaseDateString("eu")
    root.findViewById<TextView>(R.id.jpDate).text = getReleaseDateString("jp")
    root.findViewById<TextView>(R.id.naDate).text = getReleaseDateString("na")
  }

  private fun getReleaseDateString(key: String): String? {
    val notApplicable = activity?.resources?.getString(R.string.not_applicable)
    val date = amiibo?.releases?.get(key)

    return if (date == null || date == "null") notApplicable else date
  }

  private fun setPurchaseButton() {
    purchaseButton = root.findViewById(R.id.buttonPurchase)
    purchaseButton.setOnClickListener {
      if (!amiibo?.purchased!!) {
        purchaseAmiibo()
      } else {
        showUnpurchaseConfirmationDialog()
      }
    }

    if (amiibo!!.purchased) {
      setPurchaseButtonPurchasedState()
    } else {
      setPurchasedButtonUnpurchasedState()
    }
  }

  private fun purchaseAmiibo() {
    val purchaser = context?.let { Purchaser(it) }
    purchaser?.purchase(amiibo!!)
    setPurchaseButtonPurchasedState()
    showPurchasedSnackbar()
    purchased = true
  }

  private fun setPurchaseButtonPurchasedState() {
    purchaseButton.text = activity?.resources?.getText(R.string.purchased)
    ViewCompat.setBackgroundTintList(
      purchaseButton,
      activity?.resources?.getColorStateList(android.R.color.darker_gray, null)
    )
  }

  private fun showPurchasedSnackbar() {
    val coordinator = activity?.findViewById<CoordinatorLayout>(R.id.detailCoordinator) ?: return

    activity?.resources?.let {
      Snackbar.make(
        coordinator,
        getString(R.string.purchase_amiibo, amiibo?.name),
        Snackbar.LENGTH_SHORT
      )
        .setBackgroundTint(it.getColor(R.color.lightGreen, null))
        .setTextColor(it.getColor(R.color.white, null))
        .show()
    }
  }

  private fun showUnpurchaseConfirmationDialog() {
    AlertDialog.Builder(context)
      .setTitle("Cancel Purchase")
      .setMessage("Are you sure you want to cancel your purchase?")
      .setNegativeButton("No", null)
      .setPositiveButton("Yes") { _, _ -> unpurchaseAmiibo() }
      .show()
  }

  private fun unpurchaseAmiibo() {
    val purchaser = context?.let { Purchaser(it) }
    purchaser?.undoPurchase(amiibo!!)
    setPurchasedButtonUnpurchasedState()
    showUnpurchasedSnackbar()
    purchased = false
  }

  private fun setPurchasedButtonUnpurchasedState() {
    purchaseButton.text = activity?.resources?.getText(R.string.purchase)
    ViewCompat.setBackgroundTintList(
      purchaseButton,
      activity?.resources?.getColorStateList(R.color.lightGreen, null)
    )
  }

  private fun showUnpurchasedSnackbar() {
    val coordinator = activity?.findViewById<CoordinatorLayout>(R.id.detailCoordinator) ?: return

    activity?.resources?.let {
      Snackbar.make(
        coordinator,
        getString(R.string.unpurchase_amiibo, amiibo?.name),
        Snackbar.LENGTH_SHORT
      )
        .setBackgroundTint(it.getColor(R.color.red, null))
        .setTextColor(it.getColor(R.color.white, null))
        .show()
    }
  }

  private fun setRemoveButton() {
    root.findViewById<Button>(R.id.buttonRemove)?.setOnClickListener {
      showRemoveConfirmationDialog()
    }
  }

  private fun showRemoveConfirmationDialog() {
    AlertDialog.Builder(context)
      .setTitle("Remove ${amiibo?.name}")
      .setMessage("Are you sure you want to remove ${amiibo?.name} from your store?")
      .setNegativeButton("Cancel", null)
      .setPositiveButton("Remove") { _, _ -> removeAmiibo() }
      .show()
  }

  private fun removeAmiibo() {
    val remover = context?.let { Remover(it) }
    remover?.remove(amiibo!!)
  }
}