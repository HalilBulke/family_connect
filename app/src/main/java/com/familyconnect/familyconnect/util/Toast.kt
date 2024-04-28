package com.familyconnect.familyconnect.util

import android.content.Context
import android.os.Build
import android.widget.Toast


// Function to generate a Toast
fun makeToast(context: Context, text: String){
    Toast.makeText(context, text, Toast.LENGTH_LONG).show()
}