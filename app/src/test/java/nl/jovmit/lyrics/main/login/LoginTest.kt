package nl.jovmit.lyrics.main.login

import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.test.runTest
import nl.jovmit.lyrics.InstantTaskExecutorExtension
import nl.jovmit.lyrics.common.TestCoroutineDispatchers
import nl.jovmit.lyrics.main.auth.AuthenticationRepository
import nl.jovmit.lyrics.main.auth.CredentialsValidator
import nl.jovmit.lyrics.main.auth.InMemoryAuthService
import nl.jovmit.lyrics.main.data.user.RegistrationData
import nl.jovmit.lyrics.main.preferences.InMemoryPreferencesManager
import nl.jovmit.lyrics.utils.IdGenerator
import nl.jovmit.lyrics.utils.observeFlow
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(InstantTaskExecutorExtension::class)
class LoginTest {

  private val validator = CredentialsValidator()
  private val authService = InMemoryAuthService(IdGenerator())
  private val repository = AuthenticationRepository(authService)
  private val preferences = InMemoryPreferencesManager()
  private val dispatchers = TestCoroutineDispatchers()

  @Test
  fun defaultStateIsIdle() {
    val viewModel = LoginViewModel(validator, repository, preferences, dispatchers)
    assertThat(viewModel.uiState.value).isEqualTo(LoginScreenState())
  }

  @Test
  fun updateUsername() {
    val newUsername = "new"
    val viewModel = LoginViewModel(validator, repository, preferences, dispatchers)

    viewModel.updateUsername(newUsername)

    assertThat(viewModel.uiState.value).isEqualTo(LoginScreenState(username = newUsername))
  }

  @Test
  fun emptyUsernameError() {
    val viewModel = LoginViewModel(validator, repository, preferences, dispatchers)

    viewModel.login()

    assertThat(viewModel.uiState.value).isEqualTo(LoginScreenState(isEmptyUsernameError = true))
  }

  @Test
  fun resetEmptyUsernameError() {
    val newUsername = "new"
    val viewModel = LoginViewModel(validator, repository, preferences, dispatchers).apply {
      login()
    }

    viewModel.updateUsername(newUsername)

    assertThat(viewModel.uiState.value).isEqualTo(LoginScreenState(username = newUsername))
  }

  @Test
  fun updatePassword() {
    val newPassword = "newPass"
    val viewModel = LoginViewModel(validator, repository, preferences, dispatchers)

    viewModel.updatePassword(newPassword)

    assertThat(viewModel.uiState.value).isEqualTo(LoginScreenState(password = newPassword))
  }

  @Test
  fun emptyPasswordError() {
    val newUsername = "username"
    val viewModel = LoginViewModel(validator, repository, preferences, dispatchers).apply {
      updateUsername(newUsername)
    }

    viewModel.login()

    assertThat(viewModel.uiState.value).isEqualTo(
      LoginScreenState(username = newUsername, isEmptyPasswordError = true)
    )
  }

  @Test
  fun resetEmptyPasswordError() {
    val username = "username"
    val password = "password"
    val viewModel = LoginViewModel(validator, repository, preferences, dispatchers).apply {
      updateUsername(username)
      login()
    }

    viewModel.updatePassword(password)

    assertThat(viewModel.uiState.value).isEqualTo(
      LoginScreenState(username = username, password = password)
    )
  }

  @Test
  fun wrongCredentialsError() {
    val username = "username"
    val password = "password"
    val viewModel = LoginViewModel(validator, repository, preferences, dispatchers).apply {
      updateUsername(username)
      updatePassword(password)
    }

    viewModel.login()

    assertThat(viewModel.uiState.value).isEqualTo(
      LoginScreenState(username = username, password = password, isErrorLoggingIn = true)
    )
  }

  @Test
  fun resetWrongCredentialsError() {
    val username = "username"
    val password = "password"
    val viewModel = LoginViewModel(validator, repository, preferences, dispatchers).apply {
      updateUsername(username)
      updatePassword(password)
      login()
    }

    viewModel.dismissLoginError()

    assertThat(viewModel.uiState.value).isEqualTo(
      LoginScreenState(username = username, password = password)
    )
  }

  @Test
  fun offlineError() {
    val username = "username"
    val password = "password"
    authService.setOffline()
    val viewModel = LoginViewModel(validator, repository, preferences, dispatchers).apply {
      updateUsername(username)
      updatePassword(password)
    }

    viewModel.login()

    assertThat(viewModel.uiState.value).isEqualTo(
      LoginScreenState(username = username, password = password, isOfflineError = true)
    )
  }

  @Test
  fun resetOfflineError() {
    val username = "username"
    val password = "password"
    authService.setOffline()
    val viewModel = LoginViewModel(validator, repository, preferences, dispatchers).apply {
      updateUsername(username)
      updatePassword(password)
      login()
    }

    viewModel.dismissOfflineError()

    assertThat(viewModel.uiState.value).isEqualTo(
      LoginScreenState(username = username, password = password)
    )
  }

  @Test
  fun loggedInSuccessfully() = runTest {
    val username = "username"
    val password = "password"
    authService.createUser(RegistrationData(username, password, ""))
    val viewModel = LoginViewModel(validator, repository, preferences, dispatchers).apply {
      updateUsername(username)
      updatePassword(password)
    }

    viewModel.login()

    assertThat(viewModel.uiState.value).isEqualTo(
      LoginScreenState(username = username, password = password, isLoggedIn = true)
    )
  }

  @Test
  fun successfulLoginPersistsUser() = runTest {
    val username = "username"
    val password = "password"
    authService.createUser(RegistrationData(username, password, ""))
    val viewModel = LoginViewModel(validator, repository, preferences, dispatchers).apply {
      updateUsername(username)
      updatePassword(password)
    }

    viewModel.login()

    assertThat(preferences.loggedInUser()?.userId).isNotEmpty()
    assertThat(preferences.loggedInUser()?.username).isEqualTo(username)
    assertThat(preferences.loggedInUser()?.password).isEqualTo(password)
  }

  @Test
  fun loginUiStateDeliveryInOrder() = runTest {
    val username = "username"
    val password = "password"
    authService.createUser(RegistrationData(username, password, ""))
    val viewModel = LoginViewModel(validator, repository, preferences, dispatchers).apply {
      updateUsername(username)
      updatePassword(password)
    }

    val deliveredStates = observeFlow(viewModel.uiState) {
      viewModel.login()
    }
    assertThat(deliveredStates).isEqualTo(
      listOf(
        LoginScreenState(isLoading = true, username = username, password = password),
        LoginScreenState(username = username, password = password, isLoggedIn = true)
      )
    )
  }

}