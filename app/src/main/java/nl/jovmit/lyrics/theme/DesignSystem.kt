package nl.jovmit.lyrics.theme

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp

@Immutable
data class AppColorScheme(
  val background: Color,
  val colorSurface: Color,
  val colorPrimary: Color,
  val colorSecondary: Color,
  val colorOnBackground: Color,
  val colorOnSurface: Color,
  val colorOnPrimary: Color,
  val colorOnSecondary: Color,
  val colorError: Color,
  val colorOnError: Color
)

@Immutable
data class AppTypography(
  val title: TextStyle,
  val body: TextStyle,
  val description: TextStyle,
  val buttonLabel: TextStyle,
  val emptyStateLabel: TextStyle,
  val labelLarge: TextStyle,
  val labelMedium: TextStyle,
  val bodyDetails: TextStyle
)

@Immutable
data class AppShape(
  val rounded: Shape,
  val circular: Shape
)

@Immutable
data class AppSize(
  val xs: Dp,
  val s: Dp,
  val m: Dp,
  val l: Dp,
  val xl: Dp,
)

val localAppColorScheme = staticCompositionLocalOf {
  AppColorScheme(
    background = Color.Unspecified,
    colorSurface = Color.Unspecified,
    colorPrimary = Color.Unspecified,
    colorSecondary = Color.Unspecified,
    colorOnBackground = Color.Unspecified,
    colorOnSurface = Color.Unspecified,
    colorOnPrimary = Color.Unspecified,
    colorOnSecondary = Color.Unspecified,
    colorError = Color.Unspecified,
    colorOnError = Color.Unspecified
  )
}

val localAppTypography = staticCompositionLocalOf {
  AppTypography(
    title = TextStyle.Default,
    body = TextStyle.Default,
    description = TextStyle.Default,
    buttonLabel = TextStyle.Default,
    emptyStateLabel = TextStyle.Default,
    labelLarge = TextStyle.Default,
    labelMedium = TextStyle.Default,
    bodyDetails = TextStyle.Default
  )
}

val localAppShape = staticCompositionLocalOf {
  AppShape(
    rounded = RectangleShape,
    circular = CircleShape
  )
}

val localAppSize = staticCompositionLocalOf {
  AppSize(
    xs = Dp.Unspecified,
    s = Dp.Unspecified,
    m = Dp.Unspecified,
    l = Dp.Unspecified,
    xl = Dp.Unspecified
  )
}
