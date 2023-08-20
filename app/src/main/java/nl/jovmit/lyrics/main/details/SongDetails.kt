package nl.jovmit.lyrics.main.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.snackbar.Snackbar
import nl.jovmit.lyrics.R
import nl.jovmit.lyrics.databinding.FragmentSongDetailsBinding
import nl.jovmit.lyrics.extensions.listen
import nl.jovmit.lyrics.main.InfoViewModel
import nl.jovmit.lyrics.main.data.result.SongResult
import nl.jovmit.lyrics.main.data.song.Song
import org.koin.androidx.viewmodel.ext.android.activityViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class SongDetails : Fragment() {

    private val navArguments by navArgs<SongDetailsArgs>()
    private val songDetailsViewModel by viewModel<SongDetailsViewModel>()
    private val infoViewModel by activityViewModel<InfoViewModel>()

    private lateinit var layout: FragmentSongDetailsBinding

    private val menuProvider = object : MenuProvider {

        override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
            menuInflater.inflate(R.menu.song_details_menu, menu)
        }

        override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
            return when (menuItem.itemId) {
                R.id.actionDelete -> {
                    deleteSong()
                    true
                }

                R.id.actionEdit -> {
                    editSong()
                    true
                }

                else -> false
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        layout = FragmentSongDetailsBinding.inflate(inflater)
        return layout.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        requireActivity().addMenuProvider(menuProvider, viewLifecycleOwner)
        observeSongDetailsLiveData()
        songDetailsViewModel.fetchSongById(navArguments.songId)
    }

    private fun editSong() {
        val direction = SongDetailsDirections.editSong(navArguments.songId)
        findNavController().navigate(direction)
    }

    private fun deleteSong() {
        Snackbar.make(layout.root, R.string.deleteSongPrompt, Snackbar.LENGTH_SHORT)
            .setAction(R.string.delete) { songDetailsViewModel.deleteSongById(navArguments.songId) }
            .show()
    }

    private fun observeSongDetailsLiveData() {
        songDetailsViewModel.songDetailsLiveData().listen(viewLifecycleOwner) {
            when (it) {
                is SongResult.Fetched -> displaySongData(it.song)
                is SongResult.Deleted -> onSongDeleted()
                is SongResult.ErrorDeleting -> displayErrorDeletingSong()
                else -> {}
            }
        }
    }

    private fun displaySongData(song: Song) {
        layout.songDetailsTitle.text = song.songTitle.value
        layout.songDetailsPerformer.text = song.songPerformer.name
        layout.songDetailsLyrics.text = song.songLyric.lyrics
    }

    private fun onSongDeleted() {
        infoViewModel.showInfo(getString(R.string.success))
        findNavController().navigateUp()
    }

    private fun displayErrorDeletingSong() {
        val errorMessage = getString(R.string.errorDeletingSong)
        infoViewModel.showError(errorMessage)
    }
}