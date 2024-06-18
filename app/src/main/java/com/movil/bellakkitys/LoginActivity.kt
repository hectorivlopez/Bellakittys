package com.movil.bellakkitys

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import com.movil.bellakkitys.data.model.Admin
import com.movil.bellakkitys.data.model.User

class LoginActivity : AppCompatActivity() {
    private lateinit var usernameTxt: EditText
    private lateinit var passwordTxt: EditText
    private lateinit var loginBtn: Button
    private lateinit var signUpBtn: Button

    private lateinit var user: User
    private lateinit var admin: Admin

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        usernameTxt = findViewById(R.id.usernameTxt)
        passwordTxt = findViewById(R.id.passwordTxt)
        loginBtn = findViewById(R.id.loginBtn)
        signUpBtn = findViewById(R.id.signUpBtn)

        loginBtn.setOnClickListener {
            login()
        }

        signUpBtn.setOnClickListener {
            redirectToSignUpActivity()
        }

        // Crear un usuario de prueba
        /*user = User(
            1,
            "user",
            "usuario@example.com",
            "12",
            listOf(),
            listOf(),
            "user"
        )*/

        // Crear un administrador de prueba
        admin = Admin(
            id = 1,
            username = "admin",
            email = "admin@example.com",
            password = "12",
            favoriteSongs = listOf(), // Reemplaza song1 y song2 con instancias reales de Song
            favoriteArtists = listOf(), // Reemplaza artist1 y artist2 con instancias reales de Artist
            rol = "admin",
            adminTitle = "Administrator",
            adminPermissions = listOf("Permission1", "Permission2"),
            adminLastLogin = System.currentTimeMillis(),
            adminTasksCompleted = 5,
            adminDepartment = "IT"
        )
        /*
                com.movil.bellakkitys.UserManager.addUser(user)

         */

        println(com.movil.bellakkitys.UserManager.userList)
    }

    private fun login() {
        /*if(usernameTxt.text.isBlank() || passwordTxt.text.isBlank()){
            Toast.makeText(this, "Complete todos los campos", Toast.LENGTH_SHORT).show()
        } else {
            val username = usernameTxt.text.toString()
            val password = passwordTxt.text.toString()

            if(username == "admin" && password == "12") {
                val intent = Intent(this, MainActivity::class.java)

                intent.putExtra("userId", admin.id)
                intent.putExtra("username", admin.username)
                intent.putExtra("email", admin.email)
                intent.putExtra("rol", admin.rol)

                startActivity(intent)
            }

            val foundUser = com.movil.bellakkitys.UserManager.findUserByUsername(username)

            if (foundUser != null && foundUser.password == password) {
                val intent = Intent(this, MainActivity::class.java)

                intent.putExtra("userId", foundUser.id)
                intent.putExtra("username", foundUser.username)
                intent.putExtra("email", foundUser.email)
                intent.putExtra("rol", foundUser.rol)

                startActivity(intent)
            } else {
                Toast.makeText(this, "Usuario no registrado o credenciales incorrectas", Toast.LENGTH_SHORT).show()
            }
        }*/
        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra("userId", 1)
        intent.putExtra("username", "hector")
        intent.putExtra("email", "hector@gmail.com")
        intent.putExtra("rol", "user")

        startActivity(intent)
    }

    private fun redirectToSignUpActivity() {
        val intent = Intent(this, SignUpActivity::class.java)
        startActivity(intent)
    }

}