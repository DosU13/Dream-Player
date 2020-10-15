package com.example.dream_player

import android.annotation.SuppressLint
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.dream_player.adapters.TrackAdapter
import com.example.dream_player.models.Track

class MainActivity : AppCompatActivity() {

    @RequiresApi(Build.VERSION_CODES.R)
    @SuppressLint("Recycle")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val selection = MediaStore.Audio.Media.IS_MUSIC + " != 0"

        val projection = arrayOf<String>(
            MediaStore.Audio.Media.DATA,
            MediaStore.Audio.Media.TITLE,
            MediaStore.Audio.Media.ARTIST,
            MediaStore.Audio.Media.DURATION
        )

        val cursor = applicationContext.contentResolver.query(
            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
            projection, selection, null,null)

        val songs = mutableListOf<Track>()
        if (cursor != null) {
            while(cursor.moveToNext()){
                songs.add(Track(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getString(3)))
            }
        }
        setUpEmailRecyclerView(songs)
    }

    private fun setUpEmailRecyclerView(tracks: List<Track>) {
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        val emailRecyclerView = findViewById<RecyclerView>(R.id.track_recycler_view)
        val recyclerAdapter = TrackAdapter(tracks, this)
        emailRecyclerView.layoutManager = layoutManager
        emailRecyclerView.adapter = recyclerAdapter
    }
}