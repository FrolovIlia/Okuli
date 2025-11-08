package com.pixelrabbit.oculi.presentation.screens

import androidx.compose.runtime.Composable
import com.pixelrabbit.oculi.utils.IosShareManager
import com.pixelrabbit.oculi.utils.ShareManager

@Composable
actual fun getShareManager(): ShareManager = IosShareManager()