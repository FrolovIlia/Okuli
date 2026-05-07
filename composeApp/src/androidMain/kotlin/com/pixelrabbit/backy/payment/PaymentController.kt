package com.pixelrabbit.backy.payment

import android.app.Activity
import android.util.Log
import androidx.activity.ComponentActivity
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.io.IOException
import java.util.*
import ru.yoomoney.sdk.kassa.payments.Checkout
import ru.yoomoney.sdk.kassa.payments.checkoutParameters.Amount
import ru.yoomoney.sdk.kassa.payments.checkoutParameters.PaymentMethodType
import ru.yoomoney.sdk.kassa.payments.checkoutParameters.PaymentParameters
import ru.yoomoney.sdk.kassa.payments.checkoutParameters.SavePaymentMethod
import ru.yoomoney.sdk.kassa.payments.ui.color.ColorScheme

actual class PaymentController {

    private lateinit var activity: Activity
    private lateinit var onSuccessCallback: (String) -> Unit
    private lateinit var onFailureCallback: (String) -> Unit

    private val client = OkHttpClient()

    private val TAG = "PaymentController"

    // ========== ДАННЫЕ ДЛЯ МАГАЗИНА ==========
    private val shopId = "368429"                                      // ID магазина
    private val secretKey = "live_-UQn7qmMiOQQCOntg9njEvy1w1CalDwZnLoF2m177_g"                                   // СЕКРЕТНЫЙ ключ (для API)
    private val clientApplicationKey = "live_MzY4NDI5w03wj0VlTBj1iajKVLO10lme8fpvmzeUaXI"                       // Ключ для SDK

    fun setActivity(activity: Activity) {
        this.activity = activity
        Log.d(TAG, "Activity set: $activity")
    }

    actual fun startPayment(
        amount: String,
        description: String,
        onSuccess: (String) -> Unit,
        onFailure: (String) -> Unit
    ) {
        Log.d(TAG, "startPayment called with amount: $amount, description: $description")

        this.onSuccessCallback = onSuccess
        this.onFailureCallback = onFailure

        PaymentResultHandler.onTokenReceived = { token ->
            Log.d(TAG, "Token received via handler: $token")
            createPaymentWithToken(token)
        }
        PaymentResultHandler.onCancelled = {
            Log.d(TAG, "Payment cancelled via handler")
            onPaymentCancelled()
        }
        PaymentResultHandler.on3DSCompleted = { success ->
            Log.d(TAG, "3DS completed via handler: $success")
            if (success) {
                onSuccessCallback("3ds_success")
            } else {
                onFailureCallback("Платёж не подтверждён")
            }
        }

        try {
            val paymentParameters = PaymentParameters(
                amount = Amount(amount.toBigDecimal(), Currency.getInstance("RUB")),
                title = "Отключение рекламы",
                subtitle = description,
                clientApplicationKey = clientApplicationKey,
                shopId = shopId,
                savePaymentMethod = SavePaymentMethod.OFF,
                paymentMethodTypes = setOf(
                    PaymentMethodType.BANK_CARD,
                    PaymentMethodType.GOOGLE_PAY,
                    PaymentMethodType.SBP
                )
            )

            Log.d(TAG, "Creating tokenize intent")
            val intent = Checkout.createTokenizeIntent(activity, paymentParameters)
            Log.d(TAG, "Intent created, starting activity for result")
            (activity as ComponentActivity).startActivityForResult(intent, REQUEST_CODE_TOKENIZE)
            Log.d(TAG, "startActivityForResult called successfully")
        } catch (e: Exception) {
            Log.e(TAG, "Error in startPayment", e)
            onFailure("Ошибка запуска оплаты: ${e.message}")
        }
    }

    private fun createPaymentWithToken(token: String) {
        Log.d(TAG, "Creating payment with token: $token")

        val json = JSONObject().apply {
            put("payment_token", token)
            put("amount", JSONObject().apply {
                put("value", "99.00")
                put("currency", "RUB")
            })
            put("capture", true)
            put("description", "Отключение рекламы в Oculi")
        }

        val request = Request.Builder()
            .url("https://api.yookassa.ru/v3/payments")
            .addHeader("Content-Type", "application/json")
            .addHeader("Idempotence-Key", UUID.randomUUID().toString())
            .addHeader("Authorization", Credentials.basic(shopId, secretKey))
            .post(json.toString().toRequestBody("application/json".toMediaType()))
            .build()

        Log.d(TAG, "Sending request to YooKassa API")

        client.newCall(request).enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {
                val responseBody = response.body?.string() ?: "{}"
                val jsonResponse = JSONObject(responseBody)

                Log.d(TAG, "Response code: ${response.code}")
                Log.d(TAG, "Response body: $responseBody")

                when {
                    response.isSuccessful -> {
                        val status = jsonResponse.optString("status")
                        Log.d(TAG, "Payment status: $status")

                        when (status) {
                            "succeeded" -> {
                                val paymentId = jsonResponse.optString("id")
                                Log.d(TAG, "Payment succeeded with id: $paymentId")
                                activity.runOnUiThread {
                                    onSuccessCallback(paymentId)
                                }
                            }
                            "pending" -> {
                                val confirmationUrl = jsonResponse.optJSONObject("confirmation")?.optString("confirmation_url")
                                val paymentMethodType = jsonResponse.optJSONObject("payment_method")?.optString("type")

                                Log.d(TAG, "Payment pending, confirmation URL: $confirmationUrl")
                                Log.d(TAG, "Payment method type: $paymentMethodType")

                                if (!confirmationUrl.isNullOrEmpty() && paymentMethodType != null) {
                                    activity.runOnUiThread {
                                        // ИСПОЛЬЗУЕМ ПРАВИЛЬНЫЙ МЕТОД createConfirmationIntent
                                        show3DSWithSDK(confirmationUrl, paymentMethodType)
                                    }
                                } else {
                                    Log.e(TAG, "Missing confirmation URL or payment method type")
                                    activity.runOnUiThread {
                                        onFailureCallback("Требуется подтверждение, но нет confirmation_url")
                                    }
                                }
                            }
                            else -> {
                                Log.e(TAG, "Unknown payment status: $status")
                                activity.runOnUiThread {
                                    onFailureCallback("Статус платежа: $status")
                                }
                            }
                        }
                    }
                    else -> {
                        val errorMsg = jsonResponse.optJSONObject("error")?.optString("description")
                            ?: "Ошибка API: ${response.code}"
                        Log.e(TAG, "API error: $errorMsg")
                        activity.runOnUiThread {
                            onFailureCallback(errorMsg)
                        }
                    }
                }
            }

            override fun onFailure(call: Call, e: IOException) {
                Log.e(TAG, "Network error", e)
                activity.runOnUiThread {
                    onFailureCallback(e.message ?: "Сетевая ошибка")
                }
            }
        })
    }

    private fun show3DSWithSDK(confirmationUrl: String, paymentMethodTypeStr: String) {
        Log.d(TAG, "Using SDK createConfirmationIntent with URL: $confirmationUrl, type: $paymentMethodTypeStr")

        try {
            // Преобразуем строку в PaymentMethodType
            val paymentMethodType = when (paymentMethodTypeStr) {
                "bank_card" -> PaymentMethodType.BANK_CARD
                "sberbank" -> PaymentMethodType.SBERBANK
                "sbp" -> PaymentMethodType.SBP
                "google_pay" -> PaymentMethodType.GOOGLE_PAY
                else -> PaymentMethodType.BANK_CARD
            }

            val intent = Checkout.createConfirmationIntent(
                context = activity,
                confirmationUrl = confirmationUrl,
                paymentMethodType = paymentMethodType,
                clientApplicationKey = clientApplicationKey,
                shopId = shopId,
                colorScheme = ColorScheme.getDefaultScheme()  // или ColorScheme.LIGHT / ColorScheme.DARK
            )

            (activity as ComponentActivity).startActivityForResult(intent, REQUEST_CODE_3DS)
        } catch (e: Exception) {
            Log.e(TAG, "Error starting 3DS intent", e)
            onFailureCallback("Ошибка запуска подтверждения платежа: ${e.message}")
        }
    }

    private fun onPaymentCancelled() {
        Log.d(TAG, "Payment cancelled")
        onFailureCallback("Платёж отменён")
    }

    actual companion object {
        actual const val REQUEST_CODE_TOKENIZE = 12345
        actual const val REQUEST_CODE_3DS = 12346
    }
}