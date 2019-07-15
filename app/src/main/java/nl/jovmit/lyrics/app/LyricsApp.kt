package nl.jovmit.lyrics.app

import android.app.Application
import nl.jovmit.lyrics.main.mainModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class LyricsApp : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@LyricsApp)
            modules(mainModule)
        }
    }
}