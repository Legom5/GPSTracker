package legom.gpstracker

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import legom.gpstracker.location.LocationModel

class MainViewModel: ViewModel() {
    val locationUpdates = MutableLiveData<LocationModel>()
    val timeData = MutableLiveData<String>()
}