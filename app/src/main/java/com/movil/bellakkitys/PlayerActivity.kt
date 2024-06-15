package com.movil.bellakkitys

import android.content.Intent
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.TextView

class PlayerActivity : AppCompatActivity() {
    // MediaPlayer
    private var mediaPlayer: MediaPlayer? = null
    private var song: Int? = null
    private var songActive = true

    // Elements
    private lateinit var playerSongImage: ImageView
    private lateinit var playerSongTitleLabel: TextView
    private lateinit var playerSongArtistLabel: TextView

    private lateinit var playerSongBar: SeekBar
    private lateinit var playerPlayBtn: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player)

        // ------------------------------ Get extras------------------------------
        var extras = intent.extras

        val songImage = extras?.getInt("songImage")
        val songTitle = extras?.getString("songTitle")
        val songArtist = extras?.getString("songArtist")
        val songFile = extras?.getInt("songFile")
        val progress = extras?.getInt("progress", 0)

        // ------------------------------ Player------------------------------
        mediaPlayer = MediaPlayerSingleton.getInstance()

        // ------------------------------ SongBar------------------------------
        playerSongBar = findViewById(R.id.playerSongBar)
        playerSongBar.max = mediaPlayer!!.duration
        if (progress != null) {
            playerSongBar.progress = progress
        }

        // Listener
        playerSongBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
            override fun onStartTrackingTouch(seekBar: SeekBar?) {}

            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                val max = mediaPlayer!!.duration / 1000
                val start = progress.toLong() / 1000

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
                    playerSongBar.progress = currentProgress

                    handler.postDelayed(this, 1000) // Actualiza cada segundo (1000 milisegundos)
                }
            }
        }, 1000)



        // ------------------------------ Elements data------------------------------
        playerSongImage = findViewById(R.id.playerSongImage)
        if (songImage != null) {
            playerSongImage.setImageResource(songImage)
        }

        playerSongTitleLabel = findViewById(R.id.playerSongTitleLabel)
        playerSongTitleLabel.text = songTitle

        playerSongArtistLabel = findViewById(R.id.playerSongArtistLabel)
        playerSongArtistLabel.text = songArtist

        song = songFile

        playerPlayBtn = findViewById(R.id.playerPlayBtn)
        playerPlayBtn.setOnClickListener {
            playPauseSong()
        }


    }

    // ------------------------------ Functions------------------------------
    fun playPauseSong() {
        if(songActive) {
            songActive = false
            mediaPlayer?.pause()
            playerPlayBtn.setImageResource(R.drawable.playpink)
        }
        else {
            songActive = true
            mediaPlayer?.start()
            playerPlayBtn.setImageResource(R.drawable.pausepink)
        }
    }
}