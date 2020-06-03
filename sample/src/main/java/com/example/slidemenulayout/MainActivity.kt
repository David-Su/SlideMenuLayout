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

    data class Bean(
        val icon: Int,
        val title: String,
        var content: String
    )

    inner class Adapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

        override fun getItemCount(): Int {
            return mData.size
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            val slideMenuLayout = holder.itemView as SlideMenuLayout
            holder.itemView.ivIcon.setImageResource(mData[position].icon)
            holder.itemView.tvTitle.text = mData[position].title
            holder.itemView.tvContent.text = mData[position].content
            slideMenuLayout.closeImmediate()


            slideMenuLayout.getMenuView<View>()?.findViewById<TextView>(R.id.tvDelete)?.setOnClickListener {
                mData.removeAt(position)
                notifyItemRemoved(position)
                notifyItemChanged(position)
            }

            slideMenuLayout.getMenuView<View>()?.findViewById<TextView>(R.id.tvUpdate)?.setOnClickListener {
                MaterialDialog(this@MainActivity).show {
                    input { _, charSequence ->
                        title = "Input content"
                        mData[position].content = charSequence.toString()
                        notifyItemChanged(position)
                    }
                }

            }

        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            return object :
                RecyclerView.ViewHolder(layoutInflater.inflate(R.layout.item, parent, false)) {}
        }

    }

    private val mData = arrayListOf<Bean>(
        Bean(R.drawable.ic_launcher_foreground, "Danny", "Hello,how are you."),
        Bean(R.drawable.ic_launcher_foreground, "Cathy", "Are you free tonight?"),
        Bean(R.drawable.ic_launcher_foreground, "Mom", "Go home,you bastard."),
        Bean(R.drawable.ic_launcher_foreground, "Danny", "Hello,how are you."),
        Bean(R.drawable.ic_launcher_foreground, "Cathy", "Are you free tonight?"),
        Bean(R.drawable.ic_launcher_foreground, "Mom", "Go home,you bastard."),
        Bean(R.drawable.ic_launcher_foreground, "Danny", "Hello,how are you."),
        Bean(R.drawable.ic_launcher_foreground, "Cathy", "Are you free tonight?"),
        Bean(R.drawable.ic_launcher_foreground, "Mom", "Go home,you bastard."),
        Bean(R.drawable.ic_launcher_foreground, "Danny", "Hello,how are you."),
        Bean(R.drawable.ic_launcher_foreground, "Cathy", "Are you free tonight?"),
        Bean(R.drawable.ic_launcher_foreground, "Mom", "Go home,you bastard."),
        Bean(R.drawable.ic_launcher_foreground, "Danny", "Hello,how are you."),
        Bean(R.drawable.ic_launcher_foreground, "Cathy", "Are you free tonight?"),
        Bean(R.drawable.ic_launcher_foreground, "Mom", "Go home,you bastard."),
        Bean(R.drawable.ic_launcher_foreground, "Danny", "Hello,how are you."),
        Bean(R.drawable.ic_launcher_foreground, "Cathy", "Are you free tonight?"),
        Bean(R.drawable.ic_launcher_foreground, "Mom", "Go home,you bastard."),
        Bean(R.drawable.ic_launcher_foreground, "Danny", "Hello,how are you."),
        Bean(R.drawable.ic_launcher_foreground, "Cathy", "Are you free tonight?"),
        Bean(R.drawable.ic_launcher_foreground, "Mom", "Go home,you bastard.")
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        recyclerView.adapter = Adapter()
    }
}