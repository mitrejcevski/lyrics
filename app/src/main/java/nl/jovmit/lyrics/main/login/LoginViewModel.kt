package nl.jovmit.lyrics.main.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import nl.jovmit.lyrics.common.CoroutineDispatchers
import nl.jovmit.lyrics.common.CoroutineViewModel
import nl.jovmit.lyrics.main.auth.AuthenticationRepository
import nl.jovmit.lyrics.main.auth.CredentialsValidator
import nl.jovmit.lyrics.main.data.result.CredentialsValidationResult
import nl.jovmit.lyrics.main.data.result.LoginResult
import nl.jovmit.lyrics.main.data.user.LoginData
import nl.jovmit.lyrics.main.data.user.User
import nl.jovmit.lyrics.main.preferences.PreferencesManager

class LoginViewModel(
    private val credentialsValidator: CredentialsValidator,
    private val authRepository: AuthenticationRepository,
    private val preferencesManager: PreferencesManager,
    private val dispatchers: CoroutineDispatchers
) : CoroutineViewModel(dispatchers) {

    private val _uiState = MutableStateFlow(LoginScreenState())
    val uiState: StateFlow<LoginScreenState> = _uiState

    private val loginLiveData = MutableLiveData<LoginResult>()
    private val credentialsValidationLiveData = MutableLiveData<CredentialsValidationResult>()

    fun loginLiveData(): LiveData<LoginResult> {
        return loginLiveData
    }

    fun credentialsValidationLiveData(): LiveData<CredentialsValidationResult> {
        return credentialsValidationLiveData
    }

    fun updateUsername(newUsername: String) {
        _uiState.update { it.copy(username = newUsername, isEmptyUsernameError = false) }
    }

    fun updatePassword(newPassword: String) {
        _uiState.update { it.copy(password = newPassword, isEmptyPasswordError = false) }
    }

    fun login() {
        login(uiState.value.username, uiState.value.password)
    }

    fun dismissLoginError() {
        _uiState.update { it.copy(isErrorLoggingIn = false) }
    }

    fun dismissOfflineError() {
        _uiState.update { it.copy(isOfflineError = false) }
    }

    fun login(username: String, password: String) {
        val validationResult = credentialsValidator.validate(username, password)
        if (validationResult == CredentialsValidationResult.Valid) {
            performLogin(username, password)
        } else {
            onCredentialsValidationResult(validationResult)
            credentialsValidationLiveData.value = validationResult
        }
    }

    private fun onCredentialsValidationResult(validationResult: CredentialsValidationResult) {
        when (validationResult) {
            is CredentialsValidationResult.EmptyUsername ->
                _uiState.update { it.copy(isEmptyUsernameError = true) }
            is CredentialsValidationResult.EmptyPassword ->
                _uiState.update { it.copy(isEmptyPasswordError = true) }
            else -> {}
        }
    }

    private fun performLogin(username: String, password: String) {
        _uiState.update { it.copy(isLoading = true) }
        launch {
            loginLiveData.value = LoginResult.Loading(true)
            val result = withContext(dispatchers.background) {
                val loginData = LoginData(username, password)
                authRepository.login(loginData)
            }
            onLoginResult(result)
            loginLiveData.value = result
            loginLiveData.value = LoginResult.Loading(false)
        }
    }

    private fun onLoginResult(result: LoginResult) {
        when(result) {
            is LoginResult.LoggedIn -> {
                persistLoggedInUser(result.user)
                _uiState.update { it.copy(isLoading = false, isLoggedIn = true) }
            }
            is LoginResult.UserNotFoundError -> _uiState.update { it.copy(isLoading = false, isErrorLoggingIn = true) }
            is LoginResult.Offline -> _uiState.update { it.copy(isLoading = false, isOfflineError = true) }
            else -> {}
        }
    }

    private fun persistLoggedInUser(user: User) {
        preferencesManager.loggedInUser(user)
    }
}
