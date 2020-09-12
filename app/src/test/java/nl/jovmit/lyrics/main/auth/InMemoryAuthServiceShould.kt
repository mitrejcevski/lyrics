package nl.jovmit.lyrics.main.auth

import com.nhaarman.mockitokotlin2.given
import kotlinx.coroutines.runBlocking
import nl.jovmit.lyrics.main.exceptions.UsernameTakenException
import nl.jovmit.lyrics.utils.IdGenerator
import nl.jovmit.lyrics.utils.RegistrationDataBuilder.Companion.aRegistrationData
import nl.jovmit.lyrics.utils.UserBuilder.Companion.aUser
import org.junit.Assert.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import java.util.*

@ExtendWith(MockitoExtension::class)
class InMemoryAuthServiceShould {

    @Mock
    private lateinit var idGenerator: IdGenerator

    private val userId = UUID.randomUUID().toString()
    private val registrationData = aRegistrationData().build()
    private val user = aUser().withUserId(userId).build()

    private lateinit var service: InMemoryAuthService

    @BeforeEach
    fun setUp() {
        service = InMemoryAuthService(idGenerator)
    }

    @Test
    fun create_a_user() = runBlocking {
        given(idGenerator.next()).willReturn(userId)

        val result = service.createUser(registrationData)

        assertEquals(user, result)
    }

    @Test
    fun throw_username_taken_exception() = runBlocking<Unit> {
        given(idGenerator.next()).willReturn(userId)
        service.createUser(registrationData)

        assertThrows<UsernameTakenException> {
            runBlocking { service.createUser(registrationData) }
        }
    }
}
