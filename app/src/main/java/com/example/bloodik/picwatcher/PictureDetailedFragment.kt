package com.example.bloodik.picwatcher

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.AsyncTask
import android.support.v4.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import kotlinx.android.synthetic.main.content_picture_detailed.*
import kotlinx.android.synthetic.main.content_picture_detailed.view.*
import java.net.URL
import java.util.concurrent.TimeUnit
import javax.xml.datatype.DatatypeConstants.SECONDS



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
        val full = DownloadFull()
        full.execute(picture?.links!!.full)
        author.text = picture?.author
        description.text = picture?.description
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

    internal inner class DownloadFull : AsyncTask<String, Unit, ByteArray>() {

        override fun doInBackground(vararg p0: String?): ByteArray {
            val url = URL(p0[0])
            val data = url.openStream()
            val res = data.readBytes()
            data.close()
            return res
        }

        override fun onPostExecute(result: ByteArray?) {
            super.onPostExecute(result)
            if (result != null) {
                val bm = BitmapFactory.decodeByteArray(result, 0, result.size)
                var scale: Int = 1
                while (result.size / scale > 5*1024*1024) scale *= 2
                val scaled = Bitmap.createScaledBitmap(bm, bm.width / scale, bm.height / scale, false)
                image.setImageBitmap(scaled)
            }
        }
    }
}
