package com.example.bloodik.picwatcher

import android.content.Context
import android.os.AsyncTask
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.item_picture_preview.view.*
import java.net.URL


fun createPics(ctx: Context, size: Int): MutableList<Picture> {
    return MutableList(size) {Picture(ctx)}
}


class PicViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    var rawImage = itemView.rawImage
    var descript = itemView.description
}

class PicAdapter(context: Context, var pictures: MutableList<Picture>, val onItemClicked: (Picture) -> Unit) :
        RecyclerView.Adapter<PicViewHolder>() {
    var inflater = LayoutInflater.from(context)


    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): PicViewHolder {
        return PicViewHolder(inflater.inflate(R.layout.item_picture_preview, parent, false)).apply {
            for (i in 0 until pictures.size) {
                Unsplash.getRandomPicture(pictures[i], i, this@PicAdapter)
            }
            itemView.setOnClickListener { onItemClicked(pictures[adapterPosition]) }
        }
    }

    override fun getItemCount(): Int {
        return pictures.size
    }

    override fun onBindViewHolder(holder: PicViewHolder, index: Int) {
        holder.rawImage = pictures[index].small
        holder.descript.text = pictures[index].description
    }
}



private class Unsplash {
    companion object {
        //val access = "89014e7699c564cd4cace57b2819a555b8bedef36c71c356a4825fb83f3a9806"
          val access = "72fb6fd978959ab637fcc4d9f89a055b8849aad66c28a9aace5c376bbee3883c"

        fun getRandomPicture(pic: Picture, index: Int, ad: PicAdapter) {
            val url = "https://api.unsplash.com/photos/random?client_id=$access"
            val info = GetPicInfo(pic, index, ad)
            info.execute(url)
        }
    }
}

class GetPicInfo(var pic: Picture, var index: Int, var adapter: PicAdapter) : AsyncTask<String, Unit, String>() {
    var json: String? = null

    override fun doInBackground(vararg p0: String?): String {
        val url = URL(p0[0])
        val data = url.openStream()
        Log.d("async", "connected")
        val s = java.util.Scanner(data).useDelimiter("\\A")
        val res = s.next()
        data.close()
        return res
    }
    override fun onPostExecute(result: String) {
        Log.d("post", "downloaded")
        super.onPostExecute(result)
        json = result
        pic.setData(result)
        adapter.notifyItemChanged(index)
    }
}