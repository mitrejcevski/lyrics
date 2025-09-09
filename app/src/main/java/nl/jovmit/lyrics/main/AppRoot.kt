package nl.jovmit.lyrics.main

import androidx.compose.animation.AnimatedContentTransitionScope.SlideDirection.Companion.Left
import androidx.compose.animation.AnimatedContentTransitionScope.SlideDirection.Companion.Right
import androidx.compose.animation.core.tween
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import nl.jovmit.lyrics.main.add.addNewSongScreen
import nl.jovmit.lyrics.main.add.navigateToNewSong
import nl.jovmit.lyrics.main.details.navigateToSongDetails
import nl.jovmit.lyrics.main.details.songDetailsScreen
import nl.jovmit.lyrics.main.edit.editSongScreen
import nl.jovmit.lyrics.main.edit.navigateToSongEditor
import nl.jovmit.lyrics.main.login.loginScreen
import nl.jovmit.lyrics.main.login.navigateToLogin
import nl.jovmit.lyrics.main.overview.navigateToSongsOverview
import nl.jovmit.lyrics.main.overview.songsOverviewScreen
import nl.jovmit.lyrics.main.preferences.UserPreferencesViewModel
import nl.jovmit.lyrics.main.register.RegistrationDestination
import nl.jovmit.lyrics.main.register.navigateToRegistration
import nl.jovmit.lyrics.main.register.registrationScreen
import org.koin.androidx.compose.koinViewModel

@Composable
fun AppRoot(
  viewModel: UserPreferencesViewModel = koinViewModel()
) {

  val navController = rememberNavController()

  LaunchedEffect(Unit) {
    val loggedInUser = viewModel.getLoggedInUser()
    loggedInUser?.let {
      navController.navigateToSongsOverview()
    }
  }

  NavHost(
    modifier = Modifier.fillMaxSize(),
    navController = navController,
    startDestination = RegistrationDestination,
    enterTransition = { slideIntoContainer(towards = Left, animationSpec = tween()) },
    exitTransition = { scaleOut(targetScale = .9f, animationSpec = tween()) },
    popEnterTransition = { scaleIn(initialScale = .9f, animationSpec = tween()) },
    popExitTransition = { slideOutOfContainer(towards = Right, animationSpec = tween()) },
  ) {
    registrationScreen(
      onRegistered = { navController.navigateToSongsOverview() },
      onHaveAnAccount = { navController.navigateToLogin() }
    )
    loginScreen(
      onNavigateUp = { navController.navigateUp() },
      onLoggedIn = { navController.navigateToSongsOverview() }
    )
    songsOverviewScreen(
      onLoggedOut = { navController.navigateToRegistration() },
      onAddNewSong = { navController.navigateToNewSong() },
      onOpenSongDetails = { songId -> navController.navigateToSongDetails(songId) }
    )
    addNewSongScreen(
      onNavigateUp = { navController.navigateUp() }
    )
    editSongScreen(
      onNavigateUp = { navController.navigateUp() }
    )
    songDetailsScreen(
      onEditSong = { songId -> navController.navigateToSongEditor(songId) },
      onNavigateUp = { navController.navigateUp() }
    )
  }
}