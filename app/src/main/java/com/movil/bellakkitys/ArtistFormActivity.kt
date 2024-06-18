package com.movil.bellakkitys

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import com.movil.bellakkitys.data.model.Artist

import com.movil.bellakkitys.data.model.Song

class ArtistFormActivity : AppCompatActivity() {
    private val sharedPreferencesManager by lazy { SharedPreferencesManager(this) }
    private lateinit var nameTxt: EditText
    private lateinit var descriptionTxt: EditText
    private lateinit var imageStringSpn: Spinner
    private lateinit var savedArtistList: ArrayList<Artist>
    private lateinit var savedSongList: ArrayList<Song>
    private lateinit var artist: Artist

    private lateinit var button: Button
    private lateinit var button2: Button

    private var rol = "user"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_artist_form)

        // ------------------------------ Get extras------------------------------
        var extras = intent.extras
        rol = extras?.getString("rol").toString()


        nameTxt = findViewById(R.id.nameTxt)
        descriptionTxt = findViewById(R.id.descriptionTxt)
        imageStringSpn = findViewById(R.id.imageStringSpn)

        savedArtistList = sharedPreferencesManager.getArtistList()
        savedSongList = sharedPreferencesManager.getSongList()

        val imageNames = arrayOf(
            "badgyal",
            "bellakath",
            "bogueto",
            "crismj",
            "daniflow",
            "elalfa",
            "jereklein",
            "jordan23",
            "jowellyrandy",
            "nengoflow",
            "standly",
            "tokischa",
            "uzielitomix",
            "yerimua"
        )

        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, imageNames)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        imageStringSpn.adapter = adapter

        button = findViewById(R.id.button)
        button2 = findViewById(R.id.button2)

        button.setOnClickListener {
            add()
        }

        button2.setOnClickListener {
            back()
        }
    }

    fun back() {
        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra("rol", rol)
        startActivity(intent)
        finish()
    }

    fun add(){
        if (nameTxt.text.isBlank() || descriptionTxt.text.isBlank()){
            Toast.makeText(this, "Llene todos los campos", Toast.LENGTH_SHORT).show()
        } else {
            artist = Artist(0,"0",0,"","", arrayListOf(), "")
            artist.id = savedArtistList.size
            artist.name = nameTxt.text.toString()
            artist.description = descriptionTxt.text.toString()
            artist.imageString = imageStringSpn.selectedItem.toString()
            val imageResourceId: Int = try {
                R.drawable::class.java.getField(artist.imageString).getInt(null)
            } catch (e: Exception) {
                // Manejar la excepción, por ejemplo, asignar un recurso predeterminado si no se encuentra el nombre de la imagen
                R.drawable.bellakath
            }
            artist.image = imageResourceId
            artist.discography = arrayListOf()
            artist.discography = arrayListOf()
            savedArtistList.add(artist)
            sharedPreferencesManager.saveArtistList(savedArtistList)

            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("rol", rol)
            startActivity(intent)
            finish()
        }

        /*savedArtistList.clear()

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


        sharedPreferencesManager.saveArtistList(savedArtistList)

        savedSongList.clear()

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

        sharedPreferencesManager.saveSongList(savedSongList)
        Toast.makeText(this, "Guardado", Toast.LENGTH_SHORT).show()
*/
    }

}