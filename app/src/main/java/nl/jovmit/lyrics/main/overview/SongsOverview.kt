package nl.jovmit.lyrics.main.overview

import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import nl.jovmit.lyrics.R
import nl.jovmit.lyrics.databinding.FragmentSongsOverviewBinding
import nl.jovmit.lyrics.extensions.applyDefaultColors
import nl.jovmit.lyrics.extensions.listen
import nl.jovmit.lyrics.extensions.setupWithLinearLayoutManager
import nl.jovmit.lyrics.main.InfoViewModel
import nl.jovmit.lyrics.main.data.result.SongsResult
import nl.jovmit.lyrics.main.data.song.Song
import nl.jovmit.lyrics.main.details.SongDetails
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class SongsOverview : Fragment() {

    private val songsAdapter: SongsAdapter by lazy { SongsAdapter() }
    private val songsViewModel by viewModel<SongsViewModel>()
    private val infoViewModel by sharedViewModel<InfoViewModel>()

    private lateinit var layout: FragmentSongsOverviewBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        layout = FragmentSongsOverviewBinding.inflate(inflater)
        return layout.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        layout.songsOverviewSwipeRefresh.applyDefaultColors()
        layout.songsOverviewSwipeRefresh.setOnRefreshListener { fetchSongs() }
        layout.songsOverviewNewSongButton.setOnClickListener { openNewSong() }
        setupRecyclerView()
        observeSongsLiveData()
        fetchSongs()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.songs_overview_menu, menu)
        val searchMenuItem = menu.findItem(R.id.actionSearch)
        val searchView = searchMenuItem.actionView as SearchView
        searchView.setOnQueryTextListener(onSearchQueryListener)
        searchMenuItem.setOnActionExpandListener(onCollapsedListener)
    }

    private val onCollapsedListener = object : MenuItem.OnActionExpandListener {
        override fun onMenuItemActionExpand(item: MenuItem?): Boolean {
            return true
        }

        override fun onMenuItemActionCollapse(item: MenuItem?): Boolean {
            songsViewModel.fetchSongs()
            return true
        }
    }

    private val onSearchQueryListener = object : SearchView.OnQueryTextListener {
        override fun onQueryTextSubmit(query: String?): Boolean {
            query?.let { songsViewModel.search(it) }
            return true
        }

        override fun onQueryTextChange(newText: String?): Boolean {
            return false
        }
    }

    private fun openNewSong() {
        findNavController().navigate(R.id.actionOpenNewSong)
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
        val arguments = bundleOf(SongDetails.SONG_ID_EXTRA to song.songId.value)
        findNavController().navigate(R.id.actionOpenSongDetails, arguments)
    }

    private fun observeSongsLiveData() {
        songsViewModel.songsLiveData().listen(viewLifecycleOwner) {
            when (it) {
                is SongsResult.Loading -> displayLoading(it.loading)
                is SongsResult.Fetched -> displaySongs(it.songs)
                is SongsResult.FetchingError -> displayFetchingError()
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
}
