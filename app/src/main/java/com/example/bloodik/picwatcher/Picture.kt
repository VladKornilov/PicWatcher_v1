package com.example.bloodik.picwatcher

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import com.google.gson.JsonParser
import java.io.*


class Picture(ctx: Context) : Serializable {
    val LOG_TAG = "Pictura"
    var isSet = false
    var id: String
    var description: String
    var author: String
    var links = Links()
    var small: ByteArray
    var full: ByteArray? = null

    class Links : Serializable {
        var thumb: String? = null
        var small: String? = null
        var regular: String? = null
        var full: String? = null
    }

    init {
        id = ""
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
        if (json == "") return
        val jsObj = JsonParser().parse(json)
        try {
            this.id = jsObj.asJsonObject.get("id").asString
            this.description = jsObj.asJsonObject.get("description").asString
            this.author = jsObj.asJsonObject.getAsJsonObject("user").get("name").asString
            this.links.thumb = jsObj.asJsonObject.getAsJsonObject("urls").get("thumb").asString
            this.links.small = jsObj.asJsonObject.getAsJsonObject("urls").get("small").asString
            this.links.regular = jsObj.asJsonObject.getAsJsonObject("urls").get("regular").asString
            this.links.full = jsObj.asJsonObject.getAsJsonObject("urls").get("full").asString
        }
        catch(e: UnsupportedOperationException) {
            Log.d(LOG_TAG, "smth wrong with JSON")
        }
    }
}

