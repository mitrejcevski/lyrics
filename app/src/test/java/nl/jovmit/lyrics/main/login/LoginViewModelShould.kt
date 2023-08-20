package nl.jovmit.lyrics.main.login

import androidx.lifecycle.Observer
import com.nhaarman.mockitokotlin2.given
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.verifyBlocking
import com.nhaarman.mockitokotlin2.verifyNoMoreInteractions
import com.nhaarman.mockitokotlin2.verifyZeroInteractions
import kotlinx.coroutines.runBlocking
import nl.jovmit.lyrics.InstantTaskExecutorExtension
import nl.jovmit.lyrics.common.TestCoroutineDispatchers
import nl.jovmit.lyrics.main.auth.AuthenticationRepository
import nl.jovmit.lyrics.main.auth.CredentialsValidator
import nl.jovmit.lyrics.main.data.result.CredentialsValidationResult
import nl.jovmit.lyrics.main.data.result.CredentialsValidationResult.*
import nl.jovmit.lyrics.main.data.result.LoginResult
import nl.jovmit.lyrics.main.data.user.LoginData
import nl.jovmit.lyrics.main.preferences.PreferencesManager
import nl.jovmit.lyrics.utils.UserBuilder.Companion.aUser
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension

@ExtendWith(MockitoExtension::class, InstantTaskExecutorExtension::class)
class LoginViewModelShould {

    @Mock
    private lateinit var authRepository: AuthenticationRepository

    @Mock
    private lateinit var credentialsValidator: CredentialsValidator

    @Mock
    private lateinit var credentialsValidationObserver: Observer<CredentialsValidationResult>

    @Mock
    private lateinit var loginObserver: Observer<LoginResult>

    @Mock
    private lateinit var preferencesManager: PreferencesManager

    private val username = "::username::"
    private val password = "::password::"
    private val user = aUser().build()
    private val loginData = LoginData(username, password)
    private val loggedIn = LoginResult.LoggedIn(user)

    private lateinit var viewModel: LoginViewModel

    @BeforeEach
    fun setUp() {
        val dispatchers = TestCoroutineDispatchers()
        viewModel = LoginViewModel(credentialsValidator, authRepository, preferencesManager, dispatchers)
        viewModel.credentialsValidationLiveData().observeForever(credentialsValidationObserver)
        viewModel.loginLiveData().observeForever(loginObserver)
    }

    @Test
    fun perform_login_for_valid_credentials() {
        given(credentialsValidator.validate(username, password)).willReturn(Valid)

        viewModel.login(username, password)

        verifyBlocking(authRepository) { login(loginData) }
    }

    @Test
    fun not_perform_login_for_invalid_credentials() {
        given(credentialsValidator.validate(username, password)).willReturn(EmptyPassword)

        viewModel.login(username, password)

        verifyNoMoreInteractions(authRepository)
    }

    @Test
    fun return_empty_username_error() {
        given(credentialsValidator.validate(username, password)).willReturn(EmptyUsername)

        viewModel.login(username, password)

        verify(credentialsValidationObserver).onChanged(EmptyUsername)
    }

    @Test
    fun return_empty_password_error() {
        given(credentialsValidator.validate(username, password)).willReturn(EmptyPassword)

        viewModel.login(username, password)

        verify(credentialsValidationObserver).onChanged(EmptyPassword)
    }

    @Test
    fun return_logged_in_user() = runBlocking {
        given(credentialsValidator.validate(username, password)).willReturn(Valid)
        given(authRepository.login(LoginData(username, password))).willReturn(loggedIn)

        viewModel.login(username, password)

        verify(loginObserver).onChanged(loggedIn)
    }

    @Test
    fun persist_logged_in_user() = runBlocking<Unit> {
        given(credentialsValidator.validate(username, password)).willReturn(Valid)
        given(authRepository.login(LoginData(username, password))).willReturn(loggedIn)

        viewModel.login(username, password)

        verify(preferencesManager).loggedInUser(user)
    }
}
