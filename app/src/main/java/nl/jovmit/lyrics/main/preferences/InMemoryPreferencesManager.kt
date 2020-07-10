package nl.jovmit.lyrics.main.preferences

import nl.jovmit.lyrics.main.data.user.User

class InMemoryPreferencesManager : PreferencesManager {

    private var loggedInUser: User? = null

    override fun loggedInUser(): User? {
        return loggedInUser
    }

    override fun loggedInUser(user: User) {
        this.loggedInUser = user
    }
}