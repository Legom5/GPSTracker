package legom.gpstracker.fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.activityViewModels
import legom.gpstracker.MainApp
import legom.gpstracker.MainViewModel
import legom.gpstracker.R
import legom.gpstracker.databinding.FragmentMainBinding
import legom.gpstracker.databinding.ViewTrackBinding
import org.osmdroid.config.Configuration
import org.osmdroid.library.BuildConfig

class ViewTrackFragment : Fragment() {

    private lateinit var binding: ViewTrackBinding

    private val model: MainViewModel by activityViewModels{
        MainViewModel.ViewModelFactory((requireContext().applicationContext as MainApp).database)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        settingsOsm()
        binding = ViewTrackBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getTrack()
    }

    private fun getTrack() = with(binding){
        model.currentTrack.observe(viewLifecycleOwner){
            val distance = getString(R.string.distance_in_view_track, it.distance)
            tvDate.text = it.date
            tvTime.text = it.time
            tvAverageVel.text = it.velocity
            tvDistance.text = distance
        }
    }

    private fun settingsOsm() {
        Configuration.getInstance().load(
            activity as AppCompatActivity,
            activity?.getSharedPreferences("osm_pref", Context.MODE_PRIVATE)
        )
        Configuration.getInstance().userAgentValue = BuildConfig.APPLICATION_ID
    }

    companion object {
        @JvmStatic
        fun newInstance() = ViewTrackFragment()
    }
}