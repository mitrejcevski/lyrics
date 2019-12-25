package nl.jovmit.lyrics.main.add

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import nl.jovmit.lyrics.R
import nl.jovmit.lyrics.databinding.FragmentNewSongBinding
import nl.jovmit.lyrics.extensions.listen
import nl.jovmit.lyrics.extensions.setError
import nl.jovmit.lyrics.main.data.result.NewSongResult
import org.koin.android.ext.android.inject

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
        layout.newSongDoneButton.setOnClickListener {
            triggerNewSongSubmission()
        }
        observeNewSongLiveData()
    }

    private fun observeNewSongLiveData() {
        newSongViewModel.newSongLiveData().listen(viewLifecycleOwner) {
            when (it) {
                is NewSongResult.EmptyTitle -> displayEmptyTitleError()
                is NewSongResult.EmptyPerformer -> displayEmptyPerformerError()
                is NewSongResult.EmptyLyrics -> displayEmptyLyricsError()
            }
        }
    }

    private fun displayEmptyTitleError() {
        layout.newSongTitleInput.setError(R.string.errorEmptySongTitle)
    }

    private fun displayEmptyPerformerError() {
        layout.newSongSingerNameInput.setError(R.string.errorEmptySongPerformer)
    }

    private fun displayEmptyLyricsError() {
        layout.newSongLyricInput.setError(R.string.errorEmptySongLyrics)
    }

    private fun triggerNewSongSubmission() {
        val title = layout.newSongTitleEditText.text.toString()
        val performer = layout.newSongSingerNameEditText.text.toString()
        val lyrics = layout.newSongLyricEditText.text.toString()
        newSongViewModel.addNewSong(title, performer, lyrics)
    }
}