package nl.jovmit.lyrics.main.auth

import nl.jovmit.lyrics.main.data.result.LoginResult
import nl.jovmit.lyrics.main.data.result.RegisterResult
import nl.jovmit.lyrics.main.data.user.LoginData
import nl.jovmit.lyrics.main.data.user.RegistrationData
import nl.jovmit.lyrics.main.exceptions.NetworkUnavailableException
import nl.jovmit.lyrics.main.exceptions.UserNotFoundException
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

    suspend fun login(loginData: LoginData): LoginResult {
        return try {
            val user = authService.login(loginData)
            LoginResult.LoggedIn(user)
        } catch (userNotFoundException: UserNotFoundException) {
            LoginResult.UserNotFoundError
        } catch (offlineException: NetworkUnavailableException) {
            LoginResult.Offline
        }
    }
}
