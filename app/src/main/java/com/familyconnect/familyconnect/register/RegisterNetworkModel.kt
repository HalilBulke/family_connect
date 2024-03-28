package com.familyconnect.familyconnect.register

import com.google.gson.annotations.SerializedName

data class RegisterScreenPostItemBody(
    @SerializedName("userName") val userName:String?,
    @SerializedName("password") val password:String?,
)