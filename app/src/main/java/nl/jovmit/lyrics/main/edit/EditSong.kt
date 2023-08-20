package nl.jovmit.lyrics.main.edit

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
import androidx.transition.TransitionManager
import nl.jovmit.lyrics.R
import nl.jovmit.lyrics.databinding.FragmentEditSongBinding
import nl.jovmit.lyrics.extensions.listen
import nl.jovmit.lyrics.main.InfoViewModel
import nl.jovmit.lyrics.main.data.result.SongResult
import nl.jovmit.lyrics.main.data.song.Song
import nl.jovmit.lyrics.main.data.song.SongLyrics
import nl.jovmit.lyrics.main.data.song.SongPerformer
import nl.jovmit.lyrics.main.data.song.SongTitle
import nl.jovmit.lyrics.main.details.SongDetailsViewModel
import org.koin.androidx.viewmodel.ext.android.activityViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class EditSong : Fragment() {

    private val navArguments by navArgs<EditSongArgs>()
    private val updateSongViewModel by viewModel<UpdateSongViewModel>()
    private val songDetailViewModel by viewModel<SongDetailsViewModel>()
    private val infoViewModel by activityViewModel<InfoViewModel>()

    private lateinit var layout: FragmentEditSongBinding

    private val menuProvider = object : MenuProvider {
        override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
            menuInflater.inflate(R.menu.song_editor_menu, menu)
        }

        override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
            if (menuItem.itemId == R.id.actionDone) {
                updateSong()
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
        layout = FragmentEditSongBinding.inflate(inflater)
        return layout.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        requireActivity().addMenuProvider(menuProvider, viewLifecycleOwner)
        observeSongDetail()
        observeSongUpdate()
        songDetailViewModel.fetchSongById(navArguments.songId)
    }

    private fun observeSongDetail() {
        songDetailViewModel.songDetailsLiveData().listen(viewLifecycleOwner) {
            when (it) {
                is SongResult.Loading -> displayLoading(it.loading)
                is SongResult.Fetched -> applySongValues(it.song)
                else -> {}
            }
        }
    }

    private fun displayLoading(loading: Boolean) {
        val visibility = if (loading) View.VISIBLE else View.GONE
        TransitionManager.beginDelayedTransition(layout.root)
        layout.editSongLoading.visibility = visibility
    }

    private fun applySongValues(song: Song) {
        layout.editSongTitleEditText.setText(song.songTitle.value)
        layout.editSongPerformerEditText.setText(song.songPerformer.name)
        layout.editSongLyricEditText.setText(song.songLyric.lyrics)
    }

    private fun updateSong() {
        val title = SongTitle(layout.editSongTitleEditText.text.toString())
        val performer = SongPerformer(layout.editSongPerformerEditText.text.toString())
        val lyrics = SongLyrics(layout.editSongLyricEditText.text.toString())
        updateSongViewModel.updateSong(navArguments.songId, title, performer, lyrics)
    }

    private fun observeSongUpdate() {
        updateSongViewModel.updateSongLiveData().listen(viewLifecycleOwner) {
            when (it) {
                is SongResult.Loading -> displayLoading(it.loading)
                is SongResult.Updated -> handleSuccessfulSongUpdate()
                is SongResult.ErrorUpdating -> handleFailedSongUpdate()
                else -> {}
            }
        }
    }

    private fun handleSuccessfulSongUpdate() {
        infoViewModel.showInfo(getString(R.string.success))
        findNavController().navigateUp()
    }

    private fun handleFailedSongUpdate() {
        infoViewModel.showError(getString(R.string.errorUnableToEditSong))
    }
}