package nl.jovmit.lyrics.main.register

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import nl.jovmit.lyrics.R
import nl.jovmit.lyrics.databinding.FragmentRegisterBinding
import nl.jovmit.lyrics.extensions.*
import nl.jovmit.lyrics.main.InfoViewModel
import nl.jovmit.lyrics.main.data.result.CredentialsValidationResult
import nl.jovmit.lyrics.main.data.result.RegisterResult
import nl.jovmit.lyrics.main.data.user.User
import nl.jovmit.lyrics.main.preferences.UserPreferencesViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class Register : Fragment() {

    private val registerViewModel by viewModel<RegisterViewModel>()
    private val userPreferencesViewModel by viewModel<UserPreferencesViewModel>()
    private val infoViewModel by sharedViewModel<InfoViewModel>()

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
        layout.registrationRegisterButton.setOnClickListener { triggerRegistration() }
        layout.registrationUsernameEditText.onAnyTextChange {
            layout.registrationUsernameTextInput.resetError()
        }
        layout.registrationPasswordEditText.onAnyTextChange {
            layout.registrationPasswordTextInput.resetError()
        }
        observeRegistrationLiveData()
        observeCredentialsValidationLiveData()
    }

    private fun openLogin() {
        val destination = RegisterDirections.openLogin()
        findNavController().navigate(destination)
    }

    private fun triggerRegistration() {
        val username = layout.registrationUsernameEditText.text.toString()
        val password = layout.registrationPasswordEditText.text.toString()
        val about = layout.registrationAboutEditText.text.toString()
        registerViewModel.register(username, password, about)
    }

    private fun observeRegistrationLiveData() {
        registerViewModel.registrationLiveData().listen(viewLifecycleOwner) {
            when (it) {
                is RegisterResult.Loading -> toggleLoading(it.loading)
                is RegisterResult.Registered -> onRegistrationSuccess(it.user)
                is RegisterResult.UsernameTakenError -> showUsernameTakenError()
                is RegisterResult.OfflineError -> showOfflineError()
            }
        }
    }

    private fun toggleLoading(loading: Boolean) {
        val visibility = if (loading) View.VISIBLE else View.GONE
        layout.registrationLoading.visibility = visibility
    }

    private fun onRegistrationSuccess(user: User) {
        userPreferencesViewModel.setLoggedInUser(user)
        layout.registrationUsernameEditText.hideKeyboard()
        val destination = RegisterDirections.openSongsOverview()
        findNavController().navigate(destination)
    }

    private fun showUsernameTakenError() {
        layout.registrationUsernameTextInput.setError(R.string.errorUsernameTaken)
    }

    private fun showOfflineError() {
        infoViewModel.showError(getString(R.string.errorNoNetworkConnection))
    }

    private fun observeCredentialsValidationLiveData() {
        registerViewModel.credentialsValidationLiveData().listen(viewLifecycleOwner) {
            when (it) {
                is CredentialsValidationResult.EmptyUsername -> showEmptyUsernameError()
                is CredentialsValidationResult.EmptyPassword -> showEmptyPasswordError()
            }
        }
    }

    private fun showEmptyUsernameError() {
        layout.registrationUsernameTextInput.setError(R.string.errorEmptyUsername)
    }

    private fun showEmptyPasswordError() {
        layout.registrationPasswordTextInput.setError(R.string.errorEmptyPassword)
    }
}
