package nl.jovmit.lyrics.main.auth

import nl.jovmit.lyrics.main.data.result.RegisterResult
import nl.jovmit.lyrics.main.data.user.RegistrationData
import nl.jovmit.lyrics.main.exceptions.NetworkUnavailableException
import nl.jovmit.lyrics.main.exceptions.UsernameTakenException

class AuthenticationRepository(
    private val authService: AuthenticationService
) {

    suspend fun registerUser(registrationData: RegistrationData): RegisterResult {
        return try {
            val user = authService.createUser(registrationData)
            RegisterResult.Registered(user)
        } catch (usernameTakenException: UsernameTakenException) {
            RegisterResult.UsernameTakenError
        } catch (networkUnavailableException: NetworkUnavailableException) {
            RegisterResult.OfflineError
        }
    }
}
