package com.familyconnect.familyconnect.login

import com.google.gson.annotations.SerializedName

class LoginScreenPostItemBody (
    @SerializedName("userName") val userName:String?,
    @SerializedName("password") val password:String?,
)