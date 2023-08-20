package nl.jovmit.lyrics.main.overview

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import nl.jovmit.lyrics.R
import nl.jovmit.lyrics.databinding.FragmentSongsOverviewBinding
import nl.jovmit.lyrics.extensions.applyDefaultColors
import nl.jovmit.lyrics.extensions.listen
import nl.jovmit.lyrics.extensions.onCollapse
import nl.jovmit.lyrics.extensions.onQuerySubmit
import nl.jovmit.lyrics.extensions.setupWithLinearLayoutManager
import nl.jovmit.lyrics.main.InfoViewModel
import nl.jovmit.lyrics.main.data.result.SongsResult
import nl.jovmit.lyrics.main.data.song.Song
import nl.jovmit.lyrics.main.preferences.UserPreferencesViewModel
import org.koin.androidx.viewmodel.ext.android.activityViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class SongsOverview : Fragment() {

    private val songsAdapter: SongsAdapter by lazy { SongsAdapter() }
    private val songsViewModel by viewModel<SongsViewModel>()
    private val infoViewModel by activityViewModel<InfoViewModel>()
    private val userPreferencesViewModel by viewModel<UserPreferencesViewModel>()

    private lateinit var layout: FragmentSongsOverviewBinding

    private val menuProvider = object : MenuProvider {
        override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
            menuInflater.inflate(R.menu.songs_overview_menu, menu)
            val searchMenuItem = menu.findItem(R.id.actionSearch)
            val searchView = searchMenuItem.actionView as SearchView
            searchView.onQuerySubmit { songsViewModel.search(it) }
            searchMenuItem.onCollapse { songsViewModel.fetchSongs() }
        }

        override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
            if (menuItem.itemId == R.id.actionLogout) {
                performLogout()
                return true
            }
            return false
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        layout = FragmentSongsOverviewBinding.inflate(inflater)
        return layout.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        requireActivity().addMenuProvider(menuProvider, viewLifecycleOwner)
        layout.songsOverviewSwipeRefresh.applyDefaultColors()
        layout.songsOverviewSwipeRefresh.setOnRefreshListener { fetchSongs() }
        layout.songsOverviewNewSongButton.setOnClickListener { openNewSong() }
        setupRecyclerView()
        observeSongsLiveData()
        fetchSongs()
    }

    private fun performLogout() {
        userPreferencesViewModel.clearLoggedInUser()
        val destination = SongsOverviewDirections.openRegistration()
        findNavController().navigate(destination)
    }

    private fun openNewSong() {
        findNavController().navigate(R.id.openNewSong)
    }

    private fun fetchSongs() {
        songsViewModel.fetchSongs()
    }

    private fun setupRecyclerView() {
        layout.songsOverviewRecycler.setupWithLinearLayoutManager()
        layout.songsOverviewRecycler.adapter = songsAdapter
        songsAdapter.onItemClickListener = { openSongDetails(it) }
    }

    private fun openSongDetails(song: Song) {
        val direction = SongsOverviewDirections.openSongDetails(song.songId.value)
        findNavController().navigate(direction)
    }

    private fun observeSongsLiveData() {
        songsViewModel.songsLiveData().listen(viewLifecycleOwner) {
            when (it) {
                is SongsResult.Loading -> displayLoading(it.loading)
                is SongsResult.Fetched -> displaySongs(it.songs)
                is SongsResult.FetchingError -> displayFetchingError()
                is SongsResult.SearchError -> displaySearchingError()
            }
        }
    }

    private fun displayLoading(loading: Boolean) {
        layout.songsOverviewSwipeRefresh.isRefreshing = loading
    }

    private fun displaySongs(songs: List<Song>) {
        updateEmptyStatePreview(songs.isEmpty())
        songsAdapter.addSongs(songs)
    }

    private fun updateEmptyStatePreview(isVisible: Boolean) {
        val emptyStateVisibility = if (isVisible) View.VISIBLE else View.GONE
        layout.songsOverviewEmptyStateLabel.visibility = emptyStateVisibility
    }

    private fun displayFetchingError() {
        val errorMessage = getString(R.string.errorFetchingSongs)
        infoViewModel.showError(errorMessage)
    }

    private fun displaySearchingError() {
        val errorMessage = getString(R.string.errorSearchingSong)
        infoViewModel.showError(errorMessage)
    }
}
