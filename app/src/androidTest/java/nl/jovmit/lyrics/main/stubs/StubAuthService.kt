package nl.jovmit.lyrics.main.stubs

import nl.jovmit.lyrics.main.auth.AuthenticationService
import nl.jovmit.lyrics.main.data.user.LoginData
import nl.jovmit.lyrics.main.data.user.RegistrationData
import nl.jovmit.lyrics.main.data.user.User

open class StubAuthService : AuthenticationService {

    override suspend fun createUser(registrationData: RegistrationData): User {
        TODO("not implemented")
    }

    override suspend fun login(loginData: LoginData): User {
        TODO("not implemented")
    }
}
