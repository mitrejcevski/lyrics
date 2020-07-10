package nl.jovmit.lyrics.main.preferences

import nl.jovmit.lyrics.main.data.user.User

class InMemoryPreferencesManagerShould : PreferencesManagerContract() {

    override fun preferencesManagerWith(user: User?): PreferencesManager {
        val manager = InMemoryPreferencesManager()
        user?.let { manager.loggedInUser(it) }
        return manager
    }
}