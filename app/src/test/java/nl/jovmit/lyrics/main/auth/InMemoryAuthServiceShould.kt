package nl.jovmit.lyrics.main.auth

import nl.jovmit.lyrics.main.data.user.RegistrationData
import nl.jovmit.lyrics.utils.IdGenerator
import nl.jovmit.lyrics.utils.RegistrationDataBuilder.Companion.aRegistrationData

class InMemoryAuthServiceShould : AuthServiceContract() {

    private val idGenerator = IdGenerator()

    override suspend fun authServiceWithout(
        registrationData: RegistrationData
    ): AuthenticationService {
        val differentRegistrationData = aRegistrationData()
            .withUsername(registrationData.username.reversed())
            .withPassword(registrationData.password.reversed())
            .withAbout(registrationData.about.reversed())
            .build()
        return InMemoryAuthService(idGenerator).also {
            it.createUser(differentRegistrationData)
        }
    }

    override suspend fun authServiceWith(
        registrationData: RegistrationData
    ): AuthenticationService {
        return InMemoryAuthService(idGenerator).also {
            it.createUser(registrationData)
        }
    }
}
