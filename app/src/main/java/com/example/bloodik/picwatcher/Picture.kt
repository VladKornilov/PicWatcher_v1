package com.example.bloodik.picwatcher

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.widget.ImageView
import com.google.gson.JsonParser
import java.io.*
import android.R.attr.bitmap
import android.os.AsyncTask
import android.util.Log
import java.net.URL


class Picture(ctx: Context) : Serializable {
    var description: String
    var author: String
    var links = Links()
    var small: ByteArray
    var full: ByteArray? = null

    class Links : Serializable {
        var small: String? = null
        var full: String? = null
    }

    //var user: User? = null

//    class User : Serializable {
//        var name: String? = null
//    }


    init {
        description = "none"
        author = "none"

        val image = ImageView(ctx)
        image.setImageResource(R.drawable.loading)
        val bm: Bitmap = (image.drawable as BitmapDrawable).bitmap
        val stream = ByteArrayOutputStream()
        bm.compress(Bitmap.CompressFormat.PNG, 100, stream)
        small = stream.toByteArray()
    }

    fun setData(json: String) {
        val jsObj = JsonParser().parse(json)
        this.description = jsObj.asJsonObject.get("description").asString
        this.author = jsObj.asJsonObject.getAsJsonObject("user").get("name").asString
        this.links.small = jsObj.asJsonObject.getAsJsonObject("urls").get("thumb").asString
        this.links.full = jsObj.asJsonObject.getAsJsonObject("urls").get("full").asString
    }
}

