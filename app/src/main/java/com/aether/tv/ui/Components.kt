package com.aether.tv.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.tv.material3.Text

/** Botón píldora enfocable. */
@Composable
fun GlassButton(
    label: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    container: Color = AetherColors.Accent,
    contentColor: Color = Color.White,
    focusBorder: Color = AetherColors.Ink,
    height: Dp = 30.dp,
    leading: (@Composable () -> Unit)? = null,
) {
    GlassFocus(
        modifier.height(height),
        shape = RoundedCornerShape(height / 2),
        fillColor = container,
        focusBorder = focusBorder,
        onClick = onClick,
    ) {
        Row(
            Modifier.align(Alignment.Center).padding(horizontal = 14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp),
        ) {
            leading?.invoke()
            Text(label, color = contentColor, fontSize = 10.sp, fontWeight = FontWeight.Bold, maxLines = 1)
        }
    }
}

@Composable
fun AetherButton(label: String, modifier: Modifier = Modifier, onClick: () -> Unit) {
    GlassButton(label, onClick, modifier, height = 34.dp)
}
