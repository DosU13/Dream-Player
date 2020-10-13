package com.example.dream_player

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.dream_player.adapters.TrackAdapter
import com.example.dream_player.models.Track

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val fakeEmails = generateFakeEmails()
        setUpEmailRecyclerView(fakeEmails)
    }

    private fun setUpEmailRecyclerView(tracks: List<Track>) {
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        val emailRecyclerView = findViewById<RecyclerView>(R.id.email_recycler_view)
        val recyclerAdapter = TrackAdapter(tracks, this)
        emailRecyclerView.layoutManager = layoutManager
        emailRecyclerView.adapter = recyclerAdapter
    }

    private fun generateFakeEmails(): List<Track> {
        val titles = listOf(
            "Hot dogs $1 only!",
            "Dev.to beats Medium.",
            "We have updated our privacy :/",
            "Nick moves to New Zealand")
        val descriptions = listOf(
            "This is truly amazing, unexpected...",
            "Yes, yes, yes! It is happening!",
            "Follow our blog to learn more...",
            "Well, it supposed to happen...")
        val times = listOf(
            "13:42",
            "16:16",
            "12:34",
            "20:20")
        val emailList = mutableListOf<Track>()
        for (i in 0..100) {
            emailList.add(
                Track(titles.random(), descriptions.random(), times.random())
            )
        }
        return emailList
    }
}