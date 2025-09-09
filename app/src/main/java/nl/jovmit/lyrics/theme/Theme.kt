package nl.jovmit.lyrics.theme

import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val darkColorScheme = AppColorScheme(
  background = ColorBackgroundDark,
  colorSurface = ColorSurfaceDark,
  colorPrimary = ColorPrimaryDark,
  colorSecondary = ColorSecondaryDark,
  colorOnBackground = ColorOnBackgroundDark,
  colorOnSurface = ColorOnSurfaceDark,
  colorOnPrimary = ColorOnPrimaryDark,
  colorOnSecondary = ColorOnSecondaryDark,
  colorError = ColorError,
  colorOnError = ColorOnError,
  colorSuccess = SuccessColor,
  colorOnSuccess = InfoMessageTextColor
)

private val lightColorScheme = AppColorScheme(
  background = ColorBackground,
  colorSurface = ColorSurface,
  colorPrimary = ColorPrimary,
  colorSecondary = ColorSecondary,
  colorOnBackground = ColorOnBackground,
  colorOnSurface = ColorOnSurface,
  colorOnPrimary = ColorOnPrimary,
  colorOnSecondary = ColorOnSecondary,
  colorError = ColorError,
  colorOnError = ColorOnError,
  colorSuccess = SuccessColor,
  colorOnSuccess = InfoMessageTextColor
)

private val typography = AppTypography(
  title = TextStyle(
    fontSize = 24.sp,
    fontWeight = FontWeight.Normal
  ),
  body = TextStyle(
    fontSize = 14.sp,
    fontWeight = FontWeight.W400
  ),
  description = TextStyle(
    fontSize = 12.sp,
    fontWeight = FontWeight.W300
  ),
  buttonLabel = TextStyle(
    fontSize = 16.sp,
    fontWeight = FontWeight.W500
  ),
  emptyStateLabel = TextStyle(
    fontSize = 20.sp,
    fontWeight = FontWeight.Medium,
  ),
  labelLarge = TextStyle(
    fontSize = 28.sp,
    fontWeight = FontWeight.Normal
  ),
  labelMedium = TextStyle(
    fontSize = 20.sp,
    fontWeight = FontWeight.Normal
  ),
  bodyDetails = TextStyle(
    fontSize = 20.sp,
    fontWeight = FontWeight.W400
  )
)

private val shape = AppShape(
  rounded = RoundedCornerShape(8.dp),
  circular = CircleShape
)

private val size = AppSize(
  xs = 4.dp,
  s = 8.dp,
  m = 12.dp,
  l = 16.dp,
  xl = 24.dp
)

@Composable
fun AppTheme(
  isDarkTheme: Boolean = isSystemInDarkTheme(),
  content: @Composable () -> Unit
) {
  val colorScheme = if (isDarkTheme) darkColorScheme else lightColorScheme
  CompositionLocalProvider(
    localAppColorScheme provides colorScheme,
    localAppTypography provides typography,
    localAppShape provides shape,
    localAppSize provides size,
    LocalIndication provides ripple(),
    content = content
  )
}

object AppTheme {

  val colorScheme: AppColorScheme
    @Composable get() = localAppColorScheme.current

  val typography: AppTypography
    @Composable get() = localAppTypography.current

  val shape: AppShape
    @Composable get() = localAppShape.current

  val size: AppSize
    @Composable get() = localAppSize.current
}