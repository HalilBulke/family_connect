package com.familyconnect.familyconnect.register

import com.google.gson.annotations.SerializedName

data class RegisterScreenPostItemBody(
    @SerializedName("email") val email:String?,
    @SerializedName("password") val password:String?,
    @SerializedName("name") val name:String?,
    @SerializedName("role") val role:String?,
)