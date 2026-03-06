package com.pixelrabbit.backy.presentation.screens

import androidx.compose.runtime.Composable
import com.pixelrabbit.backy.utils.IosShareManager
import com.pixelrabbit.backy.utils.ShareManager

@Composable
actual fun getShareManager(): ShareManager = IosShareManager()