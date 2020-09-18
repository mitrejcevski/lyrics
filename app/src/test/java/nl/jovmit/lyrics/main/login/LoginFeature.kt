package nl.jovmit.lyrics.main.login

import androidx.lifecycle.Observer
import com.nhaarman.mockitokotlin2.inOrder
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.runBlocking
import nl.jovmit.lyrics.InstantTaskExecutorExtension
import nl.jovmit.lyrics.common.TestCoroutineDispatchers
import nl.jovmit.lyrics.main.auth.AuthenticationRepository
import nl.jovmit.lyrics.main.auth.AuthenticationService
import nl.jovmit.lyrics.main.auth.CredentialsValidator
import nl.jovmit.lyrics.main.auth.InMemoryAuthService
import nl.jovmit.lyrics.main.data.result.LoginResult
import nl.jovmit.lyrics.main.data.user.RegistrationData
import nl.jovmit.lyrics.main.preferences.InMemoryPreferencesManager
import nl.jovmit.lyrics.utils.IdGenerator
import nl.jovmit.lyrics.utils.UserBuilder.Companion.aUser
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension

@ExtendWith(MockitoExtension::class, InstantTaskExecutorExtension::class)
class LoginFeature {

    @Mock
    private lateinit var loginObserver: Observer<LoginResult>

    @Mock
    private lateinit var idGenerator: IdGenerator

    private val user = aUser().build()
    private val startLoading = LoginResult.Loading(true)
    private val loggedIn = LoginResult.LoggedIn(user)
    private val stopLoading = LoginResult.Loading(false)

    private lateinit var authService: AuthenticationService
    private lateinit var loginViewModel: LoginViewModel

    @BeforeEach
    fun setUp() {
        val credentialsValidator = CredentialsValidator()
        authService = InMemoryAuthService(idGenerator)
        val authRepository = AuthenticationRepository(authService)
        val preferencesManager = InMemoryPreferencesManager()
        val dispatchers = TestCoroutineDispatchers()
        loginViewModel = LoginViewModel(
            credentialsValidator,
            authRepository,
            preferencesManager,
            dispatchers
        )
        loginViewModel.loginLiveData().observeForever(loginObserver)
        whenever(idGenerator.next()).thenReturn(user.userId)
    }

    @Test
    fun should_perform_login() = runBlocking {
        authService.createUser(RegistrationData(user.username, user.password, user.about))

        loginViewModel.login(user.username, user.password)

        val inOrder = inOrder(loginObserver)
        inOrder.verify(loginObserver).onChanged(startLoading)
        inOrder.verify(loginObserver).onChanged(loggedIn)
        inOrder.verify(loginObserver).onChanged(stopLoading)
    }
}
