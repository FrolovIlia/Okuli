package com.pixelrabbit.oculi.presentation.screens

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.pixelrabbit.oculi.utils.AndroidShareManager
import com.pixelrabbit.oculi.utils.ShareManager

@Composable
actual fun getShareManager(): ShareManager {
    val context = LocalContext.current
    return AndroidShareManager(context)
}