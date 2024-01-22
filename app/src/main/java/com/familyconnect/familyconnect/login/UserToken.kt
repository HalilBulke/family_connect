package com.familyconnect.familyconnect.login

object UserToken {
    private var userToken: String = ""

    fun saveToken(token: String) {
        userToken = token
    }

    fun getToken(): String {
        return userToken
    }
}