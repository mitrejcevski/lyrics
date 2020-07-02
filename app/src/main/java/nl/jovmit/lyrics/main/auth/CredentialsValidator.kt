package nl.jovmit.lyrics.main.auth

import nl.jovmit.lyrics.main.data.result.CredentialsValidationResult
import nl.jovmit.lyrics.main.data.result.CredentialsValidationResult.*

class CredentialsValidator {

    fun validate(username: String, password: String): CredentialsValidationResult {
        return when {
            username.isBlank() -> EmptyUsername
            password.isBlank() -> EmptyPassword
            else -> Valid
        }
    }
}
