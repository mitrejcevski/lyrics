package nl.jovmit.lyrics.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
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
        setupActionBarWithNavController(navigationController)
        observeInfoViewModel()
    }

    private fun observeInfoViewModel() {
        infoViewModel.infoLiveData().listen(this) {
            when (it) {
                is InfoResult.Success -> layout.mainInfoView.displayInfo(it.message)
                is InfoResult.Error -> layout.mainInfoView.displayError(it.message)
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean = navigationController.navigateUp()
}