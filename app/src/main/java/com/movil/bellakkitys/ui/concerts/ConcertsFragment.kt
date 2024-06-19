package com.movil.bellakkitys.ui.concerts

import android.app.NotificationManager
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CalendarView
import android.widget.EditText
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.movil.bellakkitys.ArtistAdapter
import com.movil.bellakkitys.ArtistDetailsFragment
import com.movil.bellakkitys.MainActivity
import com.movil.bellakkitys.MyApp
import com.movil.bellakkitys.R
import com.movil.bellakkitys.databinding.FragmentConcertsBinding
import com.movil.bellakkitys.data.model.Artist
import com.movil.bellakkitys.ui.artists.ArtistsViewModel
import java.util.Calendar

class ConcertsFragment : Fragment() {

    private var _binding: FragmentConcertsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    // Elements
    private lateinit var artistsConcertSearchBar: EditText
    private lateinit var calendarView: CalendarView

    private lateinit var artistsRecyclerView: RecyclerView
    private lateinit var artistAdapter: ArtistAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val artistsViewModel = ViewModelProvider(requireActivity()).get(ArtistsViewModel::class.java)
        val concertsViewModel = ViewModelProvider(this).get(ConcertsViewModel::class.java)

        _binding = FragmentConcertsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // ------------------------------ Elements------------------------------
        calendarView = binding.calendarView

        // ------------------------------ Validate user type------------------------------
        val rol = concertsViewModel.rol


        // ------------------------------ Artists List------------------------------
        Artist.all { artists ->
            artistsViewModel.artistList = artists

            artistsConcertSearchBar = binding.artistsConcertsSearchBar
            artistsConcertSearchBar.addTextChangedListener(object : TextWatcher {
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

            artistsRecyclerView = binding.calendarRecyclerView
            artistsRecyclerView.layoutManager = LinearLayoutManager(requireContext())
            artistAdapter = ArtistAdapter(artists)
            artistsRecyclerView.adapter = artistAdapter

            // ------------------------------ Artists Adapter ------------------------------

            artistAdapter.setOnItemClickListener(object : ArtistAdapter.OnItemClickListener {
                override fun onItemClick(position: Int, artist: Artist) {
                    val dateArr = artist.concert.split("/")
                    val year = dateArr[2].toInt() // Specify the year
                    val month = dateArr[1].toInt() // Specify the month (0-11, January is 0)
                    val dayOfMonth = dateArr[0].toInt() // Specify the day of the month

                    val calendar = Calendar.getInstance()
                    calendar.set(year, month, dayOfMonth)
                    val millis = calendar.timeInMillis

                    calendarView.setDate(millis, true, true)

                    sendNotification(artist.name, artist.concert)

                    Toast.makeText(requireContext(), artist.concert, Toast.LENGTH_SHORT).show()
                }
            })
        }
        /*val artists = artistsViewModel.artistList

        artistsConcertSearchBar = binding.artistsConcertsSearchBar
        artistsConcertSearchBar.addTextChangedListener(object : TextWatcher {

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
        })*/

        /*artistsRecyclerView = binding.calendarRecyclerView
        artistsRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        artistAdapter = ArtistAdapter(artists)
        artistsRecyclerView.adapter = artistAdapter

        artistAdapter.setOnItemClickListener(object : ArtistAdapter.OnItemClickListener {
            override fun onItemClick(position: Int, artist: Artist) {
                val dateArr = artist.concert.split("/")
                val year = dateArr[2].toInt() // Specify the year
                val month = dateArr[1].toInt() // Specify the month (0-11, January is 0)
                val dayOfMonth = dateArr[0].toInt() // Specify the day of the month

                val calendar = Calendar.getInstance()
                calendar.set(year, month, dayOfMonth)
                val millis = calendar.timeInMillis

                calendarView.setDate(millis, true, true)

                sendNotification(artist.name, artist.concert)

                Toast.makeText(requireContext(), artist.concert, Toast.LENGTH_SHORT).show()
            }
        })*/

        return root
    }

    private fun sendNotification(artistName: String, concertDate: String){
        val notificationManager = requireContext().getSystemService(NotificationManager::class.java)
        val notification = NotificationCompat.Builder(requireContext(), MyApp.CHANNEL_ID)
            .setContentTitle("Bellakittys")
            .setContentText("No olvides el concierto de ${artistName} el ${concertDate}")
            .setSmallIcon(R.mipmap.ic_launcher)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .build()
        notificationManager.notify(100, notification)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}