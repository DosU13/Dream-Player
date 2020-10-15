package com.example.dream_player.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.dream_player.R
import com.example.dream_player.models.Track

class TrackAdapter(private val trackList: List<Track>, private val context: Context) : RecyclerView.Adapter<TrackAdapter.TrackViewHolder>() {

    override fun onBindViewHolder(trackViewHolder: TrackViewHolder, index: Int) {
        trackViewHolder.nameTextView.text = trackList[index].artist.plus(" - ").plus(trackList[index].name)
        trackViewHolder.artistTextView.text = trackList[index].path
        trackViewHolder.durationTextView.text = trackList[index].duration
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        val cellLayout = LayoutInflater.from(context).inflate(R.layout.cell_layout, parent, false)
        return TrackViewHolder(cellLayout)
    }

    override fun getItemCount(): Int {
        return trackList.size
    }

    inner class TrackViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nameTextView: TextView = view.findViewById(R.id.track_name)
        val artistTextView: TextView = view.findViewById(R.id.track_artist)
        val durationTextView: TextView = view.findViewById(R.id.track_duration)
    }
}