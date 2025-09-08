package nl.jovmit.lyrics.main.register

data class RegisterScreenState(
  val isLoading: Boolean = false,
  val username: String = "",
  val password: String = "",
  val aboutMe: String = "",
  val isEmptyUsernameError: Boolean = false,
  val isEmptyPasswordError: Boolean = false,
  val isRegistrationError: Boolean = false,
  val isOfflineError: Boolean = false,
  val isRegistered: Boolean = false
)