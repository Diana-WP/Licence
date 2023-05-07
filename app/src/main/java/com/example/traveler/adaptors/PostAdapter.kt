package com.example.traveler.adaptors

import DataClass
import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.traveler.R

class PostAdapter(context: Context, dataList: List<DataClass>) : RecyclerView.Adapter<PostViewHolder>() {

    private val context: Context
    private var dataList: List<DataClass>

    init {
        this.context = context
        this.dataList = dataList
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recycler_item, parent, false)
        return PostViewHolder(view)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        Glide.with(context).load(dataList[position].dataImage).into(holder.recImage)
        holder.recName.text = dataList[position].dataName
        holder.recDesc.text = dataList[position].dataDesc
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun searchDataList(searchList: ArrayList<DataClass>) {
        dataList = searchList
        notifyDataSetChanged()
    }
}

class PostViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    var recImage: ImageView
    var recName: TextView
    var recDesc: TextView
    var recCard: CardView

    init {
        recImage = itemView.findViewById(R.id.recImage)
        recCard = itemView.findViewById(R.id.recCard)
        recName = itemView.findViewById(R.id.recName)
        recDesc = itemView.findViewById(R.id.recDesc)
    }
}