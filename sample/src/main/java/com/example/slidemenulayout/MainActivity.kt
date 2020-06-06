package com.example.slidemenulayout

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.input.input
import com.davidsu.slidemenulayout.SlideMenuLayout
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.item.view.*

class MainActivity : AppCompatActivity() {

    private val mWithRecyclerView = WithRecyclerViewFragment()
    private val mWithScrollView = WithScrollViewFragment()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnRV.setOnClickListener {
            containerBtns.visibility = View.GONE
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.container, mWithRecyclerView)
                .commit()
        }
        btnSV.setOnClickListener {
            containerBtns.visibility = View.GONE
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.container, mWithScrollView)
                .commit()
        }
    }
}