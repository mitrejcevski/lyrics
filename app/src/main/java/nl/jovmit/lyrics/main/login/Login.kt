package nl.jovmit.lyrics.main.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import nl.jovmit.lyrics.databinding.FragmentLoginBinding

class Login : Fragment() {

    private lateinit var layout: FragmentLoginBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        layout = FragmentLoginBinding.inflate(inflater)
        return layout.root
    }
}