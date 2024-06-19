package com.movil.bellakkitys

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.movil.bellakkitys.data.model.Artist
import com.movil.bellakkitys.data.model.Song
import java.io.File
import java.io.FileOutputStream

class SongFormActivity : AppCompatActivity() {
    private lateinit var titleTxt: EditText
    private lateinit var artistTxt: EditText
    private lateinit var durationTxt: EditText
    private lateinit var audioLabel: TextView
    private lateinit var songImage: ImageView
    private lateinit var registerSongBtn: Button

    private lateinit var spinnerContainer: LinearLayout
    private var spinnerCount = 1
    private lateinit var spinners: ArrayList<Spinner>

    private lateinit var song: Song
    private lateinit var artistsList: List<Artist>
    private lateinit var artistsListStr: List<String>

    private var selectedImageUri: Uri? = null

    private val PICK_AUDIO_REQUEST = 1
    private var selectedAudioUri: Uri? = null

    private var rol = "user"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_song_form)

        Artist.all { artists ->
            artistsList = artists

            artistsListStr = artistsList.map { artist ->
                artist.name
            }

            titleTxt= findViewById(R.id.titleTxt)
            durationTxt = findViewById(R.id.durationTxt)
            audioLabel = findViewById(R.id.audioLabel)

            registerSongBtn = findViewById(R.id.registerSongBtn)
            registerSongBtn.setOnClickListener {
                add()
            }

            spinnerContainer = findViewById(R.id.spinnerContainer)

            spinners = ArrayList<Spinner>()

            addNewArtistSpinner()

            // Add button listener to add more spinners
            val addArtistBtn: Button = findViewById(R.id.addArtistBtn)
            addArtistBtn.setOnClickListener {
                addNewArtistSpinner()
            }

            // Añadir listener al botón para seleccionar un archivo de audio
            val pickAudioButton: Button = findViewById(R.id.pickAudioBtn)
            pickAudioButton.setOnClickListener {
                val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
                intent.addCategory(Intent.CATEGORY_OPENABLE)
                intent.type = "audio/*"
                startActivityForResult(intent, PICK_AUDIO_REQUEST)
            }
        }

        songImage = findViewById(R.id.songImage)

        songImage.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                if (ContextCompat.checkSelfPermission(
                        this,
                        Manifest.permission.READ_MEDIA_IMAGES
                    ) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(
                        this,
                        Manifest.permission.CAMERA
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    ActivityCompat.requestPermissions(
                        this,
                        arrayOf(Manifest.permission.READ_MEDIA_IMAGES, Manifest.permission.CAMERA),
                        SongFormActivity.REQUEST_CODE_PERMISSIONS
                    )
                } else {
                    showImagePickerOptions()
                }
            } else {
                if (ContextCompat.checkSelfPermission(
                        this,
                        Manifest.permission.READ_EXTERNAL_STORAGE
                    ) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(
                        this,
                        Manifest.permission.CAMERA
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    ActivityCompat.requestPermissions(
                        this,
                        arrayOf(
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.CAMERA
                        ),
                        SongFormActivity.REQUEST_CODE_PERMISSIONS
                    )
                } else {
                    showImagePickerOptions()
                }
            }


        }


    }


    private fun addNewArtistSpinner() {
        spinnerCount++

        // Inflate the custom spinner layout
        val newSpinner = LayoutInflater.from(this).inflate(R.layout.custom_spinner, null) as Spinner
        newSpinner.id = View.generateViewId()

        // Set layout parameters for the new spinner
        val params = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
        params.topMargin = 25
        newSpinner.layoutParams = params

        // Set adapter for the new spinner
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, artistsListStr)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        newSpinner.adapter = adapter

        // Add the new spinner to the layout
        spinnerContainer.addView(newSpinner)

        spinners.add(newSpinner)
    }




    fun back() {
        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra("rol", rol)
        finish()
        startActivity(intent)
    }

    fun add() {
        if (titleTxt.text.isBlank() || durationTxt.text.isBlank()) {
            Toast.makeText(this, "Llene todos los campos", Toast.LENGTH_SHORT).show()
        } else {
            val songArtists = mutableListOf<Artist>()
            for(spinner in spinners) {
                val artistName = spinner.selectedItem.toString()
                Artist.find("name", artistName) {artist ->
                    songArtists.add(artist)
                }
            }

            song = Song(
                "",
                titleTxt.text.toString(),
                songArtists,
                selectedImageUri.toString(),
                durationTxt.text.toString(),
                selectedAudioUri.toString()
            )

            song.add()

            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }


    private fun showImagePickerOptions() {
        val pickIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        pickIntent.type = "image/*"

        val takePhotoIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

        val chooserIntent = Intent.createChooser(pickIntent, "Select or take a new Picture")
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, arrayOf(takePhotoIntent))

        startActivityForResult(chooserIntent, REQUEST_CODE_IMAGE_PICKER)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                REQUEST_CODE_IMAGE_PICKER -> {
                    val isCamera = data == null || data.data == null
                    if (isCamera) {
                        val photo: Bitmap = data?.extras?.get("data") as Bitmap
                        songImage.setImageBitmap(photo)
                        selectedImageUri = saveImageToExternalStorage(photo)
                    } else {
                        selectedImageUri = data?.data

                        songImage.setImageURI(selectedImageUri)
                    }
                }
                PICK_AUDIO_REQUEST -> {
                    selectedAudioUri = data?.data

                    audioLabel.text = selectedAudioUri.toString()
                }
            }
        }
    }

    fun getFileName(filePath: String): String? {
        val file = java.io.File(filePath)
        if (!file.exists()) {
            return null
        }

        val wholeName = file.name
        val pointIndex = wholeName.lastIndexOf('.')

        val name = if (pointIndex > 0) {
            wholeName.substring(0, pointIndex)
        } else {
            wholeName
        }

        val extension = if (pointIndex > 0 && pointIndex < wholeName.length - 1) {
            wholeName.substring(pointIndex + 1)
        } else {
            ""
        }

        return name + extension
    }

    private fun saveImageToExternalStorage(bitmap: Bitmap): Uri {
        val imageFileName = "JPEG_${System.currentTimeMillis()}.jpg"
        val storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val imageFile = File(storageDir, imageFileName)
        val fos = FileOutputStream(imageFile)
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos)
        fos.close()
        return Uri.fromFile(imageFile)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (grantResults.isNotEmpty() && grantResults.all { it == PackageManager.PERMISSION_GRANTED }) {
                showImagePickerOptions()
            } else {
                println("Permisos denegados")
            }
        }
    }

    companion object {
        private const val REQUEST_CODE_PERMISSIONS = 101
        private const val REQUEST_CODE_IMAGE_PICKER = 102
    }
}