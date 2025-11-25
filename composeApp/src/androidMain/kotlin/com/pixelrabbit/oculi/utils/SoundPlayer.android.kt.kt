package com.pixelrabbit.oculi.utils

import android.media.AudioManager
import android.media.ToneGenerator

// SoundPlayer.android.kt
actual fun playBeep() {
    ToneGenerator(AudioManager.STREAM_NOTIFICATION, 100)
        .startTone(ToneGenerator.TONE_PROP_BEEP, 150)
}
