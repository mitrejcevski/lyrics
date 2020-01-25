package nl.jovmit.lyrics.main.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import nl.jovmit.lyrics.databinding.FragmentSongDetailsBinding
import kotlin.LazyThreadSafetyMode.NONE

class SongDetails : Fragment() {

    companion object {
        const val SONG_ID_EXTRA = "songIdExtra"
    }

    private val songId by lazy(NONE) { requireArguments().getString(SONG_ID_EXTRA, "") }

    private lateinit var layout: FragmentSongDetailsBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        layout = FragmentSongDetailsBinding.inflate(inflater)
        return layout.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        Toast.makeText(requireContext(), songId, Toast.LENGTH_SHORT).show()
    }
}