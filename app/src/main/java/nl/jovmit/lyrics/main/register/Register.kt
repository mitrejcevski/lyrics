package nl.jovmit.lyrics.main.register

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        layout.registrationAlreadyRegisteredLabel.setOnClickListener { openLogin() }
    }

    private fun openLogin() {
        val destination = RegisterDirections.openLogin()
        findNavController().navigate(destination)
    }
}