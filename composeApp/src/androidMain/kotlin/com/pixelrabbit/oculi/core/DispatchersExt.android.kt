package com.pixelrabbit.oculi.core

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

actual val AppIODispatcher: CoroutineDispatcher = Dispatchers.IO
