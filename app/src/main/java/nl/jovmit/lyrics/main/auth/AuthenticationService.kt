package nl.jovmit.lyrics.main.auth

import nl.jovmit.lyrics.main.data.user.LoginData
import nl.jovmit.lyrics.main.data.user.RegistrationData
import nl.jovmit.lyrics.main.data.user.User

interface AuthenticationService {

    suspend fun createUser(registrationData: RegistrationData): User

    suspend fun login(loginData: LoginData): User
}
