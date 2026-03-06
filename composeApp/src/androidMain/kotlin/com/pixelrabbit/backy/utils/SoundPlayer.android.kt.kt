package com.pixelrabbit.backy.utils

import android.media.AudioManager
import android.media.ToneGenerator

// SoundPlayer.android.kt
actual fun playBeep() {
    ToneGenerator(AudioManager.STREAM_NOTIFICATION, 100)
        .startTone(ToneGenerator.TONE_PROP_ACK, 150)
}
