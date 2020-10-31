package com.example.dreamplayer.fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.dreamplayer.R
import com.example.dreamplayer.activity.MainActivity
import com.example.dreamplayer.activity.PlayerActivity
import com.example.dreamplayer.adapter.AlbumAdapter
import com.example.dreamplayer.adapter.MusicAdapter

class AlbumFragment : Fragment(){
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view  = inflater.inflate(R.layout.fragment_album, container, false)
        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.setHasFixedSize(true)
        if (MainActivity.musicFiles.size >= 1) {
            val albumAdapter = AlbumAdapter(context,  MainActivity.musicFiles)
            recyclerView.adapter = albumAdapter
            recyclerView.layoutManager = GridLayoutManager(context,2)
        }
        return view
    }
}