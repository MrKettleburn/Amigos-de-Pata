package Views

import androidx.compose.material.Colors
import androidx.compose.material.lightColors
import androidx.compose.ui.graphics.Color

class RefugioColorPalette {
    val primary = Color(0xFF4CAF50)
    val secondary = Color(0xFF8BC34A)
    val background = Color(0xFFF1F8E9)
    val menuBackground = Color(0xFFE8F5E9)
    val menuItem = Color(0xFFC8E6C9)
    val menuItemSelected = Color(0xFF66BB6A)
    val onMenuItem = Color(0xFF33691E)
    val onMenuItemSelected = Color.White
    val onBackground = Color(0xFF33691E)
    val pawPrint = Color(0xFF33691E)


    fun toMaterialColors(): Colors {
        return lightColors(
            primary = primary,
            primaryVariant = primary,
            secondary = secondary,
            background = background,
            surface = background,
            onPrimary = Color.White,
            onSecondary = Color.White,
            onBackground = onBackground,
            onSurface = onBackground
        )
    }
}