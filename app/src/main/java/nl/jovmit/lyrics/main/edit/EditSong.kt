package nl.jovmit.lyrics.main.edit

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import nl.jovmit.lyrics.databinding.FragmentEditSongBinding

class EditSong : Fragment() {

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
}