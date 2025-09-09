package nl.jovmit.lyrics.main.register

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import nl.jovmit.lyrics.R
import nl.jovmit.lyrics.common.composables.ErrorBanner
import nl.jovmit.lyrics.common.composables.PasswordInput
import nl.jovmit.lyrics.common.composables.PrimaryButton
import nl.jovmit.lyrics.common.composables.SingleLineTextInput
import nl.jovmit.lyrics.theme.AppTheme
import org.koin.androidx.compose.koinViewModel

@Composable
fun RegisterScreen(
  viewModel: RegisterViewModel = koinViewModel(),
  onRegistered: () -> Unit,
  onHaveAnAccount: () -> Unit
) {

  val uiState by viewModel.uiState.collectAsStateWithLifecycle()

  LaunchedEffect(uiState.isRegistered) {
    if (uiState.isRegistered) {
      onRegistered()
    }
  }

  RegisterScreenContent(
    state = uiState,
    onUpdateUsername = viewModel::updateUsername,
    onUpdatePassword = viewModel::updatePassword,
    onUpdateAboutMe = viewModel::updateAboutMe,
    onDismissRegistrationError = viewModel::dismissRegistrationError,
    onDismissOfflineError = viewModel::dismissOfflineError,
    onRegister = viewModel::register,
    onHaveAnAccount = onHaveAnAccount
  )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun RegisterScreenContent(
  state: RegisterScreenState,
  onUpdateUsername: (newUsername: String) -> Unit,
  onUpdatePassword: (newPassword: String) -> Unit,
  onUpdateAboutMe: (newAbout: String) -> Unit,
  onDismissRegistrationError: () -> Unit,
  onDismissOfflineError: () -> Unit,
  onRegister: () -> Unit,
  onHaveAnAccount: () -> Unit
) {
  Scaffold(
    containerColor = AppTheme.colorScheme.background,
    topBar = {
      CenterAlignedTopAppBar(
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
          containerColor = AppTheme.colorScheme.colorPrimary,
          titleContentColor = AppTheme.colorScheme.colorOnPrimary,
          navigationIconContentColor = AppTheme.colorScheme.colorOnPrimary,
          actionIconContentColor = AppTheme.colorScheme.colorOnPrimary
        ),
        title = {
          Text(
            text = stringResource(R.string.createUser),
            style = AppTheme.typography.title
          )
        },
      )
    }
  ) { paddingValues ->
    Box(
      modifier = Modifier
        .fillMaxSize()
        .padding(paddingValues)
    ) {
      Column(
        modifier = Modifier
          .padding(AppTheme.size.l)
          .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
      ) {
        val focusManager = LocalFocusManager.current
        val passwordFocusRequester = FocusRequester()
        SingleLineTextInput(
          modifier = Modifier
            .fillMaxWidth()
            .testTag("username"),
          value = state.username,
          onValueChanged = onUpdateUsername,
          errorLabel = if (state.isEmptyUsernameError) stringResource(R.string.errorEmptyUsername) else null,
          labelValue = stringResource(R.string.username),
          keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
          keyboardActions = KeyboardActions(onNext = { passwordFocusRequester.requestFocus() })
        )
        val aboutFocusRequester = FocusRequester()
        PasswordInput(
          modifier = Modifier
            .fillMaxWidth()
            .focusRequester(passwordFocusRequester)
            .testTag("password"),
          value = state.password,
          onValueChanged = onUpdatePassword,
          errorLabel = if (state.isEmptyPasswordError) stringResource(R.string.errorEmptyPassword) else null,
          keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
          keyboardActions = KeyboardActions(onNext = { aboutFocusRequester.requestFocus() })
        )
        SingleLineTextInput(
          modifier = Modifier
            .fillMaxWidth()
            .focusRequester(aboutFocusRequester)
            .testTag("aboutMe"),
          value = state.aboutMe,
          onValueChanged = onUpdateAboutMe,
          labelValue = stringResource(R.string.about),
          keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
          keyboardActions = KeyboardActions(
            onDone = {
              focusManager.clearFocus()
              onRegister()
            }
          )
        )
        PrimaryButton(
          modifier = Modifier.fillMaxWidth().testTag("registerAction"),
          labelValue = stringResource(R.string.createUser),
          onClick = onRegister,
          showLoading = state.isLoading
        )
        Spacer(Modifier.height(AppTheme.size.l))
        Text(
          modifier = Modifier
            .testTag("gotAccountLabel")
            .clickable { onHaveAnAccount() },
          text = stringResource(R.string.alreadyRegistered),
          style = AppTheme.typography.body,
          color = AppTheme.colorScheme.colorPrimary
        )
      }
      ErrorBanner(
        modifier = Modifier.fillMaxWidth(),
        visible = state.isRegistrationError,
        label = stringResource(R.string.errorUsernameTaken),
        onDismiss = onDismissRegistrationError
      )
      ErrorBanner(
        modifier = Modifier.fillMaxWidth(),
        visible = state.isOfflineError,
        label = stringResource(R.string.errorNoNetworkConnection),
        onDismiss = onDismissOfflineError
      )
    }
  }
}

@Composable
@PreviewLightDark
private fun PreviewRegistrationScreen() {
  AppTheme {
    RegisterScreenContent(
      state = RegisterScreenState(),
      onRegister = {},
      onHaveAnAccount = {},
      onUpdateUsername = {},
      onUpdatePassword = {},
      onUpdateAboutMe = {},
      onDismissRegistrationError = {},
      onDismissOfflineError = {}
    )
  }
}