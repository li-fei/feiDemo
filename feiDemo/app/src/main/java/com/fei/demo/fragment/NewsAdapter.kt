package com.fei.demo.fragment

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.fei.demo.R
import kotlinx.android.synthetic.main.item_left.view.*

class NewsAdapter(private val list: List<News>) :
        RecyclerView.Adapter<NewsAdapter.NewsViewHolder>() {

    interface OnItemClickListener {
        fun OnItemClick(view: View, position: Int)
    }

    private lateinit var onClick: OnItemClickListener
    fun setOnItemClick(onItemClickListener: OnItemClickListener) {
        this.onClick = onItemClickListener

    }

    class NewsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val view = itemView
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        return NewsViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_left, parent, false))
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        holder.view.tv_item.text = list[position].title
        holder.view.setOnClickListener { onClick.OnItemClick(holder.view, position) }
    }

    override fun getItemCount(): Int {
        return list.size
    }
}