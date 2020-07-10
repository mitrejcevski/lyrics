package nl.jovmit.lyrics.main.auth

import nl.jovmit.lyrics.main.data.user.RegistrationData
import nl.jovmit.lyrics.main.data.user.User
import nl.jovmit.lyrics.utils.IdGenerator

class InMemoryAuthService(
    private val idGenerator: IdGenerator
) : AuthenticationService {

    override suspend fun createUser(registrationData: RegistrationData): User {
        val userId = idGenerator.next()
        return User(userId, registrationData.username, registrationData.about)
    }
}
