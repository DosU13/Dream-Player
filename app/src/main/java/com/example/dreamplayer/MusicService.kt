package com.example.dreamplayer

import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.Binder
import android.os.IBinder
import android.util.Log
import com.example.dreamplayer.model.MusicFiles

class MusicService : Service() {
    var mBinder: IBinder = MyBinder()
    var mediaPlayer: MediaPlayer? = null
    val musicFiles = ArrayList<MusicFiles>()
    override fun onBind(intent: Intent): IBinder? {
        Log.e("Bind", "Method")
        return mBinder
    }

    inner class MyBinder : Binder() {
        val service: MusicService
            get() = this@MusicService
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        return super.onStartCommand(intent, flags, startId)
    }
}