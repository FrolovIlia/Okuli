package com.pixelrabbit.backy.payment

object PaymentResultHandler {
    var onTokenReceived: ((String) -> Unit)? = null
    var onCancelled: (() -> Unit)? = null
    var on3DSCompleted: ((Boolean) -> Unit)? = null
}