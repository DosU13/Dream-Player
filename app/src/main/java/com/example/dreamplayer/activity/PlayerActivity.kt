package com.example.dreamplayer.activity

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.media.MediaMetadataRetriever
import android.media.MediaPlayer
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.*
import androidx.palette.graphics.Palette
import com.bumptech.glide.Glide
import com.example.dreamplayer.R
import com.example.dreamplayer.activity.MainActivity.Companion.musicFiles
import com.example.dreamplayer.activity.MainActivity.Companion.repeatBoolean
import com.example.dreamplayer.activity.MainActivity.Companion.shuffleBoolean
import com.example.dreamplayer.model.MusicFiles
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.activity_player.*
import kotlin.random.Random

class PlayerActivity : AppCompatActivity() , MediaPlayer.OnCompletionListener{
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
        mediaPlayer.setOnCompletionListener(this)
        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (isInitialized && fromUser){
                    mediaPlayer.seekTo(progress * 1000)
                }
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {

            }

            override fun onStopTrackingTouch(p0: SeekBar?) {

            }
        })

        handler = Handler(Looper.getMainLooper())
        this@PlayerActivity.runOnUiThread(runnable {
            if (isInitialized){
                val mCurrentPosition = mediaPlayer.currentPosition/1000
                seekBar.progress = mCurrentPosition
                durationPlayed.text = formattedTime(mCurrentPosition)
            }
            handler.postDelayed(this, 1000)
        })
        shuffleBtn.setOnClickListener(object: View.OnClickListener{
            override fun onClick(p0: View?) {
                if (shuffleBoolean){
                    shuffleBoolean = false
                    shuffleBtn.setImageResource(R.drawable.ic_baseline_shuffle_24)
                }
                else{
                    shuffleBoolean = true
                    shuffleBtn.setImageResource(R.drawable.ic_baseline_shuffle_off_24)
                }
            }
        })
        repeatBtn.setOnClickListener(object : View.OnClickListener {
            override fun onClick(p0: View?) {
                if (repeatBoolean){
                    repeatBoolean = false
                    repeatBtn.setImageResource(R.drawable.ic_baseline_repeat_off_24)
                }
                else{
                    repeatBoolean = true
                    repeatBtn.setImageResource(R.drawable.ic_baseline_repeat_24)
                }
            }

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
            if (shuffleBoolean && !repeatBoolean){
                position = Random.nextInt(0,listSongs.size)
            }
            else if(!shuffleBoolean && !repeatBoolean) {
                position = ((position + 1) % listSongs.size)
            }
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
            mediaPlayer.setOnCompletionListener(this)
            playPauseBtn.setBackgroundResource(R.drawable.ic_baseline_pause_24)
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
            mediaPlayer.setOnCompletionListener(this)
            playPauseBtn.setBackgroundResource(R.drawable.ic_baseline_play_arrow_24)
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
            if (shuffleBoolean && !repeatBoolean){
                position = Random.nextInt(0,listSongs.size)
            }
            else if(!shuffleBoolean && !repeatBoolean) {
                if (position - 1 < 0) position = listSongs.size - 1
                else position--
            }
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
            mediaPlayer.setOnCompletionListener(this)
            playPauseBtn.setBackgroundResource(R.drawable.ic_baseline_pause_24)
            mediaPlayer.start()
        }
        else{
            mediaPlayer.stop()
            mediaPlayer.release()
            if (shuffleBoolean && !repeatBoolean){
                position = Random.nextInt(0,listSongs.size)
            }
            else if(!shuffleBoolean && !repeatBoolean) {
                if (position - 1 < 0) position = listSongs.size - 1
                else position--
            }
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
            mediaPlayer.setOnCompletionListener(this)
            playPauseBtn.setBackgroundResource(R.drawable.ic_baseline_play_arrow_24)
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
        var bitmap : Bitmap?
        if (art != null){
            bitmap = BitmapFactory.decodeByteArray(art, 0, art.size)
            imageAnimation(this, coverArt, bitmap)
            Palette.from(bitmap).generate(Palette.PaletteAsyncListener {
                val swatch = it?.dominantSwatch
                if(swatch!=null){
                    val gradient = findViewById<ImageView>(R.id.imageViewGradient)
                    val mContainer = findViewById<RelativeLayout>(R.id.mContainer)
                    gradient.setBackgroundResource(R.drawable.gradient_bg)
                    mContainer.setBackgroundResource(R.drawable.main_bg)
                    val gradientDrawable = GradientDrawable(GradientDrawable.Orientation.BOTTOM_TOP,
                    intArrayOf(swatch.rgb, 0x00000000))
                    gradient.background = gradientDrawable
                    val gradientDrawableBg = GradientDrawable(GradientDrawable.Orientation.BOTTOM_TOP,
                    intArrayOf(swatch.rgb, swatch.rgb))
                    mContainer.background = gradientDrawableBg
                    songName.setTextColor(swatch.titleTextColor)
                    artistName.setTextColor(swatch.bodyTextColor)
                }
                else{
                    val gradient = findViewById<ImageView>(R.id.imageViewGradient)
                    val mContainer = findViewById<RelativeLayout>(R.id.mContainer)
                    gradient.setBackgroundResource(R.drawable.gradient_bg)
                    mContainer.setBackgroundResource(R.drawable.main_bg)
                    val gradientDrawable = GradientDrawable(GradientDrawable.Orientation.BOTTOM_TOP,
                        intArrayOf(0xff000000.toInt(), 0x00000000))
                    gradient.background = gradientDrawable
                    val gradientDrawableBg = GradientDrawable(GradientDrawable.Orientation.BOTTOM_TOP,
                        intArrayOf(0xff000000.toInt(), 0xff000000.toInt()))
                    mContainer.background = gradientDrawableBg
                    songName.setTextColor(Color.WHITE)
                    artistName.setTextColor(Color.DKGRAY)
                }
            })
        }
        else {
            Glide.with(this).asBitmap().load(R.drawable.ic_launcher_background).into(coverArt)
            val gradient = findViewById<ImageView>(R.id.imageViewGradient)
            val mContainer = findViewById<RelativeLayout>(R.id.mContainer)
            gradient.setBackgroundResource(R.drawable.gradient_bg)
            mContainer.setBackgroundResource(R.drawable.main_bg)
            songName.setTextColor(Color.WHITE)
            artistName.setTextColor(Color.DKGRAY)
        }
    }

    private fun imageAnimation(context: Context, imageView: ImageView, bitmap: Bitmap) {
        val animOut = AnimationUtils.loadAnimation(context, android.R.anim.fade_out)
        val animIn = AnimationUtils.loadAnimation(context, android.R.anim.fade_in)
        animOut.setAnimationListener(object : Animation.AnimationListener{
            override fun onAnimationRepeat(p0: Animation?) {
            }

            override fun onAnimationEnd(p0: Animation?) {
                Glide.with(context).load(bitmap).into(imageView)
                animIn.setAnimationListener(object : Animation.AnimationListener {
                    override fun onAnimationRepeat(p0: Animation?) {
                    }

                    override fun onAnimationEnd(p0: Animation?) {
                    }

                    override fun onAnimationStart(p0: Animation?) {
                    }

                })
                imageView.startAnimation(animIn)
            }

            override fun onAnimationStart(p0: Animation?) {
            }
        })
        imageView.startAnimation(animOut)
    }

    private fun runnable(body: Runnable.(Runnable)->Unit) = object : Runnable{
        override fun run() {
            this.body(this)
        }
    }

    override fun onCompletion(p0: MediaPlayer?) {
        nextBtnClicked()
        if (isInitialized){
            mediaPlayer = MediaPlayer.create(applicationContext, uri)
            mediaPlayer.start()
            mediaPlayer.setOnCompletionListener(this)
        }
    }
}