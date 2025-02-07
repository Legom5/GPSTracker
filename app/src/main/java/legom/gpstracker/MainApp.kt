package legom.gpstracker

import android.app.Application
import legom.gpstracker.db.MainDb

class MainApp : Application() {
    val database by lazy {
        MainDb.getDatabase(this)
    }
}