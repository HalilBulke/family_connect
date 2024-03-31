package com.familyconnect.familyconnect.login

import com.google.gson.annotations.SerializedName

class LoginScreenPostItemBody (
    @SerializedName("email") val email:String?,
    @SerializedName("password") val password:String?,
)