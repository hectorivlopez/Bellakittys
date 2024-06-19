package com.movil.bellakkitys

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.movil.bellakkitys.data.model.Artist
import java.io.File
import java.io.FileOutputStream

class ArtistFormActivity : AppCompatActivity() {
    private lateinit var artistNameTxt: EditText
    private lateinit var descriptionTxt: EditText
    private lateinit var artistConcertTxt: EditText
    private lateinit var artistImage: ImageView

    private lateinit var createArtistBtn: Button
    private lateinit var backBtn: ImageButton

    private var selectedImageUri: Uri? = null
    private var artistId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_artist_form)

        artistNameTxt = findViewById(R.id.artistNameTxt)
        descriptionTxt = findViewById(R.id.descriptionTxt)
        artistConcertTxt = findViewById(R.id.artistConcertTxt)
        artistImage = findViewById(R.id.artistImage)
        createArtistBtn = findViewById(R.id.createArtistBtn)
        backBtn = findViewById(R.id.backBtn)

        artistId = intent.getStringExtra("ARTIST_ID")

        if (artistId != null) {
            loadArtistData(artistId!!)
            createArtistBtn.text = "Guardar cambios"
            createArtistBtn.setOnClickListener { update() }
        } else {
            createArtistBtn.text = "Crear artista"
            createArtistBtn.setOnClickListener { add() }
        }

        backBtn.setOnClickListener {
            back()
        }

        artistImage.setOnClickListener {
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
                        REQUEST_CODE_PERMISSIONS
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
                        arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA),
                        REQUEST_CODE_PERMISSIONS
                    )
                } else {
                    showImagePickerOptions()
                }
            }
        }
    }

    private fun loadArtistData(artistId: String) {
        Artist.findById(artistId) { artist ->
            if (artist != null) {
                artistNameTxt.setText(artist.name)
                descriptionTxt.setText(artist.description)
                selectedImageUri = Uri.parse(artist.imageUrl)
                artistImage.setImageURI(selectedImageUri)
                artistConcertTxt.setText(artist.concert)
            }
        }
    }

    fun back() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    fun add() {
        if (artistNameTxt.text.isEmpty() || descriptionTxt.text.isEmpty() || selectedImageUri == null) {
            Toast.makeText(this, "Llene todos los campos", Toast.LENGTH_SHORT).show()
        } else {
            val artist = Artist(
                "",
                artistNameTxt.text.toString(),
                selectedImageUri.toString(),
                descriptionTxt.text.toString(),
                artistConcertTxt.text.toString()
            )
            artist.add()

            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun update() {
        if (artistId != null && artistNameTxt.text.isNotEmpty() && descriptionTxt.text.isNotEmpty() && selectedImageUri != null) {
            val artist = Artist(
                artistId!!,
                artistNameTxt.text.toString(),
                selectedImageUri.toString(),
                descriptionTxt.text.toString(),
                artistConcertTxt.text.toString()
            )
            artist.update()

            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        } else {
            Toast.makeText(this, "Llene todos los campos", Toast.LENGTH_SHORT).show()
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
                        artistImage.setImageBitmap(photo)
                        selectedImageUri = saveImageToExternalStorage(photo)
                    } else {
                        selectedImageUri = data?.data
                        Toast.makeText(this, selectedImageUri.toString(), Toast.LENGTH_SHORT).show()
                        artistImage.setImageURI(selectedImageUri)
                    }
                }
            }
        }
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