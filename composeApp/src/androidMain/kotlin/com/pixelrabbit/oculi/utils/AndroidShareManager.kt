package com.pixelrabbit.oculi.utils

import android.content.Context
import android.content.Intent

class AndroidShareManager(private val context: Context) : ShareManager {
    override fun shareText(text: String) {
        val shareIntent = Intent().apply {
            action = Intent.ACTION_SEND
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, text)
        }
        context.startActivity(Intent.createChooser(shareIntent, "Поделиться Oculi"))
    }
}