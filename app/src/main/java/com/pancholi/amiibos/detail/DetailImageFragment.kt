package com.pancholi.amiibos.detail

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.pancholi.amiibos.R

const val IMAGE_URL = "com.pancholi.amiibos.IMAGE_URL"

class DetailImageFragment(private val imageUrl: String) : Fragment(R.layout.fragment_detail_image) {

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
  ): View? {
    val root = inflater.inflate(R.layout.fragment_detail_image, container, false)
    loadImage(root)

    return root
  }

  private fun loadImage(root: View) {
    val detailImage = root.findViewById<ImageView>(R.id.detailImage)

    if (detailImage != null) {
      Glide.with(this)
        .load(imageUrl)
        .into(detailImage)
    }
  }
}