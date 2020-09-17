package nl.jovmit.lyrics.main.register

import androidx.lifecycle.Observer
import com.nhaarman.mockitokotlin2.inOrder
import com.nhaarman.mockitokotlin2.whenever
import nl.jovmit.lyrics.InstantTaskExecutorExtension
import nl.jovmit.lyrics.common.TestCoroutineDispatchers
import nl.jovmit.lyrics.main.auth.AuthenticationRepository
import nl.jovmit.lyrics.main.auth.CredentialsValidator
import nl.jovmit.lyrics.main.auth.InMemoryAuthService
import nl.jovmit.lyrics.main.data.result.RegisterResult
import nl.jovmit.lyrics.main.data.user.User
import nl.jovmit.lyrics.utils.IdGenerator
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import java.util.*

@ExtendWith(MockitoExtension::class, InstantTaskExecutorExtension::class)
class RegisterFeature {

    @Mock
    private lateinit var registrationObserver: Observer<RegisterResult>

    @Mock
    private lateinit var idGenerator: IdGenerator

    private val userId = UUID.randomUUID().toString()
    private val username = "username"
    private val password = "asd123jh12j3h"
    private val about = "about"
    private val user = User(userId, username, password, about)
    private val startLoading = RegisterResult.Loading(true)
    private val stopLoading = RegisterResult.Loading(false)
    private val registered = RegisterResult.Registered(user)

    private lateinit var registerViewModel: RegisterViewModel

    @BeforeEach
    fun setUp() {
        val credentialsValidator = CredentialsValidator()
        val authService = InMemoryAuthService(idGenerator)
        val authRepository = AuthenticationRepository(authService)
        val dispatchers = TestCoroutineDispatchers()
        registerViewModel = RegisterViewModel(credentialsValidator, authRepository, dispatchers)
        registerViewModel.registrationLiveData().observeForever(registrationObserver)
        whenever(idGenerator.next()).thenReturn(userId)
    }

    @Test
    fun should_perform_registration() {

        registerViewModel.register(username, password, about)

        val inOrder = inOrder(registrationObserver)
        inOrder.verify(registrationObserver).onChanged(startLoading)
        inOrder.verify(registrationObserver).onChanged(registered)
        inOrder.verify(registrationObserver).onChanged(stopLoading)
    }
}
