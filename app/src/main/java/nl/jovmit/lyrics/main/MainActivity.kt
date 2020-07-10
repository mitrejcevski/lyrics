package nl.jovmit.lyrics.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavDestination
import androidx.navigation.findNavController
import androidx.navigation.ui.setupActionBarWithNavController
import nl.jovmit.lyrics.R
import nl.jovmit.lyrics.databinding.ActivityMainBinding
import nl.jovmit.lyrics.extensions.listen
import nl.jovmit.lyrics.main.data.result.InfoResult
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlin.LazyThreadSafetyMode.NONE

class MainActivity : AppCompatActivity() {

    private val infoViewModel by viewModel<InfoViewModel>()
    private val navigationController by lazy(NONE) {
        findNavController(R.id.mainNavigationController)
    }
    private lateinit var layout: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        layout = ActivityMainBinding.inflate(layoutInflater)
        setContentView(layout.root)
        setupToolbar()
        observeInfoViewModel()
        performLoggedInUserCheck()
    }

    private fun setupToolbar() {
        setupActionBarWithNavController(navigationController)
        navigationController.addOnDestinationChangedListener { _, destination, _ ->
            setupToolbarBackButtonBasedOn(destination)
        }
    }

    private fun setupToolbarBackButtonBasedOn(destination: NavDestination) {
        val showToolbarBackButton = when {
            destination.isRegistration() -> false
            destination.isSongsOverview() -> false
            else -> true
        }
        supportActionBar?.setDisplayHomeAsUpEnabled(showToolbarBackButton)
    }

    private fun observeInfoViewModel() {
        infoViewModel.infoLiveData().listen(this) {
            when (it) {
                is InfoResult.Success -> layout.mainInfoView.displayInfo(it.message)
                is InfoResult.Error -> layout.mainInfoView.displayError(it.message)
            }
        }
    }

    private fun performLoggedInUserCheck() {
//        navigationController.navigate(R.id.songsOverview)
    }

    override fun onSupportNavigateUp(): Boolean = navigationController.navigateUp()

    override fun onBackPressed() {
        if (navigationController.currentDestination?.isSongsOverview() == true) {
            finish()
        } else {
            super.onBackPressed()
        }
    }

    private fun NavDestination.isRegistration() = id == R.id.register

    private fun NavDestination.isSongsOverview() = id == R.id.songsOverview
}