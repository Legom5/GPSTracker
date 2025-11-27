package legom.gpstracker.fragments

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import legom.gpstracker.R

class CustomRatingDialogFragment : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = Dialog(requireContext())
        val view =
            LayoutInflater.from(requireContext()).inflate(R.layout.dialog_rating_custom, null)

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(view)
        dialog.setCancelable(true)

        // ФИКС: Устанавливаем правильные размеры
        dialog.window?.setLayout(
            resources.getDimensionPixelSize(R.dimen.dialog_width), // 300dp
            WindowManager.LayoutParams.WRAP_CONTENT
        )


        // Скругленные углы диалога
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

        // Находим кнопки
        view.findViewById<View>(R.id.btnRate).setOnClickListener {
            openRuStore()
            dismiss()
        }

        view.findViewById<View>(R.id.btnLater).setOnClickListener {
            dismiss()
        }

        view.findViewById<View>(R.id.btnNever).setOnClickListener {
            saveNeverShowRating()
            dismiss()
        }

        return dialog
    }

    override fun onStart() {
        super.onStart()

        // Анимация появления
        dialog?.window?.setWindowAnimations(R.style.DialogAnimation)
    }

    private fun openRuStore() {
        val packageName = requireContext().packageName
        val webUrl = "https://apps.rustore.ru/app/$packageName"

        try {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(webUrl)).apply {
                setPackage("ru.rustore.app")
            }
            startActivity(intent)
        } catch (e: Exception) {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(webUrl)))
        }
    }

    private fun saveNeverShowRating() {
        val prefs = requireContext()
            .getSharedPreferences("app_rating", Context.MODE_PRIVATE)
        prefs.edit().putBoolean("never_show_rating", true).apply()
    }
}