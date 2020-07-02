package nl.jovmit.lyrics.main.register

import androidx.lifecycle.Observer
import com.nhaarman.mockitokotlin2.given
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.verifyBlocking
import com.nhaarman.mockitokotlin2.verifyZeroInteractions
import kotlinx.coroutines.runBlocking
import nl.jovmit.lyrics.InstantTaskExecutorExtension
import nl.jovmit.lyrics.common.TestCoroutineDispatchers
import nl.jovmit.lyrics.main.auth.AuthenticationRepository
import nl.jovmit.lyrics.main.auth.CredentialsValidator
import nl.jovmit.lyrics.main.data.result.CredentialsValidationResult
import nl.jovmit.lyrics.main.data.result.CredentialsValidationResult.*
import nl.jovmit.lyrics.main.data.result.RegisterResult
import nl.jovmit.lyrics.main.data.result.RegisterResult.Registered
import nl.jovmit.lyrics.main.data.user.RegistrationData
import nl.jovmit.lyrics.main.data.user.User
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import java.util.*


@ExtendWith(MockitoExtension::class, InstantTaskExecutorExtension::class)
class RegisterViewModelShould {

    @Mock
    private lateinit var credentialsValidator: CredentialsValidator

    @Mock
    private lateinit var authRepository: AuthenticationRepository

    @Mock
    private lateinit var credentialsValidationObserver: Observer<CredentialsValidationResult>

    @Mock
    private lateinit var registrationLiveDataObserver: Observer<RegisterResult>

    private val userId = UUID.randomUUID().toString()
    private val username = "username"
    private val password = "password"
    private val about = "about"
    private val registrationData = RegistrationData(username, password, about)
    private val user = User(userId, username, about)
    private val registered = Registered(user)

    private lateinit var viewModel: RegisterViewModel

    @BeforeEach
    fun setUp() {
        val dispatchers = TestCoroutineDispatchers()
        viewModel = RegisterViewModel(credentialsValidator, authRepository, dispatchers)
        viewModel.credentialsValidationLiveData().observeForever(credentialsValidationObserver)
        viewModel.registrationLiveData().observeForever(registrationLiveDataObserver)
    }

    @Test
    fun perform_registration_for_valid_credentials() {
        given(credentialsValidator.validate(username, password)).willReturn(Valid)

        viewModel.register(username, password, about)

        verifyBlocking(authRepository) { registerUser(registrationData) }
    }

    @Test
    fun not_perform_registration_for_invalid_credentials() {
        given(credentialsValidator.validate(username, password)).willReturn(EmptyUsername)

        viewModel.register(username, password, about)

        verifyZeroInteractions(authRepository)
    }

    @Test
    fun return_empty_username_error() {
        given(credentialsValidator.validate(username, password)).willReturn(EmptyUsername)

        viewModel.register(username, password, about)

        verify(credentialsValidationObserver).onChanged(EmptyUsername)
    }

    @Test
    fun return_empty_password_error() {
        given(credentialsValidator.validate(username, password)).willReturn(EmptyPassword)

        viewModel.register(username, password, about)

        verify(credentialsValidationObserver).onChanged(EmptyPassword)
    }

    @Test
    fun return_registered_user() = runBlocking {
        given(credentialsValidator.validate(username, password)).willReturn(Valid)
        given(authRepository.registerUser(registrationData)).willReturn(registered)

        viewModel.register(username, password, about)

        verify(registrationLiveDataObserver).onChanged(registered)
    }
}