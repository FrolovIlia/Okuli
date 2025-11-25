package com.pixelrabbit.oculi.utils
//НЕ УДАЛЯТЬ! ВСё должно работать при запуске в XCode
import platform.AudioToolbox.AudioServicesPlaySystemSound

actual fun playBeep() {
    AudioServicesPlaySystemSound(1104)
}