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
import android.os.Environment
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModelProvider
import com.movil.bellakkitys.data.auth.Auth
import com.movil.bellakkitys.data.auth.User
import com.movil.bellakkitys.data.firebase.FirebaseManager
import com.squareup.picasso.Picasso
import java.io.File
import java.io.FileOutputStream

class ProfileActivity : AppCompatActivity() {
    private lateinit var imgBtnBack: ImageButton
    private lateinit var profileImg: ImageView
    private lateinit var username: TextView
    private lateinit var email: TextView
    private lateinit var logoutBtn: Button

    private var selectedImageUri: Uri? = null

    private lateinit var currentUser: User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        val firebaseManager = FirebaseManager()

        Auth.currentUser {user ->
            if (user != null) {
                currentUser = user
                if (!user.imageUrl.isNullOrEmpty()) {
                    firebaseManager.loadImage(user.imageUrl!!, profileImg){}
                }
            }
        }

        imgBtnBack = findViewById(R.id.imgBtnBack)
        profileImg = findViewById(R.id.profileImage)
        username = findViewById(R.id.usernameProfileTxt)
        email = findViewById(R.id.emailProfileTxt)
        logoutBtn = findViewById(R.id.logoutBtn)

        imgBtnBack.setOnClickListener { back() }
        logoutBtn.setOnClickListener { logout() }



        /*if(currentUser.imageUrl?.isEmpty() == false) {
            Picasso.get()
                .load(currentUser.imageUrl)
                .into(profileImg)
        }*/

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

    private fun updateUser() {
        Auth.currentUser { user ->
            if(user != null) {
                user.imageUrl = selectedImageUri.toString()
                user.update()
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
                        selectedImageUri = saveImageToExternalStorage(photo)
                    } else {
                        selectedImageUri = data?.data
                        profileImg.setImageURI(selectedImageUri)
                    }
                    updateUser()
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