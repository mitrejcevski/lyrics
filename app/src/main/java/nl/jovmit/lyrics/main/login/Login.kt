package nl.jovmit.lyrics.main.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import nl.jovmit.lyrics.R
import nl.jovmit.lyrics.databinding.FragmentLoginBinding
import nl.jovmit.lyrics.extensions.*
import nl.jovmit.lyrics.main.InfoViewModel
import nl.jovmit.lyrics.main.data.result.CredentialsValidationResult
import nl.jovmit.lyrics.main.data.result.LoginResult
import nl.jovmit.lyrics.main.data.user.User
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class Login : Fragment() {

    private val loginViewModel by viewModel<LoginViewModel>()
    private val infoViewModel by sharedViewModel<InfoViewModel>()

    private lateinit var layout: FragmentLoginBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        layout = FragmentLoginBinding.inflate(inflater)
        return layout.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        layout.loginActionButton.setOnClickListener { triggerLogin() }
        layout.loginUsernameEditText.onAnyTextChange {
            layout.loginUsernameTextInput.resetError()
        }
        layout.loginPasswordEditText.onAnyTextChange {
            layout.loginPasswordTextInput.resetError()
        }
        observeLoginLiveData()
        observeCredentialsValidationLiveData()
    }

    private fun triggerLogin() {
        val username = layout.loginUsernameEditText.text.toString()
        val password = layout.loginPasswordEditText.text.toString()
        loginViewModel.login(username, password)
    }

    private fun observeLoginLiveData() {
        loginViewModel.loginLiveData().listen(viewLifecycleOwner) {
            when (it) {
                is LoginResult.Loading -> toggleLoading(it.loading)
                is LoginResult.LoggedIn -> onLoggedIn()
                is LoginResult.UserNotFoundError -> displayLoginError()
                is LoginResult.Offline -> displayOfflineError()
            }
        }
    }

    private fun toggleLoading(loading: Boolean) {
        val visibility = if (loading) View.VISIBLE else View.GONE
        layout.loginLoading.visibility = visibility
    }

    private fun onLoggedIn() {
        layout.loginUsernameEditText.hideKeyboard()
        val destination = LoginDirections.openSongsOverview()
        findNavController().navigate(destination)
    }

    private fun displayLoginError() {
        infoViewModel.showError(getString(R.string.errorIncorrectCredentials))
    }

    private fun displayOfflineError() {
        infoViewModel.showError(getString(R.string.errorNoNetworkConnection))
    }

    private fun observeCredentialsValidationLiveData() {
        loginViewModel.credentialsValidationLiveData().listen(viewLifecycleOwner) {
            when (it) {
                is CredentialsValidationResult.EmptyUsername -> showEmptyUsernameError()
                is CredentialsValidationResult.EmptyPassword -> showEmptyPasswordError()
            }
        }
    }

    private fun showEmptyUsernameError() {
        layout.loginUsernameTextInput.setError(R.string.errorEmptyUsername)
    }

    private fun showEmptyPasswordError() {
        layout.loginPasswordTextInput.setError(R.string.errorEmptyPassword)
    }
}
