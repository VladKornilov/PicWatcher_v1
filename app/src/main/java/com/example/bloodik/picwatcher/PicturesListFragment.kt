package com.example.bloodik.picwatcher

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.*
import kotlinx.android.synthetic.main.pictures_list.view.*

class PicturesListFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) : View {
        val view = inflater.inflate(R.layout.pictures_list, container, false)
        createPics(context!!, 20)
        view.pictures_list.layoutManager = LinearLayoutManager(context)
        view.pictures_list.adapter = PicAdapter(context!!) {
            if (activity?.findViewById<View>(R.id.fragment_detail) != null) {
                val transaction = activity!!.supportFragmentManager.beginTransaction()
                transaction.replace(R.id.fragment_detail, PictureDetailedFragment.newInstance(it))
                transaction.commit()
            } else {
                val intent = Intent(context, PictureDetailedActivity::class.java)
                intent.putExtra("Picture", it)
                startActivity(intent)
            }
        }
        view.pictures_list.setHasFixedSize(true)

        return view
    }

}

