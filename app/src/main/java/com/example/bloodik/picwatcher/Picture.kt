package com.example.bloodik.picwatcher

import android.content.Context
import android.widget.ImageView
import com.google.gson.JsonParser
import java.io.*

class Picture(ctx: Context) : Serializable {
    var description: String
    var author: String
    var links = Links()
    var small = ImageView(ctx)
    var full = ImageView(ctx)

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
        small.setImageResource(R.drawable.loading)
    }

    fun setData(json: String) {
        //val tmp = Gson().fromJson(json, Picture::class.java)

        val jsObj = JsonParser().parse(json)
        this.description = jsObj.asJsonObject.get("description").asString
        this.author = jsObj.asJsonObject.getAsJsonObject("user").get("name").asString
        this.links.small = jsObj.asJsonObject.getAsJsonObject("urls").get("thumb").asString
        this.links.full = jsObj.asJsonObject.getAsJsonObject("urls").get("full").asString

        downloadSmall()
    }

    fun downloadSmall() {
        //raw = ImageView(context)
        small.setImageResource(R.mipmap.ic_launcher)
    }

    fun downloadFull(context: Context) {
        full = ImageView(context)
        full.setImageResource(R.mipmap.ic_launcher)
    }

}

