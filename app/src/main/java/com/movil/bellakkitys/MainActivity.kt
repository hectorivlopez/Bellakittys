package com.movil.bellakkitys

import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.TextView
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.movil.bellakkitys.databinding.ActivityMainBinding
import com.movil.bellakkitys.data.model.Artist
import com.movil.bellakkitys.ui.artists.ArtistsViewModel
import com.movil.bellakkitys.ui.concerts.ConcertsViewModel
import com.movil.bellakkitys.data.model.Song
import com.movil.bellakkitys.ui.songs.SongsViewModel

class MainActivity : AppCompatActivity(), MediaPlayerPreparedListener {
    private val sharedPreferencesManager by lazy { SharedPreferencesManager(this) }
    private lateinit var binding: ActivityMainBinding

    // MediaPlayer
    private var mediaPlayer: MediaPlayer? = null
    private var currentSong: Song? = null
    private var songActive = false

    // Elements
    private lateinit var miniplayerContainer: CardView
    private lateinit var miniplayer: ConstraintLayout
    private lateinit var miniplayerSongTitleLabel: TextView
    private lateinit var miniplayerSongArtistLabel: TextView
    private lateinit var miniplayerSongImage: ImageView
    private lateinit var miniplayerPlayBtn: ImageButton
    private lateinit var miniplayerSongBar: SeekBar

    // Shared view models
    private lateinit var artistsViewModel: ArtistsViewModel
    private lateinit var songsViewModel: SongsViewModel
    private lateinit var concertsViewModel: ConcertsViewModel
    private lateinit var playerViewModel: PlayerViewModel


    private var currentSongTitle: TextView? = null
    private var rol = "user"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // ------------------------------ Bottom Navigation ------------------------------
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_artists, R.id.navigation_songs, R.id.navigation_concerts
            )
        )
        //setupActionBarWithNavController(navController, appBarConfiguration)

        navView.setupWithNavController(navController)
        navView.elevation = 0F

        // ------------------------------ Share View Models initialization ------------------------------
        songsViewModel = ViewModelProvider(this).get(SongsViewModel::class.java)
        artistsViewModel = ViewModelProvider(this).get(ArtistsViewModel::class.java)
        concertsViewModel = ViewModelProvider(this).get(ConcertsViewModel::class.java)
        playerViewModel = ViewModelProvider(this).get(PlayerViewModel::class.java)



        // ------------------------------ Get extras------------------------------
        var extras = intent.extras


        if (rol != null) {
            artistsViewModel.rol = rol
            songsViewModel.rol = rol
            concertsViewModel.rol = rol
        }

        /*val savedArtistList = sharedPreferencesManager.getArtistList()
        if (savedArtistList != null){
            artistsViewModel.artistList = savedArtistList
        } else {
            artistsViewModel.artistList = ArrayList<Artist>()
        }*/
        sharedPreferencesManager.getArtistList().clear()
        sharedPreferencesManager.getSongList().clear()

        val savedArtistList = ArrayList<Artist>()
        savedArtistList.add(Artist(0, "Bad Gyal", R.drawable.badgyal, "badgyal", "Descripción de Badgyal", ArrayList(), "15/09/2023"))
        savedArtistList.add(Artist(1, "Bellakath", R.drawable.bellakath, "bellakath", "Descripción de Bellakath", ArrayList(), "28/03/2024"))
        savedArtistList.add(Artist(2, "Cris Mj", R.drawable.crismj, "crismj", "Descripción de Cris Mj", ArrayList(), "10/11/2023"))
        savedArtistList.add(Artist(3, "Dani Flow", R.drawable.daniflow, "daniflow", "Descripción de Dany Flow", ArrayList(), "04/07/2024"))
        savedArtistList.add(Artist(4, "El Alfa", R.drawable.elalfa, "elalfa", "Descripción de El Alfa", ArrayList(), "19/01/2024"))
        savedArtistList.add(Artist(5, "El Bogueto", R.drawable.bogueto, "bogueto", "Descripción de El Bogueto", ArrayList(), "22/10/2023"))
        savedArtistList.add(Artist(6, "Jere Klein", R.drawable.jereklein, "jereklein", "Descripción de Jere Klein", ArrayList(), "07/06/2024"))
        savedArtistList.add(Artist(7, "Jordan 23", R.drawable.jordan23, "jordan23", "Descripción de Jordan 23", ArrayList(), "30/12/2023"))
        savedArtistList.add(Artist(8, "Jowell & Randy", R.drawable.jowellyrandy, "jowellyrandy", "Descripción de Jowell & Randy", ArrayList(), "12/08/2024"))
        savedArtistList.add(Artist(9, "Standly", R.drawable.standly, "standly", "Descripción de Standly", ArrayList(), "25/04/2024"))
        savedArtistList.add(Artist(10, "Tokischa", R.drawable.tokischa, "tokischa", "Descripción de Tokischa", ArrayList(), "03/10/2023"))
        savedArtistList.add(Artist(11, "Uzielito Mix", R.drawable.uzielitomix, "uzielitomix", "Descripción de Uzielito Mix", ArrayList(), "14/02/2024"))
        savedArtistList.add(Artist(12, "Yeri Mua", R.drawable.yerimua, "yerimua", "Descripción de Yeri Mua", ArrayList(), "09/05/2024"))
        savedArtistList.add(Artist(13, "Ñengo Flow", R.drawable.nengoflow, "nengoflow", "Descripción de Ñengo Flow", ArrayList(), "18/11/2023"))

        artistsViewModel.artistList = savedArtistList


        /*val savedSongList = sharedPreferencesManager.getSongList()
        if (savedSongList != null){
            songsViewModel.songList = savedSongList
        } else {
            songsViewModel.songList = ArrayList<Song>()
        }*/
        val savedSongList = ArrayList<Song>()
        savedSongList.add(Song(0, "Gatita", "Bellakath", R.drawable.gatita, "2:33", R.raw.gatita))
        savedSongList.add(Song(1, "Reggaeton Champagne", "Bellakath, Dani Flow", R.drawable.bellakath, "2:39", R.raw.reggaetonchampagne))
        savedSongList.add(Song(2, "Otro Show", "Uzielito Mix, El Bogueto, Dani Flow", R.drawable.otroshow, "2:40", R.raw.otro_show))
        savedSongList.add(Song(3, "Martillazo", "Uzielito Mix, El Bogueto, Dani Flow", R.drawable.martillazo, "2:38", R.raw.martillazo))
        savedSongList.add(Song(4, "Que Rollito Primavera", "Dani Flow", R.drawable.querollitoprimavera, "3:12", R.raw.que_rollito_primavera))
        savedSongList.add(Song(5, "Las Que No Tienen Papá", "Dani Flow", R.drawable.lasquenotienenpapa, "3:18", R.raw.las_que_no_tienen_papa))
        savedSongList.add(Song(6, "Abre Las Patotas", "Dani Flow", R.drawable.abrelaspatotas, "2:49", R.raw.abre_las_patotas))
        savedSongList.add(Song(7, "Y Yo Me Le Pego", "Bellakath, Profeta Yao Yao, Smi-Lee", R.drawable.yyomelepego, "2:52", R.raw.y_yo_me_le_pego))
        savedSongList.add(Song(8, "Chulo pt.2", "Bad Gyal, Tokischa, Young Miko", R.drawable.chulo2, "3:39", R.raw.chulo_pt2))
        savedSongList.add(Song(9, "Blin Blin", "Bad Gyal, Juanka", R.drawable.blinblin, "2:24", R.raw.blin_blin))
        savedSongList.add(Song(10, "Enamórate", "Bad Gyal, Nicki Nicole", R.drawable.enamorate, "2:27", R.raw.enamorate))
        savedSongList.add(Song(11, "Flow 2000 - Remix", "Bad Gyal, Beny Jr", R.drawable.flow2000, "2:48", R.raw.flow_2000))
        savedSongList.add(Song(12, "Una Noche En Medellin", "Cris Mj", R.drawable.unanocheenmedellin, "2:34", R.raw.una_noche_en_medellin))
        savedSongList.add(Song(13, "Booty Booty", "Cris Mj", R.drawable.bootybooty, "2:17", R.raw.booty_booty))
        savedSongList.add(Song(14, "Dime Tú", "Cris Mj, Pailita, Big Cvyu", R.drawable.dimetu, "3:46", R.raw.dime_tu))
        savedSongList.add(Song(15, "Marisola - Remix", "Cris Mj, Standly, Nicki Nicole, Duki", R.drawable.marisola, "3:57", R.raw.marisola))
        savedSongList.add(Song(16, "Los Malvekes", "Cris Mj, Marcianeke, Simon la Letra", R.drawable.losmalvekes, "3:38", R.raw.los_malvekes))
        savedSongList.add(Song(17, "Plebada", "El Alfa, Peso Pluma", R.drawable.plebada, "3:41", R.raw.plebada))
        savedSongList.add(Song(18, "Muñekita", "El Alfa, Kali Uchis, JT", R.drawable.munekita, "3:39", R.raw.munekita))
        savedSongList.add(Song(19, "Dembow y Reggaeton", "El Alfa, Yandel, Myke Towers", R.drawable.dembowyreggaeton, "4:08", R.raw.dembow_y_reggaeton))
        savedSongList.add(Song(20, "4K", "El Alfa, Darell, Noriel", R.drawable.cuatrok, "3:21", R.raw.cuatro_k))
        savedSongList.add(Song(21, "La Mamá de la Mamá", "El Alfa, El Cherry Scom, CJ", R.drawable.lamamadelamama, "3:39", R.raw.la_mama_de_la_mama))
        savedSongList.add(Song(22, "G Low Kitty", "El Bogueto, Uzielito Mix, El Malilla", R.drawable.glowkitty, "3:34", R.raw.g_low_kitty))
        savedSongList.add(Song(23, "Piripituchy", "El Bogueto, Uzielito Mix", R.drawable.piripituchy, "3:00", R.raw.piripituchy))
        savedSongList.add(Song(24, "Dale Bogueto", "El Bogueto, Uzielito Mix, DJ Antena", R.drawable.bogueto, "3:07", R.raw.dale_bogueto))
        savedSongList.add(Song(25, "Jordan Flight", "El Bogueto, Uzielito Mix, Tensec", R.drawable.jordanflight, "2:39", R.raw.jordan_flight))
        savedSongList.add(Song(26, "Espantan - Remix", "Uzielito Mix, El Bogueto, Alnz G, Tensec, Dani Flow", R.drawable.espantan, "3:38", R.raw.espantan))
        savedSongList.add(Song(27, "Ando", "Jere Klein, Gittobeatz", R.drawable.ando, "2:52", R.raw.ando))
        savedSongList.add(Song(28, "X ESO BB", "Jere Klein, Nicki Nicole", R.drawable.xesobb, "3:13", R.raw.x_eso_bb))
        savedSongList.add(Song(29, "2 Hielos", "Jere Klein, King Savagge", R.drawable.doshielos, "3:14", R.raw.dos_hielos))
        savedSongList.add(Song(30, "Mandame Tu Ubi ", "Jere Klein, Lucky Brown", R.drawable.mandametuubi, "1:59", R.raw.mandame_tu_ubi))
        savedSongList.add(Song(31, "Bailando", "Jordan 23, Standly", R.drawable.bailando, "3:44", R.raw.bailando))
        savedSongList.add(Song(32, "PERREA KTM", "Jordan 23, Julianno Sosa, Endo, Noiss", R.drawable.perreaktm, "3:42", R.raw.perrea_ktm))
        savedSongList.add(Song(33, "SHERATON", "Jordan 23", R.drawable.sheraton, "2:22", R.raw.sheraton))
        savedSongList.add(Song(34, "BELLAKITA", "Jordan 23", R.drawable.bellakita, "2:12", R.raw.bellakita))
        savedSongList.add(Song(35, "MAMI", "Jordan 23", R.drawable.mami, "2:41", R.raw.mami))
        savedSongList.add(Song(36, "ID", "Jowell & Randy, Young Miko", R.drawable.id, "3:55", R.raw.id))
        savedSongList.add(Song(37, "Safaera", "Jowell & Randy, Ñengo Flow, Bad Bunny", R.drawable.safaera, "4:55", R.raw.safaera))
        savedSongList.add(Song(38, "Bonita", "Jowell & Randy, J Balvin", R.drawable.bonita, "4:25", R.raw.bonita))
        savedSongList.add(Song(39, "Hoy Se Chicha", "Jowell & Randy", R.drawable.hoysechicha, "2:34 ", R.raw.hoy_se_chicha))
        savedSongList.add(Song(40, "Guadalupe", "Jowell & Randy", R.drawable.guadalupe, "4:03", R.raw.guadalupe))
        savedSongList.add(Song(41, "TORO", "Jowell & Randy", R.drawable.toro, "4:14", R.raw.toro))
        savedSongList.add(Song(42, "Pégate", "Standly", R.drawable.pegate, "3:06", R.raw.pegate))
        savedSongList.add(Song(43, "Mi Gata", "Standly, El Barto", R.drawable.migata, "3:22", R.raw.mi_gata))
        savedSongList.add(Song(44, "Delincuente", "Tokischa, Anuel AA, Ñengo Flow", R.drawable.delincuente, "3:46", R.raw.delincuente))
        savedSongList.add(Song(45, "Perra", "Tokischa, J Balvin", R.drawable.perra, "2:38", R.raw.perra))
        savedSongList.add(Song(46, "Linda", "Tokischa, Rosalía", R.drawable.linda, "2:24", R.raw.linda))
        savedSongList.add(Song(47, "Chupón", "Yeri Mua, El Gudi, Jey F, Alan Dazmel, Oviña", R.drawable.chupon, "3:21", R.raw.chupon))
        savedSongList.add(Song(48, "Con To'", "Yeri Mua, Andre Noriega, Oviña, Flow Nasty", R.drawable.conto, "2:12", R.raw.conto))
        savedSongList.add(Song(49, "Dónde Estás", "Yeri Mua", R.drawable.dondeestas, "1:32", R.raw.donde_estas))
        savedSongList.add(Song(50, "Cuando Me Dirá", "Ñengo Flow", R.drawable.cuandomedira, "2:29", R.raw.cuando_me_dira))
        savedSongList.add(Song(51, "Yo Sé Que Tú Quieres", "Ñengo Flow, Sammy & Falsetto", R.drawable.yosequetuquieres, "3:28", R.raw.yo_se_que_tu_quieres))
        savedSongList.add(Song(52, "La Prision", "Ñengo Flow", R.drawable.laprision, "3:17", R.raw.la_prision))

        songsViewModel.songList = savedSongList




        // ------------------------------ Mini player ------------------------------
        mediaPlayer = MediaPlayerSingleton.getInstance()

        miniplayerContainer = findViewById(R.id.miniplayerContainer)
        miniplayer = findViewById(R.id.miniplayer)
        miniplayerSongImage = findViewById(R.id.miniplayerSongImage)
        miniplayerSongTitleLabel = findViewById(R.id.miniplayerSongTitleLabel)
        miniplayerSongArtistLabel = findViewById(R.id.miniplayerSongArtistLabel)
        miniplayerPlayBtn = findViewById(R.id.miniplayerPlayBtn)

        if(!songActive) {
            miniplayerContainer.visibility = View.GONE
        }
        else {
            miniplayerContainer.visibility = View.VISIBLE
        }

        miniplayer.setOnClickListener{
            val intent = Intent(this, PlayerActivity::class.java)
            intent.putExtra("songTitle", currentSong?.title)
            intent.putExtra("songArtist", currentSong?.artist)
            currentSong?.let { it1 -> intent.putExtra("songImage", it1.image) }
            intent.putExtra("progress", miniplayerSongBar.progress)
            startActivity(intent)
        }

        miniplayerPlayBtn.setOnClickListener{
            playPauseSong()
        }

        // ------------------------------ SongBar------------------------------
        miniplayerSongBar = findViewById(R.id.coco)
       /* miniplayerSongBar.max = 25000*/

        // Listener
        miniplayerSongBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
            override fun onStartTrackingTouch(seekBar: SeekBar?) {}

            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {

                Log.d("Progress miniplayer", "Si")
                if (fromUser) {
                    mediaPlayer!!.pause()
                    mediaPlayer!!.seekTo(progress)
                    mediaPlayer!!.start()
                }
            }
        })

        // Handler
        val handler = Handler()
        handler.postDelayed(object : Runnable {
            override fun run() {
                if (mediaPlayer != null) {
                    val currentProgress = mediaPlayer!!.currentPosition
                    miniplayerSongBar.progress = currentProgress

                    Log.d("Progressr", currentProgress.toString())


                    handler.postDelayed(this, 1000) // Actualiza cada segundo (1000 milisegundos)
                }
            }
        }, 1000)


    }

    private val handler = Handler()
    private val updateSeekBarRunnable = object : Runnable {
        override fun run() {
            if (mediaPlayer != null ) {

                Log.d("Halayer", "Siuo")
                miniplayerSongBar.progress = mediaPlayer!!.currentPosition
                handler.postDelayed(this, 1000)
            }
        }
    }

    override fun onMediaPlayerPrepared(duration: Int) {
        miniplayerSongBar.max = duration
    }


    // ------------------------------ Functions------------------------------
    fun playSong(song: Song, songTitle: TextView) {
        currentSong?.active = false
        song.active = true
        currentSongTitle?.setTextColor(ContextCompat.getColor(this, R.color.white))
        songTitle.setTextColor(ContextCompat.getColor(this, R.color.accentColor))
        currentSongTitle = songTitle

        MediaPlayerSingleton.stop()
        mediaPlayer?.reset()

        MediaPlayerSingleton.play(applicationContext, song.file, this)
        // wait
        //miniplayerSongBar.max = mediaPlayer!!.duration

       // handler.post(updateSeekBarRunnable)


        currentSong = song
        miniplayerPlayBtn.setImageResource(R.drawable.pause)

        miniplayerSongTitleLabel.text = song.title
        miniplayerSongArtistLabel.text = song.artist
        miniplayerSongImage.setImageResource(song.image)

        if(miniplayerContainer.visibility != View.VISIBLE) {
            songActive = true
            miniplayerContainer.visibility = View.VISIBLE
        }

    }

    fun playPauseSong() {
        if(songActive) {
            songActive = false
            mediaPlayer?.pause()
            miniplayerPlayBtn.setImageResource(R.drawable.play)
        }
        else {
            songActive = true
            mediaPlayer?.start()
            miniplayerPlayBtn.setImageResource(R.drawable.pause)
        }
    }


    fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().replace(R.id.fragment_artists_container, fragment).commit()
    }


    fun addSong() {
        val intent = Intent(this, SongFormActivity::class.java)
        intent.putExtra("rol", rol)
        finish()
        startActivity(intent)
    }

    fun addArtist() {
        val intent = Intent(this, ArtistFormActivity::class.java)
        intent.putExtra("rol", rol)
        finish()
        startActivity(intent)
    }

}