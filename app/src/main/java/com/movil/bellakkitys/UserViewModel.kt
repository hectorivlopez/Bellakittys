package com.movil.bellakkitys

import androidx.lifecycle.ViewModel

class UserViewModel: ViewModel() {
    var userId: Int = -1
    var username: String = ""
    var email: String = ""
    var rol: String = "user"

}