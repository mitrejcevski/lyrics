package nl.jovmit.lyrics.main.register

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import nl.jovmit.lyrics.databinding.FragmentRegisterBinding

class Register : Fragment() {

    private lateinit var layout: FragmentRegisterBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        layout = FragmentRegisterBinding.inflate(inflater)
        return layout.root
    }
}