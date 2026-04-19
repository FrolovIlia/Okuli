package com.pixelrabbit.backy.payment

actual class PaymentController {
    actual fun startPayment(
        amount: String,
        description: String,
        onSuccess: (String) -> Unit,
        onFailure: (String) -> Unit
    ) {
        onFailure("iOS payment not implemented yet")
    }

    actual companion object {
        actual const val REQUEST_CODE_TOKENIZE = 12345
    }
}