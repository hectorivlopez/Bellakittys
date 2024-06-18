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
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                    REQUEST_CODE_PERMISSIONS
                )
            } else {
                openGalleryForImage()
            }
        }
    }

    private fun openGalleryForImage() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, REQUEST_CODE_SELECT_IMAGE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_SELECT_IMAGE && resultCode == Activity.RESULT_OK) {
            val selectedImageUri: Uri? = data?.data
            profileImg.setImageURI(selectedImageUri)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openGalleryForImage()
            } else {
                println("Permiso denegado")
            }
        }
    }

    companion object {
        private const val REQUEST_CODE_PERMISSIONS = 101
        private const val REQUEST_CODE_SELECT_IMAGE = 100
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