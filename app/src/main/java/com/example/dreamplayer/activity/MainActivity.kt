package com.example.dreamplayer.activity

import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.MediaStore
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.example.dreamplayer.fragment.AlbumFragment
import com.example.dreamplayer.R
import com.example.dreamplayer.fragment.SongsFragment
import com.example.dreamplayer.model.MusicFiles
import com.google.android.material.tabs.TabLayout

class MainActivity : AppCompatActivity(){
    val REQUEST_CODE = 1
    companion object{ var musicFiles = ArrayList<MusicFiles>()}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        permission()
    }

    private fun permission(){
        if (ContextCompat.checkSelfPermission(applicationContext, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
            != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this@MainActivity, arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE), REQUEST_CODE)
        }
        else {
            musicFiles = getAllAudio(this)
            initViewPager()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE){
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED){
                musicFiles = getAllAudio(this)
                initViewPager()
            }
            else{
                ActivityCompat.requestPermissions(this@MainActivity, arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE), REQUEST_CODE)
            }
        }
    }

    private fun initViewPager() {
        val viewPager = findViewById<ViewPager>(R.id.viewpager)
        val tabLayout = findViewById<TabLayout>(R.id.tab_layout)
        val viewPagerAdapter = ViewPagerAdapter(supportFragmentManager)
        viewPagerAdapter.addFragments(SongsFragment(), "Songs")
        viewPagerAdapter.addFragments(AlbumFragment(), "Albums")
        viewPager.adapter = viewPagerAdapter
        tabLayout.setupWithViewPager(viewPager)
    }

    class ViewPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {
        private var fragments = ArrayList<Fragment>()
        private var titles = ArrayList<String>()

        fun addFragments(fragment: Fragment, title: String){
            fragments.add(fragment)
            titles.add(title)
        }

        override fun getItem(position: Int): Fragment {
            return fragments[position]
        }

        override fun getCount(): Int {
            return  fragments.size
        }

        override fun getPageTitle(position: Int): CharSequence? {
            return titles[position]
        }
    }

    private fun getAllAudio(context : Context) : ArrayList<MusicFiles> {
        val tempAudioList = ArrayList<MusicFiles>()
        val uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
        val projection = arrayOf(
            MediaStore.Audio.Media.ALBUM,
            MediaStore.Audio.Media.TITLE,
            MediaStore.Audio.Media.DURATION,
            MediaStore.Audio.Media.DATA,
            MediaStore.Audio.Media.ARTIST
        )

        val cursor = context.contentResolver.query(uri, projection, null, null, null)
        if (cursor!=null){
            while(cursor.moveToNext()){
                val album = cursor.getString(0)
                val title = cursor.getString(1)
                val duration = cursor.getString(2)
                val path = cursor.getString(3)
                val artist = cursor.getString(4)

                val musicFiles = MusicFiles(
                    path,
                    title,
                    artist,
                    album,
                    duration
                )
                tempAudioList.add(musicFiles)
            }
            cursor.close()
        }
        return tempAudioList
    }
}