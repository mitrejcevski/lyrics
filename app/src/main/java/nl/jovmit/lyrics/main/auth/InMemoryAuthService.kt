package nl.jovmit.lyrics.main.auth

import nl.jovmit.lyrics.main.data.user.RegistrationData
import nl.jovmit.lyrics.main.data.user.User
import nl.jovmit.lyrics.main.exceptions.UsernameTakenException
import nl.jovmit.lyrics.utils.IdGenerator

class InMemoryAuthService(
    private val idGenerator: IdGenerator
) : AuthenticationService {

    private val users = mutableListOf<User>()

    override suspend fun createUser(registrationData: RegistrationData): User {
        validate(registrationData)
        val userId = idGenerator.next()
        val user = User(userId, registrationData.username, registrationData.about)
        users.add(user)
        return user
    }

    private fun validate(registrationData: RegistrationData) {
        if (users.any { it.username == registrationData.username }) {
            throw UsernameTakenException()
        }
    }
}
