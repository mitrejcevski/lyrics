package nl.jovmit.lyrics.utils

import nl.jovmit.lyrics.main.data.user.RegistrationData

class RegistrationDataBuilder {

    companion object {

        fun aRegistrationData(): RegistrationDataBuilder {
            return RegistrationDataBuilder()
        }
    }

    private var username = "username"
    private var password = "asd!23ras$#sd324"
    private var about = "about"

    fun withUsername(username: String): RegistrationDataBuilder {
        this.username = username
        return this
    }

    fun withPassword(password: String): RegistrationDataBuilder {
        this.password = password
        return this
    }

    fun withAbout(about: String): RegistrationDataBuilder {
        this.about = about
        return this
    }

    fun build(): RegistrationData {
        return RegistrationData(username, password, about)
    }
}
