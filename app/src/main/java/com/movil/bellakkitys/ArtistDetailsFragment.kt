package com.movil.bellakkitys

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.storage
import com.movil.bellakkitys.data.firebase.FirebaseManager
import com.movil.bellakkitys.data.model.Artist
import com.movil.bellakkitys.databinding.FragmentArtistDetailsBinding
import com.movil.bellakkitys.ui.artists.ArtistsViewModel
import com.movil.bellakkitys.ui.songs.SongsViewModel

class ArtistDetailsFragment : Fragment() {
    private var _binding: FragmentArtistDetailsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    lateinit var storage: FirebaseStorage

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
        storage = Firebase.storage
        val firebaseManager = FirebaseManager()

        // ------------------------------ Elements------------------------------
        artistModifyBtn = binding.artistModifyBtn
        artistDeleteBtn = binding.artistDeleteBtn
        artistDetailsImage = binding.artistDetailsImage
        artistDetailsNameLabel = binding.artistDetailsNameLabel
        artistDetailsDescriptionLabel = binding.artistDetailsDescriptionLabel
        artistDetailsImage.isVisible = false

        firebaseManager.loadImage(artistsViewModel.artistImageUrl, artistDetailsImage){
            artistDetailsImage.isVisible = true
        }
        artistDetailsNameLabel.text = artistsViewModel.artistName
        artistDetailsDescriptionLabel.text = artistsViewModel.artistDescription

        // ------------------------------ Validate user type------------------------------
        firebaseManager.getCurrentUser { user ->
            if (user?.rol == "user") {
                val parentLayout = binding.fragmentArtistDetails
                parentLayout.removeView(artistModifyBtn)
                parentLayout.removeView(artistDeleteBtn)
            }
        }

        artistModifyBtn.setOnClickListener {
            val artistName = artistDetailsNameLabel.text.toString()
            findArtistByNameAndStartFormActivity(artistName)
        }

        artistDeleteBtn.setOnClickListener {
            val artistName = artistDetailsNameLabel.text.toString()
            findArtistByNameAndDelete(artistName)
        }
        // ------------------------------ Songs List------------------------------
        /* val songs = songsViewModel.songList
        var filteredSongs = ArrayList<Song>()
        for(song in songs) {
            val artistsArray = song.artist.split(", ")
            if(artistsArray.contains(artistsViewModel.artistName)) {
                filteredSongs.add(song)
            }
        }*/

        // ------------------------------ Artists Adapter ------------------------------
        /* artistSongsRecyclerView = binding.artistSongsRecyclerView
        artistSongsRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        songAdapter = SongAdapter(filteredSongs)
        artistSongsRecyclerView.adapter = songAdapter

        songAdapter.setOnItemClickListener(object : SongAdapter.OnItemClickListener {
            override fun onItemClick(position: Int, song: Song, songTitle: TextView) {
                (activity as? MainActivity)?.playSong(song, songTitle)
            }
        })*/

        return root
    }

    private fun findArtistByNameAndStartFormActivity(artistName: String) {
        Artist.find("name", artistName) { artist ->
            if (artist != null) {
                val intent = Intent(activity, ArtistFormActivity::class.java)
                intent.putExtra("ARTIST_ID", artist.id)
                startActivity(intent)
            }
        }
    }

    private fun findArtistByNameAndDelete(artistName: String) {
        Artist.find("name", artistName) { artist ->
            if (artist != null) {
                artist.delete()
                Toast.makeText(activity, "Artista eliminado", Toast.LENGTH_SHORT).show()
                activity?.onBackPressed()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}