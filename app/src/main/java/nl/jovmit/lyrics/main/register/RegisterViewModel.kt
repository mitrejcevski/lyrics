package nl.jovmit.lyrics.main.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import nl.jovmit.lyrics.common.CoroutineDispatchers
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
) : ViewModel() {

  private val _uiState = MutableStateFlow(RegisterScreenState())
  val uiState: StateFlow<RegisterScreenState> = _uiState

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
    val validationResult = credentialsValidator.validate(state.username, state.password)
    if (validationResult == CredentialsValidationResult.Valid) {
      performRegistration(state.username, state.password, state.aboutMe)
    } else {
      onCredentialsValidationResult(validationResult)
    }
  }

  private fun onCredentialsValidationResult(result: CredentialsValidationResult) {
    when (result) {
      is CredentialsValidationResult.EmptyUsername -> onEmptyUsernameError()
      is CredentialsValidationResult.EmptyPassword -> onEmptyPasswordError()
      else -> {}
    }
  }

  private fun performRegistration(username: String, password: String, about: String) {
    _uiState.update { it.copy(isLoading = true) }
    viewModelScope.launch {
      val result = withContext(dispatchers.background) {
        val registerData = RegistrationData(username, password, about)
        authRepository.registerUser(registerData)
      }
      onRegistrationResult(result)
    }
  }

  private fun onEmptyUsernameError() {
    _uiState.update { it.copy(isEmptyUsernameError = true) }
  }

  private fun onEmptyPasswordError() {
    _uiState.update { it.copy(isEmptyPasswordError = true) }
  }

  private fun onRegistrationResult(result: RegisterResult) {
    when (result) {
      is RegisterResult.UsernameTakenError -> onRegistrationError()
      is RegisterResult.OfflineError -> onOfflineError()
      is RegisterResult.Registered -> onRegistered(result)
    }
  }

  private fun onRegistrationError() {
    _uiState.update { it.copy(isRegistrationError = true, isLoading = false) }
  }

  private fun onOfflineError() {
    _uiState.update { it.copy(isOfflineError = true, isLoading = false) }
  }

  private fun onRegistered(result: RegisterResult.Registered) {
    preferences.loggedInUser(result.user)
    _uiState.update { it.copy(isRegistered = true, isLoading = false) }
  }
}
