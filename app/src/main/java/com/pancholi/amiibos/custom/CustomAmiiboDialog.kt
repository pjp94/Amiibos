package com.pancholi.amiibos.custom

import android.app.Dialog
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.view.Window
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.annotation.StyleRes
import com.pancholi.amiibos.GlideApp
import com.pancholi.amiibos.Logger
import com.pancholi.amiibos.R
import com.pancholi.amiibos.database.Amiibo

class CustomAmiiboDialog(
  context: Context,
  @StyleRes themeResId: Int,
  private val customAmiiboListener: CustomAmiiboListener
) :
  Dialog(context, themeResId) {

  private lateinit var image: ImageView
  private lateinit var name: EditText
  private lateinit var character: EditText
  private lateinit var amiiboSeries: EditText
  private lateinit var gameSeries: EditText
  private lateinit var type: EditText
  private var uri: Uri? = null

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    requestWindowFeature(Window.FEATURE_NO_TITLE)
    setContentView(R.layout.custom_amiibo_dialog)
    setViews()
    setCloseButtonListener()
    setChooseImageListener()
    setAddButtonListener()
  }

  private fun setViews() {
    image = findViewById(R.id.customAmiiboImage)
    name = findViewById(R.id.customAmiiboName)
    character = findViewById(R.id.customAmiiboCharacter)
    amiiboSeries = findViewById(R.id.customAmiiboSeries)
    gameSeries = findViewById(R.id.customAmiiboGameSeries)
    type = findViewById(R.id.customAmiiboType)
  }

  private fun setCloseButtonListener() {
    findViewById<ImageView>(R.id.closeCustomDialog).setOnClickListener {
      dismiss()
    }
  }

  private fun setChooseImageListener() {
    image.setOnClickListener {
      customAmiiboListener.pickImageOrRequestPermission()
    }
  }

  private fun setAddButtonListener() {
    findViewById<Button>(R.id.buttonAddCustom).setOnClickListener {
      saveCustomAmiibo()
    }
  }

  private fun saveCustomAmiibo() {
    val randomIdentifier = RandomIdentifier()
    val head = randomIdentifier.getRandomNumberString()
    val tail = randomIdentifier.getRandomNumberString()
    val amiibo = Amiibo(
      amiiboSeries.text.toString(),
      character.text.toString(),
      gameSeries.text.toString(),
      head,
      uri.toString(),
      name.text.toString(),
      null,
      tail,
      type.text.toString(),
      purchased = false,
      custom = true
    )

    Logger.log("Saving custom Amiibo \"${amiibo.name}\" with head $head and tail $tail.")
    customAmiiboListener.saveAmiiboToDatabase(amiibo)
    dismiss()
  }

  fun saveFields(): CustomAmiibo {
    return CustomAmiibo(
      uri.toString(),
      name.text.toString(),
      character.text.toString(),
      amiiboSeries.text.toString(),
      gameSeries.text.toString(),
      type.text.toString()
    )
  }

  fun setFields(customAmiibo: CustomAmiibo?) {
    setSelectedImage(Uri.parse(customAmiibo?.image))
    name.setText(customAmiibo?.name)
    character.setText(customAmiibo?.character)
    amiiboSeries.setText(customAmiibo?.amiiboSeries)
    gameSeries.setText(customAmiibo?.gameSeries)
    type.setText(customAmiibo?.type)
  }

  fun setSelectedImage(uri: Uri) {
    this.uri = uri

    GlideApp.with(context)
      .load(uri)
      .into(image)
  }
}