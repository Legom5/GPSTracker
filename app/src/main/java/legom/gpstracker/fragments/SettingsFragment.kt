package legom.gpstracker.fragments

import android.os.Bundle
import androidx.preference.Preference
import androidx.preference.Preference.OnPreferenceChangeListener
import androidx.preference.PreferenceFragmentCompat
import legom.gpstracker.R
import legom.gpstracker.utils.showToast

class SettingsFragment : PreferenceFragmentCompat() {

    private lateinit var timePref: Preference

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.main_preference, rootKey)
        init()
    }

    private fun init() {
        timePref = findPreference("update_time_key") ?: throw RuntimeException("Unknown key")
        val changeListener = onChangeListener()
        timePref.onPreferenceChangeListener = changeListener
        initPrefs()
    }

    private fun onChangeListener(): OnPreferenceChangeListener {
        return Preference.OnPreferenceChangeListener { pref, newValue ->
            val nameArray = resources.getStringArray(R.array.loc_time_update_name)
            val valueArray = resources.getStringArray(R.array.loc_time_update_value)
            val title = pref.title.toString().substringBefore(":")
            pref.title = "$title: ${nameArray[valueArray.indexOf(newValue)]}"
            true
        }
    }

    private fun initPrefs() {
        val pref = timePref.preferenceManager.sharedPreferences
        val nameArray = resources.getStringArray(R.array.loc_time_update_name)
        val valueArray = resources.getStringArray(R.array.loc_time_update_value)
        val title = timePref.title
        timePref.title =
            "$title: ${nameArray[valueArray.indexOf(pref?.getString("update_time_key", "3000"))]}"
    }

}
