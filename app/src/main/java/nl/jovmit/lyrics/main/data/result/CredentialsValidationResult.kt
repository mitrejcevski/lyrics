package nl.jovmit.lyrics.main.data.result

sealed class CredentialsValidationResult {

    object EmptyUsername : CredentialsValidationResult()

    object EmptyPassword : CredentialsValidationResult()

    object Valid : CredentialsValidationResult()
}
