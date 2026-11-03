package com.aether.tv.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.tv.material3.Text

/** Marca de Aether: una "A" cuyo travesaño es una órbita. [progress] 0..1 anima el trazado. */
@Composable
fun AetherMark(size: Dp, modifier: Modifier = Modifier, progress: Float = 1f) {
    Canvas(modifier.size(size)) {
        val w = this.size.width
        val stroke = w * 0.065f
        val a = Path().apply {
            moveTo(w * 0.22f, w * 0.82f)
            lineTo(w * 0.50f, w * 0.14f)
            lineTo(w * 0.78f, w * 0.82f)
        }
        // Dibujo progresivo del trazo de la A
        val measure = androidx.compose.ui.graphics.PathMeasure().apply { setPath(a, false) }
        val partial = Path()
        measure.getSegment(0f, measure.length * progress, partial, true)
        drawPath(
            partial,
            AetherColors.Halo,
            style = Stroke(stroke, cap = StrokeCap.Round, join = StrokeJoin.Round),
        )
        val orbit = Path().apply {
            moveTo(w * 0.14f, w * 0.62f)
            quadraticBezierTo(w * 0.50f, w * 0.44f, w * 0.86f, w * 0.62f)
        }
        val om = androidx.compose.ui.graphics.PathMeasure().apply { setPath(orbit, false) }
        val op = Path()
        om.getSegment(0f, om.length * progress, op, true)
        drawPath(op, AetherColors.Violet, style = Stroke(stroke * 0.6f, cap = StrokeCap.Round))
        if (progress >= 1f) {
            drawCircle(AetherColors.Violet, stroke * 0.7f, Offset(w * 0.86f, w * 0.62f))
        }
    }
}

@Composable
fun AetherWordmark(markSize: Dp = 40.dp, textSize: Int = 26) {
    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
        AetherMark(markSize)
        Text(
            "AETHER",
            color = AetherColors.Halo,
            fontSize = textSize.sp,
            fontWeight = FontWeight.Light,
            letterSpacing = (textSize * 0.28f).sp,
        )
    }
}
