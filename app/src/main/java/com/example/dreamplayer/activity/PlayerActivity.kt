package com.example.dreamplayer.activity

import android.graphics.BitmapFactory
import android.media.MediaMetadataRetriever
import android.media.MediaPlayer
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.dreamplayer.R
import com.example.dreamplayer.activity.MainActivity.Companion.musicFiles
import com.example.dreamplayer.model.MusicFiles
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.activity_player.*

class PlayerActivity : AppCompatActivity() , SeekBar.OnSeekBarChangeListener{
    private var position = -1
    private lateinit var songName : TextView
    private lateinit var artistName : TextView
    private lateinit var durationPlayed : TextView
    private lateinit var durationTotal : TextView
    private lateinit var coverArt : ImageView
    private lateinit var nextBtn : ImageView
    private lateinit var prevBtn : ImageView
    private lateinit var backBtn : ImageView
    private lateinit var shuffleBtn : ImageView
    private lateinit var repeatBtn : ImageView
    private lateinit var playPauseBtn : FloatingActionButton
    private lateinit var seekBar : SeekBar
    private lateinit var playThread : Thread
    private lateinit var prevThread : Thread
    private lateinit var nextThread: Thread
    companion object {
        var listSongs = ArrayList<MusicFiles>()
        lateinit var uri : Uri
        lateinit var mediaPlayer: MediaPlayer
        var isInitialized = false
    }
    private lateinit var handler : Handler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player)
        initViews()
        getIntentMethod()
        songName.text = listSongs[position].title
        song_artist.text = listSongs[position].artist
        seekBar.setOnSeekBarChangeListener(this)

        handler = Handler(Looper.getMainLooper())
        this@PlayerActivity.runOnUiThread(runnable {
            if (isInitialized){
                val mCurrentPosition = mediaPlayer.currentPosition/1000
                seekBar.progress = mCurrentPosition
                durationPlayed.text = formattedTime(mCurrentPosition)
            }
            handler.postDelayed(this, 1000)
        })
    }

    override fun onResume() {
        playThreadBtn()
        prevThreadBtn()
        nextThreadBtn()
        super.onResume()
    }

    private fun nextThreadBtn() {
        nextThread = Thread(Runnable {
            nextBtn.setOnClickListener(View.OnClickListener {
                nextBtnClicked()
            })
        })
        nextThread.start()
    }

    private fun nextBtnClicked() {
        if (mediaPlayer.isPlaying){
            mediaPlayer.stop()
            mediaPlayer.release()
            position = ((position + 1) % listSongs.size)
            uri = Uri.parse(listSongs[position].path)
            mediaPlayer = MediaPlayer.create(applicationContext, uri)
            metaData(uri)
            songName.text = listSongs[position].title
            artistName.text = listSongs[position].artist
            seekBar.max = mediaPlayer.duration / 1000
            this@PlayerActivity.runOnUiThread(runnable {
                if (isInitialized){
                    val mCurrentPosition = mediaPlayer.currentPosition/1000
                    seekBar.progress = mCurrentPosition
                }
                handler.postDelayed(this, 1000)
            })
            playPauseBtn.setImageResource(R.drawable.ic_baseline_pause_24)
            mediaPlayer.start()
        }
        else{
            mediaPlayer.stop()
            mediaPlayer.release()
            position = ((position + 1) % listSongs.size)
            uri = Uri.parse(listSongs[position].path)
            mediaPlayer = MediaPlayer.create(applicationContext, uri)
            metaData(uri)
            songName.text = listSongs[position].title
            artistName.text = listSongs[position].artist
            seekBar.max = mediaPlayer.duration / 1000
            this@PlayerActivity.runOnUiThread(runnable {
                if (isInitialized){
                    val mCurrentPosition = mediaPlayer.currentPosition/1000
                    seekBar.progress = mCurrentPosition
                }
                handler.postDelayed(this, 1000)
            })
            playPauseBtn.setImageResource(R.drawable.ic_baseline_play_arrow_24)
        }
    }

    private fun prevThreadBtn() {
        prevThread = Thread(Runnable {
            prevBtn.setOnClickListener(View.OnClickListener {
                prevBtnClicked()
            })
        })
        prevThread.start()
    }

    private fun prevBtnClicked() {
        if (mediaPlayer.isPlaying){
            mediaPlayer.stop()
            mediaPlayer.release()
            if (position - 1 < 0)  position = listSongs.size - 1
            else position--
            uri = Uri.parse(listSongs[position].path)
            mediaPlayer = MediaPlayer.create(applicationContext, uri)
            metaData(uri)
            songName.text = listSongs[position].title
            artistName.text = listSongs[position].artist
            seekBar.max = mediaPlayer.duration / 1000
            this@PlayerActivity.runOnUiThread(runnable {
                if (isInitialized){
                    val mCurrentPosition = mediaPlayer.currentPosition/1000
                    seekBar.progress = mCurrentPosition
                }
                handler.postDelayed(this, 1000)
            })
            playPauseBtn.setImageResource(R.drawable.ic_baseline_pause_24)
            mediaPlayer.start()
        }
        else{
            mediaPlayer.stop()
            mediaPlayer.release()
            if (position - 1 < 0)  position = listSongs.size - 1
            else position--
            uri = Uri.parse(listSongs[position].path)
            mediaPlayer = MediaPlayer.create(applicationContext, uri)
            metaData(uri)
            songName.text = listSongs[position].title
            artistName.text = listSongs[position].artist
            seekBar.max = mediaPlayer.duration / 1000
            this@PlayerActivity.runOnUiThread(runnable {
                if (isInitialized){
                    val mCurrentPosition = mediaPlayer.currentPosition/1000
                    seekBar.progress = mCurrentPosition
                }
                handler.postDelayed(this, 1000)
            })
            playPauseBtn.setImageResource(R.drawable.ic_baseline_play_arrow_24)
        }
    }

    private fun playThreadBtn() {
        playThread = Thread(Runnable {
            playPauseBtn.setOnClickListener(View.OnClickListener {
                playPauseBtnClicked()
            })
        })
        playThread.start()
    }

    private fun playPauseBtnClicked() {
        if (mediaPlayer.isPlaying){
            playPauseBtn.setImageResource(R.drawable.ic_baseline_play_arrow_24)
            mediaPlayer.pause()
            seekBar.max = mediaPlayer.duration / 1000
            this@PlayerActivity.runOnUiThread(runnable {
                if (isInitialized){
                    val mCurrentPosition = mediaPlayer.currentPosition/1000
                    seekBar.progress = mCurrentPosition
                }
                handler.postDelayed(this, 1000)
            })
        }
        else{
            playPauseBtn.setImageResource(R.drawable.ic_baseline_pause_24)
            mediaPlayer.start()
            seekBar.max = mediaPlayer.duration / 1000
            this@PlayerActivity.runOnUiThread(runnable {
                if (isInitialized){
                    val mCurrentPosition = mediaPlayer.currentPosition/1000
                    seekBar.progress = mCurrentPosition
                }
                handler.postDelayed(this, 1000)
            })
        }
    }

    private fun formattedTime(mCurrentPosition: Int): String {
        var totalOut = ""
        var totalNew = ""
        val seconds : String = (mCurrentPosition % 60).toString()
        val minutes : String = (mCurrentPosition / 60).toString()
        totalOut = "$minutes:$seconds"
        totalNew = "$minutes:0$seconds"
        return if (seconds.length == 1){
            totalNew
        } else {
            totalOut
        }
    }

    private fun getIntentMethod() {
        position = intent.getIntExtra("position", -1)
        listSongs = musicFiles
        playPauseBtn.setImageResource(R.drawable.ic_baseline_pause_24)
        uri = Uri.parse(listSongs[position].path)
        if (isInitialized){
            mediaPlayer.stop()
            mediaPlayer.reset()
            mediaPlayer.release()
            mediaPlayer = MediaPlayer.create(applicationContext, uri)
            mediaPlayer.start()
            Toast.makeText(this,"initialized", Toast.LENGTH_LONG).show()
        }
        else{
            mediaPlayer = MediaPlayer.create(applicationContext, uri)
            mediaPlayer.start()
            isInitialized = true
            Toast.makeText(this, "not init", Toast.LENGTH_LONG).show()
        }
        seekBar.max = mediaPlayer.duration / 1000
        metaData(uri)
    }

    private fun initViews() {
        songName = findViewById<TextView>(R.id.song_name)
        artistName = findViewById<TextView>(R.id.song_artist)
        durationPlayed = findViewById<TextView>(R.id.durationPlayed)
        durationTotal = findViewById<TextView>(R.id.durationTotal)
        coverArt =findViewById<ImageView>(R.id.cover_art)
        nextBtn = findViewById<ImageView>(R.id.id_next)
        prevBtn = findViewById<ImageView>(R.id.id_prev)
        backBtn = findViewById<ImageView>(R.id.back_btn)
        shuffleBtn = findViewById<ImageView>(R.id.id_shuffle)
        repeatBtn = findViewById<ImageView>(R.id.id_repeat)
        playPauseBtn = findViewById<FloatingActionButton>(R.id.play_pause)
        seekBar = findViewById<SeekBar>(R.id.seekbar)
    }

    private fun metaData(uri: Uri){
        val retriever = MediaMetadataRetriever()
        retriever.setDataSource(uri.toString())
        val durationTotalInt = Integer.parseInt(listSongs[position].duration) / 1000
        durationTotal.text = formattedTime(durationTotalInt)
        val art : ByteArray? = retriever.embeddedPicture
        if (art != null){
            Glide.with(this).asBitmap().load(art).into(coverArt)
            val bitmap = BitmapFactory.decodeByteArray(art, 0, art.size)
        }
        else {
            Glide.with(this).asBitmap().load(R.drawable.ic_launcher_background).into(coverArt)
        }
    }

    override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
        if (isInitialized && fromUser){
            mediaPlayer.seekTo(progress * 1000)
        }
    }

    override fun onStartTrackingTouch(p0: SeekBar?) {

    }

    override fun onStopTrackingTouch(p0: SeekBar?) {

    }

    private fun runnable(body: Runnable.(Runnable)->Unit) = object : Runnable{
        override fun run() {
            this.body(this)
        }
    }
}