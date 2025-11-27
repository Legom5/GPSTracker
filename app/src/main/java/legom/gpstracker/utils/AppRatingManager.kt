package legom.gpstracker.utils

import android.content.Context
import android.content.SharedPreferences

class AppRatingManager(private val context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences("app_rating", Context.MODE_PRIVATE)

    fun incrementLaunchCount() {
        val currentCount = prefs.getInt("launch_count", 0)
        prefs.edit().putInt("launch_count", currentCount + 1).apply()
    }

    fun shouldShowRating(): Boolean {
        if (prefs.getBoolean("never_show_rating", false)) {
            return false
        }

        val launchCount = prefs.getInt("launch_count", 0)
        val lastPrompt = prefs.getLong("last_prompt", 0)
        val currentTime = System.currentTimeMillis()

        return launchCount >= 5 &&
                (currentTime - lastPrompt) > 7 * 24 * 60 * 60 * 1000
    }

    fun setRatingPromptShown() {
        prefs.edit().putLong("last_prompt", System.currentTimeMillis()).apply()
    }

    fun setNeverShowRating() {
        prefs.edit().putBoolean("never_show_rating", true).apply()
    }
}