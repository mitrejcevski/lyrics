package nl.jovmit.lyrics.main.register

import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.test.runTest
import nl.jovmit.lyrics.InstantTaskExecutorExtension
import nl.jovmit.lyrics.common.TestCoroutineDispatchers
import nl.jovmit.lyrics.main.auth.AuthenticationRepository
import nl.jovmit.lyrics.main.auth.CredentialsValidator
import nl.jovmit.lyrics.main.auth.InMemoryAuthService
import nl.jovmit.lyrics.main.preferences.InMemoryPreferencesManager
import nl.jovmit.lyrics.utils.IdGenerator
import nl.jovmit.lyrics.utils.observeFlow
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(InstantTaskExecutorExtension::class)
class CreateNewUserTest {

  private val validator = CredentialsValidator()
  private val authService = InMemoryAuthService(IdGenerator())
  private val repository = AuthenticationRepository(authService)
  private val preferences = InMemoryPreferencesManager()
  private val dispatchers = TestCoroutineDispatchers()

  @Test
  fun defaultStateIsIdle() {
    val viewModel = RegisterViewModel(validator, repository, preferences, dispatchers)
    assertThat(viewModel.uiState.value).isEqualTo(RegisterScreenState())
  }

  @Test
  fun updateUsername() {
    val newUsername = "username"
    val viewModel = RegisterViewModel(validator, repository, preferences, dispatchers)

    viewModel.updateUsername(newUsername)

    assertThat(viewModel.uiState.value).isEqualTo(RegisterScreenState(username = newUsername))
  }

  @Test
  fun emptyUsernameError() {
    val viewModel = RegisterViewModel(validator, repository, preferences, dispatchers)

    viewModel.register()

    assertThat(viewModel.uiState.value).isEqualTo(RegisterScreenState(isEmptyUsernameError = true))
  }

  @Test
  fun resetEmptyUsernameError() {
    val newUsername = "username"
    val viewModel = RegisterViewModel(validator, repository, preferences, dispatchers).apply {
      register()
    }

    viewModel.updateUsername(newUsername)

    assertThat(viewModel.uiState.value).isEqualTo(RegisterScreenState(username = newUsername))
  }

  @Test
  fun updatePassword() {
    val newPassword = "password"
    val viewModel = RegisterViewModel(validator, repository, preferences, dispatchers)

    viewModel.updatePassword(newPassword)

    assertThat(viewModel.uiState.value).isEqualTo(RegisterScreenState(password = newPassword))
  }

  @Test
  fun emptyPasswordError() {
    val username = "username"
    val viewModel = RegisterViewModel(validator, repository, preferences, dispatchers).apply {
      updateUsername(username)
    }

    viewModel.register()

    assertThat(viewModel.uiState.value).isEqualTo(
      RegisterScreenState(username = username, isEmptyPasswordError = true)
    )
  }

  @Test
  fun resetEmptyPasswordError() {
    val username = "username"
    val password = "password"
    val viewModel = RegisterViewModel(validator, repository, preferences, dispatchers).apply {
      updateUsername(username)
      register()
    }

    viewModel.updatePassword(password)

    assertThat(viewModel.uiState.value).isEqualTo(
      RegisterScreenState(username = username, password = password)
    )
  }

  @Test
  fun updateAboutMe() {
    val aboutMe = "about me"
    val viewModel = RegisterViewModel(validator, repository, preferences, dispatchers)

    viewModel.updateAboutMe(aboutMe)

    assertThat(viewModel.uiState.value).isEqualTo(RegisterScreenState(aboutMe = aboutMe))
  }

  @Test
  fun registrationError() {
    val username = "username"
    val password = "password"
    authService.setUnavailable()
    val viewModel = RegisterViewModel(validator, repository, preferences, dispatchers).apply {
      updateUsername(username)
      updatePassword(password)
    }

    viewModel.register()

    assertThat(viewModel.uiState.value).isEqualTo(
      RegisterScreenState(isRegistrationError = true, username = username, password = password)
    )
  }

  @Test
  fun dismissRegistrationError() {
    val username = "username"
    val password = "password"
    authService.setUnavailable()
    val viewModel = RegisterViewModel(validator, repository, preferences, dispatchers).apply {
      updateUsername(username)
      updatePassword(password)
      register()
    }

    viewModel.dismissRegistrationError()

    assertThat(viewModel.uiState.value).isEqualTo(
      RegisterScreenState(username = username, password = password)
    )
  }

  @Test
  fun offlineError() {
    val username = "username"
    val password = "password"
    authService.setOffline()
    val viewModel = RegisterViewModel(validator, repository, preferences, dispatchers).apply {
      updateUsername(username)
      updatePassword(password)
    }

    viewModel.register()

    assertThat(viewModel.uiState.value).isEqualTo(
      RegisterScreenState(isOfflineError = true, username = username, password = password)
    )
  }

  @Test
  fun dismissOfflineError() {
    val username = "username"
    val password = "password"
    authService.setOffline()
    val viewModel = RegisterViewModel(validator, repository, preferences, dispatchers).apply {
      updateUsername(username)
      updatePassword(password)
      register()
    }

    viewModel.dismissOfflineError()

    assertThat(viewModel.uiState.value).isEqualTo(
      RegisterScreenState(username = username, password = password)
    )
  }

  @Test
  fun successfulRegistration() {
    val username = "username"
    val password = "password"
    val viewModel = RegisterViewModel(validator, repository, preferences, dispatchers).apply {
      updateUsername(username)
      updatePassword(password)
    }

    viewModel.register()

    assertThat(viewModel.uiState.value).isEqualTo(
      RegisterScreenState(username = username, password = password, isRegistered = true)
    )
  }

  @Test
  fun registeredUserGetsPersistedLocally() {
    val username = "username"
    val password = "password"
    val viewModel = RegisterViewModel(validator, repository, preferences, dispatchers).apply {
      updateUsername(username)
      updatePassword(password)
    }

    viewModel.register()

    assertThat(preferences.loggedInUser()?.userId).isNotEmpty()
    assertThat(preferences.loggedInUser()?.username).isEqualTo(username)
  }

  @Test
  fun registrationUiStatesDeliveredInOrder() = runTest {
    val username = "username"
    val password = "password"
    val viewModel = RegisterViewModel(validator, repository, preferences, dispatchers).apply {
      updateUsername(username)
      updatePassword(password)
    }

    val deliveredStates = observeFlow(viewModel.uiState) {
      viewModel.register()
    }

    assertThat(deliveredStates).isEqualTo(
      listOf(
        RegisterScreenState(username = username, password = password, isLoading = true),
        RegisterScreenState(username = username, password = password, isRegistered = true)
      )
    )
  }
}