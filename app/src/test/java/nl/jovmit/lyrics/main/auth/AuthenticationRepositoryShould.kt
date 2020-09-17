package nl.jovmit.lyrics.main.auth

import com.nhaarman.mockitokotlin2.given
import kotlinx.coroutines.runBlocking
import nl.jovmit.lyrics.main.data.result.LoginResult
import nl.jovmit.lyrics.main.data.result.RegisterResult
import nl.jovmit.lyrics.main.data.user.LoginData
import nl.jovmit.lyrics.main.exceptions.NetworkUnavailableException
import nl.jovmit.lyrics.main.exceptions.UserNotFoundException
import nl.jovmit.lyrics.main.exceptions.UsernameTakenException
import nl.jovmit.lyrics.utils.RegistrationDataBuilder.Companion.aRegistrationData
import nl.jovmit.lyrics.utils.UserBuilder.Companion.aUser
import org.junit.Assert.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension

@ExtendWith(MockitoExtension::class)
class AuthenticationRepositoryShould {

    @Mock
    private lateinit var authService: AuthenticationService

    private val user = aUser().build()
    private val registrationData = aRegistrationData().build()
    private val loginData = LoginData(registrationData.username, registrationData.password)

    private lateinit var authRepository: AuthenticationRepository

    @BeforeEach
    fun setUp() {
        authRepository = AuthenticationRepository(authService)
    }

    @Test
    fun register_a_user() = runBlocking {
        given(authService.createUser(registrationData)).willReturn(user)

        val result = authRepository.registerUser(registrationData)

        assertEquals(RegisterResult.Registered(user), result)
    }

    @Test
    fun return_error_if_username_is_taken() = runBlocking {
        given(authService.createUser(registrationData)).willThrow(UsernameTakenException::class.java)

        val result = authRepository.registerUser(registrationData)

        assertEquals(RegisterResult.UsernameTakenError, result)
    }

    @Test
    fun return_offline_error_while_registering() = runBlocking {
        given(authService.createUser(registrationData)).willThrow(NetworkUnavailableException::class.java)

        val result = authRepository.registerUser(registrationData)

        assertEquals(RegisterResult.OfflineError, result)
    }

    @Test
    fun login_a_user() = runBlocking {
        given(authService.login(loginData)).willReturn(user)

        val result = authRepository.login(loginData)

        assertEquals(LoginResult.LoggedIn(user), result)
    }

    @Test
    fun return_error_for_non_existent_user() = runBlocking {
        given(authService.login(loginData)).willThrow(UserNotFoundException::class.java)

        val result = authRepository.login(loginData)

        assertEquals(LoginResult.UserNotFoundError, result)
    }

    @Test
    fun return_offline_error_while_logging_in() = runBlocking {
        given(authService.login(loginData)).willThrow(NetworkUnavailableException::class.java)

        val result = authRepository.login(loginData)

        assertEquals(LoginResult.Offline, result)
    }
}
