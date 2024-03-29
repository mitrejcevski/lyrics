package nl.jovmit.lyrics.main.auth

import kotlinx.coroutines.runBlocking
import nl.jovmit.lyrics.main.data.user.LoginData
import nl.jovmit.lyrics.main.data.user.RegistrationData
import nl.jovmit.lyrics.main.exceptions.NetworkUnavailableException
import nl.jovmit.lyrics.main.exceptions.UserNotFoundException
import nl.jovmit.lyrics.main.exceptions.UsernameTakenException
import nl.jovmit.lyrics.utils.RegistrationDataBuilder.Companion.aRegistrationData
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.util.regex.Pattern

abstract class AuthServiceContract {

    private val uuidPattern =
        Pattern.compile("[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}")
    private val registrationData = aRegistrationData().build()
    private val loginData = LoginData(registrationData.username, registrationData.password)

    @Test
    fun create_a_user() = runBlocking {
        val service = authServiceWithout(registrationData)

        val result = service.createUser(registrationData)

        assertTrue(uuidPattern.matcher(result.userId).matches())
        assertEquals(registrationData.username, result.username)
        assertEquals(registrationData.about, result.about)
    }

    @Test
    fun throw_username_taken_exception() = runBlocking<Unit> {
        val service = authServiceWith(registrationData)

        assertThrows<UsernameTakenException> {
            runBlocking { service.createUser(registrationData) }
        }
    }

    @Test
    fun throw_offline_exception_when_registration() = runBlocking<Unit> {
        val emptyRegistrationData = aRegistrationData()
            .withUsername("")
            .withPassword("")
            .withAbout("")
            .build()
        val service = authServiceWithout(registrationData)
        assertThrows<NetworkUnavailableException> {
            runBlocking { service.createUser(emptyRegistrationData) }
        }
    }

    @Test
    fun login_a_user() = runBlocking {
        val service = authServiceWith(loginData)

        val result = service.login(loginData)

        assertTrue(uuidPattern.matcher(result.userId).matches())
        assertEquals(loginData.username, result.username)
    }

    @Test
    fun throw_user_not_found_for_incorrect_username_while_logging_in() = runBlocking<Unit> {
        val service = authServiceWithoutUsernameLike(loginData)

        assertThrows<UserNotFoundException> {
            runBlocking { service.login(loginData) }
        }
    }

    @Test
    fun throw_user_not_found_for_incorrect_password_while_logging_in() = runBlocking<Unit> {
        val service = authServiceWithoutPasswordLike(loginData)

        assertThrows<UserNotFoundException> {
            runBlocking { service.login(loginData) }
        }
    }

    @Test
    fun throw_offline_exception_when_logging_in() = runBlocking<Unit> {
        val emptyLoginData = LoginData("", "")
        val service = authServiceWithout(loginData)

        assertThrows<NetworkUnavailableException> {
            runBlocking { service.login(emptyLoginData) }
        }
    }

    abstract suspend fun authServiceWith(registrationData: RegistrationData): AuthenticationService

    abstract suspend fun authServiceWithout(registrationData: RegistrationData): AuthenticationService

    abstract suspend fun authServiceWith(loginData: LoginData): AuthenticationService

    abstract suspend fun authServiceWithout(loginData: LoginData): AuthenticationService

    abstract suspend fun authServiceWithoutUsernameLike(loginData: LoginData): AuthenticationService

    abstract suspend fun authServiceWithoutPasswordLike(loginData: LoginData): AuthenticationService
}
