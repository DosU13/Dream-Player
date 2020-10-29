package com.example.dreamplayer

import android.content.Context
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class MusicAdapter(private val mContext: Context?, private val mFiles: ArrayList<MusicFiles>) : RecyclerView.Adapter<MusicAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MusicAdapter.MyViewHolder {
        val view = LayoutInflater.from(mContext).inflate(R.layout.music_items, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.file_name.text = mFiles[position].title
        val image = getAlbumArt(mFiles[position].path)
        if (mContext != null) {
            if (image != null) {
                Glide.with(mContext).asBitmap().load(image).into(holder.album_art)
            } else {
                Glide.with(mContext).load(R.drawable.ic_launcher_background)
                    .into(holder.album_art)
            }
        }
    }

    override fun getItemCount(): Int {
        return mFiles.size
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val file_name = itemView.findViewById<TextView>(R.id.music_file_name)
        val album_art = itemView.findViewById<ImageView>(R.id.music_img)
    }

    private fun getAlbumArt(uri: String): ByteArray? {
        val retriever = MediaMetadataRetriever()
        retriever.setDataSource(uri)
        val art = retriever.embeddedPicture
        retriever.release()
        return art
    }
}