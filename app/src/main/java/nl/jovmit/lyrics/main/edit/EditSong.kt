package nl.jovmit.lyrics.main.edit

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import nl.jovmit.lyrics.databinding.FragmentEditSongBinding
import nl.jovmit.lyrics.extensions.listen
import nl.jovmit.lyrics.main.data.result.SongResult
import nl.jovmit.lyrics.main.data.song.Song
import nl.jovmit.lyrics.main.details.SongDetailsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class EditSong : Fragment() {

    private val navArguments by navArgs<EditSongArgs>()
    private val songDetailViewModel by viewModel<SongDetailsViewModel>()

    private lateinit var layout: FragmentEditSongBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        layout = FragmentEditSongBinding.inflate(inflater)
        return layout.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        observeSongDetail()
        songDetailViewModel.fetchSongById(navArguments.songId)
    }

    private fun observeSongDetail() {
        songDetailViewModel.songDetailsLiveData().listen(viewLifecycleOwner) {
            when (it) {
                is SongResult.Fetched -> applySongValues(it.song)
            }
        }
    }

    private fun applySongValues(song: Song) {
        layout.editSongTitleEditText.setText(song.songTitle.value)
        layout.editSongPerformerEditText.setText(song.songPerformer.name)
        layout.editSongLyricEditText.setText(song.songLyric.lyrics)
    }
}