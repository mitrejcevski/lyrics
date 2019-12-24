package nl.jovmit.lyrics.main.add

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import nl.jovmit.lyrics.databinding.FragmentNewSongBinding

class NewSong : Fragment() {

    private lateinit var layoutBinding: FragmentNewSongBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        layoutBinding = FragmentNewSongBinding.inflate(inflater)
        return layoutBinding.root
    }
}