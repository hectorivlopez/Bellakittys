package com.movil.bellakkitys.ui.songs

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.movil.bellakkitys.MainActivity
import com.movil.bellakkitys.R
import com.movil.bellakkitys.SongAdapter
import com.movil.bellakkitys.data.model.Song
import com.movil.bellakkitys.databinding.FragmentSongsBinding
import com.movil.bellakkitys.ui.artists.ArtistsViewModel

class SongsFragment : Fragment() {
    private var _binding: FragmentSongsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    // Elements
    private lateinit var addSongBtn: Button
    private lateinit var songsSearchBar: EditText
    private lateinit var songsRecyclerView: RecyclerView
    private lateinit var songAdapter: SongAdapter

    private var currentSongTitle: TextView? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val songsViewModel = ViewModelProvider(requireActivity()).get(SongsViewModel::class.java)
        val artistsViewModel = ViewModelProvider(requireActivity()).get(ArtistsViewModel::class.java)

        _binding = FragmentSongsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // ------------------------------ Elements------------------------------
        addSongBtn = binding.addSongBtn
        addSongBtn.setOnClickListener {
            addSong()
        }

        // ------------------------------ Validate user type------------------------------
        val rol = songsViewModel.rol
        if(rol == "user") {
            val parentLayout = binding.fragmentSongs
            parentLayout.removeView(addSongBtn)
        }

        // ------------------------------ Songs List------------------------------
        val songs = songsViewModel.songList





        songsSearchBar = root.findViewById(R.id.songsSearchBar)
        songsSearchBar.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(charSequence: CharSequence, start: Int, before: Int, count: Int) {
                // Filtrar canciones según el texto de búsqueda
                val filteredSongs = filterSongs(charSequence.toString())
                // Actualizar el adaptador con la lista filtrada
                songAdapter.updateList(filteredSongs)
            }

            override fun afterTextChanged(editable: Editable) {}
            fun filterSongs(query: String): List<Song> {
                val filteredList = ArrayList<Song>()

                for (song in songs) {
                    // Filtrar por nombre de la canción o artista (puedes ajustar según tus necesidades)
                    if (song.title.toLowerCase().contains(query.toLowerCase()) ||
                        song.artist.toLowerCase().contains(query.toLowerCase())) {
                        filteredList.add(song)
                    }
                }

                return filteredList
            }
        })


        // ------------------------------ Songs Adapter------------------------------
        songsRecyclerView = binding.songsRecyclerView
        songsRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        songAdapter = SongAdapter(songs)
        songsRecyclerView.adapter = songAdapter

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

    fun addSong() {
        (activity as? MainActivity)?.addSong()
    }

}