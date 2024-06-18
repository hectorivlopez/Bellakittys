package com.movil.bellakkitys

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.movil.bellakkitys.data.firebase.FirebaseManager
import com.movil.bellakkitys.data.auth.User
import com.movil.bellakkitys.data.auth.UserManager
import com.movil.bellakkitys.data.auth.Auth

class SignUpActivity : AppCompatActivity() {

    private lateinit var nameTxt: EditText
    private lateinit var emailTxt: EditText
    private lateinit var passwordTxt: EditText
    private lateinit var confirmPasswordTxt: EditText
    private lateinit var registerBtn: Button

    private lateinit var firebaseManager: FirebaseManager

    private lateinit var user: User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        firebaseManager = FirebaseManager()

        nameTxt = findViewById(R.id.nameTxt)
        emailTxt = findViewById(R.id.emailTxt)
        passwordTxt = findViewById(R.id.passwordTxt)
        confirmPasswordTxt = findViewById(R.id.confirmPasswordTxt)
        registerBtn = findViewById(R.id.registerBtn)

        registerBtn.setOnClickListener {
            if (!nameTxt.text.isEmpty() && !emailTxt.text.isEmpty() && !passwordTxt.text.isEmpty()) {
                register()
            } else {
                Toast.makeText(
                    this,
                    "Campos vacíos",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun register() {
        val name = nameTxt.text.toString()
        val email = emailTxt.text.toString()
        val password = passwordTxt.text.toString()

        Auth.signUp(name, email, password) { result ->
            if (result.isSuccess) {
                val user = result.getOrNull()
                if (user != null) {
                    UserManager.setUser(user, this)

                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                }
            } else {
                Toast.makeText(
                    this,
                    "Signup failed: ${result.exceptionOrNull()?.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        /*if(nameTxt.text.isBlank() || passwordTxt.text.isBlank() || emailTxt.text.isBlank() || confirmPasswordTxt.text.isBlank()){
            Toast.makeText(this, "Llene todos los campos", Toast.LENGTH_SHORT).show()
        } else {
            if (!passwordTxt.text.toString().equals(confirmPasswordTxt.text.toString())) {
                Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show()
            } else {
                val newUser = User (
                    1,
                    nameTxt.text.toString(),
                    emailTxt.text.toString(),
                    passwordTxt.text.toString(),
                    listOf(),
                    listOf(),
                    "user"
                )

                UserManager.addUser(newUser)

                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
            }
        }*/
    }
}