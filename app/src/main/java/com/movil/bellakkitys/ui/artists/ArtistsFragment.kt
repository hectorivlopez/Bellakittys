package com.movil.bellakkitys.ui.artists

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.movil.bellakkitys.ArtistAdapter
import com.movil.bellakkitys.ArtistDetailsFragment
import com.movil.bellakkitys.MainActivity
import com.movil.bellakkitys.data.model.Artist
import com.movil.bellakkitys.databinding.FragmentArtistsBinding

class ArtistsFragment : Fragment() {
    private var _binding: FragmentArtistsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    // Elements
    private lateinit var addArtistBtn: Button
    private lateinit var artistsSearchBar: EditText
    private lateinit var artistsRecyclerView: RecyclerView
    private lateinit var artistAdapter: ArtistAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val artistsViewModel = ViewModelProvider(requireActivity()).get(ArtistsViewModel::class.java)

        _binding = FragmentArtistsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // ------------------------------ Elements------------------------------
        addArtistBtn = binding.addArtistBtn
        addArtistBtn.setOnClickListener {
            addArtist()
        }

        // ------------------------------ Validate user type------------------------------
        val rol = artistsViewModel.rol
        if(rol == "user") {
            val parentLayout = binding.fragmentArtists
            parentLayout.removeView(addArtistBtn)
        }

        // ------------------------------ Artists List------------------------------
        val artists = artistsViewModel.artistList

        artistsSearchBar = binding.artistsSearchBar
        artistsSearchBar.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(charSequence: CharSequence, start: Int, before: Int, count: Int) {
                // Filtrar canciones según el texto de búsqueda
                val filteredArtists = filterArtists(charSequence.toString())
                // Actualizar el adaptador con la lista filtrada
                artistAdapter.updateList(filteredArtists)
            }

            override fun afterTextChanged(editable: Editable) {}
            fun filterArtists(query: String): List<Artist> {
                val filteredList = ArrayList<Artist>()

                for (artist in artists) {
                    if (artist.name.toLowerCase().contains(query.toLowerCase())){
                        filteredList.add(artist)
                    }
                }

                return filteredList
            }
        })




        // ------------------------------ Artists Adapter ------------------------------
        artistsRecyclerView = binding.artistsRecyclerView
        artistsRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        artistAdapter = ArtistAdapter(artists)
        artistsRecyclerView.adapter = artistAdapter

        /*artistAdapter.setOnItemClickListener(object : ArtistAdapter.OnItemClickListener {
            override fun onItemClick(position: Int, artist: Artist) {
                //(activity as? MainActivity)?.playSong(artist)
                artistsViewModel.artistImage = artist.image
                artistsViewModel.artistName = artist.name
                artistsViewModel.artistDescription = artist.description


                val mainActivity = activity as MainActivity?
                mainActivity?.replaceFragment(ArtistDetailsFragment())
            }
        })*/

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun addArtist() {
        (activity as? MainActivity)?.addArtist()
    }

}