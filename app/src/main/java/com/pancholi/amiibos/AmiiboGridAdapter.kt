package com.pancholi.amiibos

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.pancholi.amiibos.database.Amiibo

class AmiiboGridAdapter(
  private val context: Context,
  private val gridItemClicked: GridItemClicked
) :
  RecyclerView.Adapter<AmiiboGridAdapter.ViewHolder>() {

  private var amiibos = ArrayList<Amiibo>()

  fun setAmiibos(amiibos: List<Amiibo>) {
    this.amiibos = amiibos as ArrayList<Amiibo>
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
    val gridItem = LayoutInflater.from(parent.context)
      .inflate(R.layout.grid_item, parent, false)

    return ViewHolder(gridItem)
  }

  override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    val amiibo = amiibos[position]
    loadGridItemImage(holder, amiibo)
  }

  private fun loadGridItemImage(holder: ViewHolder, amiibo: Amiibo) {
    val loading = getLoadingDrawable()

    GlideApp.with(context)
      .load(amiibo.image)
      .placeholder(loading)
      .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
      .into(object : CustomTarget<Drawable>(150, 270) {
        override fun onResourceReady(resource: Drawable, transition: Transition<in Drawable>?) {
          displayViewsAfterLoad(holder, amiibo, resource)
        }

        override fun onLoadCleared(placeholder: Drawable?) {
          holder.name.setCompoundDrawablesWithIntrinsicBounds(
            null, placeholder, null, null
          )
        }
      })
  }

  private fun getLoadingDrawable(): Drawable {
    val drawable = CircularProgressDrawable(context)
    drawable.run {
      setColorSchemeColors(R.color.lightGray)
      centerRadius = 30f
      strokeWidth = 5f
      start()
    }

    return drawable
  }

  private fun displayViewsAfterLoad(holder: ViewHolder, amiibo: Amiibo, image: Drawable) {
    holder.name.setCompoundDrawablesWithIntrinsicBounds(null, image, null, null)
    setGridItemName(holder, amiibo)
    setPurchasedVisibility(holder, amiibo.purchased)
  }

  private fun setGridItemName(holder: ViewHolder, amiibo: Amiibo) {
    holder.name.text = amiibo.name
  }

  private fun setPurchasedVisibility(holder: ViewHolder, purchased: Boolean) {
    holder.gridItemContainer.background = if (purchased)
      ResourcesCompat.getDrawable(
        context.resources,
        R.drawable.purchased_background,
        null
      ) else null
  }

  override fun getItemCount(): Int {
    return amiibos.size
  }

  override fun getItemId(position: Int): Long {
    return position.toLong()
  }

  override fun getItemViewType(position: Int): Int {
    return position
  }

  inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {

    val name: TextView = itemView.findViewById(R.id.amiiboGridName)
    val gridItemContainer: View = itemView.findViewById(R.id.gridItemContainer)

    init {
      gridItemContainer.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
      Logger.log("Grid item clicked.")
      gridItemClicked.onGridItemClicked(absoluteAdapterPosition)
    }
  }
}