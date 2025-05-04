package legom.gpstracker.fragments

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.preference.PreferenceManager
import com.yandex.mobile.ads.banner.BannerAdEventListener
import com.yandex.mobile.ads.banner.BannerAdSize
import com.yandex.mobile.ads.banner.BannerAdView
import com.yandex.mobile.ads.common.AdError
import com.yandex.mobile.ads.common.AdRequest
import com.yandex.mobile.ads.common.AdRequestConfiguration
import com.yandex.mobile.ads.common.AdRequestError
import com.yandex.mobile.ads.common.ImpressionData
import com.yandex.mobile.ads.interstitial.InterstitialAdEventListener
import legom.gpstracker.MainApp
import legom.gpstracker.MainViewModel
import legom.gpstracker.R
import legom.gpstracker.databinding.ViewTrackBinding
import org.osmdroid.config.Configuration
import org.osmdroid.library.BuildConfig
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.Polyline
import kotlin.math.roundToInt

class ViewTrackFragment : Fragment() {

    private var startPoint: GeoPoint? = null

    private lateinit var binding: ViewTrackBinding

    private val model: MainViewModel by activityViewModels {
        MainViewModel.ViewModelFactory((requireContext().applicationContext as MainApp).database)
    }


    private var bannerAd: BannerAdView? = null
    private val adSize: BannerAdSize
        get() {
            // Calculate the width of the ad, taking into account the padding in the ad container.
            var adWidthPixels = binding.adContainerView.width
            if (adWidthPixels == 0) {
                // If the ad hasn't been laid out, default to the full screen width
                adWidthPixels = resources.displayMetrics.widthPixels
            }
            val adWidth = (adWidthPixels / resources.displayMetrics.density).roundToInt()

            return BannerAdSize.fixedSize(requireActivity(), adWidth, 50)
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
        binding.adContainerView.viewTreeObserver.addOnGlobalLayoutListener(object :
            ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                binding.adContainerView.viewTreeObserver.removeOnGlobalLayoutListener(this);
                bannerAd = loadBannerAd(adSize)
            }
        })

    }

    private fun loadBannerAd(adSize: BannerAdSize): BannerAdView {
        return binding.adContainerView.apply {
            setAdSize(adSize)
            setAdUnitId("demo-banner-yandex")
            setBannerAdEventListener(object : BannerAdEventListener {
                override fun onAdLoaded() {
                    // If this callback occurs after the activity is destroyed, you
                    // must call destroy and return or you may get a memory leak.
                    // Note `isDestroyed` is a method on Activity.

                }

                override fun onAdFailedToLoad(adRequestError: AdRequestError) {
                    // Ad failed to load with AdRequestError.
                    // Attempting to load a new ad from the onAdFailedToLoad() method is strongly discouraged.
                }

                override fun onAdClicked() {
                    // Called when a click is recorded for an ad.
                }

                override fun onLeftApplication() {
                    // Called when user is about to leave application (e.g., to go to the browser), as a result of clicking on the ad.
                }

                override fun onReturnedToApplication() {
                    // Called when user returned to application after click.
                }

                override fun onImpression(impressionData: ImpressionData?) {
                    // Called when an impression is recorded for an ad.
                }
            })
            loadAd(
                AdRequest.Builder()
                    // Methods in the AdRequest.Builder class can be used here to specify individual options settings.
                    .build()
            )
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
        map.setBuiltInZoomControls(false)
        fabZoomIn.setOnClickListener { map.controller.zoomIn() }
        fabZoomOut.setOnClickListener { map.controller.zoomOut() }
        map.setMultiTouchControls(true)
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