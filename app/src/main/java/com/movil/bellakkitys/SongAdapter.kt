package com.movil.bellakkitys

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.movil.bellakkitys.data.model.Song

class SongAdapter(private var songs: ArrayList<Song>, private val rol: String) : RecyclerView.Adapter<SongAdapter.SongViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SongViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.songs_list_item, parent, false)
        return SongViewHolder(view)
    }

    override fun onBindViewHolder(holder: SongViewHolder, position: Int) {
        val currentSong = songs[position]
        holder.bind(currentSong, rol)

        // Margin bottom to the last element
        if (position == itemCount - 1) {
            val layoutParams = holder.itemView.layoutParams as RecyclerView.LayoutParams
            val marginBottom = holder.itemView.context.resources.getDimensionPixelSize(R.dimen.last_item_margin_bottom)
            layoutParams.setMargins(
                layoutParams.leftMargin,
                layoutParams.topMargin,
                layoutParams.rightMargin,
                marginBottom
            )
            holder.itemView.layoutParams = layoutParams
        } else {
            // Reset margins for other items
            val layoutParams = holder.itemView.layoutParams as RecyclerView.LayoutParams
            layoutParams.setMargins(
                layoutParams.leftMargin,
                layoutParams.topMargin,
                layoutParams.rightMargin,
                0 // No bottom margin for other items
            )
            holder.itemView.layoutParams = layoutParams
        }
    }

    // Interface for click listener
    interface OnItemClickListener {
        fun onItemClick(position: Int, song: Song, songTitle: TextView)
    }

    // Click listener variable
    private var listener: OnItemClickListener? = null

    // Function to set click listener
    fun setOnItemClickListener(clickListener: OnItemClickListener) {
        listener = clickListener
    }



    override fun getItemCount(): Int {
        return songs.size
    }

    inner class SongViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val songTitle: TextView = itemView.findViewById(R.id.songTitle)
        private val songArtist: TextView = itemView.findViewById(R.id.songArtist)
        private val songThumbnail: ImageView = itemView.findViewById(R.id.songThumbnail)
        private val songDuration: TextView = itemView.findViewById(R.id.songDuration)
        private val editSongBtn: Button = itemView.findViewById(R.id.editSongBtn)

        init {
            // New code: Set click listener for the item view
            itemView.setOnClickListener {
                for (i in songs.indices) {
                    // Update the text color of songTitle for all items
                    songTitle?.setTextColor(ContextCompat.getColor(itemView.context, R.color.white))
                }
                listener?.onItemClick(adapterPosition, songs[adapterPosition], songTitle)
            }


        }

        fun bind(song: Song, rol: String) {
            songTitle.text = song.title
            songArtist.text = song.artists.toString()
            if (song.imageUrl.isNotEmpty()) {
                Song.firebaseManager.loadImage(song.imageUrl, songThumbnail)
            }
            songDuration.text = song.duration
            if (song.active) {
                songTitle.setTextColor(ContextCompat.getColor(itemView.context, R.color.accentColor))
            } else {
                // Set default color if not active
                songTitle.setTextColor(ContextCompat.getColor(itemView.context, R.color.white))
            }
            // Load song thumbnail/image using a library like Picasso/Glide
            // Example: Glide.with(itemView.context).load(song.thumbnailUrl).into(songThumbnail)
            // Show/hide elements based on the user role
            if (rol == "user") {
                editSongBtn.visibility = View.GONE
                songDuration.visibility = View.VISIBLE
            } else if (rol == "admin") {
                editSongBtn.visibility = View.VISIBLE
                songDuration.visibility = View.GONE
            }
        }
    }

    fun updateList(newList: List<Song>) {
        songs = newList as ArrayList<Song>
        notifyDataSetChanged()
    }
}