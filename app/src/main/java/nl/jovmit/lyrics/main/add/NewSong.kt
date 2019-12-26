package nl.jovmit.lyrics.main.add

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import nl.jovmit.lyrics.R
import nl.jovmit.lyrics.databinding.FragmentNewSongBinding
import nl.jovmit.lyrics.extensions.*
import nl.jovmit.lyrics.main.data.result.NewSongResult
import org.koin.android.ext.android.inject
import java.util.concurrent.TimeUnit

class NewSong : Fragment() {

    private val newSongViewModel by inject<NewSongViewModel>()

    private lateinit var layout: FragmentNewSongBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        layout = FragmentNewSongBinding.inflate(inflater)
        return layout.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        layout.newSongTitleEditText.onAnyTextChange { layout.newSongTitleInput.resetError() }
        layout.newSongPerformerEditText.onAnyTextChange { layout.newSongPerformerInput.resetError() }
        layout.newSongLyricEditText.onAnyTextChange { layout.newSongLyricInput.resetError() }
        layout.newSongDoneButton.setOnClickListener { triggerNewSongSubmission() }
        observeNewSongLiveData()
    }

    private fun observeNewSongLiveData() {
        newSongViewModel.newSongLiveData().listen(viewLifecycleOwner) {
            when (it) {
                is NewSongResult.EmptyTitle -> displayEmptyTitleError()
                is NewSongResult.EmptyPerformer -> displayEmptyPerformerError()
                is NewSongResult.EmptyLyrics -> displayEmptyLyricsError()
                is NewSongResult.SongAdded -> displaySongAddingSuccess()
            }
        }
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
        layout.newSongInfoView.timeout(500, TimeUnit.MILLISECONDS)
        layout.newSongInfoView.setOnDismissCallback { findNavController().navigateUp() }
        layout.newSongInfoView.displayInfo(R.string.success)
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