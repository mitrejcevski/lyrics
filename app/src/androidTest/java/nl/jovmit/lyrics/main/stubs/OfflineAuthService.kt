package nl.jovmit.lyrics.main.stubs

import nl.jovmit.lyrics.main.data.user.LoginData
import nl.jovmit.lyrics.main.data.user.RegistrationData
import nl.jovmit.lyrics.main.data.user.User
import nl.jovmit.lyrics.main.exceptions.NetworkUnavailableException

class OfflineAuthService : StubAuthService() {

    override suspend fun createUser(registrationData: RegistrationData): User {
        throw NetworkUnavailableException()
    }

    override suspend fun login(loginData: LoginData): User {
        throw NetworkUnavailableException()
    }
}
