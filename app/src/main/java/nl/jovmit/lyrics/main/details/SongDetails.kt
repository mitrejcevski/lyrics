package nl.jovmit.lyrics.main.details

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import nl.jovmit.lyrics.R
import nl.jovmit.lyrics.databinding.FragmentSongDetailsBinding
import nl.jovmit.lyrics.extensions.listen
import nl.jovmit.lyrics.main.data.result.SongResult
import nl.jovmit.lyrics.main.data.song.Song
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlin.LazyThreadSafetyMode.NONE

class SongDetails : Fragment() {

    companion object {
        const val SONG_ID_EXTRA = "songIdExtra"
    }

    private val songId by lazy(NONE) { requireArguments().getString(SONG_ID_EXTRA, "") }
    private val songDetailsViewModel by viewModel<SongDetailsViewModel>()

    private lateinit var layout: FragmentSongDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        layout = FragmentSongDetailsBinding.inflate(inflater)
        return layout.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        observeSongDetailsLiveData()
        songDetailsViewModel.fetchSongById(songId)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.song_details_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.actionDelete) {
            deleteSong()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun deleteSong() {
        Snackbar.make(layout.root, R.string.deleteSongPrompt, Snackbar.LENGTH_SHORT)
            .setAction(R.string.delete) { }
            .show()
    }

    private fun observeSongDetailsLiveData() {
        songDetailsViewModel.songDetailsLiveData().listen(viewLifecycleOwner) {
            when (it) {
                is SongResult.Fetched -> displaySongData(it.song)
            }
        }
    }

    private fun displaySongData(song: Song) {
        layout.songDetailsTitle.text = song.songTitle.value
        layout.songDetailsPerformer.text = song.songPerformer.name
        layout.songDetailsLyrics.text = song.songLyric.lyrics
    }
}