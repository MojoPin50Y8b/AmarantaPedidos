package com.amaranta.pedidos.ui.theme

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val AmarantaScheme = lightColorScheme(
    primary = Gamboge,
    onPrimary = Color.White,
    secondary = Anzac,
    onSecondary = Russett,
    tertiary = Tacha,
    background = QuarterSpanishWhite,
    onBackground = Russett,
    surface = Color.White,
    onSurface = Russett,
    surfaceVariant = Buff,
    onSurfaceVariant = Russett,
    outline = DustyGray,
    error = CopperRose,
    onError = Color.White
)

@Composable
fun AmarantaPedidosTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = AmarantaScheme,
        typography = Typography(),
        content = content
    )
}