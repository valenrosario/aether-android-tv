package com.aether.tv.ui

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.tv.material3.Border
import androidx.tv.material3.ClickableSurfaceDefaults
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.Surface
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Vidrio claro y liviano: relleno blanco translúcido con degradé + borde con brillo.
 * Sin desenfoque real (caro para la GPU de muchos TVs); el efecto viene del fondo aurora.
 */
fun Modifier.glass(shape: Shape, fill: Float = 0.55f): Modifier = this
    .clip(shape)
    .background(
        Brush.linearGradient(
            listOf(
                Color.White.copy(alpha = fill * 0.22f),
                Color.White.copy(alpha = fill * 0.08f),
            ),
        ),
    )
    .border(
        1.dp,
        Brush.linearGradient(listOf(Color.White.copy(alpha = 0.35f), Color.White.copy(alpha = 0.08f))),
        shape,
    )

/** Fondo oscuro cinematográfico con un resplandor violeta muy suave. */
@Composable
fun AuroraBackground(modifier: Modifier = Modifier) {
    Canvas(modifier.fillMaxSize()) {
        drawRect(AetherColors.Black)
        val c = Offset(size.width * 0.85f, 0f)
        val rad = size.width * 0.55f
        drawCircle(
            Brush.radialGradient(listOf(Color(0xFF5B45D6).copy(alpha = 0.28f), Color.Transparent), center = c, radius = rad),
            radius = rad,
            center = c,
        )
    }
}

/** Elemento enfocable con el control remoto (Surface nativa de TV): escala y aro al recibir foco, forma siempre redondeada. */
@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun GlassFocus(
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(16.dp),
    fillColor: Color = Color.Transparent,
    focusBorder: Color = Color.White,
    onClick: () -> Unit = {},
    content: @Composable BoxScope.() -> Unit,
) {
    Surface(
        onClick = onClick,
        modifier = modifier,
        shape = ClickableSurfaceDefaults.shape(shape = shape, focusedShape = shape, pressedShape = shape),
        colors = ClickableSurfaceDefaults.colors(
            containerColor = fillColor,
            focusedContainerColor = fillColor,
            pressedContainerColor = fillColor,
            contentColor = Color.White,
            focusedContentColor = Color.White,
            pressedContentColor = Color.White,
        ),
        border = ClickableSurfaceDefaults.border(focusedBorder = Border(BorderStroke(3.dp, focusBorder), shape = shape)),
        scale = ClickableSurfaceDefaults.scale(focusedScale = 1.06f),
        content = content,
    )
}

/** Portada generada con degradé de dos colores y un brillo, hasta tener imágenes reales. */
fun Modifier.posterArt(a: Color, b: Color): Modifier = this
    .background(Brush.linearGradient(listOf(a, b)))
    .drawBehind {
        drawRect(
            Brush.radialGradient(
                listOf(Color.White.copy(alpha = 0.32f), Color.Transparent),
                center = Offset(size.width * 0.7f, size.height * 0.22f),
                radius = size.width * 0.7f,
            ),
        )
    }

/** Escena: cielo, luna y montañas. */
@Composable
fun SceneArt(a: Color, b: Color, modifier: Modifier = Modifier) {
    Canvas(modifier) {
        val w = size.width
        val h = size.height
        drawRect(Brush.linearGradient(listOf(Color(0xFF0B1030), a, b), start = Offset(0f, 0f), end = Offset(w, h)))
        val moon = Offset(w * 0.80f, h * 0.30f)
        drawCircle(Color(0xFFC9BEFF).copy(alpha = 0.22f), radius = h * 0.34f, center = moon)
        drawCircle(
            Brush.radialGradient(
                listOf(Color.White, Color(0xFFD3CAFF), Color(0xFF7C6AE6)),
                center = Offset(moon.x - h * 0.05f, moon.y - h * 0.06f),
                radius = h * 0.22f,
            ),
            radius = h * 0.19f,
            center = moon,
        )
        ridge(
            listOf(0f to .62f, .09f to .50f, .21f to .60f, .35f to .40f, .47f to .58f, .59f to .46f, .71f to .64f, .83f to .44f, .93f to .56f, 1f to .48f),
            Color(0xFF231B66),
        )
        ridge(
            listOf(0f to .78f, .13f to .68f, .27f to .78f, .41f to .62f, .55f to .76f, .67f to .66f, .79f to .80f, .91f to .66f, 1f to .74f),
            Color(0xFF0D0B2E),
        )
    }
}

private fun DrawScope.ridge(points: List<Pair<Float, Float>>, color: Color) {
    val p = Path().apply {
        moveTo(0f, size.height)
        points.forEach { lineTo(size.width * it.first, size.height * it.second) }
        lineTo(size.width, size.height)
        close()
    }
    drawPath(p, color)
}

@Composable
fun PlayIcon(color: Color, size: Dp) {
    Canvas(Modifier.size(size)) {
        val w = this.size.width
        val p = Path().apply {
            moveTo(w * 0.22f, w * 0.10f)
            lineTo(w * 0.92f, w * 0.50f)
            lineTo(w * 0.22f, w * 0.90f)
            close()
        }
        drawPath(p, color)
    }
}

@Composable
fun SearchIcon(color: Color, size: Dp) {
    Canvas(Modifier.size(size)) {
        val w = this.size.width
        drawCircle(color, radius = w * 0.30f, center = Offset(w * 0.44f, w * 0.44f), style = Stroke(width = w * 0.10f))
        drawLine(color, Offset(w * 0.66f, w * 0.66f), Offset(w * 0.90f, w * 0.90f), strokeWidth = w * 0.10f, cap = StrokeCap.Round)
    }
}

@Composable
fun GearIcon(color: Color, size: Dp) {
    Canvas(Modifier.size(size)) {
        val w = this.size.width
        val c = Offset(w / 2, w / 2)
        drawCircle(color, radius = w * 0.17f, center = c, style = Stroke(width = w * 0.09f))
        for (i in 0 until 8) {
            val ang = Math.toRadians(i * 45.0)
            val dx = Math.cos(ang).toFloat()
            val dy = Math.sin(ang).toFloat()
            drawLine(
                color,
                Offset(c.x + dx * w * 0.30f, c.y + dy * w * 0.30f),
                Offset(c.x + dx * w * 0.44f, c.y + dy * w * 0.44f),
                strokeWidth = w * 0.09f,
                cap = StrokeCap.Round,
            )
        }
    }
}

enum class NavKind { HOME, SEARCH, FILM, TV, LIST, GEAR }

@Composable
fun NavIcon(kind: NavKind, color: Color, size: Dp) {
    when (kind) {
        NavKind.SEARCH -> SearchIcon(color, size)
        NavKind.GEAR -> GearIcon(color, size)
        else -> Canvas(Modifier.size(size)) {
            val w = this.size.width
            val sw = w * 0.09f
            val st = Stroke(width = sw, cap = StrokeCap.Round, join = StrokeJoin.Round)
            when (kind) {
                NavKind.HOME -> {
                    val p = Path().apply {
                        moveTo(w * 0.12f, w * 0.50f)
                        lineTo(w * 0.50f, w * 0.16f)
                        lineTo(w * 0.88f, w * 0.50f)
                        moveTo(w * 0.24f, w * 0.44f)
                        lineTo(w * 0.24f, w * 0.84f)
                        lineTo(w * 0.76f, w * 0.84f)
                        lineTo(w * 0.76f, w * 0.44f)
                    }
                    drawPath(p, color, style = st)
                }
                NavKind.FILM -> {
                    drawRoundRect(color, Offset(w * 0.14f, w * 0.20f), Size(w * 0.72f, w * 0.60f), CornerRadius(w * 0.08f), style = st)
                    drawLine(color, Offset(w * 0.36f, w * 0.20f), Offset(w * 0.36f, w * 0.80f), strokeWidth = sw)
                    drawLine(color, Offset(w * 0.64f, w * 0.20f), Offset(w * 0.64f, w * 0.80f), strokeWidth = sw)
                }
                NavKind.TV -> {
                    drawRoundRect(color, Offset(w * 0.12f, w * 0.20f), Size(w * 0.76f, w * 0.50f), CornerRadius(w * 0.08f), style = st)
                    drawLine(color, Offset(w * 0.50f, w * 0.70f), Offset(w * 0.50f, w * 0.84f), strokeWidth = sw, cap = StrokeCap.Round)
                    drawLine(color, Offset(w * 0.32f, w * 0.84f), Offset(w * 0.68f, w * 0.84f), strokeWidth = sw, cap = StrokeCap.Round)
                }
                NavKind.LIST -> {
                    for (i in 0 until 3) {
                        val y = w * (0.30f + i * 0.20f)
                        drawCircle(color, radius = sw * 0.6f, center = Offset(w * 0.18f, y))
                        drawLine(color, Offset(w * 0.36f, y), Offset(w * 0.86f, y), strokeWidth = sw, cap = StrokeCap.Round)
                    }
                }
                else -> Unit
            }
        }
    }
}
