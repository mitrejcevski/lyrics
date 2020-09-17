package nl.jovmit.lyrics.main.login

import androidx.lifecycle.Observer
import com.nhaarman.mockitokotlin2.inOrder
import nl.jovmit.lyrics.InstantTaskExecutorExtension
import nl.jovmit.lyrics.common.TestCoroutineDispatchers
import nl.jovmit.lyrics.main.auth.AuthenticationRepository
import nl.jovmit.lyrics.main.auth.CredentialsValidator
import nl.jovmit.lyrics.main.auth.InMemoryAuthService
import nl.jovmit.lyrics.main.data.result.LoginResult
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

    private val username = "username"
    private val password = "asd123jh12j3h"
    private val user = aUser().withUsername(username).build()
    private val startLoading = LoginResult.Loading(true)
    private val loggedIn = LoginResult.LoggedIn(user)
    private val stopLoading = LoginResult.Loading(false)

    private lateinit var loginViewModel: LoginViewModel

    @BeforeEach
    fun setUp() {
        val credentialsValidator = CredentialsValidator()
        val idGenerator = IdGenerator()
        val authService = InMemoryAuthService(idGenerator)
        val authRepository = AuthenticationRepository(authService)
        val dispatchers = TestCoroutineDispatchers()
        loginViewModel = LoginViewModel(credentialsValidator, authRepository, dispatchers)
        loginViewModel.loginLiveData().observeForever(loginObserver)
    }

    @Test
    fun should_perform_login() {

        loginViewModel.login(username, password)

        val inOrder = inOrder(loginObserver)
        inOrder.verify(loginObserver).onChanged(startLoading)
        inOrder.verify(loginObserver).onChanged(loggedIn)
        inOrder.verify(loginObserver).onChanged(stopLoading)
    }
}
