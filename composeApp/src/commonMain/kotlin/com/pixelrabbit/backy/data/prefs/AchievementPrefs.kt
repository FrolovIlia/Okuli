package com.pixelrabbit.backy.data.prefs

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AchievementPrefs(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("achievement_prefs", Context.MODE_PRIVATE)

    suspend fun getShownAchievementIds(): Set<String> = withContext(Dispatchers.IO) {
        prefs.getStringSet("shown_achievements", emptySet()) ?: emptySet()
    }

    suspend fun markAchievementAsShown(achievementId: String) = withContext(Dispatchers.IO) {
        val current = prefs.getStringSet("shown_achievements", emptySet()) ?: emptySet()
        val newSet = current.toMutableSet().apply { add(achievementId) }
        prefs.edit { putStringSet("shown_achievements", newSet) }
    }
}