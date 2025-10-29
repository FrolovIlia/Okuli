package com.pixelrabbit.oculi.domain.repositories

import com.pixelrabbit.oculi.domain.models.TestType
import com.pixelrabbit.oculi.domain.models.VisionTest

interface VisionTestRepository {
    suspend fun saveTestResult(test: VisionTest)
    suspend fun getTestHistory(): List<VisionTest>
    suspend fun getLatestTest(type: TestType): VisionTest?
}