package com.pixelrabbit.backy.presentation.screens

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.pixelrabbit.backy.utils.AndroidShareManager
import com.pixelrabbit.backy.utils.ShareManager

@Composable
actual fun getShareManager(): ShareManager {
    val context = LocalContext.current
    return AndroidShareManager(context)
}