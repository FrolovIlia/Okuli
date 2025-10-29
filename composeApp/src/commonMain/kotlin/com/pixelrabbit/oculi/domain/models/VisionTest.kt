package com.pixelrabbit.oculi.domain.models

data class VisionTest(
    val id: String,
    val type: TestType,
    val date: String,
    val result: TestResult,
    val notes: String? = null
)

enum class TestType {
    ACUITY,       // Острота зрения
    ASTIGMATISM,  // Астигматизм
    COLOR_BLIND   // Цветовосприятие
}

data class TestResult(
    val leftEye: Double? = null,
    val rightEye: Double? = null,
    val additionalData: Map<String, String> = emptyMap()
)