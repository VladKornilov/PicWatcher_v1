package com.example.bloodik.picwatcher

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity;

import kotlinx.android.synthetic.main.activity_picture_detailed.*

class PictureDetailedActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_picture_detailed)

        val picture = intent?.getSerializableExtra("Picture") as Picture
        if (supportFragmentManager.backStackEntryCount > 0) return
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.picture_detailed, PictureDetailedFragment.newInstance(picture), "TAG_HOME")
            commit()
        }
    }

}
