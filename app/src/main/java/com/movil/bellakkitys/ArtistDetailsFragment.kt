package com.movil.bellakkitys

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.movil.bellakkitys.databinding.FragmentArtistDetailsBinding
import com.movil.bellakkitys.ui.artists.ArtistsViewModel
import com.movil.bellakkitys.data.model.Song
import com.movil.bellakkitys.ui.songs.SongsViewModel

class ArtistDetailsFragment : Fragment() {
    private var _binding: FragmentArtistDetailsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    // Elements
    private lateinit var artistModifyBtn: Button
    private lateinit var artistDeleteBtn: Button
    private lateinit var artistDetailsImage: ImageView
    private lateinit var artistDetailsNameLabel: TextView
    private lateinit var artistDetailsDescriptionLabel: TextView
    private lateinit var artistSongsRecyclerView: RecyclerView
    private lateinit var songAdapter: SongAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val artistsViewModel = ViewModelProvider(requireActivity()).get(ArtistsViewModel::class.java)
        val songsViewModel = ViewModelProvider(requireActivity()).get(SongsViewModel::class.java)

        _binding = FragmentArtistDetailsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // ------------------------------ Elements------------------------------
        artistModifyBtn = binding.artistModifyBtn
        artistDeleteBtn = binding.artistDeleteBtn
        artistDetailsImage = binding.artistDetailsImage
        artistDetailsNameLabel = binding.artistDetailsNameLabel
        artistDetailsDescriptionLabel = binding.artistDetailsDescriptionLabel

        artistsViewModel.artistImage?.let { artistDetailsImage.setImageResource(it) }
        artistDetailsNameLabel.text = artistsViewModel.artistName
        artistDetailsDescriptionLabel.text = artistsViewModel.artistDescription

        // ------------------------------ Validate user type------------------------------
        val rol = artistsViewModel.rol

        if(rol == "user") {
            val parentLayout = binding.fragmentArtistDetails
            parentLayout.removeView(artistModifyBtn)
            parentLayout.removeView(artistDeleteBtn)
        }

        // ------------------------------ Songs List------------------------------
        val songs = songsViewModel.songList
        var filteredSongs = ArrayList<Song>()
        for(song in songs) {
            val artistsArray = song.artist.split(", ")
            if(artistsArray.contains(artistsViewModel.artistName)) {
                filteredSongs.add(song)
            }
        }

        // ------------------------------ Artists Adapter ------------------------------
        artistSongsRecyclerView = binding.artistSongsRecyclerView
        artistSongsRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        songAdapter = SongAdapter(filteredSongs)
        artistSongsRecyclerView.adapter = songAdapter

        songAdapter.setOnItemClickListener(object : SongAdapter.OnItemClickListener {
            override fun onItemClick(position: Int, song: Song, songTitle: TextView) {
                (activity as? MainActivity)?.playSong(song, songTitle)
            }
        })

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}