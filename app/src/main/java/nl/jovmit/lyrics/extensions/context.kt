package nl.jovmit.lyrics.extensions

import android.content.Context
import androidx.core.content.ContextCompat

inline fun <reified T> Context.getSystemService() =
    ContextCompat.getSystemService(this, T::class.java)