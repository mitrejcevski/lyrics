package nl.jovmit.lyrics.main.auth

import nl.jovmit.lyrics.main.data.user.RegistrationData
import nl.jovmit.lyrics.main.data.user.User

class InMemoryAuthService : AuthenticationService {

    override suspend fun createUser(registrationData: RegistrationData): User {
        TODO("not implemented")
    }
}
