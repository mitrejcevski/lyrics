package nl.jovmit.lyrics.main.preferences

import com.nhaarman.mockitokotlin2.verify
import nl.jovmit.lyrics.utils.UserBuilder.Companion.aUser
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension

@ExtendWith(MockitoExtension::class)
class UserPreferencesViewModelShould {

    @Mock
    private lateinit var preferencesManager: PreferencesManager

    private val user = aUser().build()

    private lateinit var userPreferencesViewModel: UserPreferencesViewModel

    @BeforeEach
    fun setUp() {
        userPreferencesViewModel = UserPreferencesViewModel(preferencesManager)
    }

    @Test
    fun return_user_from_preferences_manager() {
        userPreferencesViewModel.getLoggedInUser()

        verify(preferencesManager).loggedInUser()
    }

    @Test
    fun store_user_into_preference_manager() {
        userPreferencesViewModel.setLoggedInUser(user)

        verify(preferencesManager).loggedInUser(user)
    }

    @Test
    fun clear_logged_in_user_from_preference_manager() {
        userPreferencesViewModel.clearLoggedInUser()

        verify(preferencesManager).clearLoggedInUser()
    }
}
