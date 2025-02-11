package legom.gpstracker.fragments

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.preference.PreferenceManager
import legom.gpstracker.MainApp
import legom.gpstracker.MainViewModel
import legom.gpstracker.R
import legom.gpstracker.databinding.ViewTrackBinding
import org.osmdroid.config.Configuration
import org.osmdroid.library.BuildConfig
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.Polyline

class ViewTrackFragment : Fragment() {

    private var startPoint: GeoPoint? = null

    private lateinit var binding: ViewTrackBinding

    private val model: MainViewModel by activityViewModels {
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
        binding.fCenter.setOnClickListener {
            if (startPoint != null) binding.map.controller.animateTo(startPoint)
        }
    }

    private fun getTrack() = with(binding) {
        model.currentTrack.observe(viewLifecycleOwner) {
            val distance = getString(R.string.distance_in_view_track, it.distance)
            tvDate.text = it.date
            tvTime.text = it.time
            tvAverageVel.text = it.velocity
            tvDistance.text = distance
            val polyLine = getPolyLine(it.geoPoints)
            map.overlays.add(polyLine)
            setMarkers(polyLine.actualPoints)
            goToStartPosition(polyLine.actualPoints[0])
            startPoint = polyLine.actualPoints[0]
        }
    }

    private fun goToStartPosition(startPosition: GeoPoint) {
        binding.map.controller.zoomTo(15.0)
        binding.map.controller.animateTo(startPosition)
    }

    private fun setMarkers(list: List<GeoPoint>) = with(binding) {
        val startMarker = Marker(map)
        val finishMarker = Marker(map)
        startMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
        finishMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
        startMarker.icon = getDrawable(requireContext(), R.drawable.ic_start)
        finishMarker.icon = getDrawable(requireContext(), R.drawable.ic_finish)
        startMarker.position = list[0]
        finishMarker.position = list[list.size - 1]
        map.overlays.add(startMarker)
        map.overlays.add(finishMarker)
    }

    private fun getPolyLine(geoPoints: String): Polyline {
        val polyLine = Polyline()
        polyLine.outlinePaint.color = Color.parseColor(
            PreferenceManager.getDefaultSharedPreferences(requireContext())
                .getString("color_key", "#DE0000")
        )
        val list = geoPoints.split("/")
        list.forEach {
            if (it.isEmpty()) return@forEach
            val points = it.split(",")
            polyLine.addPoint(GeoPoint(points[0].toDouble(), points[1].toDouble()))
        }
        return polyLine
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