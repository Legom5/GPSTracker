package legom.gpstracker.fragments

import android.app.AlertDialog
import android.app.Dialog
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.DialogFragment

class RatingDialogFragment : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return AlertDialog.Builder(requireContext())
            .setTitle("Оцените приложение")
            .setMessage("Понравилось приложение? Помогите нам стать лучше - оцените в RuStore!")
            .setPositiveButton("Оценить в RuStore") { _, _ ->
                openRuStore()
            }
            .setNeutralButton("Позже") { _, _ ->

            }
            .setNegativeButton("Не предлагать") { _, _ ->
                saveNeverShowRating()
            }
            .create()
    }

    private fun openRuStore() {

        val packageName = requireContext().packageName
        val webUrl = "https://apps.rustore.ru/app/$packageName"

        try {
            val intent = Intent(
                Intent.ACTION_VIEW, Uri.parse(webUrl)
            ).apply {
                setPackage("ru.rustore.app")
            }
            startActivity(intent)
        } catch (e: ActivityNotFoundException) {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(webUrl)))
        }
    }

    private fun saveNeverShowRating() {
        val prefs = requireContext()
            .getSharedPreferences("app_rating", Context.MODE_PRIVATE)
        prefs.edit().putBoolean("never_show_rating", true).apply()
    }

}