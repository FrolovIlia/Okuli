package com.pixelrabbit.backy.core

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

actual val AppIODispatcher: CoroutineDispatcher = Dispatchers.IO
