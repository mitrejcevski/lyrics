package nl.jovmit.lyrics.main.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import nl.jovmit.lyrics.common.CoroutineDispatchers
import nl.jovmit.lyrics.common.CoroutineViewModel
import nl.jovmit.lyrics.main.auth.AuthenticationRepository
import nl.jovmit.lyrics.main.auth.CredentialsValidator
import nl.jovmit.lyrics.main.data.result.CredentialsValidationResult
import nl.jovmit.lyrics.main.data.result.RegisterResult
import nl.jovmit.lyrics.main.data.user.RegistrationData

class RegisterViewModel(
    private val credentialsValidator: CredentialsValidator,
    private val authRepository: AuthenticationRepository,
    private val dispatchers: CoroutineDispatchers
) : CoroutineViewModel(dispatchers) {

    private val credentialsValidationLiveData = MutableLiveData<CredentialsValidationResult>()
    private val registrationLiveData = MutableLiveData<RegisterResult>()

    fun credentialsValidationLiveData(): LiveData<CredentialsValidationResult> {
        return credentialsValidationLiveData
    }

    fun registrationLiveData(): LiveData<RegisterResult> {
        return registrationLiveData
    }

    fun register(username: String, password: String, about: String) {
        val validationResult = credentialsValidator.validate(username, password)
        if (validationResult == CredentialsValidationResult.Valid) {
            performRegistration(username, password, about)
        } else {
            credentialsValidationLiveData.value = validationResult
        }
    }

    private fun performRegistration(username: String, password: String, about: String) {
        launch {
            registrationLiveData.value = RegisterResult.Loading(true)
            val result = withContext(dispatchers.background) {
                val registerData = RegistrationData(username, password, about)
                authRepository.registerUser(registerData)
            }
            registrationLiveData.value = result
            registrationLiveData.value = RegisterResult.Loading(false)
        }
    }
}
