package nl.jovmit.lyrics.main.register

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
import nl.jovmit.lyrics.main.data.result.RegisterResult
import nl.jovmit.lyrics.main.data.user.RegistrationData
import nl.jovmit.lyrics.main.preferences.PreferencesManager

class RegisterViewModel(
    private val credentialsValidator: CredentialsValidator,
    private val authRepository: AuthenticationRepository,
    private val preferences: PreferencesManager,
    private val dispatchers: CoroutineDispatchers
) : CoroutineViewModel(dispatchers) {

    private val _uiState = MutableStateFlow(RegisterScreenState())
    val uiState: StateFlow<RegisterScreenState> = _uiState

    private val credentialsValidationLiveData = MutableLiveData<CredentialsValidationResult>()
    private val registrationLiveData = MutableLiveData<RegisterResult>()

    fun credentialsValidationLiveData(): LiveData<CredentialsValidationResult> {
        return credentialsValidationLiveData
    }

    fun registrationLiveData(): LiveData<RegisterResult> {
        return registrationLiveData
    }

    fun updateUsername(newUsername: String) {
        _uiState.update { it.copy(username = newUsername, isEmptyUsernameError = false) }
    }

    fun updatePassword(newPassword: String) {
        _uiState.update { it.copy(password = newPassword, isEmptyPasswordError = false) }
    }

    fun updateAboutMe(newAboutMe: String) {
        _uiState.update { it.copy(aboutMe = newAboutMe) }
    }

    fun dismissRegistrationError() {
      _uiState.update { it.copy(isRegistrationError = false) }
    }

    fun dismissOfflineError() {
      _uiState.update { it.copy(isOfflineError = false) }
    }

    fun register() {
        val state = uiState.value
        register(
            state.username,
            state.password,
            state.aboutMe
        )
    }

    fun register(username: String, password: String, about: String) {
        val validationResult = credentialsValidator.validate(username, password)
        if (validationResult == CredentialsValidationResult.Valid) {
            performRegistration(username, password, about)
        } else {
            credentialsValidationLiveData.value = validationResult
            onCredentialsValidationResult(validationResult)
        }
    }

    private fun onCredentialsValidationResult(result: CredentialsValidationResult) {
        when(result) {
            is CredentialsValidationResult.EmptyUsername ->
                _uiState.update { it.copy(isEmptyUsernameError = true) }
            is CredentialsValidationResult.EmptyPassword ->
                _uiState.update { it.copy(isEmptyPasswordError = true) }
            else -> {}
        }
    }

    private fun performRegistration(username: String, password: String, about: String) {
        _uiState.update { it.copy(isLoading = true) }
        launch {
            registrationLiveData.value = RegisterResult.Loading(true)
            val result = withContext(dispatchers.background) {
                val registerData = RegistrationData(username, password, about)
                authRepository.registerUser(registerData)
            }
            onRegistrationResult(result)
            registrationLiveData.value = result
            registrationLiveData.value = RegisterResult.Loading(false)
        }
    }

    private fun onRegistrationResult(result: RegisterResult) {
        when (result) {
            is RegisterResult.UsernameTakenError ->
                _uiState.update { it.copy(isRegistrationError = true, isLoading = false) }

            is RegisterResult.OfflineError ->
                _uiState.update { it.copy(isOfflineError = true, isLoading = false) }
            is RegisterResult.Registered -> {
                preferences.loggedInUser(result.user)
                _uiState.update { it.copy(isRegistered = true, isLoading = false) }
            }
            is RegisterResult.Loading -> {}
        }
    }
}
