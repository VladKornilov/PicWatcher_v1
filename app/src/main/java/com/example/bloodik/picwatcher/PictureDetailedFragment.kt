package com.example.bloodik.picwatcher

import android.graphics.BitmapFactory
import android.os.AsyncTask
import android.support.v4.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.content_picture_detailed.*
import kotlinx.android.synthetic.main.content_picture_detailed.view.*
import java.io.InputStream
import java.net.URL


/**
 * A placeholder fragment containing a simple view.
 */
class PictureDetailedFragment : Fragment() {
    private var picture: Picture? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            picture = it.getSerializable("Picture") as Picture
        }
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.content_picture_detailed, container, false).apply {
        author.text = picture!!.author
        description.text = picture!!.description
        val full = DownloadFull(picture!!)
        full.execute()
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: Picture) =
                PictureDetailedFragment().apply {
                    arguments = Bundle().apply {
                        putSerializable("Picture", param1)
                    }
                }
    }

    internal inner class DownloadFull(val pic: Picture) : AsyncTask<Unit, Unit, Unit>() {

        override fun doInBackground(vararg p0: Unit?) {
            if (pic.full == null) {
                if (pic.links.full == null) {
                    pic.full = pic.small
                    return
                }
                var url = URL(pic.links.full).openConnection()
                if (url.contentLength > 2 * 1024 * 1024) {
                    url = URL(pic.links.regular).openConnection()
                    if (url.contentLength > 2 * 1024 * 1024)
                        url = URL(pic.links.small).openConnection()
                }
                pic.full = (url.content as InputStream).readBytes()
                for (i in 0 until pictures!!.size) {
                    if (pictures!![i].id == pic.id) {
                        pictures!![i] = pic
                        break
                    }
                }
            }
        }

        override fun onPostExecute(result: Unit) {
            super.onPostExecute(result)

            val bm = BitmapFactory.decodeByteArray(pic.full, 0, pic.full!!.size)
            image.setImageBitmap(bm)
        }
    }
}
