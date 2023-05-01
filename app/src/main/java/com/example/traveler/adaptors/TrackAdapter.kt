package com.example.traveler.adaptors

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.traveler.DB.Track
import com.example.traveler.R
import com.example.traveler.TrackingUtility
import java.text.SimpleDateFormat
import java.util.*



class TrackAdapter : RecyclerView.Adapter<TrackAdapter.TrackViewHolder>(){
    inner class TrackViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    private lateinit var ivTrackImage: ImageView
    private lateinit var tvDate: TextView
    private lateinit var tvTime: TextView
    private lateinit var tvAvgSpeed: TextView
    private lateinit var tvDistance: TextView

    val diffCallback = object: DiffUtil.ItemCallback<Track>(){
        override fun areItemsTheSame(oldItem: Track, newItem: Track): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Track, newItem: Track): Boolean {
            return oldItem.hashCode() == newItem.hashCode() //compare the hashvalues
        }
    }

    val differ = AsyncListDiffer(this, diffCallback)

    fun submitList(list: List<Track>) = differ.submitList(list)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        return TrackViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_track,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        val track = differ.currentList[position]
        holder.itemView.apply {
            Glide.with(this).load(track.img).into(ivTrackImage)
            val calendar = Calendar.getInstance().apply {
                timeInMillis = track.timestamp
            }
            val dateFormat = SimpleDateFormat("dd.MM.yy", Locale.getDefault())
            tvDate.text = dateFormat.format(calendar.time)

            val avgSpeed = "${track.avgSpeedInKMH}km/h"
            tvAvgSpeed.text = avgSpeed

            val distanceInKm = "${track.distanceInMeters / 1000f}km"    // divided by 1000f to be in km
            tvDistance.text = distanceInKm

            tvTime.text = TrackingUtility.getFormattedStopWatchTime(track.timeInMillis)

        }
    }
}