package nl.jovmit.lyrics.main.login

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
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
fun LoginScreen(
  viewModel: LoginViewModel = koinViewModel(),
  onNavigateUp: () -> Unit,
  onLoggedIn: () -> Unit
) {
  val uiState by viewModel.uiState.collectAsStateWithLifecycle()

  LaunchedEffect(uiState.isLoggedIn) {
    if (uiState.isLoggedIn) {
      onLoggedIn()
    }
  }

  LoginScreenContent(
    state = uiState,
    onUsernameUpdated = viewModel::updateUsername,
    onPasswordUpdated = viewModel::updatePassword,
    onLogin = viewModel::login,
    onDismissLoginError = viewModel::dismissLoginError,
    onDismissOfflineError = viewModel::dismissOfflineError,
    onNavigateUp = onNavigateUp
  )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun LoginScreenContent(
  state: LoginScreenState,
  onUsernameUpdated: (newValue: String) -> Unit,
  onPasswordUpdated: (newValue: String) -> Unit,
  onLogin: () -> Unit,
  onDismissLoginError: () -> Unit,
  onDismissOfflineError: () -> Unit,
  onNavigateUp: () -> Unit
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
        navigationIcon = {
          IconButton(
            modifier = Modifier.testTag("navigateUp"),
            onClick = onNavigateUp
          ) {
            Icon(
              imageVector = Icons.AutoMirrored.Filled.ArrowBack,
              contentDescription = stringResource(R.string.cdNavigateUp)
            )
          }
        },
        title = {
          Text(
            text = stringResource(R.string.login),
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
          .fillMaxSize()
          .padding(AppTheme.size.l),
      ) {
        val passwordFocusRequester = FocusRequester()
        val focusManager = LocalFocusManager.current
        SingleLineTextInput(
          modifier = Modifier.fillMaxWidth(),
          value = state.username,
          onValueChanged = onUsernameUpdated,
          labelValue = stringResource(R.string.username),
          errorLabel = if (state.isEmptyUsernameError) stringResource(R.string.errorEmptyUsername) else null,
          keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Next
          ),
          keyboardActions = KeyboardActions(
            onNext = { passwordFocusRequester.requestFocus() }
          )
        )
        PasswordInput(
          modifier = Modifier.fillMaxWidth().focusRequester(passwordFocusRequester),
          value = state.password,
          onValueChanged = onPasswordUpdated,
          errorLabel = if (state.isEmptyPasswordError) stringResource(R.string.errorEmptyPassword) else null,
          keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Done
          ),
          keyboardActions = KeyboardActions(
            onDone = {
              focusManager.clearFocus()
              onLogin()
            }
          ),
        )
        Spacer(Modifier.height(AppTheme.size.s))
        PrimaryButton(
          modifier = Modifier.fillMaxWidth(),
          onClick = {
            focusManager.clearFocus()
            onLogin()
          },
          labelValue = stringResource(R.string.login),
          showLoading = state.isLoading
        )
      }
      ErrorBanner(
        modifier = Modifier.fillMaxWidth(),
        visible = state.isErrorLoggingIn,
        label = stringResource(R.string.errorIncorrectCredentials),
        onDismiss = onDismissLoginError
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
private fun PreviewLoginScreen() {
  AppTheme {
    LoginScreenContent(
      state = LoginScreenState(),
      onUsernameUpdated = {},
      onPasswordUpdated = {},
      onLogin = {},
      onDismissLoginError = {},
      onDismissOfflineError = {},
      onNavigateUp = {}
    )
  }
}