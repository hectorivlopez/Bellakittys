package com.movil.bellakkitys

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import android.Manifest
import android.graphics.Bitmap
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModelProvider
import com.movil.bellakkitys.data.firebase.FirebaseManager

class ProfileActivity : AppCompatActivity() {
    private lateinit var imgBtnBack: ImageButton
    private lateinit var profileImg: ImageView
    private lateinit var username: TextView
    private lateinit var email: TextView
    private lateinit var logoutBtn: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        imgBtnBack = findViewById(R.id.imgBtnBack)
        profileImg = findViewById(R.id.profileImage)
        username = findViewById(R.id.usernameProfileTxt)
        email = findViewById(R.id.emailProfileTxt)
        logoutBtn = findViewById(R.id.logoutBtn)
        imgBtnBack.setOnClickListener { back() }
        logoutBtn.setOnClickListener { logout() }


        profileImg.setOnClickListener {
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
                        profileImg.setImageBitmap(photo)
                    } else {
                        val selectedImageUri: Uri? = data?.data
                        profileImg.setImageURI(selectedImageUri)
                    }
                }
            }
        }
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

    private fun logout(){
        val firebaseManager = FirebaseManager()
        firebaseManager.logout()

        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun back(){
        finish()
    }

}