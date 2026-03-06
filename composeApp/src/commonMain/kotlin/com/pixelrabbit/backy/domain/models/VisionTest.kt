package com.pixelrabbit.backy.domain.models

data class VisionTest(
    val id: String,
    val type: TestType,
    val date: String,
    val result: TestResult,
    val notes: String? = null
)

enum class TestType {
    ACUITY,
    ASTIGMATISM,
    COLOR_BLIND
}

data class TestResult(
    val leftEye: Double? = null,
    val rightEye: Double? = null,
    val additionalData: Map<String, String> = emptyMap()
)