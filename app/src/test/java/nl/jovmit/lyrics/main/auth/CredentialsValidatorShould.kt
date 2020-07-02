package nl.jovmit.lyrics.main.auth

import nl.jovmit.lyrics.main.data.result.CredentialsValidationResult.*
import org.junit.Assert.assertEquals
import org.junit.jupiter.api.Test

class CredentialsValidatorShould {

    private val validator = CredentialsValidator()

    @Test
    fun return_empty_username() {
        assertEquals(EmptyUsername, validator.validate("", "password"))
        assertEquals(EmptyUsername, validator.validate("  ", "password"))
    }

    @Test
    fun return_empty_password() {
        assertEquals(EmptyPassword, validator.validate("username", ""))
        assertEquals(EmptyPassword, validator.validate("username", " "))
    }

    @Test
    fun return_valid() {
        assertEquals(Valid, validator.validate("username", "password"))
    }
}