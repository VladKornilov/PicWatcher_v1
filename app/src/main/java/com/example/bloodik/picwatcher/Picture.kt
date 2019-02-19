package com.example.bloodik.picwatcher

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.AsyncTask
import android.support.v7.widget.RecyclerView
import android.widget.ImageView
import com.beust.klaxon.Klaxon
import java.io.*
import java.net.URL

class Picture : Serializable {
    var description: String?
    var urls: Urls? = null
    var author: User? = null

    class Urls : Serializable {
        var raw: String? = null
        var full: String? = null
        var regular: String? = null
        var small: String? = null
        var thumb: String? = null
    }

    class User : Serializable {
        var name: String? = null
    }

    init {
        description = "none"
    }

    fun setData(json: String) {
        val tmp = Klaxon().parse<Picture>(json)
        this.author = tmp?.author
        this.description = tmp?.description
        this.urls = tmp?.urls
    }

    fun downloadRaw() {

    }

    fun downloadFull(image: ImageView) {
        val bm = BitmapFactory.decodeFile("C:/Users/Bloodik/Desktop/PicWatcher/app/src/main/res/mipmap-xxxhdpi/ic_launcher.png")
        image.setImageBitmap(bm)
    }




    companion object {
        fun nextPic(index: Int, ad: PicAdapter): Picture {
            val pic = Unsplash.getRandomPicture(index, ad)
            return pic
        }

        private class Unsplash {
            companion object {
                val access = "89014e7699c564cd4cace57b2819a555b8bedef36c71c356a4825fb83f3a9806"

                fun getRandomPicture(index: Int, ad: PicAdapter): Picture {
                    val url = "https://api.unsplash.com/photos/random?client_id=$access"
                    var info = GetPicInfo()
                    info.index = index
                    info.adapter = ad
                    info.execute(url)
//                    return info.pic ?: Picture().apply {
//                        description = "error"
//                        author = User()
//                        author!!.name = "none"
//                    }
                    return Picture()
                }
            }
        }


    }
}

class GetPicInfo : AsyncTask <String, Unit, String>() {
    var json: String? = null
    lateinit var adapter: PicAdapter
    var index = 0

    override fun doInBackground(vararg p0: String?): String {
        val url = URL(p0[0])
        val data = url.openStream()
        val s = java.util.Scanner(data).useDelimiter("\\A")
        val res = s.next()
        data.close()
        return res
    }
    override fun onPostExecute(result: String) {
        super.onPostExecute(result)
        json = result

        adapter.notifyItemChanged(index)
    }
}