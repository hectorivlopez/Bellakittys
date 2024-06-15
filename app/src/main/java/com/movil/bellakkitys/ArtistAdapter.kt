package com.movil.bellakkitys

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.movil.bellakkitys.ui.artists.Artist
import com.movil.bellakkitys.ui.songs.Song

class ArtistAdapter(private var artists: ArrayList<Artist>) : RecyclerView.Adapter<ArtistAdapter.ArtistViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArtistViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.artists_list_item, parent, false)
        return ArtistViewHolder(view)
    }

    override fun onBindViewHolder(holder: ArtistViewHolder, position: Int) {
        val currentArtist = artists[position]
        holder.bind(currentArtist)

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
        fun onItemClick(position: Int, artist: Artist)
    }

    // Click listener variable
    private var listener: OnItemClickListener? = null

    // Function to set click listener
    fun setOnItemClickListener(clickListener: OnItemClickListener) {
        listener = clickListener
    }

    override fun getItemCount(): Int {
        return artists.size
    }

    inner class ArtistViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val artistName: TextView = itemView.findViewById(R.id.artistName)
        private val artistImage: ImageView = itemView.findViewById(R.id.artistImage)

        init {
            // New code: Set click listener for the item view
            itemView.setOnClickListener {
                listener?.onItemClick(adapterPosition, artists[adapterPosition])
            }
        }

        fun bind(artist: Artist) {
            artistName.text = artist.name
            artistImage.setImageResource(artist.image)
            // Load song thumbnail/image using a library like Picasso/Glide
            // Example: Glide.with(itemView.context).load(song.thumbnailUrl).into(artistImage)
        }
    }

    fun updateList(newList: List<Artist>) {
        artists = newList as ArrayList<Artist>
        notifyDataSetChanged()
    }
}