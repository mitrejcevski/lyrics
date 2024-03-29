package nl.jovmit.lyrics.main.add

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import nl.jovmit.lyrics.R
import nl.jovmit.lyrics.databinding.FragmentNewSongBinding
import nl.jovmit.lyrics.extensions.getSystemService
import nl.jovmit.lyrics.extensions.listen
import nl.jovmit.lyrics.extensions.onAnyTextChange
import nl.jovmit.lyrics.extensions.resetError
import nl.jovmit.lyrics.extensions.setError
import nl.jovmit.lyrics.main.InfoViewModel
import nl.jovmit.lyrics.main.data.result.NewSongResult
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.activityViewModel

class NewSong : Fragment() {

    private val newSongViewModel by inject<NewSongViewModel>()
    private val infoViewModel by activityViewModel<InfoViewModel>()

    private lateinit var layout: FragmentNewSongBinding

    private val menuProvider = object : MenuProvider {

        override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
            menuInflater.inflate(R.menu.song_editor_menu, menu)
        }

        override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
            if (menuItem.itemId == R.id.actionDone) {
                triggerNewSongSubmission()
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
        layout = FragmentNewSongBinding.inflate(inflater)
        return layout.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        requireActivity().addMenuProvider(menuProvider, viewLifecycleOwner)
        layout.newSongTitleEditText.onAnyTextChange { layout.newSongTitleInput.resetError() }
        layout.newSongPerformerEditText.onAnyTextChange { layout.newSongPerformerInput.resetError() }
        layout.newSongLyricEditText.onAnyTextChange { layout.newSongLyricInput.resetError() }
        observeNewSongLiveData()
    }

    private fun observeNewSongLiveData() {
        newSongViewModel.newSongLiveData().listen(viewLifecycleOwner) {
            when (it) {
                is NewSongResult.Loading -> displayLoading(it.value)
                is NewSongResult.EmptyTitle -> displayEmptyTitleError()
                is NewSongResult.EmptyPerformer -> displayEmptyPerformerError()
                is NewSongResult.EmptyLyrics -> displayEmptyLyricsError()
                is NewSongResult.SongAdded -> displaySongAddingSuccess()
                is NewSongResult.ErrorAddingSong -> displayErrorSavingSong()
                is NewSongResult.Valid -> {}
            }
        }
    }

    private fun displayLoading(loading: Boolean) {
        val visibility = if (loading) View.VISIBLE else View.GONE
        layout.newSongLoading.visibility = visibility
    }

    private fun displayEmptyTitleError() {
        layout.newSongTitleInput.setError(R.string.errorEmptySongTitle)
    }

    private fun displayEmptyPerformerError() {
        layout.newSongPerformerInput.setError(R.string.errorEmptySongPerformer)
    }

    private fun displayEmptyLyricsError() {
        layout.newSongLyricInput.setError(R.string.errorEmptySongLyrics)
    }

    private fun displaySongAddingSuccess() {
        infoViewModel.showInfo(getString(R.string.success))
        findNavController().navigateUp()
    }

    private fun displayErrorSavingSong() {
        infoViewModel.showError(getString(R.string.errorSavingSong))
    }

    private fun triggerNewSongSubmission() {
        hideKeyboard()
        val title = layout.newSongTitleEditText.text.toString()
        val performer = layout.newSongPerformerEditText.text.toString()
        val lyrics = layout.newSongLyricEditText.text.toString()
        newSongViewModel.addNewSong(title, performer, lyrics)
    }

    private fun hideKeyboard() {
        val inputManager = requireContext().getSystemService<InputMethodManager>()
        inputManager?.hideSoftInputFromWindow(layout.newSongLyricEditText.windowToken, 0)
    }
}