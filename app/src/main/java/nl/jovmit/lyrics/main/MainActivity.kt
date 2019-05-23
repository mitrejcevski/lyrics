package nl.jovmit.lyrics.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.setupActionBarWithNavController
import nl.jovmit.lyrics.R
import kotlin.LazyThreadSafetyMode.NONE

class MainActivity : AppCompatActivity() {

    private val navigationController by lazy(NONE) {
        findNavController(R.id.mainNavigationController)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupActionBarWithNavController(navigationController)
    }

    override fun onSupportNavigateUp(): Boolean = navigationController.navigateUp()
}