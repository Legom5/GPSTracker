package legom.gpstracker.fragments

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import legom.gpstracker.R

class SettingsFragment : PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.main_preference, rootKey)
    }
}