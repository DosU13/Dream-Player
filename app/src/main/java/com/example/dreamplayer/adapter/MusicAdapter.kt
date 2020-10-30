package com.example.dreamplayer.adapter

import android.content.Context
import android.media.MediaMetadataRetriever
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.dreamplayer.R
import com.example.dreamplayer.model.MusicFiles

class MusicAdapter(private val mContext: Context?, private val mFiles: ArrayList<MusicFiles>, private val cellClickListener: CellClickListener) :
    RecyclerView.Adapter<MusicAdapter.MyViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
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
        val musicFiles = mFiles[position]
        holder.itemView.setOnClickListener{
            cellClickListener.onCellClickListener(mContext, position)
        }
    }

    override fun getItemCount(): Int {
        return mFiles.size
    }

    interface CellClickListener {
        fun onCellClickListener(mContext: Context?, position: Int)
    }

    private fun getAlbumArt(uri: String): ByteArray? {
        val retriever = MediaMetadataRetriever()
        retriever.setDataSource(uri)
        val art = retriever.embeddedPicture
        retriever.release()
        return art
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val file_name = itemView.findViewById<TextView>(R.id.music_file_name)
        val album_art = itemView.findViewById<ImageView>(R.id.music_img)
    }
}