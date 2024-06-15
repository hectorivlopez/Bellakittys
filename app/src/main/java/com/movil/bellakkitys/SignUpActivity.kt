package com.movil.bellakkitys

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

class SignUpActivity : AppCompatActivity() {

    private lateinit var usernameTxt: EditText
    private lateinit var emailTxt: EditText
    private lateinit var passwordTxt: EditText
    private lateinit var confirmPasswordTxt: EditText
    private lateinit var registerBtn: Button

    private lateinit var user: User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        usernameTxt = findViewById(R.id.usernameTxt)
        emailTxt = findViewById(R.id.emailTxt)
        passwordTxt = findViewById(R.id.passwordTxt)
        confirmPasswordTxt = findViewById(R.id.confirmPasswordTxt)
        registerBtn = findViewById(R.id.registerBtn)

        registerBtn.setOnClickListener {
            register()
        }
    }

    private fun register() {
        if(usernameTxt.text.isBlank() || passwordTxt.text.isBlank() || emailTxt.text.isBlank() || confirmPasswordTxt.text.isBlank()){
            Toast.makeText(this, "Llene todos los campos", Toast.LENGTH_SHORT).show()
        } else {
            if (!passwordTxt.text.toString().equals(confirmPasswordTxt.text.toString())) {
                Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show()
            } else {
                val newUser = User (
                    1,
                    usernameTxt.text.toString(),
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
        }
    }
}