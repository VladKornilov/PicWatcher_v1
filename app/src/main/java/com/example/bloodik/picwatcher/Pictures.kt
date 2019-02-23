package com.example.bloodik.picwatcher

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.AsyncTask
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import kotlinx.android.synthetic.main.content_picture_detailed.*
import kotlinx.android.synthetic.main.item_picture_preview.view.*
import java.io.FileNotFoundException
import java.net.URL

private val LOG_TAG = "Pictures"
var pictures: MutableList<Picture>? = null

fun createPics(ctx: Context, size: Int) {
    if (pictures == null)
        pictures = MutableList(size) {Picture(ctx)}
}


class PicViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    var rawImage = itemView.rawImage!!
    var descript = itemView.description!!
}

class PicAdapter(context: Context, val onItemClicked: (Picture) -> Unit) :
        RecyclerView.Adapter<PicViewHolder>() {
    var inflater = LayoutInflater.from(context)

    init {
        for (i in 0 until pictures!!.size) {
            if (!pictures!![i].isSet) {
                Unsplash.getRandomPicture(pictures!![i], i, this@PicAdapter)
                pictures!![i].isSet = true
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): PicViewHolder {
        return PicViewHolder(inflater.inflate(R.layout.item_picture_preview, parent, false)).apply {
            itemView.setOnClickListener { onItemClicked(pictures!![adapterPosition]) }
        }
    }

    override fun getItemCount(): Int {
        return pictures!!.size
    }

    override fun onBindViewHolder(holder: PicViewHolder, index: Int) {
        val src = pictures!![index].small
        holder.rawImage.setImageBitmap(BitmapFactory.decodeByteArray(src, 0, src.size))
        holder.descript.text = pictures!![index].description
    }
}



private class Unsplash {
    companion object {
        val access = "89014e7699c564cd4cace57b2819a555b8bedef36c71c356a4825fb83f3a9806"
        //val access = "72fb6fd978959ab637fcc4d9f89a055b8849aad66c28a9aace5c376bbee3883c"

        fun getRandomPicture(pic: Picture, index: Int, ad: PicAdapter) {
            val url = "https://api.unsplash.com/photos/random?client_id=$access"
            val info = GetPicInfo(pic, index, ad)
            info.execute(url)
        }
    }
}

class GetPicInfo(var pic: Picture, var index: Int, var adapter: PicAdapter) : AsyncTask<String, Unit, String>() {

    override fun doInBackground(vararg p0: String?): String {
        val url = URL(p0[0])
        var res = ""
        try {
            val data = url.openStream()
            Log.d("async", "connected")
            val s = java.util.Scanner(data).useDelimiter("\\A")
            res = s.next()
            data.close()
        }
        catch(e: FileNotFoundException) {
            Log.d(LOG_TAG, "unsplash limit has reached, index: $index")
        }

        return res
    }
    override fun onPostExecute(result: String) {
        Log.d("post", "downloaded")
        super.onPostExecute(result)
        pic.setData(result)
        adapter.notifyItemChanged(index)
        val small = DownloadSmall(pic, index, adapter)
        small.execute()
    }
}

class DownloadSmall(var pic: Picture, var index: Int, var adapter: PicAdapter) : AsyncTask<Unit, Unit, ByteArray>() {

    override fun doInBackground(vararg p0: Unit?): ByteArray? {
        if (pic.links.thumb != null) {
            val url = URL(pic.links.thumb)
            val data = url.openStream()
            val res = data.readBytes()
            data.close()
            return res
        }
        return null
    }

    override fun onPostExecute(result: ByteArray?) {
        super.onPostExecute(result)
        if (result != null) {
            pic.small = result
            adapter.notifyItemChanged(index)
        }
    }
}