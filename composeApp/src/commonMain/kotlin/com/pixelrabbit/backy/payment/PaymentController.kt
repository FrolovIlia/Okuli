package com.pixelrabbit.backy.payment

expect class PaymentController() {
    fun startPayment(
        amount: String,
        description: String,
        onSuccess: (String) -> Unit,
        onFailure: (String) -> Unit
    )

    companion object {
        val REQUEST_CODE_TOKENIZE: Int
        val REQUEST_CODE_3DS: Int
    }
}