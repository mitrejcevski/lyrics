package nl.jovmit.lyrics

import android.app.Application
import android.content.Context
import androidx.test.runner.AndroidJUnitRunner
import nl.jovmit.lyrics.app.TestApp

@SuppressWarnings("unused")
class TestAppRunner : AndroidJUnitRunner() {

    override fun newApplication(
        loader: ClassLoader?,
        className: String?,
        context: Context?
    ): Application {
        return super.newApplication(loader, TestApp::class.java.name, context)
    }
}