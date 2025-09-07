package nl.jovmit.lyrics.main.login

data class LoginScreenState(
  val isLoading: Boolean = false,
  val username: String = "",
  val password: String = "",
  val isEmptyUsernameError: Boolean = false,
  val isEmptyPasswordError: Boolean = false,
  val isErrorLoggingIn: Boolean = false,
  val isOfflineError: Boolean = false,
  val isLoggedIn: Boolean = false
)