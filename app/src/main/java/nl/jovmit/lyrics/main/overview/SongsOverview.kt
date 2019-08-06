package nl.jovmit.lyrics.main.overview

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_songs_overview.*
import nl.jovmit.lyrics.R
import nl.jovmit.lyrics.extensions.applyDefaultColors
import nl.jovmit.lyrics.extensions.listen
import nl.jovmit.lyrics.extensions.setupWithLinearLayoutManager
import nl.jovmit.lyrics.main.data.Song
import nl.jovmit.lyrics.main.data.result.SongsResult
import org.koin.android.ext.android.inject

class SongsOverview : Fragment() {

    private val songsAdapter: SongsAdapter by lazy { SongsAdapter() }
    private val songsOverviewViewModel: SongsOverviewViewModel by inject()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_songs_overview, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        songsOverviewSwipeRefresh.applyDefaultColors()
        setupRecyclerView()
        observeSongsLiveData()
        songsOverviewViewModel.fetchSongs()
    }

    private fun setupRecyclerView() {
        songsOverviewRecycler.setupWithLinearLayoutManager()
        songsOverviewRecycler.adapter = songsAdapter
    }

    private fun observeSongsLiveData() {
        songsOverviewViewModel.songsLiveData().listen(viewLifecycleOwner) {
            when (it) {
                is SongsResult.Loading -> displayLoading(it.loading)
                is SongsResult.Fetched -> displaySongs(it.songs)
                is SongsResult.FetchingError -> displayFetchingError()
            }
        }
    }

    private fun displayLoading(loading: Boolean) {
        songsOverviewSwipeRefresh.isRefreshing = loading
    }

    private fun displaySongs(songs: List<Song>) {
        updateEmptyStatePreview(songs.isEmpty())
        songsAdapter.addSongs(songs)
    }

    private fun updateEmptyStatePreview(isVisible: Boolean) {
        val emptyStateVisibility = if (isVisible) View.VISIBLE else View.GONE
        songsOverviewEmptyStateLabel.visibility = emptyStateVisibility
    }

    private fun displayFetchingError() {
        songsOverviewInfoView.displayError(R.string.errorFetchingSongs)
    }
}
