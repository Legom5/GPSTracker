package legom.gpstracker

import android.app.Application
import io.appmetrica.analytics.AppMetrica
import io.appmetrica.analytics.AppMetricaConfig
import legom.gpstracker.db.MainDb

class MainApp : Application() {
    val database by lazy {
        MainDb.getDatabase(this)
    }

    override fun onCreate() {
        super.onCreate()
        val config = AppMetricaConfig
            .newConfigBuilder("ac1b4c1b-1f09-49f1-9832-b5d37bde9533")
            .build()
        AppMetrica.activate(this, config)
    }

}