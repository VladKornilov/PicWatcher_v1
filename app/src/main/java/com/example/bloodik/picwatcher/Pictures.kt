package com.example.bloodik.picwatcher

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.item_picture_preview.view.*


fun createPics(size: Int, ad: PicAdapter): List<Picture> {

    return List(size) {index -> Picture.nextPic(index, ad)}
}

class PicViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val rawImage = itemView.rawImage
    val descript = itemView.description
}

class PicAdapter(context: Context, val pictures: List<Picture>, val onItemClicked: (Picture) -> Unit) :
        RecyclerView.Adapter<PicViewHolder>() {
    var inflater = LayoutInflater.from(context)


    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): PicViewHolder {
        return PicViewHolder(inflater.inflate(R.layout.item_picture_preview, parent, false)).apply {
            itemView.setOnClickListener { onItemClicked(pictures[adapterPosition]) }
        }
    }

    override fun getItemCount(): Int {
        return pictures.size
    }

    override fun onBindViewHolder(holder: PicViewHolder, index: Int) {
        holder.descript.text = pictures[index].description
    }
}