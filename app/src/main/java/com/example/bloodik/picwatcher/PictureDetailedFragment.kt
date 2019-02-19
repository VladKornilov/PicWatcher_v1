package com.example.bloodik.picwatcher

import android.support.v4.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.content_picture_detailed.view.*

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
    ): View? = inflater.inflate(R.layout.content_picture_detailed, container, false).apply { ->
        picture?.downloadFull(image)
        author.text = picture?.author?.name
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
}
