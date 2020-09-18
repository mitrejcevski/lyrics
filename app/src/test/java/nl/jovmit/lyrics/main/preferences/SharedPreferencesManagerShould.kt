package nl.jovmit.lyrics.main.preferences

import nl.jovmit.lyrics.main.data.user.User

class SharedPreferencesManagerShould : PreferencesManagerContract() {

    private val preferenceManager = MockSharedPreferences()

    override fun preferencesManagerWith(user: User?): PreferencesManager {
        user?.let {
            preferenceManager.putString("userId", it.userId)
            preferenceManager.putString("username", it.username)
            preferenceManager.putString("password", it.password)
            preferenceManager.putString("about", it.about)
        }
        return SharedPreferencesManager(preferenceManager)
    }
}
