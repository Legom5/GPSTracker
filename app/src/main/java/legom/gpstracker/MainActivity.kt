package legom.gpstracker

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import legom.gpstracker.databinding.ActivityMainBinding
import legom.gpstracker.fragments.MainFragment
import legom.gpstracker.fragments.SettingsFragment
import legom.gpstracker.fragments.TracksFragment
import legom.gpstracker.utils.openFragment

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        onButtonNavClicks()
        openFragment(MainFragment.newInstance(), "main")
    }


    private fun onButtonNavClicks() {
        binding.bNav.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.id_home -> openFragment(MainFragment.newInstance(), "main")
                R.id.id_tracks -> openFragment(TracksFragment.newInstance(), "tracks")
                R.id.id_settings -> openFragment(SettingsFragment(), "settings")
            }
            true
        }
    }
}