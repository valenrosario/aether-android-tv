package com.aether.tv.ui

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.size

/**
 * Vidrio liviano para TV: relleno translúcido con degradé + borde con brillo.
 * No usa desenfoque real (caro para la GPU de muchos TVs); el efecto viene del fondo aurora.
 */
fun Modifier.glass(shape: Shape, fill: Float = 0.16f): Modifier = this
    .clip(shape)
    .background(Brush.linearGradient(listOf(Color.White.copy(alpha = fill), Color.White.copy(alpha = 0.04f))))
    .border(
        1.dp,
        Brush.linearGradient(listOf(Color.White.copy(alpha = 0.55f), Color.White.copy(alpha = 0.08f), Color.White.copy(alpha = 0.25f))),
        shape,
    )

/** Fondo aurora: manchas de color suaves sobre negro. */
@Composable
fun AuroraBackground(modifier: Modifier = Modifier) {
    Canvas(modifier.fillMaxSize().background(AetherColors.Black)) {
        fun blob(color: Color, cx: Float, cy: Float, r: Float, a: Float) {
            val c = Offset(size.width * cx, size.height * cy)
            val rad = size.width * r
            drawCircle(Brush.radialGradient(listOf(color.copy(alpha = a), Color.Transparent), center = c, radius = rad), radius = rad, center = c)
        }
        blob(Color(0xFF7C5CFF), 0.05f, 0.05f, 0.38f, 0.85f)
        blob(Color(0xFF18C6E8), 0.98f, 0.30f, 0.30f, 0.50f)
        blob(Color(0xFFFF4FB8), 0.45f, 0.98f, 0.30f, 0.45f)
        blob(Color(0xFFFFB347), 0.85f, 1.00f, 0.24f, 0.35f)
        blob(Color(0xFF2B4BFF), 0.00f, 1.00f, 0.30f, 0.55f)
    }
}

/** Elemento enfocable con el control remoto: escala y borde brillante al recibir foco. */
@Composable
fun GlassFocus(
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(16.dp),
    fillColor: Color = Color.Transparent,
    focusBorder: Color = Color.White,
    onClick: () -> Unit = {},
    content: @Composable BoxScope.() -> Unit,
) {
    var focused by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(if (focused) 1.07f else 1f, label = "glassFocus")
    Box(
        modifier
            .graphicsLayer { scaleX = scale; scaleY = scale }
            .onFocusChanged { focused = it.isFocused }
            .clip(shape)
            .background(fillColor)
            .then(if (focused) Modifier.border(3.dp, focusBorder, shape) else Modifier)
            .clickable(onClick = onClick),
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

/** Escena del banner: cielo, luna y montañas. */
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
        val p = Path().apply {
            moveTo(this@Canvas.size.width * 0.2f, this@Canvas.size.height * 0.1f)
            lineTo(this@Canvas.size.width * 0.9f, this@Canvas.size.height * 0.5f)
            lineTo(this@Canvas.size.width * 0.2f, this@Canvas.size.height * 0.9f)
            close()
        }
        drawPath(p, color)
    }
}

@Composable
fun SearchIcon(color: Color, size: Dp) {
    Canvas(Modifier.size(size)) {
        val w = this.size.width
        val s = Stroke(width = w * 0.11f, cap = StrokeCap.Round)
        drawCircle(color, radius = w * 0.30f, center = Offset(w * 0.44f, w * 0.44f), style = s)
        drawLine(color, Offset(w * 0.66f, w * 0.66f), Offset(w * 0.90f, w * 0.90f), strokeWidth = w * 0.11f, cap = StrokeCap.Round)
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
