package nl.jovmit.lyrics.app

import android.app.Application
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin

class TestApp : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin { }
    }

    override fun onTerminate() {
        stopKoin()
        super.onTerminate()
    }
}