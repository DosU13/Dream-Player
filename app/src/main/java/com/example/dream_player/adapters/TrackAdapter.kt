package com.example.dream_player.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.dream_player.R
import com.example.dream_player.models.Track

class TrackAdapter(private val trackList: List<Track>, private val context: Context) : RecyclerView.Adapter<TrackAdapter.EmailViewHolder>() {

    override fun onBindViewHolder(emailViewHolder: EmailViewHolder, index: Int) {
        emailViewHolder.nameTextView.text = trackList[index].artist.plus(" - ").plus(trackList[index].name)
        emailViewHolder.artistTextView.text = trackList[index].path
        emailViewHolder.durationTextView.text = trackList[index].duration
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EmailViewHolder {
        return EmailViewHolder(LayoutInflater.from(context).inflate(R.layout.cell_layout, parent, false))
    }

    override fun getItemCount(): Int {
        return trackList.size
    }

    inner class EmailViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nameTextView: TextView = view.findViewById(R.id.track_name)
        val artistTextView: TextView = view.findViewById(R.id.track_artist)
        val durationTextView: TextView = view.findViewById(R.id.track_duration)

    }
}