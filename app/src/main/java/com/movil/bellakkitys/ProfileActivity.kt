package com.movil.bellakkitys

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider

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

    }

    private fun logout(){
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }

    private fun back(){
        finish()
    }

}