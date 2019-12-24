package nl.jovmit.lyrics.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.setupActionBarWithNavController
import nl.jovmit.lyrics.R
import nl.jovmit.lyrics.databinding.ActivityMainBinding
import kotlin.LazyThreadSafetyMode.NONE

class MainActivity : AppCompatActivity() {

    private val navigationController by lazy(NONE) {
        findNavController(R.id.mainNavigationController)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupActionBarWithNavController(navigationController)
    }

    override fun onSupportNavigateUp(): Boolean = navigationController.navigateUp()
}