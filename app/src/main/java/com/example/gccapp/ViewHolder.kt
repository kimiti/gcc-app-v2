package com.example.gccapp

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    var videoTitle: TextView
    var subtitle: TextView
    var imageUrl: ImageView

    private var mClickListener: Clicklistener? = null

    init {
        videoTitle = itemView.findViewById(R.id.title_textView)
        subtitle = itemView.findViewById(R.id.description_textView)
        imageUrl = itemView.findViewById(R.id.dvd_imageView)

        itemView.setOnClickListener {
            mClickListener!!.onItemClick(itemView,bindingAdapterPosition)
        }
    }

    interface Clicklistener {
        fun onItemClick(view: View?, position: Int)
    }

    fun setOnClicklistener(clicklistener: ViewHolder.Clicklistener) {
        mClickListener = clicklistener
    }
}