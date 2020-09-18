package nl.jovmit.lyrics.main.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import nl.jovmit.lyrics.common.CoroutineDispatchers
import nl.jovmit.lyrics.common.CoroutineViewModel
import nl.jovmit.lyrics.main.auth.AuthenticationRepository
import nl.jovmit.lyrics.main.auth.CredentialsValidator
import nl.jovmit.lyrics.main.data.result.CredentialsValidationResult
import nl.jovmit.lyrics.main.data.result.LoginResult
import nl.jovmit.lyrics.main.data.user.LoginData
import nl.jovmit.lyrics.main.data.user.User
import nl.jovmit.lyrics.main.preferences.PreferencesManager

class LoginViewModel(
    private val credentialsValidator: CredentialsValidator,
    private val authRepository: AuthenticationRepository,
    private val preferencesManager: PreferencesManager,
    private val dispatchers: CoroutineDispatchers
) : CoroutineViewModel(dispatchers) {

    private val loginLiveData = MutableLiveData<LoginResult>()
    private val credentialsValidationLiveData = MutableLiveData<CredentialsValidationResult>()

    fun loginLiveData(): LiveData<LoginResult> {
        return loginLiveData
    }

    fun credentialsValidationLiveData(): LiveData<CredentialsValidationResult> {
        return credentialsValidationLiveData
    }

    fun login(username: String, password: String) {
        val validationResult = credentialsValidator.validate(username, password)
        if (validationResult == CredentialsValidationResult.Valid) {
            performLogin(username, password)
        } else {
            credentialsValidationLiveData.value = validationResult
        }
    }

    private fun performLogin(username: String, password: String) {
        launch {
            loginLiveData.value = LoginResult.Loading(true)
            val result = withContext(dispatchers.background) {
                val loginData = LoginData(username, password)
                authRepository.login(loginData)
            }
            if (result is LoginResult.LoggedIn) persistLoggedInUser(result.user)
            loginLiveData.value = result
            loginLiveData.value = LoginResult.Loading(false)
        }
    }

    private fun persistLoggedInUser(user: User) {
        preferencesManager.loggedInUser(user)
    }
}
